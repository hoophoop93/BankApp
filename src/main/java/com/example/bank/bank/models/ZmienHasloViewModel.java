package com.example.bank.bank.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class ZmienHasloViewModel {

    @NotEmpty(message = "{NotEmpty.message}")
    public String hasloStare;

    @NotEmpty(message = "{NotEmpty.message}")
    @Size(min = 8, message = "{Size.password}")
    public String hasloNowe;

    @NotEmpty(message = "{NotEmpty.message}")
    @Size(min = 8, message = "{Size.password}")
    public String hasloNowe2;

    public String getHasloStare() {
        return hasloStare;
    }

    public void setHasloStare(String hasloStare) {
        this.hasloStare = hasloStare;
    }

    public String getHasloNowe() {
        return hasloNowe;
    }

    public void setHasloNowe(String hasloNowe) {
        this.hasloNowe = hasloNowe;
    }

    public String getHasloNowe2() {
        return hasloNowe2;
    }

    public void setHasloNowe2(String hasloNowe2) {
        this.hasloNowe2 = hasloNowe2;
    }
}
