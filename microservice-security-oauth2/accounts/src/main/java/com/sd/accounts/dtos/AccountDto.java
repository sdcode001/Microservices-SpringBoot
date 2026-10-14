package com.sd.accounts.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * Using DTO pattern is a good practice. DTO(Data Transfer Object) pattern allows you to
 * transfer data between different layers of app with simple object that only contains data.
 * Such as presentation layer and data access layer.
 *
 * Benefits:
 *  Reduces network round-trips: Aggregates data from multiple internal objects into a single.
 *  Filters sensitive data: Restricts fields like password hashes, internal IDs.
 *  Minimizes payload size: Strips out unnecessary fields, reducing bandwidth.
 *  Hides internal structures: Prevents external clients from seeing your exact database schema/entities
 * */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class AccountDto {
    /**
     * DTO fields should have input validation using spring-boot-starter-validation.
     * It provides wide range of validation constraint annotations.
     * */
    @NotEmpty(message = "Account number can not be null or empty.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Account number must be 10 digits.")
    private long accountNumber;
    @NotEmpty(message = "Account type can not be null or empty.")
    private String accountType;
    @NotEmpty(message = "Branch address can not be null or empty.")
    @Size(max = 50, message = "Branch address can not be more than 5 characters long.")
    private String branchAddress;
    private CustomerDto customer;
}
