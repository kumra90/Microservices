package com.programming.orderservice.controller;

import com.programming.orderservice.dto.Payment;
import com.programming.orderservice.dto.TransactionRequest;
import com.programming.orderservice.dto.TransactionResponse;
import com.programming.orderservice.entity.Order;
import com.programming.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/bookOrder")
    public TransactionResponse BookOrder(@RequestBody TransactionRequest transactionRequest) {
        return orderService.saveOrder(transactionRequest);
    }
}
