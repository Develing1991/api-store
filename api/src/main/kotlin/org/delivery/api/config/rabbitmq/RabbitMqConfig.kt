package org.delivery.api.config.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig {

    @Bean
    fun directExchange(): DirectExchange{
        return DirectExchange("delivery.exchange")
    }

    @Bean
    fun queue(): Queue {
        return Queue("delivery.queue")
    }

    @Bean
    fun binding(directExchange: DirectExchange, queue: Queue): Binding {
        return BindingBuilder.bind(queue).to(directExchange).with("delivery.key")
    }

    // end queue 설정
    // producer가 exchage에 데이터를 보낼 때 프로토콜이 정해져 있음
    // 보통 httpClient를 사용해 통신할 때는 Restemplate같은걸 쓰고
    // rabbitmq에서 제공해주는 template은 RabbitTemplate

    // 경로 주의
    //import org.springframework.amqp.rabbit.connection.ConnectionFactory;
    //import org.springframework.amqp.support.converter.MessageConverter;
    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, messageConverter: MessageConverter) :RabbitTemplate{
        val rabbitTemplate = RabbitTemplate().apply {
            setConnectionFactory(connectionFactory)
            setMessageConverter(messageConverter)
        }
        return rabbitTemplate
    }

    // MessageConverter
    // ObjectMappter를 통해 객체를 json으로 직렬화, 반대로 역직렬화 하듯이
    // 같은 역할을 해주는 것
    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }
}