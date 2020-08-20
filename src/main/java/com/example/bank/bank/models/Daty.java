package com.example.bank.bank.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "daty")
public class Daty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daty_id")
    private Long idDaty;

    @Column(name = "data")
    private String data;

    @OneToMany(mappedBy = "datyTransakcje", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transakcje> transakcjeList;

    public Long getIdDaty() {
        return idDaty;
    }

    public void setIdDaty(Long idDaty) {
        this.idDaty = idDaty;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Transakcje> getTransakcjeList() {
        return transakcjeList;
    }

    public void setTransakcjeList(List<Transakcje> transakcjeList) {
        this.transakcjeList = transakcjeList;
    }
}
