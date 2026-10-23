package org.example.ecommerceorderprocessing;

import java.util.UUID;

public class OrderEvent {

    private String orderId;
    private String category;
    private int quantity;

    public OrderEvent() {
    }

    public OrderEvent(String category, int quantity) {
        this.orderId = UUID.randomUUID().toString();
        this.category = category;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }
}
