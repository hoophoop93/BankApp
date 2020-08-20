package com.example.bank.bank.services;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NumerKontaService {

    public String numerKonta(){
        Random r = new Random();
        String nr="";
        for (int i=0; i<=25;i++) {
            nr+=r.nextInt(10)+"";
        }

        System.out.println(nr);

        String nrkonta="";
        int licznik=0;
        for(int i=0;i<nr.length();i++){

            nrkonta+=nr.charAt(i);

            if(i==1) nrkonta+=" ";
            if(i>1) licznik++;
            if(licznik==4) {
                nrkonta+=" ";
                licznik=0;
            }
        }
        return nrkonta;
    }
}
