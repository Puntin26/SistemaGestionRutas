package com.example.demoproyfinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {

    private Button Agregar;

    @FXML
    private ImageView miImageView; // Asegúrate que coincide con fx:id del FXML

    @FXML
    public void initialize() {
        try {
            // Carga la imagen desde recursos (recomendado)
            Image imagen = new Image(getClass().getResourceAsStream("/com/example/demoproyfinal/Imagen _page-0001.jpg"));

            // O desde sistema de archivos (alternativa)
            // Image imagen = new Image("file:ruta/completa/image.png");

            miImageView.setImage(imagen);
            miImageView.setPreserveRatio(true);
            miImageView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            // Puedes cargar una imagen por defecto aquí
        }
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

    @FXML
    private void openThridWindow() {
        try {
            // Cargar el FXML de la segunda ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demoproyfinal/frontEndEliminar.fxml"));
            Scene scene = new Scene(loader.load());


            // Crear una nueva escena y ventana
            Stage stage = new Stage();
            stage.setTitle("Eliminar Paradas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openFourthWindow() {
        try {
            // Cargar el FXML de la segunda ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demoproyfinal/frontEndEliminarRuta.fxml"));
            Scene scene = new Scene(loader.load());


            // Crear una nueva escena y ventana
            Stage stage = new Stage();
            stage.setTitle("Eliminar Rutas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void openModifyParadaWindow() {
        try {
            // Cargar el FXML de modificar parada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demoproyfinal/frontEndModParada.fxml"));
            Scene scene = new Scene(loader.load());

            // Crear una nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Modificar Parada");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
