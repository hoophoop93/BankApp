package com.example.bank.bank.dao;

import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.StaliOdbiorcy;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KlientRepository extends CrudRepository<Klient,Long> {


    public Klient findByPesel(String pesel);

    public Klient findByIdKlient(Long id);

    public Klient findByrachunekiDanegoKlienta(Rachunek rachunek);



}
