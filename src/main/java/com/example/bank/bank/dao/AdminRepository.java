package com.example.bank.bank.dao;

import com.example.bank.bank.models.Administrator;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Administrator,Long> {


    public Administrator findByPesel(String pesel);
    public Administrator findByIdAdmin(Long id);

}
