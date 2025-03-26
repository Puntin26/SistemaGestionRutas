package com.example.demoproyfinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {

    private Button Agregar;

    @FXML
    public void initialize() {

    }

    @FXML
    private void openSecondWindow() {
        try {
            // Cargar el FXML de la segunda ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demoproyfinal/frontEnd.fxml"));
            Scene scene = new Scene(loader.load());


            // Crear una nueva escena y ventana
            Stage stage = new Stage();
            stage.setTitle("Agregar paradas y rutas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
