package com.ipsator.foodOrderingSystem.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ipsator.foodOrderingSystem.entity.Address;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {
    private Address orderAddress;

    public Address getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(Address orderAddress) {
        this.orderAddress = orderAddress;
    }
}
