package com.dx.star.model.remote

import com.dx.star.ext.sp
import com.dx.star.util.Constant.NEW_BASE_URL
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *
 * java类作用描述
 * zrj 2021/8/11 11:20
 * 更新者 2021/8/11 11:20
 */
class ChangeUrlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val oldUrl = originalRequest.url
        val builder = originalRequest.newBuilder()
        val newUrl = sp().getString(NEW_BASE_URL,"") ?: ""
        if (newUrl.isEmpty()) return chain.proceed(originalRequest)
        newUrl.toHttpUrlOrNull()?.let {
            if (it.host != oldUrl.host) {
                val newHttpUrl = oldUrl.newBuilder()
                    .scheme(it.scheme)
                    .host(it.host)
                    .port(it.port)
                    .build()
                val newRequest: Request = builder.url(newHttpUrl).build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(originalRequest)
    }
}