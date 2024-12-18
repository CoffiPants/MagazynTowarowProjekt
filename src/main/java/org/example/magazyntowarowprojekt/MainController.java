// src/main/java/org/example/magazyntowarowprojekt/MainController.java
package org.example.magazyntowarowprojekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TableView<Produkt> productTable;
    @FXML
    private TableColumn<Produkt, Integer> idColumn;
    @FXML
    private TableColumn<Produkt, String> nameColumn;
    @FXML
    private TableColumn<Produkt, Double> priceColumn;
    @FXML
    private TableColumn<Produkt, Integer> quantityColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    private ObservableList<Produkt> productList = FXCollections.observableArrayList();
    private ProduktRepository repository = new ProduktRepository();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        productTable.setItems(productList);
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        Produkt newProduct = new Produkt();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setQuantity(quantity);

        repository.addProdukt(newProduct);
        productList.add(newProduct);
        clearFields();
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Produkt selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            selectedProduct.setName(nameField.getText());
            selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
            selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));
            productTable.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            productTable.getItems().remove(selectedIndex);
        }
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        quantityField.clear();
    }
}