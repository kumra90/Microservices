package com.programming.orderservice.service;

import com.programming.orderservice.dto.Inventory;
import com.programming.orderservice.dto.Payment;
import com.programming.orderservice.dto.TransactionRequest;
import com.programming.orderservice.dto.TransactionResponse;
import com.programming.orderservice.entity.Order;
import com.programming.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public TransactionResponse saveOrder(TransactionRequest transactionRequest) {
        String paymentMessage;

        Order order = transactionRequest.getOrder();
        Payment payment = transactionRequest.getPayment();
        payment.setOrderId(order.getId());
        payment.setPrice(order.getPrice());

        /*Make a rest call to inventory service to check the stock*/

        Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/inventory/" + transactionRequest.getOrder().getProductId(), Inventory.class);

        if (inventory != null && inventory.getStock() > 0) {
            /*Make a rest call to payment service*/
            Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment", payment, Payment.class);

            if (paymentResponse != null && paymentResponse.getPaymentStatus().equalsIgnoreCase("Success")) {
                paymentMessage = "Payment Done successfully";
                order.setOrderMessage("Order Placed");
                orderRepository.save(order);
                return new TransactionResponse(order, paymentResponse.getTransactionId(), paymentResponse.getPrice(), paymentMessage);
            } else {
                paymentMessage = "Payment Failed";
                order.setOrderMessage("Order Failed due to payment");
                return new TransactionResponse(order, "NA", 0.0, "NA");
            }
        }
        order.setOrderMessage("Order Failed.Items not in stcck");
        return new TransactionResponse(order, "NA", 0.0, "NA");

    }
}
