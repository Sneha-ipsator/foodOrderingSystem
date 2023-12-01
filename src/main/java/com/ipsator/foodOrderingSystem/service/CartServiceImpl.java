package com.ipsator.foodOrderingSystem.service;
import com.ipsator.foodOrderingSystem.dto.CartDto;
import com.ipsator.foodOrderingSystem.dto.CartItemDto;
import com.ipsator.foodOrderingSystem.dto.FoodItemsDto;
import com.ipsator.foodOrderingSystem.dto.UserDto;
import com.ipsator.foodOrderingSystem.entity.Cart;
import com.ipsator.foodOrderingSystem.entity.CartItem;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.*;
import com.ipsator.foodOrderingSystem.repository.CartRepository;
import com.ipsator.foodOrderingSystem.repository.FoodItemsRepository;
import com.ipsator.foodOrderingSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private FoodItemsRepository foodItemsRepository;

    @Autowired
    private UserRepo userRepo;


    public CartDto mapCartToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        return cartDto;
    }

    public FoodItemsDto mapFoodItemsToFoodItemsDto(FoodItems foodItems) {
        FoodItemsDto foodItemsDto = new FoodItemsDto();
        foodItemsDto.setId(foodItems.getId());
        foodItemsDto.setName(foodItems.getName());
        foodItemsDto.setPrice(foodItems.getPrice());
        foodItemsDto.setFoodType(foodItems.getFoodType());
        foodItemsDto.setCuisine(foodItems.getCuisine());
        foodItemsDto.setFilter(foodItems.getFilter());

        return foodItemsDto;
    }
    public CartItemDto mapCartItemToCartItemDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCartItemId(cartItem.getCartItemId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setTotalPrice(cartItem.getTotalPrice());
        FoodItems foodItems = cartItem.getFoodItems();
        FoodItemsDto foodItemsDto = mapFoodItemsToFoodItemsDto(foodItems);

        cartItemDto.setFoodItems(foodItemsDto);

        return cartItemDto;
    }


    public CartDto addItem(ItemRequest item, String username) {
        Long productId = item.getFoodItemId();
        int quantity = item.getQuantity();
        User user = this.userRepo.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        FoodItems foodItems = this.foodItemsRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (foodItems != null) {
            Cart cart = user.getCart();

            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
            }
            boolean itemExists = false;
            CartItem existingCartItem = null;
            for (CartItem cartItem : cart.getItem()) {
                if (cartItem.getFoodItems().getId().equals(foodItems.getId())) {
                    existingCartItem = cartItem;
                    itemExists = true;
                    break;
                }
            }

            if (itemExists) {
                int newQuantity = existingCartItem.getQuantity() + quantity;
                existingCartItem.setQuantity(newQuantity);
                existingCartItem.setTotalPrice(foodItems.getPrice() * newQuantity);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setFoodItems(foodItems);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(foodItems.getPrice() * quantity);
                cartItem.setCart(cart);

                cart.getItem().add(cartItem);
            }

            this.cartRepository.save(cart);
            CartDto cartDto = mapCartToCartDto(cart);

            List<CartItemDto> itemDtos = cart.getItem().stream()
                    .map(this::mapCartItemToCartItemDto)
                    .collect(Collectors.toList());

            cartDto.setItem(itemDtos);


            return cartDto;
        }

        throw new RuntimeException("Product out of stock");
    }


    @Override
    public CartDto getcartAll(String email) {
        User user=this.userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Cart cart=this.cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("There is no cart"));
        CartDto cartDto = new CartDto();
        cartDto.setUser(new UserDto(user));
        Cart savedCart = this.cartRepository.save(cart);

        List<CartItemDto> itemDtos = savedCart.getItem().stream()
                .map(this::mapCartItemToCartItemDto)
                .collect(Collectors.toList());

        cartDto.setItem(itemDtos);
        cartDto.setCartId(cart.getCartId());
        return cartDto;
    }

    public CartDto getCartByID(int cartId){
        Cart cart= this.cartRepository.findBycartId((long) cartId).orElseThrow(()->new RuntimeException("There is no cart"));
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());


        List<CartItem> cartItems = cart.getItem();
        List<CartItemDto> cartItemDtos = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setCartItemId(cartItem.getCartItemId());
            cartItemDto.setQuantity(cartItem.getQuantity());
            cartItemDto.setTotalPrice(cartItem.getTotalPrice());


            FoodItems foodItems = cartItem.getFoodItems();
            FoodItemsDto foodItemsDto = new FoodItemsDto();
            foodItemsDto.setId(foodItems.getId());
            foodItemsDto.setName(foodItems.getName());
            foodItemsDto.setPrice(foodItems.getPrice());
            foodItemsDto.setFoodType(foodItems.getFoodType());
            foodItemsDto.setCuisine(foodItems.getCuisine());
            foodItemsDto.setFilter(foodItems.getFilter());


            cartItemDto.setFoodItems(foodItemsDto);

            cartItemDtos.add(cartItemDto);

        }
        cartDto.setItem(cartItemDtos);

        User user = cart.getUser();
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setGender(user.getGender());
        userDto.setRole(user.getRole());
        userDto.setEmail(user.getEmail());

        cartDto.setUser(userDto);


        return cartDto;
    }

    public CartDto removeCartItemFromCart(String userName, int foodItemId) {
        User user = this.userRepo.findByEmail(userName).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        List<CartItem> items = cart.getItem();

        items.removeIf(item -> item.getFoodItems().getId() == foodItemId);

        Cart savedCart = this.cartRepository.save(cart);

        CartDto cartDto = new CartDto();
        cartDto.setCartId(savedCart.getCartId());

        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : savedCart.getItem()) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setCartItemId(cartItem.getCartItemId());
            cartItemDto.setQuantity(cartItem.getQuantity());
            cartItemDto.setTotalPrice(cartItem.getTotalPrice());

            FoodItems foodItems = cartItem.getFoodItems();
            FoodItemsDto foodItemsDto = new FoodItemsDto();
            foodItemsDto.setId(foodItems.getId());
            foodItemsDto.setName(foodItems.getName());
            foodItemsDto.setPrice(foodItems.getPrice());

            cartItemDto.setFoodItems(foodItemsDto);

            cartItemDtos.add(cartItemDto);
        }

        cartDto.setItem(cartItemDtos);

        User saveUser = savedCart.getUser();
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(saveUser.getFirstName());
        userDto.setLastName(saveUser.getLastName());
        userDto.setGender(saveUser.getGender());
        userDto.setRole(saveUser.getRole());
        userDto.setEmail(saveUser.getEmail());

        cartDto.setUser(userDto);

        return cartDto;
    }



}
