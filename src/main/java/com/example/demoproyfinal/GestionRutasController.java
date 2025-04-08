package com.example.demoproyfinal;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;

public class GestionRutasController {

    @FXML private AnchorPane graphContainer;
    @FXML private Pane panelCalculos;
    @FXML private Pane panelExtra;

    @FXML private Button btnDistancia;
    @FXML private Button btnTiempo;
    @FXML private Button btnPrecio;
    @FXML private Button btnTransbordos;

    private Graph<Parada,Ruta> graph;
    private SmartGraphPanel<Parada,Ruta> graphView;

    private Parada  origenSel  = null;
    private Parada  destinoSel = null;
    private boolean esperando  = false;

    private String modoCalculo = "distancia";
    private final Label lblResultado = new Label();

    @FXML
    public void initialize() {
        crearGrafo();
        prepararPanelExtra();
        prepararBotones();
    }

    private void crearGrafo() {
        graph = new GraphEdgeList<>();

        Controlador c = Controlador.getInstance();

        c.getParadas().forEach(p -> graph.insertVertex(p));
        c.getRutas().forEach(r -> graph.insertEdge(r.getOrigen(), r.getDestino(), r));

        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        graphView.setPrefSize(403,370);
        AnchorPane.setTopAnchor   (graphView,0d);
        AnchorPane.setBottomAnchor(graphView,0d);
        AnchorPane.setLeftAnchor  (graphView,0d);
        AnchorPane.setRightAnchor (graphView,0d);
        graphContainer.getChildren().add(graphView);

        Platform.runLater(() -> {
            graphView.init();
            graphView.setAutomaticLayout(true);

            for (Vertex<Parada> v : graph.vertices()) {
                SmartGraphVertexNode node = (SmartGraphVertexNode) graphView.getStylableVertex(v);

                node.setOnMouseClicked(e -> procesarClickVertice(v, node));
            }
        });
    }

    private void prepararPanelExtra() {
        lblResultado.setWrapText(true);
        lblResultado.setStyle("-fx-text-fill:white;");
        lblResultado.setPrefWidth(panelExtra.getPrefWidth()-10);
        lblResultado.setLayoutX(5);
        lblResultado.setLayoutY(5);
        panelExtra.getChildren().add(lblResultado);
    }

    private void prepararBotones() {

        btnDistancia.setOnAction(e -> {
            modoCalculo = "distancia";
            esperando   = true;
            origenSel   = null;
            destinoSel  = null;
            lblResultado.setText("Seleccione la parada de ORIGEN.");
            graph.vertices().forEach(v -> {
                SmartGraphVertexNode n = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                n.setStyle("");
            });
        });

        btnTiempo.setOnAction(e -> {
            modoCalculo = "tiempo";
            esperando   = true;
            origenSel   = null;
            destinoSel  = null;
            lblResultado.setText("Seleccione la parada de ORIGEN.");
            graph.vertices().forEach(v -> {
                SmartGraphVertexNode n = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                n.setStyle("");
            });
        });

        btnPrecio.setOnAction(e -> System.out.println("Pendiente: precio"));
        btnTransbordos.setOnAction(e -> System.out.println("Pendiente: transbordos"));
    }

    private void procesarClickVertice(Vertex<Parada> v, SmartGraphVertexNode node) {
        if (!esperando) return;

        if (origenSel == null) {
            origenSel = v.element();
            node.setStyle("-fx-fill: #f1c40f;");
            lblResultado.setText("Origen: " + origenSel.getNombre() + "\nSeleccione la parada de DESTINO.");
            return;
        }

        if (destinoSel == null && !v.element().equals(origenSel)) {
            destinoSel  = v.element();
            node.setStyle("-fx-fill: #e67e22;");
            esperando = false;

            if (modoCalculo.equals("distancia")) {
                calcularRuta();
            } else if (modoCalculo.equals("tiempo")) {
                calcularRutaPorTiempo();
            }
        }
    }

    private void calcularRuta() {
        Map<Parada, List<Ruta>> ady = Controlador.getInstance().getListaAdyacencia();
        List<Parada> camino = AlgoritmosGrafo.dijkstra(ady, origenSel, destinoSel);

        if (camino.isEmpty()) {
            lblResultado.setText("No existe ruta entre " + origenSel.getNombre() + " y " + destinoSel.getNombre() + ".");
            return;
        }

        int total = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i).getNombre());
            if (i < camino.size() - 1) {
                sb.append(" -> ");
                for (Ruta r : ady.get(camino.get(i))) {
                    if (r.getDestino().equals(camino.get(i + 1))) {
                        total += r.getDistancia();
                        break;
                    }
                }
            }
        }
        sb.append("\nDistancia total: ").append(total).append(" km");
        lblResultado.setText(sb.toString());
    }

    private void calcularRutaPorTiempo() {
        Map<Parada, List<Ruta>> ady = Controlador.getInstance().getListaAdyacencia();
        List<Parada> camino = AlgoritmosGrafo.dijkstraTiempo(ady, origenSel, destinoSel);

        if (camino.isEmpty()) {
            lblResultado.setText("No existe ruta entre " + origenSel.getNombre() + " y " + destinoSel.getNombre() + ".");
            return;
        }

        int totalTiempo = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i).getNombre());
            if (i < camino.size() - 1) {
                sb.append("->");
                for (Ruta r : ady.get(camino.get(i))) {
                    if (r.getDestino().equals(camino.get(i + 1))) {
                        totalTiempo += r.getTiempo();
                        break;
                    }
                }
            }
        }
        sb.append("\nTiempo total: ").append(totalTiempo).append(" minutos");
        lblResultado.setText(sb.toString());
    }
}
