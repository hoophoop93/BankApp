package com.example.bank.bank.dao;

import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.Transakcje;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TransakcjeDao {


    @PersistenceContext
    private EntityManager entityManager;


}
