package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
