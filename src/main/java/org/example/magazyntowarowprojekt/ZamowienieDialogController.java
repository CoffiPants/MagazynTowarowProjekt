package org.example.magazyntowarowprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ZamowienieDialogController {
    @FXML
    private TextField klientField;
    @FXML
    private TextField wartoscField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ListView<Produkt> produktyListView;

    private Stage dialogStage;
    private MainController mainController;
    private Zamowienie zamowienie;

    @FXML
    private void initialize() {
        statusComboBox.getItems().addAll("Nowe", "W realizacji", "Zrealizowane", "Anulowane");

        // Add custom cell factory for produktyListView
        produktyListView.setCellFactory(lv -> new ListCell<Produkt>() {
            @Override
            protected void updateItem(Produkt item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa() + " - " + item.getCena() + " zł");
                }
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setZamowienie(Zamowienie zamowienie) {
        this.zamowienie = zamowienie;
        klientField.setText(zamowienie.getKlient());
        wartoscField.setText(zamowienie.getWartoscZamowienia().toString());
        statusComboBox.setValue(zamowienie.getStatus());
        produktyListView.getItems().setAll(zamowienie.getProdukty());
    }

    @FXML
    private void handleDodajProdukt() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Produkt> dostepneProdukty = session.createQuery("from Produkt", Produkt.class).list();

            ComboBox<Produkt> produktComboBox = new ComboBox<>();
            produktComboBox.getItems().addAll(dostepneProdukty);
            produktComboBox.setCellFactory(lv -> new ListCell<Produkt>() {
                @Override
                protected void updateItem(Produkt item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item.getNazwa());
                }
            });
            produktComboBox.setButtonCell(produktComboBox.getCellFactory().call(null));

            Dialog<Produkt> dialog = new Dialog<>();
            dialog.setTitle("Wybierz produkt");
            dialog.setHeaderText(null);

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setContent(produktComboBox);
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return produktComboBox.getValue();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(produkt -> {
                if (zamowienie == null) {
                    zamowienie = new Zamowienie();
                }
                if (zamowienie.getProdukty() == null) {
                    zamowienie.setProdukty(new ArrayList<>());
                }
                zamowienie.getProdukty().add(produkt);
                produktyListView.getItems().add(produkt);
            });
        }
    }

    @FXML
    private void handleSave() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (zamowienie == null) {
                zamowienie = new Zamowienie();
                zamowienie.setDataZamowienia(LocalDateTime.now());
            }

            zamowienie.setKlient(klientField.getText());
            zamowienie.setWartoscZamowienia(new BigDecimal(wartoscField.getText()));
            zamowienie.setStatus(statusComboBox.getValue());

            session.persist(zamowienie);
            transaction.commit();
        }

        mainController.refreshTables();
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}