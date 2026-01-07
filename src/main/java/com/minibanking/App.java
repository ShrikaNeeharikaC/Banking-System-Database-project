package com.minibanking;

import com.minibanking.dao.AccountDao;
import com.minibanking.dao.AccountDaoImpl;
import com.minibanking.dao.CustomerDao;
import com.minibanking.dao.CustomerDaoImpl;
import com.minibanking.model.Account;
import com.minibanking.model.Customer;
import com.minibanking.util.HibernateUtil;

import java.math.BigDecimal;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        // Create DAO instances
        CustomerDao customerDao = new CustomerDaoImpl();
        AccountDao accountDao = new AccountDaoImpl();

        // 1. Create a new Customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhoneNumber("1234567890");
        customer.setAddress("123 Main St");

        // Save the customer to the database
        customerDao.save(customer);
        System.out.println("Customer saved with ID: " + customer.getCustomerId());

        // 2. Create a new Account for this Customer
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 16)); // Generate a random account number
        account.setAccountType("Savings");
        account.setBalance(new BigDecimal("1000.00"));

        // Save the account to the database
        accountDao.save(account);
        System.out.println("Account saved with ID: " + account.getAccountId() + " for Customer: " + customer.getFirstName());
        System.out.println("Initial Balance: " + account.getBalance());

        // 3. Retrieve and verify
        accountDao.findByAccountNumber(account.getAccountNumber()).ifPresent(retrievedAccount -> {
            System.out.println("Successfully retrieved account: " + retrievedAccount.getAccountNumber());
            System.out.println("Balance from DB: " + retrievedAccount.getBalance());
            System.out.println("Customer Name: " + retrievedAccount.getCustomer().getFirstName());
        });

        // Shutdown Hibernate
        HibernateUtil.shutdown();
    }
}
