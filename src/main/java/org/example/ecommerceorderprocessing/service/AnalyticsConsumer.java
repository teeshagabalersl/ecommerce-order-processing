package org.example.ecommerceorderprocessing.service;

import com.rabbitmq.client.Channel;
import org.example.ecommerceorderprocessing.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnalyticsConsumer {

    private final Set<String> processedOrders =
            ConcurrentHashMap.newKeySet();

    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_QUEUE)
    public void processOrder(
            Message message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws IOException {

        String body = new String(message.getBody());
        String orderId = message.getMessageProperties().getMessageId();

        try {
            String[] order = body.split("\\|", -1);

            if (order.length != 3
                    || orderId == null
                    || orderId.isBlank()
                    || order[1].isBlank()) {
                throw new IllegalArgumentException("Invalid order message");
            }

            int quantity = Integer.parseInt(order[2]);

            if (quantity < 0) {
                System.out.println(
                        "Analytics Service: Invalid order ignored: " + orderId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            if (!processedOrders.add(orderId)) {
                System.out.println(
                        "Analytics Service: Duplicate order ignored: " + orderId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            System.out.println(
                    "Analytics Service: Recording high-value electronics sale for order "
                            + orderId);

            channel.basicAck(deliveryTag, false);

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Analytics Service: Rejecting malformed order message");
            channel.basicReject(deliveryTag, false);
        }
    }
}
