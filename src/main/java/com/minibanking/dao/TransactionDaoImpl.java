package com.minibanking.dao;

import com.minibanking.model.Transaction;
import com.minibanking.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class TransactionDaoImpl implements TransactionDao {

    @Override
    public void save(Transaction transaction) {
        org.hibernate.Transaction hibernateTransaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            hibernateTransaction = session.beginTransaction();
            session.persist(transaction);
            hibernateTransaction.commit();
        } catch (Exception e) {
            if (hibernateTransaction != null) {
                hibernateTransaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use JOIN FETCH to eagerly load the associated accounts to prevent LazyInitializationException
            // Using LEFT JOIN allows for transactions like deposits/withdrawals where one account may be null.
            return session.createQuery(
                    "SELECT t FROM Transaction t LEFT JOIN FETCH t.fromAccount LEFT JOIN FETCH t.toAccount ORDER BY t.transactionDate DESC",
                    Transaction.class
            ).list();
        }
    }
}

