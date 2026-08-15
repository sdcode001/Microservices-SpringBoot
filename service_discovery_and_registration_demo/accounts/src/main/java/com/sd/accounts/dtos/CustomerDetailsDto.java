package com.sd.accounts.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Schema(
        name = "CustomerDetailsDto",
        description = "Schema to hold Customer, Account, Loan and Card information"
)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CustomerDetailsDto {
    @Schema(
            description = "Name of the customer"
    )
    private String name;
    @Schema(
            description = "Email of the customer"
    )
    private String email;
    @Schema(
            description = "Phone number of the customer"
    )
    private String mobileNumber;
    @Schema(
            description = "Account details of the customer"
    )
    private AccountDto accountDto;
    @Schema(
            description = "Card details of the customer"
    )
    private CardsDto cardsDto;
    @Schema(
            description = "Loan details of the customer"
    )
    private LoansDto loansDto;
}
