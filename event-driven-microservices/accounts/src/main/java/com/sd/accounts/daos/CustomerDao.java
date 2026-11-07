package com.sd.accounts.daos;

import com.sd.accounts.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * This Dao extends JpaRepository to DB interaction for customer table
 * */
@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {

    /**
     * This is Derived-Name-Method support of JpaRepository.
     * Here 'findBy' clause tells Jpa to execute select query and filter a single row by mobileNumber.
     * */
    Optional<Customer> findByMobileNumber(String mobileNumber);

    /**
     * This is Derived-Name-Method support of JpaRepository.
     * Here 'findBy' clause tells Jpa to execute select query and filter a single row by
     * mobileNumber and email.
     * */
    Optional<Customer> findByMobileNumberAndEmail(String mobileNumber, String email);
}
