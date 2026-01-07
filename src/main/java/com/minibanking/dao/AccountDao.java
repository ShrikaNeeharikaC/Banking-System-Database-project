package com.minibanking.dao;

import com.minibanking.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    void save(Account account);
    void update(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAll(); // Add this line
}

