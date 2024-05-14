package com.example.demo

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import kotlin.RuntimeException

@RestController
class DemoController(
    private val restTemplate: CustomRestTemplate
) {
    private val host = "http://localhost:8082"

    @GetMapping("/response")
    fun demoGetResponse(): ResponseEntity<String> {
        return ResponseEntity.ok("response")
    }

    @GetMapping("/tryrest")
    fun demoGet() {
        get("/response", String::class.java)
    }


    private fun <S> get(
        path: String,
        responseClass: Class<S>,
        uriVariables: Map<String, *> = emptyMap<String, Any>(),
        queryParams: MultiValueMap<String, String> = LinkedMultiValueMap(),
    ): S {
        val requestMetaData = RequestMetaData(HttpMethod.GET, path, uriVariables, queryParams)
        return exchange(requestMetaData, body = null, responseClass = responseClass)
    }

    private fun <Q, S> exchange(
        requestMetaData: RequestMetaData,
        body: Q,
        responseClass: Class<S>,
    ): S = try {
            val uriString = UriComponentsBuilder
                .fromHttpUrl(host)
                .uriVariables(requestMetaData.uriVariables)
                .queryParams(requestMetaData.queryParams)
                .path(requestMetaData.path)
                .toUriString()
            val bodyEntity = HttpEntity(body, withMetricHeaders(requestMetaData.path))
            restTemplate
                .exchange(
                    uriString,
                    requestMetaData.method,
                    bodyEntity,
                    responseClass
                ).body ?: throw RuntimeException()
        } catch (e: RuntimeException) {
            throw e
        }

    private fun withMetricHeaders(
        uriTemplate: String,
        headers: MultiValueMap<String, String> = HttpHeaders()
    ): MultiValueMap<String, String> {
        headers.add("X-Uri-Tag-Header", uriTemplate)
        return headers
    }

    data class RequestMetaData(
        val method: HttpMethod,
        val path: String,
        val uriVariables: Map<String, *> = emptyMap<String, Any>(),
        val queryParams: MultiValueMap<String, String>? = null
    )
}