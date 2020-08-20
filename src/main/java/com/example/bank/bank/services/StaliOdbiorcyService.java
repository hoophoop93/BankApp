package com.example.bank.bank.services;

import com.example.bank.bank.dao.StaliOdbiorcyRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.StaliOdbiorcy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaliOdbiorcyService {

    @Autowired
    StaliOdbiorcyRepository staliOdbiorcyRepository;

    public void zapiszStalegoOdiorce(Klient klient, Klient klient2) {
        StaliOdbiorcy staliOdbiorcy = new StaliOdbiorcy();

        staliOdbiorcy.setKlientPierwszy(klient);
        staliOdbiorcy.setKlientDrugi(klient2);
        staliOdbiorcyRepository.save(staliOdbiorcy);


    }
}
