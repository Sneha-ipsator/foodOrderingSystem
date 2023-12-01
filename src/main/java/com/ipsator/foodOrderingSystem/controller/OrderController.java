package com.ipsator.foodOrderingSystem.controller;
import com.ipsator.foodOrderingSystem.dto.OrderDto;
import com.ipsator.foodOrderingSystem.entity.Address;
import com.ipsator.foodOrderingSystem.entity.Cart;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.enums.OrderStatus;
import com.ipsator.foodOrderingSystem.payload.*;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.repository.UserRepo;
import com.ipsator.foodOrderingSystem.service.OrderService;
import com.ipsator.foodOrderingSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/place")
    public ResponseEntity<ApiResponse> placeOrder(
            @RequestBody(required = false) OrderRequest orderRequest, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepo.findByEmail(email).get();
            Cart cart = user.getCart();
            long cartId = cart.getCartId();
            if (orderRequest != null && orderRequest.getOrderAddress() != null) {
                Address orderAddress = orderRequest.getOrderAddress();
                ServiceResponse<OrderDto> orderResponse = orderService.orderCreate(cartId, orderAddress, email);
                if (orderResponse.getSuccess()) {
                    return new ResponseEntity<>(new ApiResponse("success", orderResponse.getData(), null), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(new ApiResponse("Error", null, new Error("Failed to create Order")), HttpStatus.NOT_FOUND);
                }
            } else {
                // Place the order on the existing address
                ServiceResponse<OrderDto> orderResponse = orderService.orderCreate(cartId, null, email);
                if (orderResponse.getSuccess()) {
                    return new ResponseEntity<>(new ApiResponse("success", orderResponse.getData(), null), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(new ApiResponse("Error", null, new Error("Failed to create Order")), HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse("Error", null, new Error(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getOrdersForUser(Principal principal) {
        String username = principal.getName();
        List<OrderDto> orders = orderService.getOrdersForUser(username);
        return new ResponseEntity<>(new ApiResponse("success", orders, null), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse>cancelOrder(@PathVariable Long orderId,Principal principal)
    {
        try {
            String email = principal.getName();
            User currentUser = userRepo.findByEmail(email).get();
            ServiceResponse<OrderCancellationResponse> cancelResponse = orderService.cancelOrder(orderId,currentUser);
            if (cancelResponse.getSuccess()) {
                OrderCancellationResponse cancellationResponseData = cancelResponse.getData();
                return new ResponseEntity<>(new ApiResponse("success", cancellationResponseData, null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("Error", null, new Error(cancelResponse.getMessage())), HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(new ApiResponse("Error",ex.getMessage(),new Error("Something went wrong")),HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/status/{orderId}")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long orderId, @RequestBody Map<String, String> requestBody) {
        try {
            String newStatusValue = requestBody.get("newStatus");
            OrderStatus newStatus = OrderStatus.valueOf(newStatusValue);
            orderService.updateStatus(orderId, newStatus);
            return new ResponseEntity<>(new ApiResponse("success", "Order Updated Successfully", null), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiResponse("Error", "Invalid OrderStatus value", new Error(ex.getMessage())), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse("Error", "Error on updating order status", new Error(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user-feedback")
    public ResponseEntity<PaymentRefundResponse<Double>> processUserFeedback(
            @RequestParam Long orderItemId,
            @RequestParam int receivedQuantity) {
        log.info("Received user feedback request. OrderItemId: {}, ReceivedQuantity: {}", orderItemId, receivedQuantity);

        PaymentRefundResponse<Double> response = orderService.processUserFeedback(orderItemId, receivedQuantity);

        if (response.getSuccess()) {
            log.info("User feedback processed successfully. Refund Amount: {}", response.getRefund());
            return ResponseEntity.ok(response);
        } else {
            log.error("Error processing user feedback: {}", response.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }



}