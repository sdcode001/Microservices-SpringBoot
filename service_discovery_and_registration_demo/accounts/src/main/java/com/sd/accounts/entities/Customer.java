package com.sd.accounts.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "customer")
//These are lombok annotations for boilerplate code and automatically generates the actual Java bytecode during compilation.
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Customer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;
    private String name;
    private String email;
    private String mobileNumber;
}
