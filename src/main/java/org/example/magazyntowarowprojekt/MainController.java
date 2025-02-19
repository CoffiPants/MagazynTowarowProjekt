package org.example.magazyntowarowprojekt;

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
    }