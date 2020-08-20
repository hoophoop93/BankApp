package com.example.bank.bank.dao;

import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.StaliOdbiorcy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StaliOdbiorcyRepository extends JpaRepository<StaliOdbiorcy,Long> {

    public List<StaliOdbiorcy> findByklientPierwszy(Klient klient);

}
