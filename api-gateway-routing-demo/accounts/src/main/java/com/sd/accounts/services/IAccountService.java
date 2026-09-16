package com.sd.accounts.services;

import com.sd.accounts.dtos.AccountDto;
import com.sd.accounts.dtos.CustomerDto;


public interface IAccountService {
    void createAccount(CustomerDto customerDto);
    AccountDto findByCustomerMobileNumber(String mobileNumber);
    boolean updateAccount(AccountDto accountDto);
    boolean deleteByCustomerMobileNumber(String mobileNumber);
}
