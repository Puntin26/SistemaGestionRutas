package com.example.demoproyfinal;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.scene.layout.AnchorPane;

import java.util.Date;

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
    private AnchorPane graphContainer;

    private Graph<Parada, Ruta> graph;
    private SmartGraphPanel<Parada, Ruta> graphView;

    @FXML
    public void initialize() {
        graph = new GraphEdgeList<>();// esto tambien resetea el grafo

        // paradas = vertices
        for (Parada p : Controlador.getInstance().getParadas()) {
            graph.insertVertex(p);
        }

        //ruta = arista
        for (Ruta r : Controlador.getInstance().getRutas()) {
            graph.insertEdge(r.getOrigen(), r.getDestino(), r);
        }

        graphView = new SmartGraphPanel<>(graph);//panel de visualizacion

        AnchorPane.setTopAnchor(graphView, 0.0);
        AnchorPane.setBottomAnchor(graphView, 0.0);
        AnchorPane.setLeftAnchor(graphView, 0.0);
        AnchorPane.setRightAnchor(graphView, 0.0);

        graphContainer.getChildren().clear();
        graphContainer.getChildren().add(graphView);

        //iniciar con runLater (evita error)
        Platform.runLater(() -> {
            graphView.init();
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
            //refrescarListas();
            txtParada.clear();
            graph.insertVertex(nueva);
            graphView.update();
        }
    }

    @FXML
    void agregarRuta(ActionEvent event) {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        String distStr = txtDistancia.getText().trim();
        String costoStr = txtCosto.getText().trim();

        if (!origen.isEmpty() && !destino.isEmpty() && !distStr.isEmpty() && !costoStr.isEmpty()) {
            try {
                int dist = Integer.parseInt(distStr);
                float cost = Float.parseFloat(costoStr);

                // Buscamos las paradas (origen/destino) en la lista
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

                // Si ambas paradas existen, creamos la ruta
                if (pOrigen != null && pDestino != null) {
                    Ruta ruta = new Ruta(pOrigen, pDestino, dist, cost, new Date());
                    Controlador.getInstance().insertarRuta(ruta);
                    //refrescarListas();
                    txtOrigen.clear();
                    txtDestino.clear();
                    txtDistancia.clear();
                    txtCosto.clear();

                    graph.insertEdge(pOrigen, pDestino, ruta);
                    graphView.update();
                }
            } catch (NumberFormatException e) {
                System.out.println("Error en los valores de distancia/costo.");
            }
        }
    }

//    private void refrescarListas() {
//        listViewParadas.getItems().setAll(Controlador.getInstance().getParadas());
//        listViewRutas.getItems().setAll(Controlador.getInstance().getRutas());
//    }
}
