package com.dx.star.model.remote

import com.dx.star.ext.sp
import com.dx.star.model.remote.gson.GsonUtil
import com.dx.star.util.Constant
//import com.mocklets.pluto.PlutoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.Protocol
import java.util.*


/**
 * 请求工具类
 * zrj 2020/7/16
 */

fun getOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
    val locale = Locale.getDefault()
    val lang = locale.language + "-" + locale.country
    val imei = builder.sp().getString(Constant.DEVICE_UNIQUE_CODE,"") ?: ""
    val basicParamsInterceptor = BasicParamsInterceptor.Builder()
        .addHeaderParam("Accept-Language", lang)
        .addHeaderParam("appid", Constant.APP_KEY)
        .addHeaderParam("Connection", "close")
        .addHeaderParam("imei", imei)
        .build()

    builder.run {
        addInterceptor(basicParamsInterceptor)
        addInterceptor(HttpLogInterceptor())
//        addInterceptor(PlutoInterceptor())
        addInterceptor(ChangeUrlInterceptor())
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        retryOnConnectionFailure(true) // 错误重连
        hostnameVerifier(UnSafeHostnameVerifier())
    }

    val trustAllCert = UnSafeTrustManager()
    builder.sslSocketFactory(SSL(trustAllCert), trustAllCert)
        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
    return builder.build()
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonUtil.gson))
        .build()
    return retrofit.create(T::class.java)
}

