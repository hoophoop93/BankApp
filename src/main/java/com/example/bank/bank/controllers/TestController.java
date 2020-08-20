package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    TransakcjeRepository transakcjeRepository;
    @Autowired
    StaliOdbiorcyRepository staliOdbiorcyRepository;
    @Autowired
    DatyRepository datyRepository;

    @RequestMapping("/test")
    public String test(){
        StringBuilder stringBuilder = new StringBuilder();

        List<Klient> klientList = (List<Klient>) klientRepository.findAll();


        int idx = 0;
        for(Klient klient : klientList) {
            if (klientList.get(idx).getLicznik() == 3) {
                stringBuilder.append(klient.getImie()).append(" --- Zablokowany").append("<br />");
            } else {
                stringBuilder.append(klient.getImie()).append(" ---nie zablokowany").append("<br />");
            }
            idx++;
        }




        return stringBuilder.toString();


    }
}

