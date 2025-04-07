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
import java.util.List;
import java.util.Map;

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

        rutasList = FXCollections.observableArrayList(control.getRutas());

        rutas.setItems(rutasList);

        rutas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    // Funcion Para eliminar
    @FXML
    private void eliminarRuta() {
        Ruta rutaSelect = rutas.getSelectionModel().getSelectedItem();

        if (rutaSelect != null) {
            Controlador control = Controlador.getInstance();
            Map<Parada, List<Ruta>> listaDeAdyacencia = control.getListaAdyacencia();

            // 1. Eliminar la ruta de la lista observable (actualiza UI)
            rutasList.remove(rutaSelect);

            // 2. Eliminar la ruta de la lista de rutas en el Controlador
            control.getRutas().remove(rutaSelect);
            RutaDAO dao = new RutaDAO();
            dao.eliminarRuta(rutaSelect);

            // 3. Eliminar la ruta del mapa de adyacencia
            listaDeAdyacencia.forEach((parada, rutas) -> rutas.remove(rutaSelect));
        }
    }




}
