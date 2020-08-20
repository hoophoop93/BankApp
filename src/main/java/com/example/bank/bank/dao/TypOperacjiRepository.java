package com.example.bank.bank.dao;

import com.example.bank.bank.models.Transakcje;
import com.example.bank.bank.models.TypOperacji;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TypOperacjiRepository extends CrudRepository<TypOperacji,Long> {

    public List<TypOperacji> findBytransakcjeList(Transakcje transakcje);

    public TypOperacji findByrodzajOperacji(String rodzajOperacji);
}
