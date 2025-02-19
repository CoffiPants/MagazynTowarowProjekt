// src/main/java/org/example/magazyntowarowprojekt/MainController.java
package org.example.magazyntowarowprojekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;

public class MainController {

    @FXML
    private TableView<Produkt> tabelaProduktow;
    @FXML
    private TableColumn<Produkt, Number> kolumnaId;
    @FXML
    private TableColumn<Produkt, String> kolumnaNazwa;
    @FXML
    private TableColumn<Produkt, Number> kolumnaCena;
    @FXML
    private TableColumn<Produkt, Number> kolumnaIlosc;
    @FXML
    private TableColumn<Produkt, String> kolumnaProducent;
    @FXML
    private TableColumn<Produkt, String> kolumnaKategoria;
    @FXML
    private TableColumn<Produkt, String> kolumnaOpis;
    @FXML
    private TextField poleNazwa;
    @FXML
    private TextField poleCena;
    @FXML
    private TextField poleIlosc;
    @FXML
    private TextField poleProducent;
    @FXML
    private TextField poleKategoria;
    @FXML
    private TextField poleOpis;

    private ObservableList<Produkt> listaProduktow = FXCollections.observableArrayList();
    private ProductService productService = new ProductService();


@FXML
private void initialize() {
    kolumnaId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
    kolumnaNazwa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNazwa()));
    kolumnaCena.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCena()));
    kolumnaIlosc.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIlosc()));
    kolumnaProducent.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducent()));
    kolumnaKategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKategoria()));
    kolumnaOpis.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOpis()));

    tabelaProduktow.setItems(listaProduktow);
    loadProducts();
}
@FXML
private void handleAdd(ActionEvent event) {
    String nazwa = poleNazwa.getText().trim();
    String cenaText = poleCena.getText().trim();
    String iloscText = poleIlosc.getText().trim();
    String producent = poleProducent.getText().trim();
    String kategoria = poleKategoria.getText().trim();
    String opis = poleOpis.getText().trim();

    if (nazwa.isEmpty() || cenaText.isEmpty() || iloscText.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText("Pola nazwa, cena i ilość muszą być wypełnione.");
        alert.showAndWait();
        return;
    }

    try {
        float cena = Float.parseFloat(cenaText);
        int ilosc = Integer.parseInt(iloscText);

        Produkt produkt = new Produkt();
        produkt.setNazwa(nazwa);
        produkt.setCena(cena);
        produkt.setIlosc(ilosc);
        produkt.setProducent(producent);
        produkt.setKategoria(kategoria);
        produkt.setOpis(opis);

        productService.saveProduct(produkt);
        loadProducts();

    } catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText("Proszę wprowadzić prawidłowe wartości dla ceny i ilości.");
        alert.showAndWait();
    }
}

@FXML
private void handleEdit(ActionEvent event) {
    Produkt wybranyProdukt = tabelaProduktow.getSelectionModel().getSelectedItem();
    if (wybranyProdukt != null) {
        String nazwa = poleNazwa.getText();
        String cenaText = poleCena.getText();
        String iloscText = poleIlosc.getText();

if (nazwa.isEmpty() || cenaText.isEmpty() || iloscText.isEmpty()) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText("All fields must be filled out.");
    alert.showAndWait();
    return;
}

        float cena = Float.parseFloat(cenaText);
        int ilosc = Integer.parseInt(iloscText);

        wybranyProdukt.setNazwa(nazwa);
        wybranyProdukt.setCena(cena);
        wybranyProdukt.setIlosc(ilosc);

        try {
            productService.updateProduct(wybranyProdukt);
            tabelaProduktow.refresh();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    @FXML
    private void handleDelete(ActionEvent event) {
        Produkt wybranyProdukt = tabelaProduktow.getSelectionModel().getSelectedItem();
        if (wybranyProdukt != null) {
            productService.deleteProduct(wybranyProdukt.getId());
            listaProduktow.remove(wybranyProdukt);
        }
    }

    private void loadProducts() {
        listaProduktow.setAll(productService.getAllProducts());
    }

    private void clearFields() {
        poleNazwa.clear();
        poleCena.clear();
        poleIlosc.clear();
    }
}