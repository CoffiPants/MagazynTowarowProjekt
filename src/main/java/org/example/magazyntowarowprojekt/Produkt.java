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

    @Column(name = "producent")
    private String producent;

    @Column(name = "kategoria")
    private String kategoria;

    @Column(name = "opis")
    private String opis;

    @ManyToOne
    @JoinColumn(name = "dostawca_id")
    private Dostawca dostawca;

    // Getery i Setery
    public Produkt() {
        // Required by Hibernate
    }

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

    public String getProducent() {
        return producent;
    }

    public void setProducent(String producent) {
        this.producent = producent;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Dostawca getDostawca() {
        return dostawca;
    }

    public void setDostawca(Dostawca dostawca) {
        this.dostawca = dostawca;
    }
}