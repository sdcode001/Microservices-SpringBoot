package com.sd.accounts.dtos;

import lombok.*;
import org.springframework.http.HttpStatusCode;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class ResponseDto {
    private String statusCode;
    private String message;
}
