package org.example.ecommerceorderprocessing.service;

import com.rabbitmq.client.Channel;
import org.example.ecommerceorderprocessing.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DeadLetterConsumer {

    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE)
    public void processDeadLetter(
            Message message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws IOException {

        String orderId = message.getMessageProperties().getMessageId();

        if (orderId == null || orderId.isBlank()) {
            orderId = "unknown";
        }

        System.out.println(
                "Error Logger: Invalid order " + orderId + " routed to DLX");

        channel.basicAck(deliveryTag, false);
    }
}
