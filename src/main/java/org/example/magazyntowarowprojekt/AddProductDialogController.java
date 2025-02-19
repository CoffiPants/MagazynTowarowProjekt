package org.example.magazyntowarowprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AddProductDialogController {
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

    private boolean isEditMode = false;
    private int editedProductId;
    private ProductService productService;
    private Stage dialogStage;
    private MainController mainController;

    public void setProduct(Produkt product) {
        isEditMode = true;
        editedProductId = product.getId();
        poleNazwa.setText(product.getNazwa());
        poleCena.setText(String.valueOf(product.getCena()));
        poleIlosc.setText(String.valueOf(product.getIlosc()));
        poleProducent.setText(product.getProducent());
        poleKategoria.setText(product.getKategoria());
        poleOpis.setText(product.getOpis());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            Produkt product = new Produkt();
            if (isEditMode) {
                product.setId(editedProductId);
            }
            product.setNazwa(poleNazwa.getText());
            product.setCena(Float.parseFloat(poleCena.getText()));
            product.setIlosc(Integer.parseInt(poleIlosc.getText()));
            product.setProducent(poleProducent.getText());
            product.setKategoria(poleKategoria.getText());
            product.setOpis(poleOpis.getText());

            if (isEditMode) {
                productService.updateProduct(product);
            } else {
                productService.saveProduct(product);
            }

            mainController.refreshTable();
            dialogStage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadź poprawne wartości liczbowe dla ceny i ilości.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        String errorMessage = "";
        if (poleNazwa.getText().trim().isEmpty()) {
            errorMessage += "Nazwa jest wymagana!\n";
        }
        if (poleCena.getText().trim().isEmpty()) {
            errorMessage += "Cena jest wymagana!\n";
        }
        if (poleIlosc.getText().trim().isEmpty()) {
            errorMessage += "Ilość jest wymagana!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nieprawidłowe dane");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}