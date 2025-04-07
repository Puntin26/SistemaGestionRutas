package com.example.demoproyfinal;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la ventana “Gestionar Ruta”.
 * Solo inicializa la visualización del grafo y coloca los botones de cálculo.
 * No contiene todavía la lógica de los cálculos.
 */
public class GestionRutasController {

    @FXML private AnchorPane graphContainer;
    @FXML private Pane panelCalculos;
    @FXML private Pane panelExtra;


    @FXML private Button btnDistancia;
    @FXML private Button btnTiempo;
    @FXML private Button btnPrecio;
    @FXML private Button btnVacio;

    private Graph<Parada, Ruta> graph;
    private SmartGraphPanel<Parada, Ruta> graphView;
    private boolean graphViewInitialized = false;

    @FXML
    public void initialize() {
        inicializarGrafo();
        inicializarBotones();
    }

    private void inicializarGrafo() {
        graph = new GraphEdgeList<>();

        Controlador control = Controlador.getInstance();

        control.getParadas().forEach(graph::insertVertex);
        control.getRutas().forEach(r ->
                graph.insertEdge(r.getOrigen(), r.getDestino(), r));

        graphView = new SmartGraphPanel<>(
                graph,
                new SmartCircularSortedPlacementStrategy());

        graphView.setPrefSize(403, 370);
        AnchorPane.setTopAnchor(graphView, 0.0);
        AnchorPane.setBottomAnchor(graphView, 0.0);
        AnchorPane.setLeftAnchor(graphView, 0.0);
        AnchorPane.setRightAnchor(graphView, 0.0);

        //

        graphContainer.getChildren().add(graphView);

        Platform.runLater(() -> {
            if (!graphViewInitialized) {
                graphView.init();
                graphView.setAutomaticLayout(true);
                graphViewInitialized = true;
            }
        });
    }


    private void inicializarBotones() {
        List<Button> botones = List.of(btnDistancia, btnTiempo, btnPrecio, btnVacio);
        botones.forEach(b -> b.setOnAction(e ->
                System.out.println("Funcionalidad pendiente para: " + ((Button) e.getSource()).getText())));
    }
}
