package com.example.bank.bank.models;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginViewModel {

    @NotEmpty(message = "{NotEmpty.message}")
    public String pesel;

    @NotEmpty(message = "{NotEmpty.message}")
    public String haslo;

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
}
