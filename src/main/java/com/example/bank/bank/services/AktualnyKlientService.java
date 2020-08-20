package com.example.bank.bank.services;

import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.Klient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AktualnyKlientService {

    private Klient klient;

    @Autowired
    KlientRepository klientRepository;


    public Klient getKlient(){
        if(klient != null){
            klient = klientRepository.findByIdKlient(klient.getIdKlient());
        }

        return klient;
    }
    public void setKlient(Klient klient){
        this.klient=klient;
    }
    public boolean isAuthenticated() {
        System.out.println(klient);
        return (klient != null);
    }

    public void logOut(){
        klient = null;
    }

}
