package com.sd.accounts.dtos;

import lombok.*;
import org.springframework.http.HttpStatusCode;
import java.time.LocalDateTime;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class ErrorResponseDto {
    private String apiPath;
    private HttpStatusCode statusCode;
    private String errorMessage;
    private LocalDateTime timestamp;
}
