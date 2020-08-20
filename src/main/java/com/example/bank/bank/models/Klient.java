package com.example.bank.bank.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "klienci")
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "klient_id")
    private long idKlient;

    @NotEmpty
    @Size(max = 32)
    @Column(name = "imie")
    private String imie;

    @NotEmpty
    @Size(max = 32)
    @Column(name = "nazwisko")
    private String nazwisko;

    @NotEmpty
    @Size(max = 32)
    @Column(name = "adres")
    private String adres;

    @NotEmpty
    @Column(name = "pesel")
    private String pesel;

    @NotEmpty
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "haslo")
    private String haslo;

    @Column(name = "licznik")
    private int licznik;

    @OneToMany(mappedBy = "rachunekKlienta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rachunek> rachunekiDanegoKlienta;

    @OneToMany(mappedBy = "klientPierwszy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StaliOdbiorcy> staliOdbiorcyList;

    @OneToMany(mappedBy = "klientDrugi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StaliOdbiorcy> staliOdbiorcyList2;

    @OneToMany(mappedBy = "klientZleceniaStale", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ZlecenieStale> zlecenieStaleList;

    @OneToMany(mappedBy = "klientZleceniaStaleOdbiorca", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ZlecenieStale> zlecenieStaleList2;


    public long getIdKlient() {
        return idKlient;
    }

    public void setIdKlient(long idKlient) {
        this.idKlient = idKlient;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public int getLicznik() {
        return licznik;
    }

    public void setLicznik(int licznik) {
        this.licznik = licznik;
    }

    public List<Rachunek> getRachunekiDanegoKlienta() {
        return rachunekiDanegoKlienta;
    }

    public void setRachunekiDanegoKlienta(List<Rachunek> rachunekiDanegoKlienta) {
        this.rachunekiDanegoKlienta = rachunekiDanegoKlienta;
    }

    public List<StaliOdbiorcy> getStaliOdbiorcyList() {
        return staliOdbiorcyList;
    }

    public void setStaliOdbiorcyList(List<StaliOdbiorcy> staliOdbiorcyList) {
        this.staliOdbiorcyList = staliOdbiorcyList;
    }

    public List<StaliOdbiorcy> getStaliOdbiorcyList2() {
        return staliOdbiorcyList2;
    }

    public void setStaliOdbiorcyList2(List<StaliOdbiorcy> staliOdbiorcyList2) {
        this.staliOdbiorcyList2 = staliOdbiorcyList2;
    }

    public List<ZlecenieStale> getZlecenieStaleList() {
        return zlecenieStaleList;
    }

    public void setZlecenieStaleList(List<ZlecenieStale> zlecenieStaleList) {
        this.zlecenieStaleList = zlecenieStaleList;
    }

    public List<ZlecenieStale> getZlecenieStaleList2() {
        return zlecenieStaleList2;
    }

    public void setZlecenieStaleList2(List<ZlecenieStale> zlecenieStaleList2) {
        this.zlecenieStaleList2 = zlecenieStaleList2;
    }

    @Override
    public String toString(){
        return "Id_user: " + idKlient +"<br />"+ "Imie: " + imie +
                "<br />"+ "Nazwisko: " + nazwisko +
                "<br />"+ "Adres: " + adres +
                "<br />"+ "Pesel: " + pesel +
                "<br />"+ "E-mail: " + email;

    }
}
