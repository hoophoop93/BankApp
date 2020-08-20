package com.example.bank.bank.services;

import com.example.bank.bank.dao.TransakcjeRepository;
import com.example.bank.bank.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransakcjeService {

    @Autowired
    TransakcjeRepository transakcjeRepository;

    public void zapiszTransakcje(String tytul, double kwota, TypOperacji typOperacji,
                                 Rachunek rachunek, Rachunek rachunek2, Daty daty){
        Transakcje transakcje = new Transakcje();

        transakcje.setKwota(kwota);
        transakcje.setTypOperacjiTransakcje(typOperacji);
        transakcje.setRachunekTransakcje(rachunek);
        transakcje.setRachunekTransakcje2(rachunek2);
        transakcje.setDatyTransakcje(daty);
        transakcje.setTytulPrzelewu(tytul);


        transakcjeRepository.save(transakcje);
    }
}
