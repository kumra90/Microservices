package com.programming.orderservice.dto;

import com.programming.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private Order order;
    private Payment payment;
}
