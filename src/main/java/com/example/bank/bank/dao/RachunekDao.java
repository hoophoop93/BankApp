package com.example.bank.bank.dao;

import com.example.bank.bank.models.Rachunek;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class RachunekDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void update(Rachunek rachunek) {
        entityManager.merge(rachunek);
    }

    public boolean sprawdzCzyNumerKontaJestWBazie(String numerKonta) {
        String queryString = "SELECT count(o.numerKonta) FROM Rachunek o where LOWER(o.numerKonta) = :numerKonta";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("numerKonta", numerKonta.toLowerCase());

        return (query.getSingleResult() > 0);
    }
}
