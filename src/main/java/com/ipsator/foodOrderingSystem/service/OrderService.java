package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.InvoiceDto;
import com.ipsator.foodOrderingSystem.dto.OrderDto;
import com.ipsator.foodOrderingSystem.entity.Address;
import com.ipsator.foodOrderingSystem.entity.Order;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.enums.OrderStatus;
import com.ipsator.foodOrderingSystem.payload.*;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public ServiceResponse<OrderDto> orderCreate(Long cartId, Address orderRequest, String user);


List<OrderDto> getOrdersForUser(String username);

    ServiceResponse<OrderCancellationResponse> cancelOrder(Long orderId, User currentUser);

    void updateStatus(Long orderId, OrderStatus newStatus);

    PaymentRefundResponse<Double> processUserFeedback(Long orderItemId, int receivedQuantity);

    public ServiceResponse<InvoiceDto> generateInvoice(Long orderId);
}
