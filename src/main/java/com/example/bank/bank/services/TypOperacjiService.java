package com.example.bank.bank.services;

import com.example.bank.bank.dao.TypOperacjiRepository;
import com.example.bank.bank.models.TypOperacji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TypOperacjiService {

    @Autowired
    TypOperacjiRepository typOperacjiRepository;

    public void zapiszRodzajOperacji(String typ){
        TypOperacji typOperacji = new TypOperacji();
        typOperacji.setRodzajOperacji(typ);

        typOperacjiRepository.save(typOperacji);
    }
}
