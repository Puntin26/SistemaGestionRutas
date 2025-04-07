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
        Controlador control = Controlador.getInstance();


        paradasList = FXCollections.observableArrayList(control.getParadas());
        paradas.setItems(paradasList);
        paradas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void modificarParada() {
        Parada paradaSeleccionada = paradas.getSelectionModel().getSelectedItem();
        if (paradaSeleccionada != null) {
            String nuevoNombre = txtNuevoNombre.getText().trim();
            if (!nuevoNombre.isEmpty()) {
                // Capturamos el nombre antiguo
                String oldName = paradaSeleccionada.getNombre();

                // Modificar en memoria (y en la tabla de paradas a través del DAO)
                Controlador.getInstance().modificarParada(paradaSeleccionada, nuevoNombre);
                ParadaDAO dao = new ParadaDAO();
                dao.actualizarParada(oldName, nuevoNombre);

                // Ahora actualizamos las rutas asociadas
                RutaDAO rutaDAO = new RutaDAO();
                rutaDAO.actualizarRutasPorParada(oldName, nuevoNombre);

                paradas.refresh();
                txtNuevoNombre.clear();
            }
        }
    }

}
