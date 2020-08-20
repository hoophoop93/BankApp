package com.example.bank.bank.services;

import com.example.bank.bank.dao.DatyRepository;
import com.example.bank.bank.models.Daty;
import com.example.bank.bank.models.PrzelewViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatyService {

    @Autowired
    DatyRepository datyRepository;

    public void zapiszDate(String data){
        Daty daty = new Daty();
        daty.setData(data);

        datyRepository.save(daty);
    }
}
