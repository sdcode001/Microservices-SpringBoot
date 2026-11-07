package com.sd.accounts.functions;

import com.sd.accounts.services.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;


@Configuration
public class AccountsFunction {
    private static final Logger log = LoggerFactory.getLogger(AccountsFunction.class);

    @Bean
    public Consumer<Long> updateMessageCommunication(IAccountService accountService){
        return accountNumber -> {
            log.info("Updating communication status for the account number: {}", accountNumber.toString());
            var result = accountService.updateCommunicationStatus(accountNumber);
            log.info("Is communication status updated to Database: {}", result);
        };
    }
}
