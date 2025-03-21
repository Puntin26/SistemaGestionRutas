package com.example.demoproyfinal;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PruebaParteBAvance {
    public static void main(String[] args) {
        // Instancia del controlador
        Controlador controlador = Controlador.getInstance();

        // Tres paradas sencillas
        Parada p1 = new Parada("A");
        Parada p2 = new Parada("B");
        Parada p3 = new Parada("C");
        Parada p4 = new Parada("D");

        // Agregar paradas al controlador
        controlador.insertarParada(p1);
        controlador.insertarParada(p2);
        controlador.insertarParada(p3);
        controlador.insertarParada(p4);

        //  rutas dirigidas
        // A -> B (distancia 5)
        // B -> C (distancia 10)
        // A -> C (distancia 20)
        Ruta r1 = new Ruta(p1, p2, 5, 2.0f, 9);
        Ruta r2 = new Ruta(p2, p3, 10, 3.0f, 8);
        Ruta r3 = new Ruta(p1, p3, 20, 5.0f, 7);
        Ruta r4 = new Ruta(p2, p4, 12, 5.0f, 10);
        Ruta r5 = new Ruta(p4, p3, 12, 5.0f, 14);

        // Agregar rutas
        controlador.insertarRuta(r1);
        controlador.insertarRuta(r2);
        controlador.insertarRuta(r3);
        controlador.insertarRuta(r4);
        controlador.insertarRuta(r5);

        //  estructura inicial
        System.out.println("Paradas: " + controlador.getParadas());
        System.out.println("Rutas:   " + controlador.getRutas());

        // PRUEBAS
        probarDijkstra(controlador, p1, p4);
        probarFloydWarshall(controlador);
    }

    private static void probarDijkstra(Controlador controlador, Parada origen, Parada destino) {
        System.out.println("\n PRUEBA: Dijkstra ---");
        List<Parada> camino = AlgoritmosGrafo.dijkstra(controlador.getListaAdyacencia(), origen, destino);
        System.out.println("Ruta mas corta desde " + origen.getNombre() + " hasta " + destino.getNombre() + ": " + camino);

    }

    private static void probarFloydWarshall(Controlador controlador) {
        System.out.println("\n PRUEBA: Floyd-Warshall ---");
        int[][] distancias = AlgoritmosGrafo.floydWarshall(controlador.getParadas(), controlador.getListaAdyacencia());
        System.out.println("Matriz de distancias minimas:");

        for (int[] fila : distancias) {
            System.out.println(Arrays.toString(fila));
        }
    }
}
