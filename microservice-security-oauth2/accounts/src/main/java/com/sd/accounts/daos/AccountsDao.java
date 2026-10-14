package com.sd.accounts.daos;

import com.sd.accounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface AccountsDao extends JpaRepository<Account, Long>{
    /**
     * This is Derived-Name-Method support of JpaRepository.
     * Here 'findBy' clause tells Jpa to execute select query and filter a single row by customerId.
     * */
    Optional<Account> findByCustomerId(int customerId);

    /**
     * This is Derived-Name-Method support of JpaRepository.
     * Here 'deleteBy' clause tells Jpa to execute delete query for a single row by customerId.
     * */
    @Transactional
    @Modifying
    void deleteByCustomerId(int customerId);
}
