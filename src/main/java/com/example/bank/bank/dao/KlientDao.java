package com.example.bank.bank.dao;


import com.example.bank.bank.models.Klient;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class KlientDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void update(Klient klient) {
        entityManager.merge(klient);
    }

    public boolean zliczKlientow(String pesel) {
        String queryString = "SELECT count(o.pesel) FROM Klient o where LOWER(o.pesel) = :pesel";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("pesel", pesel.toLowerCase());

        return (query.getSingleResult() > 0);
    }


}
