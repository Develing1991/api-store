package org.delivery.api.config.rabbitmq;


// 경로 주의
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("delivery.exchange");
    }

    @Bean
    public Queue queue(){
        return new Queue("delivery.queue");
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("delivery.key");
    }

    // end queue 설정
    // producer가 exchage에 데이터를 보낼 때 프로토콜이 정해져 있음
    // 보통 httpClient를 사용해 통신할 때는 Restemplate같은걸 쓰고
    // rabbitmq에서 제공해주는 template은 RabbitTemplate

    // 경로 주의
    //import org.springframework.amqp.rabbit.connection.ConnectionFactory;
    //import org.springframework.amqp.support.converter.MessageConverter;
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter){

        var rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    // MessageConverter
    // ObjectMappter를 통해 객체를 json으로 직렬화, 반대로 역직렬화 하듯이
    // 같은 역할을 해주는 것

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
