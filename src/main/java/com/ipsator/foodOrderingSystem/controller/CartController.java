package com.ipsator.foodOrderingSystem.controller;

import com.ipsator.foodOrderingSystem.dto.CartDto;
import com.ipsator.foodOrderingSystem.payload.*;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse> addtoCart(@RequestBody ItemRequest itemRequest, Principal principal) {
        try {
            CartDto addItem = this.cartService.addItem(itemRequest, principal.getName());
            if(addItem !=null)
            {
                return new ResponseEntity<>(new ApiResponse("success",addItem,null),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new ApiResponse("Error",null,new Error("item not present")),HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(new ApiResponse("Error",ex.getMessage(),new Error("Something went wrong")),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getAllCart(Principal principal) {
        CartDto getcartAll = this.cartService.getcartAll(principal.getName());

        return new ResponseEntity<CartDto>(getcartAll, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable int cartId){
        System.out.println(cartId);
        CartDto cartByID=this.cartService.getCartByID(cartId);
        return new ResponseEntity<CartDto>(cartByID,HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("foodItems/{fId}")
    public ResponseEntity<ApiResponse> removeCartItemFromCart(@PathVariable int fId,Principal p){
        try {
            CartDto remove = this.cartService.removeCartItemFromCart(p.getName(), fId);
            return new ResponseEntity<>(new ApiResponse("success",remove,null),HttpStatus.OK);
        }
        catch(Exception ex)
        {
            return  new ResponseEntity<>(new ApiResponse("Error",ex.getMessage(),new Error("Something went wrong")),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
