package com.example.bank.bank.services;

import com.example.bank.bank.dao.ZleceniaStaleRepository;
import com.example.bank.bank.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CyklicznyService {

    @Autowired
    ZleceniaStaleRepository zleceniaStaleRepository;

    public void dodajPrzelewCyliczny(String tytul,double kwota,int liczbaDni,
                                Klient klient,Klient klient2, Rachunek rachunek2, String data){
        ZlecenieStale zlecenieStale = new ZlecenieStale();
        zlecenieStale.setKwota(kwota);
        zlecenieStale.setDataWykoniani(data);
        zlecenieStale.setLiczbaDni(liczbaDni);
        zlecenieStale.setKlientZleceniaStale(klient);
        zlecenieStale.setKlientZleceniaStaleOdbiorca(klient2);
        zlecenieStale.setRachunekZlecenieStale(rachunek2);
        zlecenieStale.setTytul( tytul);



        zleceniaStaleRepository.save(zlecenieStale);
    }
}