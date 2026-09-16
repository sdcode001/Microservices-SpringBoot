package com.sd.accounts.controllers;

import com.sd.accounts.constants.AccountsConstants;
import com.sd.accounts.dtos.*;
import com.sd.accounts.services.AccountsService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.concurrent.TimeoutException;


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
        name = "CRUD REST APIs for Accounts in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
)
public class AccountController {
    private final AccountsService accountsService;

    //Read application property values(from application.yaml) using @Value. it injects value.
    @Value("${build.version}")
    private String buildVersion;

    //Read system environment properties using Environment interface. its field injection.
    @Autowired
    private Environment environment;

    //Inject the ConfigurationProperties bean
    @Autowired
    private AccountsContactInfoDto accountsContactInfo;

    public AccountController(AccountsService accountsService){
        this.accountsService = accountsService;
    }


    /**
     * OpenApi documentation for API endpoint.
     * It provides info about endpoint operation and example response.
     * */
    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer &  Account inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
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
    @PostMapping(value = "/accounts")
    /**
     * @Valid tells spring to perform validation on the following input object.
     * */
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customer){
       accountsService.createAccount(customer);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }


    @Operation(
            summary = "Fetch Account Details REST API",
            description = "REST API to fetch Customer &  Account details based on a mobile number"
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
    @GetMapping(value = "/accounts")
    public ResponseEntity<AccountDto> getAccount(
            @RequestParam
            @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits.")
            String mobileNumber
    ){
        AccountDto accountDto = accountsService.findByCustomerMobileNumber(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(accountDto);
    }


    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to update Customer &  Account details based on a account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
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
    @PutMapping(value = "/accounts")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody AccountDto accountDto){
        boolean isUpdated = accountsService.updateAccount(accountDto);
        if(isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.STATUS_500));
    }


    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
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
    @DeleteMapping(value = "/accounts")
    public ResponseEntity<ResponseDto> deleteAccountByCustomerMobileNumber(
            @RequestParam
            @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits.")
            String mobileNumber
    ){
        boolean isDeleted = accountsService.deleteByCustomerMobileNumber(mobileNumber);
        if(isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.STATUS_500));
    }


    @Operation(
            summary = "Fetch Version Info REST API",
            description = "REST API to fetch version number of Accounts."
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
    //Here implementing endpoint level retry with resilience4j
    @Retry(name = "getVersion", fallbackMethod = "getVersionFallback")
    @GetMapping(value = "/build-info")
    public ResponseEntity<String> getVersion() throws TimeoutException {
        System.out.println("GET /build-info is called...");
        double exceptionPredictor = Math.random();
        if(exceptionPredictor<=0.4){
            throw new TimeoutException();
        }
        else if(exceptionPredictor>0.4 && exceptionPredictor<=0.7){
            throw new NullPointerException();
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.buildVersion);
        }
    }
    //This fallback method will be invoked if all retries attempt fails.
    //Methods signature should be same but with an extra Throwable parameter.
    public ResponseEntity<String> getVersionFallback(Throwable throwable){
        System.out.println("GET /build-info fallback is called...");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("-1");
    }

    @Operation(
            summary = "Fetch Java Version REST API",
            description = "REST API to fetch version of java in system environment of Accounts microservice."
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
    @GetMapping(value = "/java-version")
    public ResponseEntity<String> getJavaVersion(){
        String javaVersion = this.environment.getProperty("JAVA_HOME");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(javaVersion);
    }

    @Operation(
            summary = "Fetch Accounts microservice contact info REST API",
            description = "REST API to fetch contact info of Accounts microservice."
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
    @GetMapping(value = "/accounts-contact-info")
    public ResponseEntity<AccountsContactInfoDto> getAccountContactInfo(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.accountsContactInfo);
    }


}
