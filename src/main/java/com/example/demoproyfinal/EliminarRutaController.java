package com.example.demoproyfinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.util.ArrayList;

public class EliminarRutaController {

    @FXML
    private Button delete;

    @FXML
    private ListView<Ruta> rutas;

    @FXML
    private ObservableList<Ruta> rutasList;


    @FXML
    public void initialize() {

        Controlador control =  Controlador.getInstance();

        rutasList = FXCollections.observableList(control.getRutas());

        rutas.setItems(rutasList);

        rutas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    // Funcion Para eliminar
    @FXML
    private void eliminarRutaa() {
        Ruta rutaSelect = rutas.getSelectionModel().getSelectedItem();

        if(rutaSelect != null) {
            rutasList.remove(rutaSelect);
        }
    }



}
