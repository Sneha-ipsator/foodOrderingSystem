package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.PaymentDto;
import com.ipsator.foodOrderingSystem.entity.Order;
import com.ipsator.foodOrderingSystem.entity.OrderItem;
import com.ipsator.foodOrderingSystem.payload.PaymentRefundResponse;
import com.ipsator.foodOrderingSystem.payload.PaymentRequest;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;

public interface PaymentService {
    ServiceResponse<PaymentDto> processPayment(Long orderId, PaymentRequest paymentRequest);

    ServiceResponse<PaymentDto> processRefund(Order order, Long payment);


    PaymentRefundResponse processRefundForItem(Order order, OrderItem orderItem, double refundAmount);
}
