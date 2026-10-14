package com.sd.accounts.services.clients.fallbacks;

import com.sd.accounts.dtos.CardsDto;
import com.sd.accounts.services.clients.CardsFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFeignClientFallback implements CardsFeignClient {
    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String mobileNumber) {
        //Add required fallback logic here
        return null;
    }
}
