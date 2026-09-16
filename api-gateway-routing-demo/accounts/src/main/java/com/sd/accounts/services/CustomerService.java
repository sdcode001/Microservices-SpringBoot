package com.sd.accounts.services;

import com.sd.accounts.daos.AccountsDao;
import com.sd.accounts.daos.CustomerDao;
import com.sd.accounts.dtoMappers.AccountMapper;
import com.sd.accounts.dtoMappers.CustomerMapper;
import com.sd.accounts.dtos.AccountDto;
import com.sd.accounts.dtos.CardsDto;
import com.sd.accounts.dtos.CustomerDetailsDto;
import com.sd.accounts.dtos.LoansDto;
import com.sd.accounts.entities.Account;
import com.sd.accounts.entities.Customer;
import com.sd.accounts.exceptions.ResourceNotFoundException;
import com.sd.accounts.services.clients.CardsFeignClient;
import com.sd.accounts.services.clients.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService{
    private AccountsDao accountsDao;
    private CustomerDao customerDao;
    private LoansFeignClient loansFeignClient;
    private CardsFeignClient cardsFeignClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Optional<Customer> optionalCustomer = customerDao.findByMobileNumber(mobileNumber);
        if(!optionalCustomer.isPresent()){
            throw new ResourceNotFoundException("Customer", "MobileNumber", mobileNumber);
        }

        Optional<Account> foundAccount = accountsDao.findByCustomerId(optionalCustomer.get().getCustomerId());
        if(!foundAccount.isPresent()){
            throw new ResourceNotFoundException("Account", "MobileNumber", mobileNumber);
        }

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(optionalCustomer.get(), new CustomerDetailsDto());
        customerDetailsDto.setAccountDto(AccountMapper.mapToAccountDto(foundAccount.get(), new AccountDto()));

        //Invoke Loans and Cards Feign Clients to get loan and card details
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);

        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        return customerDetailsDto;
    }

}
