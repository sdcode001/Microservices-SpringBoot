package com.sd.accounts.controllers;

import com.sd.accounts.dtos.CustomerDetailsDto;
import com.sd.accounts.dtos.ErrorResponseDto;
import com.sd.accounts.services.CustomerService;
import com.sd.accounts.services.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
/**
 * @Validated tells spring to perform validation to all the endpoints in this controller
 * */
@Validated
/**
 * OpenAPi documentation for controller
 * */
@Tag(
        name = "REST API for Customer in EazyBank",
        description = "REST API in EazyBank to FETCH customer details"
)
public class CustomerController
{
    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService){
        this.customerService = customerService;
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch whole Customer details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping(value = "/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails (
            @RequestParam
            @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits.")
            String mobileNumber
    ){
        CustomerDetailsDto customerDetailsDto = customerService.fetchCustomerDetails(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(customerDetailsDto);
    }


}
