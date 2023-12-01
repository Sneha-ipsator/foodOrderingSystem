package com.ipsator.foodOrderingSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private  Long cartItemId;
    private Integer quantity;
    private double totalPrice;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private FoodItems foodItems;
}
