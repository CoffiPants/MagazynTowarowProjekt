package org.example.magazyntowarowprojekt;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "zamowienia")
public class Zamowienie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_zamowienia")
    private LocalDateTime dataZamowienia;

    @Column(name = "klient")
    private String klient;

    @Column(name = "wartosc_zamowienia")
    private BigDecimal wartoscZamowienia;

    @Column(name = "status")
    private String status;

    @ManyToMany
    @JoinTable(
            name = "zamowienia_produkty",
            joinColumns = @JoinColumn(name = "zamowienie_id"),
            inverseJoinColumns = @JoinColumn(name = "produkt_id")
    )
    private List<Produkt> produkty = new ArrayList<>();

    // Required by JPA
    public Zamowienie() {
        this.dataZamowienia = LocalDateTime.now();
        this.status = "Nowe";
        this.wartoscZamowienia = BigDecimal.ZERO;
    }

    public Zamowienie(Long id, LocalDateTime dataZamowienia, String klient,
                      BigDecimal wartoscZamowienia, String status, List<Produkt> produkty) {
        this.id = id;
        this.dataZamowienia = dataZamowienia;
        this.klient = klient;
        this.wartoscZamowienia = wartoscZamowienia;
        this.status = status;
        this.produkty = produkty != null ? produkty : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataZamowienia() {
        return dataZamowienia;
    }

    public void setDataZamowienia(LocalDateTime dataZamowienia) {
        this.dataZamowienia = dataZamowienia;
    }

    public String getKlient() {
        return klient;
    }

    public void setKlient(String klient) {
        this.klient = klient;
    }

    public BigDecimal getWartoscZamowienia() {
        return wartoscZamowienia;
    }

    public void setWartoscZamowienia(BigDecimal wartoscZamowienia) {
        this.wartoscZamowienia = wartoscZamowienia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Produkt> getProdukty() {
        return produkty;
    }

    public void setProdukty(List<Produkt> produkty) {
        this.produkty = produkty;
    }
}
