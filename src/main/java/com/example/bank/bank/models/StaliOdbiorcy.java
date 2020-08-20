package com.example.bank.bank.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "stali_odbiorcy")
public class StaliOdbiorcy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstali_odbiorcy_id")
    private Long idOdbiorcy;

    @ManyToOne
    @JoinColumn(name = "klient_id")
    private Klient klientPierwszy;

    @ManyToOne
    @JoinColumn(name = "klient_id2")
    private Klient klientDrugi;

    public Long getIdOdbiorcy() {
        return idOdbiorcy;
    }

    public void setIdOdbiorcy(Long idOdbiorcy) {
        this.idOdbiorcy = idOdbiorcy;
    }

    public Klient getKlientPierwszy() {
        return klientPierwszy;
    }

    public void setKlientPierwszy(Klient klientPierwszy) {
        this.klientPierwszy = klientPierwszy;
    }

    public Klient getKlientDrugi() {
        return klientDrugi;
    }

    public void setKlientDrugi(Klient klientDrugi) {
        this.klientDrugi = klientDrugi;
    }

    @Override
    public String toString(){
        return "ID staliodbiory: " + idOdbiorcy +"<br />"+ "Klient pierwszy: " + klientPierwszy +
                "<br />"+ "Klient drugi: " + klientDrugi ;

    }
}
