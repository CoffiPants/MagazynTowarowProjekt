package org.example.magazyntowarowprojekt;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Dostawca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nazwa;
    private String adres;
    private String nip;
    private String telefon;
    private String email;

    @OneToMany(mappedBy = "dostawca")
    private List<Produkt> produkty = new ArrayList<>();

    public Dostawca() {
        // Required by Hibernate
    }

    public Dostawca(Long id, String nazwa, String adres, String nip, String telefon, String email, List<Produkt> produkty) {
        this.id = id;
        this.nazwa = nazwa;
        this.adres = adres;
        this.nip = nip;
        this.telefon = telefon;
        this.email = email;
        this.produkty = produkty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Produkt> getProdukty() {
        return produkty;
    }

    public void setProdukty(List<Produkt> produkty) {
        this.produkty = produkty;
    }
}
