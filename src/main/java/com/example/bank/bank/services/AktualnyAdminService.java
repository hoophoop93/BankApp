package com.example.bank.bank.services;

import com.example.bank.bank.dao.AdminRepository;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AktualnyAdminService {

    private Administrator admin;

    @Autowired
    AdminRepository adminRepository;

    public Administrator getAdmin(){
        if(admin != null){
            admin = adminRepository.findByIdAdmin(admin.getIdAdmin());
        }

        return admin;
    }
    public void setAdmin(Administrator admin){
        this.admin=admin;
    }
    public boolean isAuthenticated() {
        System.out.println(admin);
        return (admin != null);
    }

    public void logOut(){
        admin = null;
    }

}
