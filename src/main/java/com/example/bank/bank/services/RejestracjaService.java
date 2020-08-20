package com.example.bank.bank.services;

import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.RejestracjaViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RejestracjaService {

    @Autowired
    KlientRepository klientRepository;

    public void registration(RejestracjaViewModel rejestracjaViewModel){
        Klient klient = new Klient();

        klient.setImie(rejestracjaViewModel.getImie());
        klient.setNazwisko(rejestracjaViewModel.getNazwisko());
        klient.setAdres(rejestracjaViewModel.getAdres());
        klient.setPesel(rejestracjaViewModel.getPesel());
        klient.setEmail(rejestracjaViewModel.getEmail());
        klient.setHaslo(rejestracjaViewModel.getHaslo());
        klient.setLicznik(0);

        klientRepository.save(klient);
    }
}
