package com.example.bank.bank.dao;

import com.example.bank.bank.models.Daty;
import com.example.bank.bank.models.Transakcje;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface DatyRepository extends CrudRepository<Daty,Long> {

    public Daty findBydata(String data);

    public Daty findBytransakcjeList(Transakcje transakcje);
}
