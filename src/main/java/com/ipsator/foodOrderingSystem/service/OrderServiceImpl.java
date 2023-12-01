package com.ipsator.foodOrderingSystem.service;
import com.ipsator.foodOrderingSystem.dto.InvoiceDto;
import com.ipsator.foodOrderingSystem.dto.OrderDto;
import com.ipsator.foodOrderingSystem.entity.*;
import com.ipsator.foodOrderingSystem.enums.OrderStatus;
import com.ipsator.foodOrderingSystem.payload.*;
import com.ipsator.foodOrderingSystem.repository.*;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private CartItemReository cartItemRepository;
    private PaymentService paymentService;

    @Autowired
    @Lazy
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    private FoodItemsRepository foodItemsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public ServiceResponse<OrderDto> orderCreate(Long cartId, Address orderRequest, String user) {
        try {
            Cart cart = cartRepository.findBycartId(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            List<CartItem> cartItems = cart.getItem();

            if (cartItems.isEmpty()) {
                return new ServiceResponse<>(false, null, "Please add items to the cart first and then place the order");
            }

            User userEntity = userRepo.findByEmail(user)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Order order = new Order();
            double taxPercentage = 0.1;
            double deliveryCharges = 5.0;

            order.setTax(taxPercentage * calculateSubtotal(cartItems));
            order.setDeliveryCharges(deliveryCharges);

            String generatedInvoice = generateAlphanumericInvoice();
            log.info("Generated Invoice: {}", generatedInvoice);
            order.setInvoice(generatedInvoice);

            double totalOrderPrice = calculateTotalOrderPrice(cartItems, taxPercentage, deliveryCharges);

            for (CartItem cartItem : cartItems) {

                OrderItem orderItem = new OrderItem();
                FoodItems foodItem = cartItem.getFoodItems();

                orderItem.setFoodItem(foodItem);
                orderItem.setTotalFooditemPrice(cartItem.getTotalPrice());
                orderItem.setFooditemQuantity(cartItem.getQuantity());
                orderItem.setOrder(order);

                order.getOrderItem().add(orderItem);
            }

            if(orderRequest !=null)
            {
                Address userAddress = orderRequest;
                if (!userEntity.getAddress().contains(userAddress)) {
                    addressRepository.save(userAddress);
                    userEntity.getAddress().add(userAddress);
                }
                order.setBillingAddress(userAddress);
            }
            else {
                List<Address> userAddresses = userEntity.getAddress();
                if (!userAddresses.isEmpty()) {
                    order.setBillingAddress(userAddresses.get(0));
                }
            }
            order.setOrderDelivered(null);
            order.setOrderStatus(OrderStatus.CREATED);
            order.setPaymentStatus("NOT PAID");
            order.setUser(userEntity);
            order.setOrderAmt(totalOrderPrice);
            order.setOrderCreateAt(new Date());

            Order savedOrder = orderRepository.save(order);

            for (CartItem cartItem : cartItems) {
                cartItemRepository.delete(cartItem);
            }

            cartItems.clear();

            return new ServiceResponse<>(true, convertOrderToOrderDto(savedOrder), "Order created successfully");
        } catch (Exception ex) {
            log.error("Error while creating an order: " + ex.getMessage(), ex);
            return new ServiceResponse<>(false, null, ex.getMessage());
        }
    }

    private String generateAlphanumericInvoice() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(3);
        String invoice = "INV" + timestamp + randomAlphanumeric;
        return invoice;
    }

    private double calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    private double calculateTotalOrderPrice(List<CartItem> cartItems, double taxPercentage, double deliveryCharges) {
        double subtotal = calculateSubtotal(cartItems);
        double tax = subtotal * taxPercentage;
        return subtotal + tax + deliveryCharges;
    }

    private OrderDto convertOrderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setOrderDelivered(order.getOrderDelivered());
        orderDto.setTax(order.getTax());
        orderDto.setDeliveryCharges(order.getDeliveryCharges());
        orderDto.setOrderAmt(order.getOrderAmt());
        orderDto.setPaymentStatus(order.getPaymentStatus());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setBillingAddress(order.getBillingAddress());
        return orderDto;
    }

    @Override
    public List<OrderDto> getOrdersForUser(String username) {
        List<Order> orders = orderRepository.findByUserEmail(username);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = convertOrderToOrderDto(order);
            orderDtos.add(orderDto);
        }

        return orderDtos;
    }

    @Transactional
    @Override
    public ServiceResponse<OrderCancellationResponse> cancelOrder(Long orderId,User currentUser) {
        try{
            Order order=orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("Order not found"));
            log.info("Order status before cancellation check: {}", order.getOrderStatus());
            if (!order.getUser().equals(currentUser)) {
                return new ServiceResponse<>(false, null, "You are not authorized to cancel this order");
            }
            if(!canCancelOrder(order))
            {
                return new ServiceResponse<>(false, null, "Order cannot be cancelled");
            }
            double refundedAmount = 0.0;

            Long paymentId = order.getPayment().getId();
            if (paymentId != null && "PAID".equals(order.getPaymentStatus())) {
                refundedAmount = paymentService.processRefund(order, paymentId).getData().getAmount();
                order.setPaymentStatus("REFUNDED");
                orderRepository.save(order);
            }

            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            OrderCancellationResponse cancellationResponse = new OrderCancellationResponse();
            cancellationResponse.setRefundedAmount(refundedAmount);
            cancellationResponse.setOrderStatus(String.valueOf(order.getOrderStatus()));
            cancellationResponse.setPaymentStatus(order.getPaymentStatus());
            return new ServiceResponse<>(true,cancellationResponse,"Order Cancelled Successfully");
        }
        catch (EntityNotFoundException ex) {
            log.error("Order not found: " + ex.getMessage(), ex);
            return new ServiceResponse<>(false, null, "Order not found");
        }
    }

    private boolean canCancelOrder(Order order) {
        String status = String.valueOf(order.getOrderStatus());
        System.out.println("Order status: " + status);
        return "CREATED".equalsIgnoreCase(status);
    }

    @Override
    @Transactional
    public void updateStatus(Long orderId, OrderStatus newStatus) {
        Order order=orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderStatus oldStatus = order.getOrderStatus();
        order.setOrderStatus(newStatus);
        if(newStatus == OrderStatus.DELIVERED)
        {
            order.setOrderDelivered(new Date());
        }
        orderRepository.save(order);
        if(newStatus==OrderStatus.CANCELLED && oldStatus != OrderStatus.CANCELLED)
        {
            refundOrder(order);
        }
    }
    private void refundOrder(Order order)
    {
        Payment payment=order.getPayment();

        if (payment !=null && "PAID".equals(order.getPaymentStatus()))
        {
            paymentService.processRefund(order, payment.getId());
        }
    }

    @Transactional
    public PaymentRefundResponse<Double> processUserFeedback(Long orderItemId, int receivedQuantity) {
        try {
            OrderItem orderItem = orderItemRepository.findById(orderItemId)
                    .orElseThrow(() -> new RuntimeException("Order Item not found"));

            if (receivedQuantity > orderItem.getFooditemQuantity()) {
                return new PaymentRefundResponse<>(false, null, "Received quantity cannot be greater than ordered quantity");
            }

            double refundAmount = calculateRefundAmount(orderItem, receivedQuantity);

            orderItem.setReceivedQuantity(receivedQuantity);
            orderItemRepository.save(orderItem);

            if (orderItem.getReceivedQuantity() == orderItem.getFooditemQuantity()) {
                return new PaymentRefundResponse<>(false, null, "Received quantity is equals to ordered quantity");

            }

            return new PaymentRefundResponse<>(true, refundAmount, "sorry for inconvenience the amount of undeliverd item is being credited to your bank account");
        } catch (Exception ex) {
            log.error("Error while processing user feedback: " + ex.getMessage(), ex);
            return new PaymentRefundResponse<>(false, null, ex.getMessage());
        }
    }



    private double calculateRefundAmount(OrderItem orderItem, int receivedQuantity) {
        int remainingQuantity = orderItem.getFooditemQuantity()- receivedQuantity;
        double unitPrice = orderItem.getTotalFooditemPrice() / orderItem.getFooditemQuantity();
        return remainingQuantity * unitPrice;
    }

    @Override
    @Transactional
    public ServiceResponse<InvoiceDto> generateInvoice(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

            if (!"PAID".equals(order.getPaymentStatus())) {
                return new ServiceResponse<>(false, null, "Payment for this order is not completed");
            }
            byte[] pdfBytes = createPdfInvoice(order);
            sendInvoiceByEmail(order.getUser().getEmail(), pdfBytes);

            InvoiceDto invoiceDto = new InvoiceDto();
            invoiceDto.setPdfBytes(pdfBytes);

            return new ServiceResponse<>(true, invoiceDto, "Invoice generated successfully");
        } catch (Exception ex) {
            return new ServiceResponse<>(false, null, ex.getMessage());
        }
    }

    private void sendInvoiceByEmail(String email, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Invoice for Your Order");
            helper.setText("Please find attached the invoice for your recent order.");

            helper.addAttachment("invoice.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Error sending invoice by email: " + ex.getMessage(), ex);
        }
    }

    private byte[] createPdfInvoice(Order order) throws DocumentException, IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();
            document.add(new Paragraph("Order ID #" + order.getOrderId()));
            document.add(new Paragraph("Invoice for Order #" + order.getInvoice()));
            document.add(new Paragraph("Order Date: " + order.getOrderCreateAt()));
            document.add(new Paragraph("Tax #" + order.getTax()));
            document.add(new Paragraph("Delivery Charge #" + order.getDeliveryCharges()));
            document.add(new Paragraph("Total Amount: $" + order.getOrderAmt()));
            document.close();

            return byteArrayOutputStream.toByteArray();
        }
    }
}






