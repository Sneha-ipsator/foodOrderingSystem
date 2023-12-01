package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.CartDto;
import com.ipsator.foodOrderingSystem.payload.ItemRequest;

public interface CartService {

    public CartDto addItem(ItemRequest itemRequest, String username);

    public CartDto getcartAll(String email);

    CartDto getCartByID(int cartId);

    CartDto removeCartItemFromCart(String name, int cartId);
}
