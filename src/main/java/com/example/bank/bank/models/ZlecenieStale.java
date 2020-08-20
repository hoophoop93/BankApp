package com.example.bank.bank.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "zlecenia_stale")
public class ZlecenieStale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zlecenia_stale_id")
    private Long idZlecenia;

    @Column(name = "data_kolejnego_wykonania")
    private String dataWykoniani;

    @Column(name = "kwota")
    private Double kwota;

    @Column(name = "liczba_dni")
    private int liczbaDni;

    @ManyToOne
    @JoinColumn(name = "klient_id")
    private Klient klientZleceniaStale;

    @ManyToOne
    @JoinColumn(name = "klient_id2")
    private Klient klientZleceniaStaleOdbiorca;

    @ManyToOne
    @JoinColumn(name = "rachunek_id")
    private Rachunek rachunekZlecenieStale;

    @Column(name = "tytul_przelewu")
    private String tytul;

    public Long getIdZlecenia() {
        return idZlecenia;
    }

    public void setIdZlecenia(Long idZlecenia) {
        this.idZlecenia = idZlecenia;
    }

    public String getDataWykoniani() {
        return dataWykoniani;
    }

    public void setDataWykoniani(String dataWykoniani) {
        this.dataWykoniani = dataWykoniani;
    }

    public int getLiczbaDni() {
        return liczbaDni;
    }

    public void setLiczbaDni(int liczbaDni) {
        this.liczbaDni = liczbaDni;
    }

    public Klient getKlientZleceniaStale() {
        return klientZleceniaStale;
    }

    public void setKlientZleceniaStale(Klient klientZleceniaStale) {
        this.klientZleceniaStale = klientZleceniaStale;
    }

    public Rachunek getRachunekZlecenieStale() {
        return rachunekZlecenieStale;
    }

    public void setRachunekZlecenieStale(Rachunek rachunekZlecenieStale) {
        this.rachunekZlecenieStale = rachunekZlecenieStale;
    }

    public Double getKwota() {
        return kwota;
    }

    public void setKwota(Double kwota) {
        this.kwota = kwota;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public Klient getKlientZleceniaStaleOdbiorca() {
        return klientZleceniaStaleOdbiorca;
    }

    public void setKlientZleceniaStaleOdbiorca(Klient klientZleceniaStaleOdbiorca) {
        this.klientZleceniaStaleOdbiorca = klientZleceniaStaleOdbiorca;
    }
}
