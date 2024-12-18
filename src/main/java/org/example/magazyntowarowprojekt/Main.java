package org.example.magazyntowarowprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getResource("/org/example/magazyntowarowprojekt/MainView.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        VBox root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.setTitle("Magazyn Towarów");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); // Ensure the stage is resizable
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}