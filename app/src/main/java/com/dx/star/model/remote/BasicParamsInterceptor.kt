package com.dx.star.model.remote

import android.text.TextUtils
import com.dx.star.util.Constant.APP_KEY
import com.dx.star.util.Constant.APP_SECRET
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 *
 * zrj 2019/4/1
 */
// 私有构造器
class BasicParamsInterceptor private constructor() : Interceptor {

    internal var queryParamsMap: MutableMap<String, String> = HashMap() // 添加到 URL 末尾，Get Post 方法都使用
    internal var paramsMap: MutableMap<String, String> = HashMap() // 添加到公共参数到消息体，适用 Post 请求
    internal var headerParamsMap: MutableMap<String, String> = HashMap() // 公共 Headers 添加
    internal var headerLinesList: MutableList<String> = ArrayList() // 消息头 集合形式，一次添加一行

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val requestBuilder = request.newBuilder()
        // process header params inject
        val headerBuilder = request.headers.newBuilder()
        val nonce = Random().nextInt().toString()
        val buffer = Buffer()
        request.body?.writeTo(buffer)
        val signApp = getSignApp(nonce, buffer.readUtf8())
        headerParamsMap["nonce"] = nonce
        headerParamsMap["signature"] = signApp

        // 以 Entry 添加消息头
        if (headerParamsMap.isNotEmpty()) {
            val iterator = headerParamsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Map.Entry<*, *>
                headerBuilder.add(entry.key as String, entry.value as String)
            }
            requestBuilder.headers(headerBuilder.build())
        }
        // 以 String 形式添加消息头
        if (headerLinesList.size > 0) {
            for (line in headerLinesList) {
                headerBuilder.add(line)
            }
            requestBuilder.headers(headerBuilder.build())
        }
        // process header params end

        // process queryParams inject whatever it's GET or POST
//        queryParamsMap["token"] = token
        if (queryParamsMap.isNotEmpty()) {
            request = injectParamsIntoUrl(request.url.newBuilder(), requestBuilder, queryParamsMap)
        }

        // process post body inject
        if (paramsMap.isNotEmpty()) {
            if (canInjectIntoBody(request)) {
                val formBodyBuilder = FormBody.Builder()
                for ((key, value) in paramsMap) {
                    formBodyBuilder.add(key, value)
                }

                val formBody = formBodyBuilder.build()
                var postBodyString = bodyToString(request.body)
                postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(
                    formBody
                )
                requestBuilder.post(
                    postBodyString
                        .toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull())
                )
            }
        }
        request = requestBuilder.build()
        return chain.proceed(request)
    }

    /**
     * 确认是否是 post 请求
     * @param request 发出的请求
     * @return true 需要注入公共参数
     */
    private fun canInjectIntoBody(request: Request?): Boolean {
        if (request == null) {
            return false
        }
        if (!TextUtils.equals(request.method, "POST")) {
            return false
        }
        val body = request.body ?: return false
        val mediaType = body.contentType() ?: return false
        return TextUtils.equals(mediaType.subtype, "x-www-form-urlencoded")
    }

    // func to inject params into url
    private fun injectParamsIntoUrl(
        httpUrlBuilder: HttpUrl.Builder,
        requestBuilder: Request.Builder,
        paramsMap: Map<String, String>
    ): Request {
        val iterator = paramsMap.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            httpUrlBuilder.addQueryParameter(entry.key, entry.value)
        }
        requestBuilder.url(httpUrlBuilder.build())
        return requestBuilder.build()
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }

    @Suppress("unused")
    class Builder {

        private var interceptor: BasicParamsInterceptor =
            BasicParamsInterceptor()

        // 添加公共参数到 post 消息体
        fun addParam(key: String, value: String): Builder {
            interceptor.paramsMap[key] = value
            return this
        }

        // 添加公共参数到 post 消息体
        fun addParamsMap(paramsMap: Map<String, String>): Builder {
            interceptor.paramsMap.putAll(paramsMap)
            return this
        }

        // 添加公共参数到消息头
        fun addHeaderParam(key: String, value: String): Builder {
            interceptor.headerParamsMap[key] = value
            return this
        }

        // 添加公共参数到消息头
        fun addHeaderParamsMap(headerParamsMap: Map<String, String>): Builder {
            interceptor.headerParamsMap.putAll(headerParamsMap)
            return this
        }

        // 添加公共参数到消息头
        fun addHeaderLine(headerLine: String): Builder {
            val index = headerLine.indexOf(":")
            require(index != -1) { "Unexpected header: $headerLine" }
            interceptor.headerLinesList.add(headerLine)
            return this
        }

        // 添加公共参数到消息头
        fun addHeaderLinesList(headerLinesList: List<String>): Builder {
            for (headerLine in headerLinesList) {
                val index = headerLine.indexOf(":")
                require(index != -1) { "Unexpected header: $headerLine" }
                interceptor.headerLinesList.add(headerLine)
            }
            return this
        }

        // 添加公共参数到 URL
        fun addQueryParam(key: String, value: String): Builder {
            interceptor.queryParamsMap[key] = value
            return this
        }

        // 添加公共参数到 URL
        fun addQueryParamsMap(queryParamsMap: Map<String, String>): Builder {
            interceptor.queryParamsMap.putAll(queryParamsMap)
            return this
        }

        fun build(): BasicParamsInterceptor {
            return interceptor
        }
    }
}


fun getSignApp(nonce: String?, requestBody: String?): String {
    //signature-app = MD5（appid+ nonce+ 请求体内容+ 签名密钥）
    val sb = StringBuilder()
    sb.append(APP_KEY)
    sb.append(nonce)
    sb.append(requestBody)
    sb.append(APP_SECRET)
    return EncodeMD5.getMd5(sb.toString())
}

object EncodeMD5 {
    private const val charSetName = "UTF-8"
    private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    @Suppress("unused")
    @Throws(NoSuchAlgorithmException::class)
    fun getMd5(b: ByteArray): String {
        val md5 = MessageDigest.getInstance("MD5")
        md5.update(b, 0, b.size)
        return byteArrayToHexString(md5.digest())
    }

    fun getMd5(data: String): String {
        val md5: MessageDigest
        try {
            md5 = MessageDigest.getInstance("MD5")
            val b = data.toByteArray(charset(charSetName))
            md5.update(b, 0, b.size)
            return byteArrayToHexString(md5.digest())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun byteArrayToHexString(b: ByteArray): String {
        val sb = StringBuffer()
        for (i in b.indices) {
            sb.append(byteToHexString(b[i]))
        }
        return sb.toString()
    }

    private fun byteToHexString(b: Byte): String {
        var n = b.toInt()
        if (n < 0) {
            n += 256
        }
        val d1 = n / 16
        val d2 = n % 16
        return hexDigits[d1] + hexDigits[d2]
    }
}