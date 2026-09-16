package com.sd.accounts.services.clients.fallbacks;

import com.sd.accounts.dtos.LoansDto;
import com.sd.accounts.services.clients.LoansFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFeignClientFallback implements LoansFeignClient {
    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String mobileNumber) {
        //Add required fallback logic here
        return null;
    }
}
