package com.example.bank.bank.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;

@Repository
public class DatyDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean zliczDaty(String data) {
        String queryString = "SELECT count(o.data) FROM Daty o where LOWER(o.data) = :data";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("data", data.toLowerCase());

        return (query.getSingleResult() > 0);
    }
}
