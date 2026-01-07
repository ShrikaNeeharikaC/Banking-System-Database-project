package com.minibanking.dao;

import com.minibanking.model.Account;
import com.minibanking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    @Override
    public void save(Account account) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Account account) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(account); // Use merge for updates
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                    "SELECT a FROM Account a JOIN FETCH a.customer WHERE a.accountNumber = :accountNumber", Account.class);
            query.setParameter("accountNumber", accountNumber);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public List<Account> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // We use "JOIN FETCH" to also load the associated customer details in a single query
            // This prevents the LazyInitializationException we saw earlier.
            return session.createQuery("SELECT a FROM Account a JOIN FETCH a.customer", Account.class).list();
        }
    }
}

