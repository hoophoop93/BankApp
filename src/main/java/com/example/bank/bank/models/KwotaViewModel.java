package com.example.bank.bank.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class KwotaViewModel {

    @Pattern(regexp ="[0-9]+(\\.){0,1}[0-9]*",message = "{Kwota.invalidKwota}")
    private String kwota;

    @NotBlank(message = "{NotEmpty.message}")
    @Length(min = 11,max = 11,message = "{Size.pesel}")
    @Pattern(regexp ="[0-9]+",message = "{Pesel.invalidPesel}")
    private String pesel;

    public String getKwota() {
        return kwota;
    }

    public void setKwota(String kwota) {
        this.kwota = kwota;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
}
