package org.delivery.common.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Service

// 순수 java, kotlin
// @Service, @AliasFor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Service
annotation class Business(
    @get:AliasFor(annotation = Service::class)
    val value: String = ""
)
