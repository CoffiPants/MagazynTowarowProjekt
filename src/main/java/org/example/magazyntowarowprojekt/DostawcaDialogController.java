package org.example.magazyntowarowprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DostawcaDialogController {
    @FXML
    private TextField nazwaField;
    @FXML
    private TextField adresField;
    @FXML
    private TextField nipField;
    @FXML
    private TextField telefonField;
    @FXML
    private TextField emailField;

    private Stage dialogStage;
    private MainController mainController;
    private Dostawca dostawca;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setDostawca(Dostawca dostawca) {
        this.dostawca = dostawca;
        nazwaField.setText(dostawca.getNazwa());
        adresField.setText(dostawca.getAdres());
        nipField.setText(dostawca.getNip());
        telefonField.setText(dostawca.getTelefon());
        emailField.setText(dostawca.getEmail());
    }

    @FXML
    private void handleSave() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (dostawca == null) {
                dostawca = new Dostawca();
            }

            dostawca.setNazwa(nazwaField.getText());
            dostawca.setAdres(adresField.getText());
            dostawca.setNip(nipField.getText());
            dostawca.setTelefon(telefonField.getText());
            dostawca.setEmail(emailField.getText());

            session.persist(dostawca);
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