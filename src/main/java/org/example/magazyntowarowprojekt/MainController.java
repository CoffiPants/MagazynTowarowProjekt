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
    private TextField poleNazwa;
    @FXML
    private TextField poleCena;
    @FXML
    private TextField poleIlosc;

    private ObservableList<Produkt> listaProduktow = FXCollections.observableArrayList();
    private ProductService productService = new ProductService();

    @FXML
    private void initialize() {
        kolumnaId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        kolumnaNazwa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNazwa()));
        kolumnaCena.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCena()));
        kolumnaIlosc.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIlosc()));

        tabelaProduktow.setItems(listaProduktow);
        loadProducts();
    }
    @FXML
    private void handleAdd(ActionEvent event) {
        String nazwa = poleNazwa.getText();
        float cena = Float.parseFloat(poleCena.getText());
        int ilosc = Integer.parseInt(poleIlosc.getText());

        Produkt nowyProdukt = new Produkt();
        nowyProdukt.setNazwa(nazwa);
        nowyProdukt.setCena(cena);
        nowyProdukt.setIlosc(ilosc);

        try {
            productService.saveProduct(nowyProdukt);
            listaProduktow.add(nowyProdukt);
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
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
            // Handle the case where one or more fields are empty (e.g., show an error message)
            System.out.println("All fields must be filled out.");
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