package es.dam.exceptions

import okhttp3.Interceptor
import okhttp3.Response

class HttpResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 404) {
            throw SpaceNotFoundException(response.body?.string()!!)
        }
        if(response.code == 400) {
            throw SpaceBadRequestException(response.body?.string()!!)
        }
        if(response.code == 500) {
            throw SpaceInternalErrorException(response.body?.string()!!)
        }
        return response
    }
}