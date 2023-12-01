package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.InvoiceDto;
import com.ipsator.foodOrderingSystem.dto.PaymentDto;
import com.ipsator.foodOrderingSystem.entity.Order;
import com.ipsator.foodOrderingSystem.entity.OrderItem;
import com.ipsator.foodOrderingSystem.entity.Payment;
import com.ipsator.foodOrderingSystem.payload.PaymentRefundResponse;
import com.ipsator.foodOrderingSystem.payload.PaymentRequest;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.repository.OrderItemRepository;
import com.ipsator.foodOrderingSystem.repository.OrderRepository;
import com.ipsator.foodOrderingSystem.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    private final OrderService orderService;

    @Autowired
    public PaymentServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }



    @Override
    public ServiceResponse<PaymentDto> processPayment(Long orderId, PaymentRequest paymentRequest) {
        try {

            Order order = orderRepository.findById(orderId).orElse(null);

            if (order == null) {
                return new ServiceResponse<>(false, null, "Order not found");
            }

            order.setPaymentStatus("PAID");

            Payment payment = new Payment();
            payment.setAmount(order.getOrderAmt());
            payment.setPaymentDate(new Date());
            payment.setModeOfPayment(paymentRequest.getModeOfPayment());
            payment.setOrder(order);

            Payment savedPayment = paymentRepository.save(payment);
            orderRepository.save(order);

            ServiceResponse<InvoiceDto> invoiceResponse = orderService.generateInvoice(order.getOrderId());

            if (invoiceResponse.getSuccess()) {
                byte[] pdfBytes = invoiceResponse.getData().getPdfBytes();
            }

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(savedPayment.getId());
            paymentDto.setAmount(savedPayment.getAmount());
            paymentDto.setPaymentDate(savedPayment.getPaymentDate());
            paymentDto.setModeOfPayment(savedPayment.getModeOfPayment());
            paymentDto.setOrder(savedPayment.getOrder());

            return new ServiceResponse<>(true, paymentDto, "Payment processed successfully");
        } catch (Exception ex) {
            return new ServiceResponse<>(false, null, ex.getMessage());
        }
    }

    @Override
    public ServiceResponse<PaymentDto> processRefund(Order order,Long paymentId) {
        try{
          Payment payment=paymentRepository.findById(paymentId).orElse(null);
          if(payment== null)
          {
              return new ServiceResponse<>(false,null,"Payment not found");
          }
          if("REFUNDED".equals(order.getPaymentStatus()))
          {
              return new ServiceResponse<>(false, null, "Payment is already refunded");
          }
          double refundAmount=payment.getAmount();
          order.setPaymentStatus("REFUNDED");
          orderRepository.save(order);

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(payment.getId());
            paymentDto.setAmount(refundAmount);
            paymentDto.setPaymentDate(new Date());
            paymentDto.setModeOfPayment(payment.getModeOfPayment());
            paymentDto.setOrder(payment.getOrder());
            return new ServiceResponse<>(true, paymentDto, "Refund processed successfully");
        }
        catch(Exception ex) {
            return new ServiceResponse<>(false, null, ex.getMessage());
        }
    }



    public PaymentRefundResponse processRefundForItem(Order order, OrderItem orderItem, double refundAmount) {
        try {
            orderItem.setReceivedQuantity(orderItem.getFooditemQuantity());
            orderItemRepository.save(orderItem);

            PaymentRefundResponse refundResponse = new PaymentRefundResponse();
            refundResponse.setSuccess(true);
            refundResponse.setRefund(refundAmount);
            refundResponse.setMessage("Refund processed successfully");
            return refundResponse;

        } catch (Exception ex) {
            log.error("Error processing refund for item: " + ex.getMessage(), ex);
            PaymentRefundResponse refundResponse = new PaymentRefundResponse();
            refundResponse.setSuccess(false);
            refundResponse.setRefund(0.0);
            refundResponse.setMessage("Error processing refund");
            return refundResponse;
        }
    }

}

