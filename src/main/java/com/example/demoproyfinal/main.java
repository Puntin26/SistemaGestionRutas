package com.example.demoproyfinal;

import com.dlsc.formsfx.model.validators.RegexValidator;

import java.lang.reflect.Array;
import java.util.*;

public class main {
    public static void main(String[] args) {

        Controlador controlador = Controlador.getInstance();

        Parada parada1 = new Parada("Parada A");
        Parada parada2 = new Parada("Parada B");
        Parada parada3 = new Parada("Parada C");


        controlador.insertarParada(parada1);
        controlador.insertarParada(parada2);
        controlador.insertarParada(parada3);

        Ruta ruta1 = new Ruta(parada1, parada2, 10, 5.5f, 12);
        Ruta ruta2 = new Ruta(parada2, parada3, 10, 5.5f, 10);
        Ruta ruta3 = new Ruta(parada1, parada3, 10, 5.5f, 9);

        controlador.insertarRuta(ruta1);
        controlador.insertarRuta(ruta2);
        controlador.insertarRuta(ruta3);

        System.out.println("Paradas: " + controlador.getParadas());
        System.out.println("Rutas: " + controlador.getRutas());

        controlador.modificarParada(parada1, "Parada X");
        System.out.println("\nDespués de modificar Parada A:");
        System.out.println("Paradas: " + controlador.getParadas());

        // Modificar una ruta
        controlador.modificarRuta(ruta1, 12, 6.0f, 15);
        System.out.println("\nDespués de modificar Ruta 1:");
        System.out.println("Rutas: " + controlador.getRutas());

        controlador.eliminarRuta(ruta2);
        System.out.println("Ruta despues de ser eliminada: ");
        System.out.println("Rutas: " + controlador.getRutas());

        controlador.eliminarParada(parada1);
        System.out.println("Parada despues de ser eliminada: ");
        System.out.println("Paradas: " + controlador.getParadas());

    }

}
