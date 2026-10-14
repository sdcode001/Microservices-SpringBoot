package com.sd.accounts.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "accounts")
//These are lombok annotations for boilerplate code and automatically generates the actual Java bytecode during compilation.
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Account extends BaseEntity{
   @Id
   private long accountNumber;
   private int customerId;
   private String accountType;
   private String branchAddress;
}
