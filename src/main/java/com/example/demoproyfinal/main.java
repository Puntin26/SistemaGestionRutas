package com.example.demoproyfinal;

import com.dlsc.formsfx.model.validators.RegexValidator;

import java.lang.reflect.Array;
import java.util.*;

public class main {
    public static void main(String[] args) {

        Map<Parada, List<Ruta>> listaAdyacencia = new HashMap<>();

        Controlador controlador = Controlador.getInstance();

        Parada parada1 = new Parada("Parada A");
        Parada parada2 = new Parada("Parada B");
        Parada parada3 = new Parada("Parada C");


        controlador.insertarParada(parada1);
        controlador.insertarParada(parada2);
        controlador.insertarParada(parada3);

        Ruta ruta1 = new Ruta(parada1, parada2, 10, 5.5f, new Date());
        Ruta ruta2 = new Ruta(parada2, parada3, 10, 5.5f, new Date());
        Ruta ruta3 = new Ruta(parada1, parada3, 10, 5.5f, new Date());

        controlador.insertarRuta(ruta1);
        controlador.insertarRuta(ruta2);
        controlador.insertarRuta(ruta3);

        listaAdyacencia.put(parada1, Arrays.asList(ruta1, ruta3));
        listaAdyacencia.put(parada2, Collections.singletonList(ruta2));
        listaAdyacencia.put(parada3, Collections.singletonList(ruta3));

        System.out.println("Lista de adyacencia:");
        listaAdyacencia.forEach((parada, rutas) -> {
            System.out.println("Parada: " + parada.getNombre());
            rutas.forEach(ruta -> System.out.println("  -> " + ruta));
        });

    }

}
