package com.sd.accounts.services;

import com.sd.accounts.dtos.CustomerDetailsDto;


public interface ICustomerService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);

}
