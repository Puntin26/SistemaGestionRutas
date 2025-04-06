package com.example.demoproyfinal;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
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
    private Graph<Parada, Ruta> graph;


    @FXML
    public void initialize() {
        Controlador control = Controlador.getInstance();

        graph = new GraphEdgeList<>();

        for (Parada p : Controlador.getInstance().getParadas()) {
            graph.insertVertex(p);
        }

        for (Ruta r : Controlador.getInstance().getRutas()) {
            graph.insertEdge(r.getOrigen(), r.getDestino(), r);
        }

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

            // 1. Eliminar del grafo
            Vertex<Parada> vertexToRemove = graph.vertices().stream()
                    .filter(v -> v.element().equals(paradaSelect))
                    .findFirst()
                    .orElse(null);

            if (vertexToRemove != null) {
                graph.removeVertex(vertexToRemove);
            }

            // 2. Eliminar del controlador
            control.eliminarParadaCompletamente(paradaSelect);

            // 3. Actualizar la lista visual
            paradasList.remove(paradaSelect);
        }
    }





}
