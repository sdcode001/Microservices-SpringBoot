package com.sd.accounts.dtos;

import jakarta.validation.constraints.Email;
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
public class CustomerDto {
    /**
     * DTO fields should have input validation using spring-boot-starter-validation.
     * It provides wide range of validation constraint annotations.
     * */
    @NotEmpty(message = "Name can not be null or empty.")
    @Size(min = 2, max = 30, message = "The name must be 2 to 30 characters long.")
    private String name;
    @NotEmpty(message = "Email can not be null or empty.")
    @Email(message = "Email should be valid value.")
    private String email;
    @NotEmpty(message = "Mobile number can not be null or empty.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits.")
    private String mobileNumber;
}
