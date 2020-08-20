package com.example.bank.bank.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class ZleceniaStaleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean zliczDaty(String dataWykoniani) {
        String queryString = "SELECT count(o.dataWykoniani) FROM ZlecenieStale o where LOWER(o.dataWykoniani) = :dataWykoniani";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("dataWykoniani", dataWykoniani.toLowerCase());

        return (query.getSingleResult() > 0);
    }
}
