package com.minibanking.dao;

import com.minibanking.model.Customer;
import com.minibanking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public void save(Customer customer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Customer customer = session.get(Customer.class, id);
            return Optional.ofNullable(customer);
        }
    }

    @Override
    public List<Customer> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // This HQL (Hibernate Query Language) query fetches all records from the Customer table.
            return session.createQuery("from Customer", Customer.class).list();
        }
    }
}

