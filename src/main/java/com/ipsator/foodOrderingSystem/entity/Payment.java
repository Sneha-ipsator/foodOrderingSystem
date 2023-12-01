package com.ipsator.foodOrderingSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private double amount;
      @Temporal(TemporalType.TIMESTAMP)
      private Date paymentDate;
      private String modeOfPayment;
      @OneToOne
      @JoinColumn(name = "order_id")
      private Order order;
}
