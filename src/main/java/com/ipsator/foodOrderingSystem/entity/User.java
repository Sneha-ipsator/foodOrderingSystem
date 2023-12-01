package com.ipsator.foodOrderingSystem.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "Email")
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;


    private String lastName;


    private String gender;

//    private boolean active;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Address> address=new ArrayList<>();

    private String role;

    @OneToOne(mappedBy = "user")
    private Cart cart;

}


