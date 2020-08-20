package com.example.bank.bank.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "transakcje")
public class Transakcje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transakcje_id")
    private Long idTransakcji;

    @Column(name = "kwota")
    private Double kwota;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private Administrator administratorTransakcje;

    @ManyToOne
    @JoinColumn(name = "typ_id")
    private TypOperacji typOperacjiTransakcje;

    @ManyToOne
    @JoinColumn(name = "rachunek_id")
    private Rachunek rachunekTransakcje;

    @ManyToOne
    @JoinColumn(name = "rachunek_id2")
    private Rachunek rachunekTransakcje2;

    @ManyToOne
    @JoinColumn(name = "daty_id")
    private Daty datyTransakcje;

    @Size(max = 32)
    @Column(name = "tytul_przelewu")
    private String tytulPrzelewu;


    public Long getIdTransakcji() {
        return idTransakcji;
    }

    public void setIdTransakcji(Long idTransakcji) {
        this.idTransakcji = idTransakcji;
    }

    public Double getKwota() {
        return kwota;
    }

    public void setKwota(Double kwota) {
        this.kwota = kwota;
    }

    public Administrator getAdministratorTransakcje() {
        return administratorTransakcje;
    }

    public void setAdministratorTransakcje(Administrator administratorTransakcje) {
        this.administratorTransakcje = administratorTransakcje;
    }

    public TypOperacji getTypOperacjiTransakcje() {
        return typOperacjiTransakcje;
    }

    public void setTypOperacjiTransakcje(TypOperacji typOperacjiTransakcje) {
        this.typOperacjiTransakcje = typOperacjiTransakcje;
    }

    public Rachunek getRachunekTransakcje() {
        return rachunekTransakcje;
    }

    public void setRachunekTransakcje(Rachunek rachunekTransakcje) {
        this.rachunekTransakcje = rachunekTransakcje;
    }

    public Rachunek getRachunekTransakcje2() {
        return rachunekTransakcje2;
    }

    public void setRachunekTransakcje2(Rachunek rachunekTransakcje2) {
        this.rachunekTransakcje2 = rachunekTransakcje2;
    }

    public Daty getDatyTransakcje() {
        return datyTransakcje;
    }

    public void setDatyTransakcje(Daty datyTransakcje) {
        this.datyTransakcje = datyTransakcje;
    }

    public String getTytulPrzelewu() {
        return tytulPrzelewu;
    }

    public void setTytulPrzelewu(String tytulPrzelewu) {
        this.tytulPrzelewu = tytulPrzelewu;
    }

    @Override
    public String toString(){
        return "ID transakcji: " + idTransakcji +"<br />"+ "typ operacji: " + typOperacjiTransakcje +
                "<br />"+ "kwota: " + kwota +
                "<br />"+ "rachunek id: " + rachunekTransakcje +
                "<br />"+ "rachunek id 2: " + rachunekTransakcje2 +
                "<br />"+ "data: " + datyTransakcje +
                "<br />"+ "Tytul przelewu: " + tytulPrzelewu;

    }
}
