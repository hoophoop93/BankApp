package com.example.bank.bank.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "typ_operacji")
public class TypOperacji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typ_id")
    private Long idTyp;

    @Size(max = 32)
    @Column(name = "rodzaj_operacji")
    private String rodzajOperacji;

    @OneToMany(mappedBy = "typOperacjiTransakcje", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transakcje> transakcjeList;

    public Long getIdTyp() {
        return idTyp;
    }

    public void setIdTyp(Long idTyp) {
        this.idTyp = idTyp;
    }

    public String getRodzajOperacji() {
        return rodzajOperacji;
    }

    public void setRodzajOperacji(String rodzajOperacji) {
        this.rodzajOperacji = rodzajOperacji;
    }

    public List<Transakcje> getTransakcjeList() {
        return transakcjeList;
    }

    public void setTransakcjeList(List<Transakcje> transakcjeList) {
        this.transakcjeList = transakcjeList;
    }
    @Override
    public String toString(){
        return "id operacji: " + idTyp +"<br />"+ "Rodzaj operacji: " + rodzajOperacji ;


    }
}
