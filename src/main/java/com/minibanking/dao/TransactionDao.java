package com.minibanking.dao;

import com.minibanking.model.Transaction;

import java.util.List;

public interface TransactionDao {
    void save(Transaction transaction);
    List<Transaction> findAll(); // Add this line
}

