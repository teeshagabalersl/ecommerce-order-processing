package org.example.ecommerceorderprocessing.controller;

import org.example.ecommerceorderprocessing.OrderEvent;
import org.example.ecommerceorderprocessing.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderEvent placeOrder(
            @RequestParam String category,
            @RequestParam int quantity) {

        if (!category.equals("electronics")
                && !category.equals("clothing")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Category must be electronics or clothing");
        }

        return orderService.placeOrder(category, quantity);
    }
}
