package com.example.bank.bank.dao;

import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.ZlecenieStale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ZleceniaStaleRepository extends CrudRepository<ZlecenieStale,Long> {

    public ZlecenieStale findBydataWykoniani(String data);
    public List<ZlecenieStale> findByklientZleceniaStale(Klient klient);
}
