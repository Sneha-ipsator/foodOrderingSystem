package com.ipsator.foodOrderingSystem.controller;


import com.ipsator.foodOrderingSystem.dto.PaymentDto;
import com.ipsator.foodOrderingSystem.entity.Payment;
import com.ipsator.foodOrderingSystem.payload.ApiResponse;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.payload.PaymentRequest;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/place/{orderId}")
    public ResponseEntity<ApiResponse> processPayment(@PathVariable Long orderId, @RequestBody PaymentRequest paymentRequest)
    {
     try{
         ServiceResponse<PaymentDto> paymentResponse = paymentService.processPayment(orderId,paymentRequest);
         if(paymentResponse.getSuccess())
         {
             return new ResponseEntity<>(new ApiResponse("success","Payment done",null),HttpStatus.CREATED);
         }
         else {
             return new ResponseEntity<>(new ApiResponse("Error",null,new Error("Payment Failed")),HttpStatus.BAD_REQUEST);
         }

     }
     catch(Exception ex)
     {
         return new ResponseEntity<>(new ApiResponse("Error",null,new Error(ex.getMessage())),HttpStatus.INTERNAL_SERVER_ERROR);
     }
    }
}
