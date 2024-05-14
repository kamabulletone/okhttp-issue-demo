package com.example.demo

import org.springframework.boot.web.client.*
import org.springframework.context.annotation.*
import org.springframework.web.client.*
import java.time.Duration

@Configuration
class CustomRestTemplateConfiguration {

    @Bean
    fun customerModifierRestTemplate(
        builder: RestTemplateBuilder,
    ) = CustomRestTemplate(
        builder
            .setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(30))
            .additionalInterceptors(CustomClientHttpRequestInterceptor())
            .build()
    )
}

open class CustomRestTemplate(delegate: RestTemplate) : RestOperations by delegate
