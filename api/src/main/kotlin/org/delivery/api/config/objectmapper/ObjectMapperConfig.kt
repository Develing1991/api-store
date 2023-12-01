package org.delivery.api.config.objectmapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// serialize, deserialize setting config
@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        // 코틀린에서 json 사용 모듈 셋팅
        val kotlinModule = KotlinModule.Builder().apply {
            withReflectionCacheSize(512) // 리플렉션 캐시 사이즈
            configure(KotlinFeature.NullToEmptyCollection, false) // 컬렉션 ex)List -> true=[], false=null
            configure(KotlinFeature.NullToEmptyMap, false) // map 상동
            configure(KotlinFeature.NullIsSameAsDefault, false) // 타입의 기본형 null로 초기화 ex) 정수형 0이 아닌 null로 초기화
            configure(KotlinFeature.SingletonSupport, false) // 찾아보기
            configure(KotlinFeature.StrictNullChecks, false) // null에대한 체크 false
        }.build()

        // java 관련 설정
        val objectMapper = ObjectMapper().apply {
            registerModules(Jdk8Module()) // 8버전 이후에 나온 클래스들을 처리 해주기 위해서 (Optional같은)
            registerModules(JavaTimeModule()) // local date같은 애들
            registerModules(kotlinModule) // 코틀린 모듈 추가
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 모르는 json field가 있더라도 익셉션 무시하고 파싱
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false) // 비어있는 객체 직렬화 익셉션 무시 그냥 비어있는 객체 만듦
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)  // 날짜 관련 직렬화 disable
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE // 스네이크 케이스 디폴트
        }
        
        return objectMapper
    }
}