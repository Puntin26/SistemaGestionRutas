package com.example.demoproyfinal;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HelloController {

    @FXML
    private TextField txtParada;

    @FXML
    private TextField txtOrigen;

    @FXML
    private TextField txtDestino;

    @FXML
    private TextField txtDistancia;

    @FXML
    private TextField txtCosto;

    @FXML
    private TextField txtTiempo;

    @FXML
    private AnchorPane graphContainer;

    private Graph<Parada, Ruta> graph;
    private SmartGraphPanel<Parada, Ruta> graphView;
    private boolean graphViewInitialized = false;

    // Modifica tu método initialize() así:
    @FXML
    public void initialize() {
        // Limpiar el grafo existente
        graph = new GraphEdgeList<>();

        // Obtener datos actualizados del Controlador
        List<Parada> paradasActuales = new ArrayList<>(Controlador.getInstance().getParadas());
        List<Ruta> rutasActuales = new ArrayList<>(Controlador.getInstance().getRutas());

        // Insertar solo paradas válidas
        for (Parada p : paradasActuales) {
            try {
                graph.insertVertex(p);
            } catch (Exception e) {
                System.err.println("Error insertando parada: " + p.getNombre());
            }
        }

        // Insertar solo rutas con paradas existentes
        for (Ruta r : rutasActuales) {
            try {
                // Verificar que ambas paradas existen en el grafo
                if (graph.vertices().stream().anyMatch(v -> v.element().equals(r.getOrigen())) &&
                        graph.vertices().stream().anyMatch(v -> v.element().equals(r.getDestino()))) {
                    graph.insertEdge(r.getOrigen(), r.getDestino(), r);
                }
            } catch (Exception e) {
                System.err.println("Error insertando ruta: " + r + " - " + e.getMessage());
            }
        }

        // Crear el panel del grafo
        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());

        // Configurar explícitamente el tamaño del graphView
        graphView.setPrefSize(1140, 250);
        graphView.setMinSize(400, 200); // Asegura un tamaño mínimo

        // Añadir el graphView al contenedor
        graphContainer.getChildren().clear();
        graphContainer.getChildren().add(graphView);

        // Ajustar graphView para que ocupe todo el espacio del contenedor
        AnchorPane.setTopAnchor(graphView, 0.0);
        AnchorPane.setRightAnchor(graphView, 0.0);
        AnchorPane.setBottomAnchor(graphView, 0.0);
        AnchorPane.setLeftAnchor(graphView, 0.0);

        // Inicializar después de estar en la escena
        Platform.runLater(() -> {
            try {
                // Asegúrate de que el contenedor tiene dimensiones
                if (graphContainer.getWidth() <= 0 || graphContainer.getHeight() <= 0) {
                    graphContainer.setPrefSize(1140, 250);
                    graphContainer.setMinSize(400, 200);
                }

                // Es crucial llamar a init() solo una vez cuando la vista está lista
                if (!graphViewInitialized) {
                    graphView.init();
                    graphView.setAutomaticLayout(true);
                    graphViewInitialized = true;
                    System.out.println("Grafo inicializado correctamente");
                }
            } catch (Exception e) {
                System.err.println("Error al inicializar el grafo: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }



//    private ListView<Parada> listViewParadas;
//
//    @FXML
//    private ListView<Ruta> listViewRutas;

    @FXML
    void agregarParada(ActionEvent event) {
        String nombre = txtParada.getText().trim();
        if (!nombre.isEmpty()) {
            Parada nueva = new Parada(nombre);
            Controlador.getInstance().insertarParada(nueva);
            txtParada.clear();

            try {
                graph.insertVertex(nueva);

                // Verificar que el graphView esté inicializado antes de actualizarlo
                if (graphViewInitialized && graphView != null) {
                    Platform.runLater(() -> {
                        try {
                            graphView.update();
                        } catch (Exception e) {
                            System.err.println("Error al actualizar el grafo: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.out.println("GraphView no inicializado aún, no se puede actualizar");
                    // Reintentar inicializar si es necesario
                    if (graphView != null && !graphViewInitialized) {
                        Platform.runLater(() -> {
                            try {
                                graphView.init();
                                graphViewInitialized = true;
                                graphView.update();
                            } catch (Exception e) {
                                System.err.println("Error al inicializar el grafo: " + e.getMessage());
                            }
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al insertar vértice: " + e.getMessage());
            }
        }
    }

    @FXML
    void agregarRuta(ActionEvent event) {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        String distStr = txtDistancia.getText().trim();
        String costoStr = txtCosto.getText().trim();
        String timeStr = txtTiempo.getText().trim();

        if (origen.isEmpty() || destino.isEmpty() || distStr.isEmpty() || costoStr.isEmpty() || timeStr.isEmpty()) {
            return;
        }

        try {
            int dist = Integer.parseInt(distStr);
            float cost = Float.parseFloat(costoStr);
            int tim = Integer.parseInt(timeStr);

            if (dist <= 0 || cost <= 0 || tim <= 0) {
                return;
            }

            Parada pOrigen = null;
            Parada pDestino = null;
            for (Parada p : Controlador.getInstance().getParadas()) {
                if (p.getNombre().equalsIgnoreCase(origen)) {
                    pOrigen = p;
                }
                if (p.getNombre().equalsIgnoreCase(destino)) {
                    pDestino = p;
                }
            }

            if (pOrigen == null || pDestino == null || pOrigen.equals(pDestino)) {
                return;
            }
            Parada finalOrigen = pOrigen;
            boolean origenEnGrafo = graph.vertices().stream()
                    .anyMatch(v -> v.element().equals(finalOrigen));
            Parada finalDestino = pDestino;
            boolean destinoEnGrafo = graph.vertices().stream()
                    .anyMatch(v -> v.element().equals(finalDestino));

            if (!origenEnGrafo || !destinoEnGrafo) {
                return;
            }

            boolean rutaExistente = graph.edges().stream()
                    .anyMatch(e -> e.element().getOrigen().equals(finalOrigen) &&
                            e.element().getDestino().equals(finalDestino));

            if (rutaExistente) {
                return;
            }

            Ruta nuevaRuta = new Ruta(pOrigen, pDestino, dist, cost, tim);
            Controlador.getInstance().insertarRuta(nuevaRuta);
            graph.insertEdge(pOrigen, pDestino, nuevaRuta);

            if (graphViewInitialized && graphView != null) {
                Platform.runLater(() -> {
                    try {
                        graphView.update();
                    } catch (Exception e) {
                        System.err.println("Error al actualizar el grafo: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
            
            //Platform.runLater(() -> graphView.update());

            txtOrigen.clear();
            txtDestino.clear();
            txtDistancia.clear();
            txtCosto.clear();
            txtTiempo.clear();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void refrescarListas() {
//        listViewParadas.getItems().setAll(Controlador.getInstance().getParadas());
//        listViewRutas.getItems().setAll(Controlador.getInstance().getRutas());
//    }
}


