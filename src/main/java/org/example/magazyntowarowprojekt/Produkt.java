package org.example.magazyntowarowprojekt;

import jakarta.persistence.*;

@Entity
@Table(name = "produkty")
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nazwa", nullable = false)
    private String nazwa;

    @Column(name = "cena", nullable = false)
    private float cena;

    @Column(name = "ilosc", nullable = false)
    private int ilosc;

    // Getery i Setery

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }
}