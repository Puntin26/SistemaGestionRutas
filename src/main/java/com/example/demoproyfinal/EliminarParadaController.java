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

public class EliminarParadaController {

    @FXML
    private Button delete;

    @FXML
    private ListView<Parada> paradas;

    @FXML
    private ObservableList<Parada> paradasList;


    @FXML
    public void initialize() {
        Controlador control = Controlador.getInstance();


        paradasList = FXCollections.observableArrayList(control.getListaAdyacencia().keySet());


        paradas.setItems(paradasList);


        paradas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    // Funcion Para eliminar
    @FXML
    private void eliminarParada() {
        Parada paradaSelect = paradas.getSelectionModel().getSelectedItem();

        if (paradaSelect != null) {
            Controlador control = Controlador.getInstance();
            Map<Parada, List<Ruta>> listaDeAdyacencia = control.getListaAdyacencia();

            listaDeAdyacencia.remove(paradaSelect);

            listaDeAdyacencia.forEach((parada, rutas) ->
                    rutas.removeIf(ruta -> ruta.getOrigen().equals(paradaSelect) || ruta.getDestino().equals(paradaSelect))
            );

            paradasList.remove(paradaSelect);

            control.getParadas().remove(paradaSelect);
        }
    }





}
