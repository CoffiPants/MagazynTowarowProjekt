package org.example.magazyntowarowprojekt;

    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.layout.VBox;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import javafx.beans.property.SimpleIntegerProperty;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.beans.property.SimpleFloatProperty;
    import javafx.beans.property.SimpleObjectProperty;
    import java.io.IOException;
    import javafx.scene.control.ContextMenu;
    import javafx.scene.control.MenuItem;
    import javafx.scene.input.MouseButton;
    import org.hibernate.Session;
    import org.hibernate.Transaction;
    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;

    import javafx.scene.control.cell.PropertyValueFactory;

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
        private TableView<Dostawca> tabelaDostawcow;
        @FXML
        private TableView<Zamowienie> tabelaZamowien;

        @FXML
        private TableColumn<Dostawca, Long> kolumnaIdDostawcy;
        @FXML
        private TableColumn<Dostawca, String> kolumnaNazwaDostawcy;
        @FXML
        private TableColumn<Dostawca, String> kolumnaAdres;
        @FXML
        private TableColumn<Dostawca, String> kolumnaNip;
        @FXML
        private TableColumn<Dostawca, String> kolumnaTelefon;
        @FXML
        private TableColumn<Dostawca, String> kolumnaEmail;

        @FXML
        private TableColumn<Zamowienie, Long> kolumnaIdZamowienia;
        @FXML
        private TableColumn<Zamowienie, LocalDateTime> kolumnaDataZamowienia;
        @FXML
        private TableColumn<Zamowienie, String> kolumnaKlient;
        @FXML
        private TableColumn<Zamowienie, BigDecimal> kolumnaWartosc;
        @FXML
        private TableColumn<Zamowienie, String> kolumnaStatus;

        private ObservableList<Produkt> listaProduktow = FXCollections.observableArrayList();
        private ProductService productService = new ProductService();

        @FXML
        private void initialize() {
            if (kolumnaId != null) kolumnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
            if (kolumnaNazwa != null) kolumnaNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            if (kolumnaCena != null) kolumnaCena.setCellValueFactory(new PropertyValueFactory<>("cena"));
            if (kolumnaIlosc != null) kolumnaIlosc.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            if (kolumnaOpis != null) kolumnaOpis.setCellValueFactory(new PropertyValueFactory<>("opis"));
            if (kolumnaProducent != null) kolumnaProducent.setCellValueFactory(new PropertyValueFactory<>("producent"));

            if (kolumnaId != null) kolumnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
            if (kolumnaNazwa != null) kolumnaNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            if (kolumnaCena != null) kolumnaCena.setCellValueFactory(new PropertyValueFactory<>("cena"));
            if (kolumnaIlosc != null) kolumnaIlosc.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
            if (kolumnaProducent != null) kolumnaProducent.setCellValueFactory(new PropertyValueFactory<>("producent"));
            if (kolumnaKategoria != null) kolumnaKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
            if (kolumnaOpis != null) kolumnaOpis.setCellValueFactory(new PropertyValueFactory<>("opis"));

            kolumnaId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
            kolumnaNazwa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNazwa()));
            kolumnaCena.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCena()));
            kolumnaIlosc.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIlosc()));
            kolumnaProducent.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducent()));
            kolumnaKategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKategoria()));
            kolumnaOpis.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOpis()));

            // Initialize suppliers table
            kolumnaIdDostawcy.setCellValueFactory(new PropertyValueFactory<>("id"));
            kolumnaNazwaDostawcy.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            kolumnaAdres.setCellValueFactory(new PropertyValueFactory<>("adres"));
            kolumnaNip.setCellValueFactory(new PropertyValueFactory<>("nip"));
            kolumnaTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
            kolumnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

            // Initialize orders table
            kolumnaIdZamowienia.setCellValueFactory(new PropertyValueFactory<>("id"));
            kolumnaDataZamowienia.setCellValueFactory(new PropertyValueFactory<>("dataZamowienia"));
            kolumnaKlient.setCellValueFactory(new PropertyValueFactory<>("klient"));
            kolumnaWartosc.setCellValueFactory(new PropertyValueFactory<>("wartoscZamowienia"));
            kolumnaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            refreshTables();
            // Create context menu
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Usuń");
            deleteItem.setOnAction(event -> {
                Produkt selectedProduct = tabelaProduktow.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    productService.deleteProduct(selectedProduct.getId());
                    loadProducts();
                }
            });
            contextMenu.getItems().add(deleteItem);
            tabelaProduktow.setContextMenu(contextMenu);

            // Handle both edit and add with double-click
            tabelaProduktow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    if (tabelaProduktow.getSelectionModel().getSelectedItem() != null) {
                        handleRowClick(); // Edit existing product
                    } else {
                        try {
                            showAddDialog(); // Add new product
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            tabelaProduktow.setItems(listaProduktow);
            loadProducts();
            refreshTables();
        }

        @FXML
        private void showAddDialog() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magazyntowarowprojekt/AddProductDialog.fxml"));
            VBox dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodaj produkt");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabelaProduktow.getScene().getWindow());

            AddProductDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainController(this);
            controller.setProductService(productService);

            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        }

        @FXML
        private void handleRowClick() {
            Produkt selectedProduct = tabelaProduktow.getSelectionModel().getSelectedItem();
            if (selectedProduct != null && tabelaProduktow.getSelectionModel().getSelectedCells().size() > 0) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magazyntowarowprojekt/AddProductDialog.fxml"));
                    VBox dialogPane = loader.load();

                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Edytuj produkt");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(tabelaProduktow.getScene().getWindow());

                    AddProductDialogController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    controller.setMainController(this);
                    controller.setProductService(productService);
                    controller.setProduct(selectedProduct); // Add this method to AddProductDialogController

                    Scene scene = new Scene(dialogPane);
                    dialogStage.setScene(scene);
                    dialogStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Błąd podczas otwierania formularza edycji");
                    alert.showAndWait();
                }
            }
        }

        @FXML
        private void handleDelete(ActionEvent event) {
            Produkt selectedProduct = tabelaProduktow.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                productService.deleteProduct(selectedProduct.getId());
                loadProducts();
            }
        }

        private void loadProducts() {
            listaProduktow.setAll(productService.getAllProducts());
        }

        public void refreshTable() {
            loadProducts();
        }

        public void refreshTables() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                List<Dostawca> dostawcy = session.createQuery("from Dostawca", Dostawca.class).list();
                List<Zamowienie> zamowienia = session.createQuery("from Zamowienie", Zamowienie.class).list();

                tabelaDostawcow.getItems().setAll(dostawcy);
                tabelaZamowien.getItems().setAll(zamowienia);
            }
        }

        @FXML
        private void handleDeleteDostawca() {
            Dostawca selectedDostawca = tabelaDostawcow.getSelectionModel().getSelectedItem();
            if (selectedDostawca != null) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.remove(selectedDostawca);
                    transaction.commit();
                    refreshTables();
                }
            }
        }

        @FXML
        private void handleDeleteZamowienie() {
            Zamowienie selectedZamowienie = tabelaZamowien.getSelectionModel().getSelectedItem();
            if (selectedZamowienie != null) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.remove(selectedZamowienie);
                    transaction.commit();
                    refreshTables();
                }
            }
        }

        @FXML
        private void showAddDostawcaDialog() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magazyntowarowprojekt/DostawcaDialog.fxml"));
            VBox dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodaj dostawcę");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabelaDostawcow.getScene().getWindow());

            DostawcaDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainController(this);

            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        }

        @FXML
        private void showAddZamowienieDialog() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magazyntowarowprojekt/ZamowienieDialog.fxml"));
            VBox dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodaj zamówienie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabelaZamowien.getScene().getWindow());

            ZamowienieDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainController(this);

            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        }
@FXML
public void handleClose() {
    Stage stage = (Stage) tabelaProduktow.getScene().getWindow();
    stage.close();
    Platform.exit();
}

    }
