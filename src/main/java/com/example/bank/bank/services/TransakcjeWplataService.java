package com.example.bank.bank.services;

import com.example.bank.bank.dao.TransakcjeRepository;
import com.example.bank.bank.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransakcjeWplataService {

    @Autowired
    TransakcjeRepository transakcjeRepository;

    public void zapiszTransakcjeWplata(Administrator administrator, double kwota, TypOperacji typOperacji,
                                 Rachunek rachunek, Daty daty,String tytul){
        Transakcje transakcje = new Transakcje();

        transakcje.setKwota(kwota);
        transakcje.setTypOperacjiTransakcje(typOperacji);
        transakcje.setRachunekTransakcje(rachunek);
        transakcje.setRachunekTransakcje2(rachunek);
        transakcje.setDatyTransakcje(daty);
        transakcje.setAdministratorTransakcje(administrator);
        transakcje.setTytulPrzelewu(tytul);


        transakcjeRepository.save(transakcje);
    }
}
