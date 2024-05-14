package com.example.demo

import okhttp3.*
import org.springframework.boot.autoconfigure.web.client.*
import org.springframework.boot.web.client.*
import org.springframework.context.annotation.*
import org.springframework.http.client.*

@Configuration
class RestTemplateConfiguration {
    @Bean
    fun restTemplateBuilder(
        restTemplateBuilderConfigurer: RestTemplateBuilderConfigurer
    ): RestTemplateBuilder {
        val requestFactorySupplier = {
            BufferingClientHttpRequestFactory(OkHttp3ClientHttpRequestFactory(OkHttpClient.Builder().build()))
        }
        val interceptors = listOf(
            CustomClientHttpRequestInterceptor()
        )
        val builder = RestTemplateBuilder()
            .requestFactory(requestFactorySupplier)
            .interceptors(interceptors)

        return restTemplateBuilderConfigurer.configure(builder)
    }
}
