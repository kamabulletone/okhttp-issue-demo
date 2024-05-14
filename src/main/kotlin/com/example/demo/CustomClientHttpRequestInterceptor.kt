package com.example.demo

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException

class CustomClientHttpRequestInterceptor : ClientHttpRequestInterceptor {
    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        println("test interceptor start")
        val response: ClientHttpResponse = execution.execute(request, body)
        println("test interceptor stop")
        return response
    }
}
