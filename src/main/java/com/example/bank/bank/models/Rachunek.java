package com.example.bank.bank.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "rachunek")
public class Rachunek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rachunek_id")
    private Long idRachunek;


    @Column(name = "numer_konta")
    private String numerKonta;

    @Column(name = "kwota")
    private Double kwota;

    @ManyToOne
    @JoinColumn(name = "klient_id")
    private Klient rachunekKlienta;

    @OneToMany(mappedBy = "rachunekTransakcje", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transakcje> transakcjeList;

    @OneToMany(mappedBy = "rachunekTransakcje2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transakcje> transakcjeList2;

    @OneToMany(mappedBy = "rachunekZlecenieStale", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ZlecenieStale> zlecenieStaleList;


    public Long getIdRachunek() {
        return idRachunek;
    }

    public void setIdRachunek(Long idRachunek) {
        this.idRachunek = idRachunek;
    }

    public String getNumerKonta() {
        return numerKonta;
    }

    public void setNumerKonta(String numerKonta) {
        this.numerKonta = numerKonta;
    }

    public Double getKwota() {
        return kwota;
    }

    public void setKwota(Double kwota) {
        this.kwota = kwota;
    }

    public Klient getRachunekKlienta() {
        return rachunekKlienta;
    }

    public void setRachunekKlienta(Klient rachunekKlienta) {
        this.rachunekKlienta = rachunekKlienta;
    }

    public List<Transakcje> getTransakcjeList() {
        return transakcjeList;
    }

    public void setTransakcjeList(List<Transakcje> transakcjeList) {
        this.transakcjeList = transakcjeList;
    }

    public List<Transakcje> getTransakcjeList2() {
        return transakcjeList2;
    }

    public void setTransakcjeList2(List<Transakcje> transakcjeList2) {
        this.transakcjeList2 = transakcjeList2;
    }

    public List<ZlecenieStale> getZlecenieStaleList() {
        return zlecenieStaleList;
    }

    public void setZlecenieStaleList(List<ZlecenieStale> zlecenieStaleList) {
        this.zlecenieStaleList = zlecenieStaleList;
    }

}

