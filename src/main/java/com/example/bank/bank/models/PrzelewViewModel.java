package com.example.bank.bank.models;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class PrzelewViewModel {

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String imie;

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String nazwisko;

    @NotBlank(message = "{NotEmpty.message}")
    @Length(min = 26,max = 26,message = "{Size.numerKonta}")
    private String numerRachunku;

    @Pattern(regexp ="[0-9]+(\\.){0,1}[0-9]*",message = "{Kwota.invalidKwota}")
    private String kwota;

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 30, message = "{Size.displayName}")
    private String tytuł;


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

    public String getKwota() {
        return kwota;
    }

    public void setKwota(String kwota) {
        this.kwota = kwota;
    }

    public String getTytuł() {
        return tytuł;
    }

    public void setTytuł(String tytuł) {
        this.tytuł = tytuł;
    }

}

