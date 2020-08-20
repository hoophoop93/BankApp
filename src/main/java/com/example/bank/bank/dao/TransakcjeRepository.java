package com.example.bank.bank.dao;

import com.example.bank.bank.models.Daty;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.Transakcje;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransakcjeRepository extends CrudRepository<Transakcje,Long> {

    public List <Transakcje> findByrachunekTransakcje(Rachunek rachunek);
    public List<Transakcje> findByrachunekTransakcje2(Rachunek rachunek);


    public List<Transakcje> findBydatyTransakcje(Daty daty);

    public Transakcje findAllByidTransakcji(long idTransakcji);
}
