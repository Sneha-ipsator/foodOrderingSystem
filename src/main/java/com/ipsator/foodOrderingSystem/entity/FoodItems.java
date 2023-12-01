package com.ipsator.foodOrderingSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ipsator.foodOrderingSystem.enums.Cuisine;
import com.ipsator.foodOrderingSystem.enums.FoodFilter;
import com.ipsator.foodOrderingSystem.enums.FoodType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;

    @Enumerated(EnumType.STRING)
    private FoodFilter filter;


    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private  Restaurant restaurant;

    @OneToMany(mappedBy = "foodItem")
    @JsonBackReference
    private List<OrderItem> orderItems = new ArrayList<>();

    private boolean limitedTimeItem;
    private boolean enabled;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
