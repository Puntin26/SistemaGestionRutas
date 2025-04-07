package com.example.demoproyfinal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class ModRutaController {

    @FXML
    private ListView<Ruta> rutas;

    @FXML
    private TextField txtDistancia;

    @FXML
    private TextField txtCosto;

    @FXML
    private TextField txtTiempo;

    @FXML
    private Button btnModificar;

    private ObservableList<Ruta> rutasList;

    @FXML
    public void initialize() {
        Controlador control = Controlador.getInstance();
        rutasList = FXCollections.observableArrayList(control.getRutas());
        rutas.setItems(rutasList);

        rutas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        rutas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtDistancia.setText(String.valueOf(newSelection.getDistancia()));
                txtCosto.setText(String.valueOf(newSelection.getCosto()));
                txtTiempo.setText(String.valueOf(newSelection.getTiempo()));
            }
        });
    }

    @FXML
    private void modificarRuta() {
        Ruta rutaSeleccionada = rutas.getSelectionModel().getSelectedItem();
        if (rutaSeleccionada != null) {
            try {
                int nuevaDistancia = Integer.parseInt(txtDistancia.getText().trim());
                float nuevoCosto = Float.parseFloat(txtCosto.getText().trim());
                int nuevoTiempo = Integer.parseInt(txtTiempo.getText().trim());

                Controlador.getInstance().modificarRuta(rutaSeleccionada, nuevaDistancia, nuevoCosto, nuevoTiempo);
                RutaDAO dao = new RutaDAO();
                dao.actualizarRuta(rutaSeleccionada);

                rutas.refresh();



            } catch (NumberFormatException e) {
                System.out.println("Error: valores invalidos para Distancia, Costo o Tiempo.");
            }
        }
    }
}
