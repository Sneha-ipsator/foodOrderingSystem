package com.ipsator.foodOrderingSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartDto {
    private Long cartId;
    private List<CartItemDto> item=new ArrayList<>();

    @JsonIgnore
    private UserDto user;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemDto> getItem() {
        return item;
    }

    public void setItem(List<CartItemDto> item) {
        this.item = item;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
