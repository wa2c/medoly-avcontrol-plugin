package com.wa2c.android.medoly.plugin.action.avcontrol.source.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Api interceptor.
 */
class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .build()
        return chain.proceed(request)
    }
}
