package com.example.bank.bank.models;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.*;

public class RejestracjaViewModel {

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String imie;

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String nazwisko;

    @NotBlank(message = "{NotEmpty.message}")
    @Size(max = 32, message = "{Size.displayName}")
    private String adres;

    @NotBlank(message = "{NotEmpty.message}")
    @Length(min = 11,max = 11,message = "{Size.pesel}")
    @Pattern(regexp ="[0-9]+",message = "{Pesel.invalidPesel}")
    private String pesel;

    @Pattern(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-+]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "{Email.invalidEmail}")
    @Size(max = 150, message = "{Size.email}")
    private String email;

    @Size(min = 8, message = "{Size.password}")
    private String haslo;

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

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
}
