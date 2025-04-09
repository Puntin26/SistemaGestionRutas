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

import java.util.ArrayList;
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
    @FXML private Button btnCostoBF;
    @FXML private Button btnRutaFloyd;

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

        ParadaDAO paradaDAO = new ParadaDAO();
        RutaDAO rutaDAO = new RutaDAO();
        List<Parada> paradasBD = paradaDAO.obtenerParadas();
        List<Ruta> rutasBD = rutaDAO.obtenerrutas();

        Controlador.getInstance().setParadas(new ArrayList<>(paradasBD));
        Controlador.getInstance().setRutas(new ArrayList<>(rutasBD));
        Controlador.getInstance().reconstruirListaAdyacencia();

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


        panelCalculos.toFront();
        panelExtra.toFront();

        graphView.setOnScroll(event -> {
            double zoomFactor = 2.0;
            if (event.getDeltaY() < 0) {
                zoomFactor = 1 / zoomFactor;
            }
            graphView.setScaleX(graphView.getScaleX() * zoomFactor);
            graphView.setScaleY(graphView.getScaleY() * zoomFactor);
            event.consume();
        });



        Platform.runLater(() -> {
            graphView.init();
            // Activa la distribución automática, si es adecuado
            graphView.setAutomaticLayout(true);

            // Iteramos sobre cada vértice para ajustar escala, habilitar pick y asignar el evento de click
            for (Vertex<Parada> v : graph.vertices()) {
                SmartGraphVertexNode vertexNode = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                // Reducir el tamaño de cada nodo (escala al 70%)
                vertexNode.setScaleX(0.7);
                vertexNode.setScaleY(0.7);

                // Aseguramos que el nodo capture el click en todo su contorno
                vertexNode.setPickOnBounds(true);

                // Asignamos un handler para que, al hacer click, lleve el nodo al frente y procese el click
                vertexNode.setOnMouseClicked(event -> {
                    vertexNode.toFront();
                    // Llamamos al método de procesamiento, que se encargará de manejar la selección
                    procesarClickVertice(v, vertexNode);
                });
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

        btnPrecio.setOnAction(e -> {
            modoCalculo = "costo";
            esperando   = true;
            origenSel   = null;
            destinoSel  = null;
            lblResultado.setText("Seleccione la parada de ORIGEN.");
            graph.vertices().forEach(v -> {
                SmartGraphVertexNode n = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                n.setStyle("");
            });
        });

        btnTransbordos.setOnAction(e -> {
            modoCalculo = "transbordos";
            esperando   = true;
            origenSel   = null;
            destinoSel  = null;
            lblResultado.setText("Seleccione la parada de ORIGEN.");
            graph.vertices().forEach(v -> {
                SmartGraphVertexNode n = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                n.setStyle("");
            });
        });

        btnCostoBF.setOnAction(e -> {
            modoCalculo = "costo_bf";
            esperando   = true;
            origenSel   = null;
            destinoSel  = null;
            lblResultado.setText("Seleccione la parada de ORIGEN.");
            graph.vertices().forEach(v -> {
                SmartGraphVertexNode n = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                n.setStyle("");
            });
        });

        btnRutaFloyd.setOnAction(e -> {
            modoCalculo = "ruta_floyd";
            esperando   = true;
            origenSel   = null;
            destinoSel  = null;
            lblResultado.setText("Seleccione la parada de ORIGEN.");
            graph.vertices().forEach(v -> {
                SmartGraphVertexNode n = (SmartGraphVertexNode) graphView.getStylableVertex(v);
                n.setStyle("");
            });
        });


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
                calcularRutaDistancia();
            } else if (modoCalculo.equals("tiempo")) {
                calcularRutaPorTiempo();
            } else if (modoCalculo.equals("costo")) {
                calcularRutaPorCosto();
            } else if (modoCalculo.equals("transbordos")) {
                calcularRutaPorTransbordos();
            } else if (modoCalculo.equals("costo_bf")) {
                calcularRutaPorCostoBF();
            } else if (modoCalculo.equals("ruta_floyd")) {
                calcularRutaFloyd();
            }
        }
    }

    private void calcularRutaFloyd(){
        // 1. Obtener la lista completa de paradas y la estructura de adyacencia
        List<Parada> paradas = Controlador.getInstance().getParadas();
        Map<Parada, List<Ruta>> adyacencia = Controlador.getInstance().getListaAdyacencia();

        // 2. Ejecutar el algoritmo Floyd–Warshall para obtener la matriz de distancias mínimas
        int[][] matrizDistancias = AlgoritmosGrafo.floydWarshall(paradas, adyacencia);

        // 3. Buscar el índice (posición) de la parada origen y destino en la lista de paradas
        int idxOrigen = paradas.indexOf(origenSel);
        int idxDestino = paradas.indexOf(destinoSel);

        if(idxOrigen == -1 || idxDestino == -1) {
            lblResultado.setText("Origen o destino no válidos.");
            return;
        }

        // 4. Extraer la distancia mínima entre origen y destino de la matriz
        int distanciaMinima = matrizDistancias[idxOrigen][idxDestino];

        // Si el valor es muy grande, se entiende que no hay ruta válida
        if(distanciaMinima >= Integer.MAX_VALUE/2) {
            lblResultado.setText("No existe ruta entre "
                    + origenSel.getNombre()
                    + " y "
                    + destinoSel.getNombre());
            return;
        }

        // 5. Mostrar el resultado en el panel extra (puedes ampliar para mostrar también la ruta completa si implementas el camino)
        lblResultado.setText("La distancia mínima entre " + origenSel.getNombre() +
                " y " + destinoSel.getNombre() + " es: " + distanciaMinima + " km");
    }


    private void calcularRutaPorCostoBF() {
        Map<Parada, List<Ruta>> ady = Controlador.getInstance().getListaAdyacencia();
        List<Parada> camino = AlgoritmosGrafo.bellmanFordCosto(ady, origenSel, destinoSel);

        if (camino.isEmpty()) {
            lblResultado.setText("No existe ruta o se encontró un ciclo negativo entre "
                    + origenSel.getNombre() + " y " + destinoSel.getNombre() + ".");
            return;
        }

        float totalCosto = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i).getNombre());
            if (i < camino.size() - 1) {
                sb.append(" -> ");
                for (Ruta r : ady.get(camino.get(i))) {
                    if (r.getDestino().equals(camino.get(i + 1))) {
                        totalCosto += r.getCosto();
                        break;
                    }
                }
            }
        }
        sb.append("\nCosto total: ").append(totalCosto).append(" $");
        lblResultado.setText(sb.toString());
    }


    private void calcularRutaDistancia() {
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

    private void calcularRutaPorTransbordos() {
        Map<Parada, List<Ruta>> ady = Controlador.getInstance().getListaAdyacencia();
        List<Parada> camino = AlgoritmosGrafo.dijkstra(ady, origenSel, destinoSel);

        if (camino.isEmpty()) {
            lblResultado.setText("No existe ruta entre " + origenSel.getNombre() + " y " + destinoSel.getNombre() + ".");
            return;
        }

        int totalTransbordos = Math.max(0, camino.size() - 2);

        StringBuilder sb = new StringBuilder();
        for (Parada p : camino) {
            sb.append(p.getNombre()).append(" -> ");
        }
        // Eliminar la flecha final y espacio extra.
        if (sb.length() >= 4)
            sb.delete(sb.length() - 4, sb.length());
        sb.append("\nTransbordos totales: ").append(totalTransbordos);
        lblResultado.setText(sb.toString());
    }


    private void calcularRutaPorCosto() {
        Map<Parada, List<Ruta>> ady = Controlador.getInstance().getListaAdyacencia();
        List<Parada> camino = AlgoritmosGrafo.dijkstra(ady, origenSel, destinoSel);

        if (camino.isEmpty()) {
            lblResultado.setText("No existe ruta entre " + origenSel.getNombre() + " y " + destinoSel.getNombre() + ".");
            return;
        }

        float totalCosto = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i).getNombre());
            if (i < camino.size() - 1) {
                sb.append("->");
                for (Ruta r : ady.get(camino.get(i))) {
                    if (r.getDestino().equals(camino.get(i + 1))) {
                        totalCosto += r.getCosto();
                        break;
                    }
                }
            }
        }
        sb.append("\nCosto total: ").append(totalCosto).append(" $");
        lblResultado.setText(sb.toString());

    }
}
