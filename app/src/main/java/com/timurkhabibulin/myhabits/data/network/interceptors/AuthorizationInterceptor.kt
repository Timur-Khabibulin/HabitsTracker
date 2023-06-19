package com.timurkhabibulin.myhabits.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .header("Authorization", "0ea7e3b4-3431-46f6-8ead-96f161c7b4c7")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}