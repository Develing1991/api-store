package org.delivery.api.common.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {
    private final RabbitTemplate rabbitTemplate;
    
    public void producer(String exchange, String routeKey, Object object){
        rabbitTemplate.convertAndSend(exchange, routeKey, object);
        // 어떤 exchange에 어떤 routeKey를 통해 어떤 object를 보낸다
    }
}
