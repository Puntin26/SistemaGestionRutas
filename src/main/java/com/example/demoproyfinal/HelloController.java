package com.example.demoproyfinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private ListView<Parada> listViewParadas;

    @FXML
    private ListView<Ruta> listViewRutas;

    @FXML
    void agregarParada(ActionEvent event) {
        String nombre = txtParada.getText().trim();
        if (!nombre.isEmpty()) {
            Controlador.getInstance().insertarParada(new Parada(nombre));
            refrescarListas();
            txtParada.clear();
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
                    refrescarListas();
                    txtOrigen.clear();
                    txtDestino.clear();
                    txtDistancia.clear();
                    txtCosto.clear();
                }
            } catch (NumberFormatException e) {
                System.out.println("Error en los valores de distancia/costo.");
            }
        }
    }

    private void refrescarListas() {
        listViewParadas.getItems().setAll(Controlador.getInstance().getParadas());
        listViewRutas.getItems().setAll(Controlador.getInstance().getRutas());
    }
}
