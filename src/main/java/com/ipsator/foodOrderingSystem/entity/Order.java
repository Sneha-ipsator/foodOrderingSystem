package com.ipsator.foodOrderingSystem.entity;

import com.ipsator.foodOrderingSystem.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String paymentStatus;
    private Date orderDelivered;
    private double tax;
    private double deliveryCharges;
    private double orderAmt;
    private Date orderCreateAt;

    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @Column(unique = true, nullable = false, length = 255)
    private String invoice;


    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItem = new ArrayList<>();

    @OneToOne(mappedBy = "order",cascade =CascadeType.ALL)
    private Payment payment;

}
