package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

}
