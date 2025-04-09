package com.example.demoproyfinal;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private TextField txtParada;
    @FXML private TextField txtOrigen;
    @FXML private TextField txtDestino;
    @FXML private TextField txtDistancia;
    @FXML private TextField txtCosto;
    @FXML private TextField txtTiempo;
    @FXML private AnchorPane graphContainer;

    private Graph<Parada,Ruta>           graph;
    private SmartGraphPanel<Parada,Ruta> graphView;
    private boolean graphViewInitialized = false;

    @FXML
    public void initialize() {

        // Obtener datos desde la base de datos y actualizar el Controlador
        ParadaDAO paradaDAO = new ParadaDAO();
        RutaDAO rutaDAO = new RutaDAO();
        List<Parada> paradasBD = paradaDAO.obtenerParadas();
        List<Ruta> rutasBD = rutaDAO.obtenerrutas();

        Controlador.getInstance().setParadas(new ArrayList<>(paradasBD));
        Controlador.getInstance().setRutas(new ArrayList<>(rutasBD));
        Controlador.getInstance().reconstruirListaAdyacencia();

        // Crear grafo e insertar vértices y aristas
        graph = new GraphEdgeList<>();
        Controlador.getInstance().getParadas().forEach(graph::insertVertex);
        Controlador.getInstance().getRutas().forEach(r -> graph.insertEdge(r.getOrigen(), r.getDestino(), r));

        // Crear el panel del grafo y vincular sus dimensiones al contenedor
        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        graphView.setMinSize(400, 200);
        graphView.prefWidthProperty().bind(graphContainer.widthProperty());
        graphView.prefHeightProperty().bind(graphContainer.heightProperty());

        // Agregar el graphView al contenedor y establecer sus anclajes
        graphContainer.getChildren().clear();
        graphContainer.getChildren().add(graphView);
        AnchorPane.setTopAnchor(graphView, 0.0);
        AnchorPane.setRightAnchor(graphView, 0.0);
        AnchorPane.setBottomAnchor(graphView, 0.0);
        AnchorPane.setLeftAnchor(graphView, 0.0);

        // Paso 3 y 4: Hacer que el graphView capture eventos en toda su área y tenga el foco
        graphView.setPickOnBounds(true);
        graphView.setFocusTraversable(true);
        graphView.requestFocus();

        // Agregar funcionalidad de zoom mediante scroll
        graphView.setOnScroll(event -> {
            System.out.println("onScroll event detected! DeltaY = " + event.getDeltaY());
            double oldScaleX = graphView.getScaleX();
            double oldScaleY = graphView.getScaleY();
            double zoomFactor = 2.0; // Factor de zoom, puedes ajustar este valor
            if (event.getDeltaY() < 0) {
                zoomFactor = 1 / zoomFactor;
            }
            double newScaleX = oldScaleX * zoomFactor;
            double newScaleY = oldScaleY * zoomFactor;
            graphView.setScaleX(newScaleX);
            graphView.setScaleY(newScaleY);
            System.out.println("ScaleX: " + oldScaleX + " -> " + newScaleX +
                    ", ScaleY: " + oldScaleY + " -> " + newScaleY);
            event.consume();
        });

        // (Opcional) Agregar un estilo de borde para visualizar el área del graphView
        graphView.setStyle("-fx-border-color: #008287; -fx-border-width: #008287;");

        // Inicializar el graphView en el Thread de JavaFX
        Platform.runLater(() -> {
            graphView.init();
            // Activa la distribución automática, si es adecuado
            graphView.setAutomaticLayout(true);

            // Iteramos sobre cada vértice para ajustar escala y agregar el manejo de eventos
            for (Vertex<Parada> v : graph.vertices()) {
                SmartGraphVertexNode vertexNode = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                // Reducimos el tamaño del nodo (ajusta el valor según lo necesites)
                vertexNode.setScaleX(0.5);
                vertexNode.setScaleY(0.5);
            }
        });
    }

    private void reconstruirGraphView() {
        // Remover el panel actual
        graphContainer.getChildren().clear();

        // Crear una nueva instancia de SmartGraphPanel con el graph actual
        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        graphView.setMinSize(400, 200);
        graphView.prefWidthProperty().bind(graphContainer.widthProperty());
        graphView.prefHeightProperty().bind(graphContainer.heightProperty());

        // Establecer anclajes
        AnchorPane.setTopAnchor(graphView, 0.0);
        AnchorPane.setRightAnchor(graphView, 0.0);
        AnchorPane.setBottomAnchor(graphView, 0.0);
        AnchorPane.setLeftAnchor(graphView, 0.0);

        // Agregar funcionalidad de zoom (puedes encapsularlo también si lo deseas)
        graphView.setOnScroll(event -> {
            System.out.println("onScroll event detected! DeltaY = " + event.getDeltaY());
            double zoomFactor = 2.0;
            if (event.getDeltaY() < 0) {
                zoomFactor = 1 / zoomFactor;
            }
            graphView.setScaleX(graphView.getScaleX() * zoomFactor);
            graphView.setScaleY(graphView.getScaleY() * zoomFactor);
            event.consume();
        });

        // Asegurarse de capturar eventos
        graphView.setPickOnBounds(true);
        graphView.setFocusTraversable(true);
        graphView.requestFocus();

        // Inicializar y configurar layout
        Platform.runLater(() -> {
            graphView.init();
            graphView.setAutomaticLayout(true);
            // Después de 2 segundos, desactiva el layout automático para evitar reajustes
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> graphView.setAutomaticLayout(false));
            }).start();
            graphViewInitialized = true;
            System.out.println("Grafo inicializado correctamente");
        });


        // Agregar el nuevo panel al contenedor
        graphContainer.getChildren().add(graphView);
    }


    @FXML
    void agregarParada(ActionEvent event) {
        String nombre = txtParada.getText().trim();
        if (nombre.isEmpty()) return;

        // Validación: si la parada ya existe
        boolean existe = Controlador.getInstance().getParadas().stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre));
        if (existe) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("La parada \"" + nombre + "\" ya existe en el sistema.");
            alert.showAndWait();
            return;
        }

        Parada nueva = new Parada(nombre);
        Controlador.getInstance().insertarParada(nueva);
        Controlador.getInstance().reconstruirListaAdyacencia();
        new ParadaDAO().insertarParada(nueva);
        txtParada.clear();

        // Insertar el nuevo vértice en el grafo
        graph.insertVertex(nueva);

        Platform.runLater(() -> {
            graphView.update();
            // Obtener el nodo que representa la nueva parada
            SmartGraphVertexNode vertexNode = (SmartGraphVertexNode) graphView.getStylableVertex(nueva);
            if (vertexNode != null) {
                // Ajustar la escala para que coincida con los demás nodos
                vertexNode.setScaleX(0.5);
                vertexNode.setScaleY(0.5);
            }
        });
    }


    @FXML
    void agregarRuta(ActionEvent event) {
        String origen   = txtOrigen.getText().trim();
        String destino  = txtDestino.getText().trim();
        String distStr  = txtDistancia.getText().trim();
        String costoStr = txtCosto.getText().trim();
        String timeStr  = txtTiempo.getText().trim();
        if (origen.isEmpty() || destino.isEmpty() || distStr.isEmpty() || costoStr.isEmpty() || timeStr.isEmpty())
            return;

        try {
            int dist = Integer.parseInt(distStr);
            float cost = Float.parseFloat(costoStr);
            int tim = Integer.parseInt(timeStr);
            if (dist <= 0 || cost <= 0 || tim <= 0) return;

            Parada pOrigen = Controlador.getInstance().getParadas().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(origen))
                    .findFirst().orElse(null);
            Parada pDestino = Controlador.getInstance().getParadas().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(destino))
                    .findFirst().orElse(null);

            if (pOrigen == null || pDestino == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                if (pOrigen == null && pDestino == null) {
                    alert.setContentText("Las paradas de ORIGEN y DESTINO no existen en el sistema.");
                } else if (pOrigen == null) {
                    alert.setContentText("La parada de ORIGEN (" + origen + ") no existe en el sistema.");
                } else {
                    alert.setContentText("La parada de DESTINO (" + destino + ") no existe en el sistema.");
                }
                alert.showAndWait();
                return;
            }

            if (pOrigen.equals(pDestino)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("El origen y destino deben ser diferentes.");
                alert.showAndWait();
                return;
            }

            boolean existeRuta = Controlador.getInstance().getRutas().stream()
                    .anyMatch(r -> r.getOrigen().getNombre().equalsIgnoreCase(origen)
                            && r.getDestino().getNombre().equalsIgnoreCase(destino));
            if (existeRuta) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("La ruta de " + origen + " a " + destino + " ya existe en el sistema.");
                alert.showAndWait();
                return;
            }

            Ruta nuevaRuta = new Ruta(pOrigen, pDestino, dist, cost, tim);
            Controlador.getInstance().insertarRuta(nuevaRuta);
            Controlador.getInstance().reconstruirListaAdyacencia();
            new RutaDAO().insertarRuta(nuevaRuta);
            graph.insertEdge(pOrigen, pDestino, nuevaRuta);

            // Reconstruir el panel para que se muestre el nuevo edge
            Platform.runLater(() -> {
                graphView.update();
            });

            txtOrigen.clear();
            txtDestino.clear();
            txtDistancia.clear();
            txtCosto.clear();
            txtTiempo.clear();
        } catch (NumberFormatException ignored) {}
    }



}
