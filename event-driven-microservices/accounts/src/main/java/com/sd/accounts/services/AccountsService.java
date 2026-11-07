package com.sd.accounts.services;

import com.sd.accounts.constants.AccountsConstants;
import com.sd.accounts.daos.AccountsDao;
import com.sd.accounts.daos.CustomerDao;
import com.sd.accounts.dtoMappers.AccountMapper;
import com.sd.accounts.dtoMappers.CustomerMapper;
import com.sd.accounts.dtos.AccountDto;
import com.sd.accounts.dtos.AccountsMessageDto;
import com.sd.accounts.dtos.CustomerDto;
import com.sd.accounts.entities.Account;
import com.sd.accounts.entities.Customer;
import com.sd.accounts.exceptions.CustomerAlreadyExistsException;
import com.sd.accounts.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
public class AccountsService implements IAccountService{
    private AccountsDao accountsDao;
    private CustomerDao customerDao;
    private StreamBridge streamBridge;
    private static final Logger log = LoggerFactory.getLogger(AccountsService.class);

    public AccountsService(AccountsDao accountsDao, CustomerDao customerDao, StreamBridge streamBridge){
        this.accountsDao = accountsDao;
        this.customerDao = customerDao;
        this.streamBridge = streamBridge;
    }

    @Override
    @Transactional
    public void createAccount(CustomerDto customerDto) {
        //Save Customer
        Customer newCustomer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerDao.findByMobileNumber(newCustomer.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber: "+newCustomer.getMobileNumber());
        }
        newCustomer.setCreatedAt(LocalDateTime.now());
        newCustomer.setCreatedBy("SYSTEM_ADMIN");
        Customer savedCustomer = customerDao.save(newCustomer);

        //Save Account
        Account newAccount = new Account();
        newAccount.setCustomerId(savedCustomer.getCustomerId());
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("SYSTEM_ADMIN");
        newAccount.setCommunicationSw(false);
        accountsDao.save(newAccount);

        //Send communication event to messageservice
        sendCommunication(newAccount, newCustomer);
    }

    private void sendCommunication(Account account, Customer customer){
        AccountsMessageDto accountsMessageDto = new AccountsMessageDto(account.getAccountNumber(), customer.getName(), customer.getEmail(), customer.getMobileNumber());
        log.info("Sending account create communication details: {}", accountsMessageDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMessageDto);
        log.info("Is communication event successfully triggered: {}", result);
    }

    @Override
    public AccountDto findByCustomerMobileNumber(String mobileNumber) {
        Optional<Customer> optionalCustomer = customerDao.findByMobileNumber(mobileNumber);
        if(!optionalCustomer.isPresent()){
            throw new ResourceNotFoundException("Customer", "MobileNumber", mobileNumber);
        }

        Optional<Account> foundAccount = accountsDao.findByCustomerId(optionalCustomer.get().getCustomerId());
        if(!foundAccount.isPresent()){
            throw new ResourceNotFoundException("Account", "MobileNumber", mobileNumber);
        }
        AccountDto accountsDto = AccountMapper.mapToAccountDto(foundAccount.get(), new AccountDto());
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(optionalCustomer.get(), new CustomerDto());
        accountsDto.setCustomer(customerDto);
        return accountsDto;
    }

    @Override
    @Transactional
    public boolean updateAccount(AccountDto accountDto) {
        boolean isUpdated = false;
        CustomerDto customerDto = accountDto.getCustomer();
        if(accountDto != null && customerDto != null){
            //Update Account
            Account account = accountsDao.findById(accountDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "accountNumber", String.valueOf(accountDto.getAccountNumber()))
            );
            Account updatedAccount = AccountMapper.mapToAccount(accountDto, account);
            updatedAccount.setUpdatedAt(LocalDateTime.now());
            updatedAccount.setUpdatedBy(customerDto.getName());
            account = accountsDao.save(updatedAccount);

            //Update Account Customer
            int customerId = account.getCustomerId();
            Customer customer = customerDao.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "accountNumber", String.valueOf(accountDto.getAccountNumber()))
            );
            Customer updatedCustomer = CustomerMapper.mapToCustomer(customerDto, customer);
            updatedCustomer.setUpdatedAt(LocalDateTime.now());
            updatedCustomer.setUpdatedBy(customerDto.getName());
            customerDao.save(updatedCustomer);

            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    @Transactional
    public boolean deleteByCustomerMobileNumber(String mobileNumber) {
        Customer customer = customerDao.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        accountsDao.deleteByCustomerId(customer.getCustomerId());
        customerDao.deleteById(customer.getCustomerId());
        return true;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber != null){
            Account account = accountsDao.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            account.setCommunicationSw(true);
            accountsDao.save(account);
            isUpdated = true;
        }
        return isUpdated;
    }


}
