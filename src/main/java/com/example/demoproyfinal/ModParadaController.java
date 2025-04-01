package com.example.demoproyfinal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Map;

public class ModParadaController {

    @FXML
    private ListView<Parada> paradas;

    @FXML
    private TextField txtNuevoNombre;

    @FXML
    private Button btnModificar;

    private ObservableList<Parada> paradasList;

    @FXML
    public void initialize() {
        // Carga la lista de paradas actuales usando el controlador singleton
        Controlador control = Controlador.getInstance();

        // Usamos las claves del mapa de adyacencia para poblar la lista (o directamente control.getParadas())
        // De esta manera mantenemos consistencia con EliminarParadaController
        paradasList = FXCollections.observableArrayList(control.getListaAdyacencia().keySet());
        paradas.setItems(paradasList);
        paradas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void modificarParada() {
        // Recupera la parada seleccionada en la lista
        Parada paradaSeleccionada = paradas.getSelectionModel().getSelectedItem();
        if (paradaSeleccionada != null) {
            // Toma el nuevo nombre del TextField
            String nuevoNombre = txtNuevoNombre.getText().trim();
            if (!nuevoNombre.isEmpty()) {
                // Invocamos el método de modificarParada en el Controlador
                Controlador.getInstance().modificarParada(paradaSeleccionada, nuevoNombre);

                // Refrescamos visualmente la ListView
                // Al cambiar el nombre internamente, a veces basta con llamar a refresh
                paradas.refresh();

                // (Opcional) Limpiar el TextField
                txtNuevoNombre.clear();
            }
        }
    }
}
