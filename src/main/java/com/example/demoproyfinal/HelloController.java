package com.example.demoproyfinal;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

        ParadaDAO paradaDAO = new ParadaDAO();
        RutaDAO   rutaDAO   = new RutaDAO();
        List<Parada> paradasBD = paradaDAO.obtenerParadas();
        List<Ruta>   rutasBD   = rutaDAO.obtenerrutas();

        Controlador.getInstance().setParadas(new ArrayList<>(paradasBD));
        Controlador.getInstance().setRutas  (new ArrayList<>(rutasBD));

        Controlador.getInstance().reconstruirListaAdyacencia();

        graph = new GraphEdgeList<>();
        Controlador.getInstance().getParadas().forEach(graph::insertVertex);
        Controlador.getInstance().getRutas()
                .forEach(r -> graph.insertEdge(r.getOrigen(), r.getDestino(), r));

        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        graphView.setMinSize(400,200);
        graphView.prefWidthProperty().bind(graphContainer.widthProperty());
        graphView.prefHeightProperty().bind(graphContainer.heightProperty());

        graphContainer.getChildren().add(graphView);
        AnchorPane.setTopAnchor   (graphView,0.0);
        AnchorPane.setRightAnchor (graphView,0.0);
        AnchorPane.setBottomAnchor(graphView,0.0);
        AnchorPane.setLeftAnchor  (graphView,0.0);

        Platform.runLater(() -> {
            graphView.init();
            graphView.setAutomaticLayout(true);
            graphViewInitialized = true;
            System.out.println("Grafo inicializado correctamente");
        });
    }

    @FXML
    void agregarParada(ActionEvent event) {
        String nombre = txtParada.getText().trim();
        if (nombre.isEmpty()) return;

        Parada nueva = new Parada(nombre);
        Controlador.getInstance().insertarParada(nueva);
        Controlador.getInstance().reconstruirListaAdyacencia();
        new ParadaDAO().insertarParada(nueva);
        txtParada.clear();

        graph.insertVertex(nueva);
        if (graphViewInitialized) Platform.runLater(graphView::update);
    }

    @FXML
    void agregarRuta(ActionEvent event) {
        String origen   = txtOrigen.getText().trim();
        String destino  = txtDestino.getText().trim();
        String distStr  = txtDistancia.getText().trim();
        String costoStr = txtCosto.getText().trim();
        String timeStr  = txtTiempo.getText().trim();
        if (origen.isEmpty()||destino.isEmpty()||distStr.isEmpty()||
                costoStr.isEmpty()||timeStr.isEmpty()) return;

        try {
            int   dist = Integer.parseInt(distStr);
            float cost = Float.parseFloat(costoStr);
            int   tim  = Integer.parseInt(timeStr);
            if (dist<=0||cost<=0||tim<=0) return;

            Parada pOrigen = Controlador.getInstance().getParadas().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(origen))
                    .findFirst().orElse(null);
            Parada pDestino = Controlador.getInstance().getParadas().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(destino))
                    .findFirst().orElse(null);
            if (pOrigen==null||pDestino==null||pOrigen.equals(pDestino)) return;

            Ruta nuevaRuta = new Ruta(pOrigen,pDestino,dist,cost,tim);
            Controlador.getInstance().insertarRuta(nuevaRuta);
            Controlador.getInstance().reconstruirListaAdyacencia();
            new RutaDAO().insertarRuta(nuevaRuta);

            graph.insertEdge(pOrigen,pDestino,nuevaRuta);
            if (graphViewInitialized) Platform.runLater(graphView::update);

            txtOrigen.clear(); txtDestino.clear(); txtDistancia.clear();
            txtCosto.clear(); txtTiempo.clear();

        } catch (NumberFormatException ignored) {}
    }
}
