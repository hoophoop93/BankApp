package com.example.bank.bank.dao;

import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import org.springframework.data.repository.CrudRepository;

public interface RachunekRepository extends CrudRepository<Rachunek,Long> {

    public Rachunek findByRachunekKlienta(Klient klient);
    public Rachunek findBynumerKonta(String numerKonta);


}
