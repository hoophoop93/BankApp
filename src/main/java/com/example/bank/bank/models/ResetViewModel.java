package com.example.bank.bank.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class ResetViewModel {

    @NotBlank(message = "{NotEmpty.message}")
    @Length(min = 11,max = 11,message = "{Size.pesel}")
    @Pattern(regexp ="[0-9]+",message = "{Pesel.invalidPesel}")
    private String pesel;

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

}
