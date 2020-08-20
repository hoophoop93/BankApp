package com.example.bank.bank.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class StalyOdbiorcaViewModel {

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String imie;

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String nazwisko;

    @NotBlank(message = "{NotEmpty.message}")
    @Length(min = 26,max = 26,message = "{Size.numerKonta}")
    private String numerRachunku;

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

    public String getNumerRachunku() {
        return numerRachunku;
    }

    public void setNumerRachunku(String numerRachunku) {
        this.numerRachunku = numerRachunku;
    }
}
