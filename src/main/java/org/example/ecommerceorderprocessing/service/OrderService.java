package org.example.ecommerceorderprocessing.service;

import org.example.ecommerceorderprocessing.OrderEvent;
import org.example.ecommerceorderprocessing.config.RabbitMQConfig;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;

    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public OrderEvent placeOrder(String category, int quantity) {
        OrderEvent order = new OrderEvent(category, quantity);

        String message = order.getOrderId() + "|"
                + order.getCategory() + "|"
                + order.getQuantity();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "order.created." + category,
                message,
                m -> {
                    m.getMessageProperties().setMessageId(order.getOrderId());
                    m.getMessageProperties().setDeliveryMode(
                            MessageDeliveryMode.PERSISTENT);
                    return m;
                }
        );

        return order;
    }
}
