package com.sd.accounts.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * @MappedSuperclass tells JPA that this class acts as super class for other
 * entity classes whoever extends this.
 * */
@MappedSuperclass
//These are lombok annotations for boilerplate code and automatically generates the actual Java bytecode during compilation.
@Getter @Setter @ToString
public class BaseEntity {
   //This tells JPA that these columns value can't be updated once inserted
   @Column(updatable = false)
   private LocalDateTime createdAt;
   @Column(updatable = false)
   private String createdBy;

   //This tells JPA that these columns value can't be inserted, only can updated and initially null
   @Column(insertable = false)
   private LocalDateTime updatedAt;
   @Column(insertable = false)
   private String updatedBy;
}
