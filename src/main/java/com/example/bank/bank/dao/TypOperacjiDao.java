package com.example.bank.bank.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;

@Repository
public class TypOperacjiDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean zliczRodzajeOperacji(String rodzajOperacji) {
        String queryString = "SELECT count(o.rodzajOperacji) FROM TypOperacji o where LOWER(o.rodzajOperacji) = :rodzajOperacji";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("rodzajOperacji", rodzajOperacji.toLowerCase());

        return (query.getSingleResult() > 0);
    }
}
