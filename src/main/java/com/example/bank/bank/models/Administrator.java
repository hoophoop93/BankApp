package com.example.bank.bank.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "administratorzy")
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrator_id")
    private Long idAdmin;

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
    @Column(name = "haslo")
    private String haslo;

    @OneToMany(mappedBy = "administratorTransakcje", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transakcje> transakcjeList;

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
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

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public List<Transakcje> getTransakcjeList() {
        return transakcjeList;
    }

    public void setTransakcjeList(List<Transakcje> transakcjeList) {
        this.transakcjeList = transakcjeList;
    }

    @Override
    public String toString(){
        return "Id_user: " + idAdmin +"<br />"+ "Imie: " + imie +
                "<br />"+ "Nazwisko: " + nazwisko +
                "<br />"+ "Adres: " + adres +
                "<br />"+ "Pesel: " + pesel;

    }
}
