package com.example.demoproyfinal;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PruebaParteCAvance {
    public static void main(String[] args) {
        // controlador
        Controlador controlador = Controlador.getInstance();

        //  paradas
        Parada p1 = new Parada("A");
        Parada p2 = new Parada("B");
        Parada p3 = new Parada("C");
        Parada p4 = new Parada("D");
        Parada p5 = new Parada("E");

        //  paradas al controlador
        controlador.insertarParada(p1);
        controlador.insertarParada(p2);
        controlador.insertarParada(p3);
        controlador.insertarParada(p4);
        controlador.insertarParada(p5);

        //  rutas (grafo dirigido con pesos)
        Ruta r1 = new Ruta(p1, p2,10,5.0f, new Date());
        Ruta r2 = new Ruta(p2, p3,20,3.0f, new Date());
        Ruta r3 = new Ruta(p3, p4,15,4.0f, new Date());
        Ruta r4 = new Ruta(p1, p4,50,10.0f, new Date());
        Ruta r5 = new Ruta(p2, p4,30,8.0f, new Date());
        Ruta r6 = new Ruta(p4, p5,10,6.0f, new Date());

        //  rutas al controlador
        controlador.insertarRuta(r1);
        controlador.insertarRuta(r2);
        controlador.insertarRuta(r3);
        controlador.insertarRuta(r4);
        controlador.insertarRuta(r5);
        controlador.insertarRuta(r6);

        // Imprimir  grafo inicial
        System.out.println("Paradas: " + controlador.getParadas());
        System.out.println("Rutas: " + controlador.getRutas());

        // PRUEBAS
        probarDijkstra(controlador, p1, p5);
        probarFloydWarshall(controlador);
    }

    private static void probarDijkstra(Controlador controlador, Parada origen, Parada destino) {
        System.out.println("\n--- PRUEBA: Dijkstra ---");
        List<Parada> camino = AlgoritmosGrafo.dijkstra(controlador.getListaAdyacencia(), origen, destino);
        System.out.println("Ruta más corta desde " + origen.getNombre() + " hasta " + destino.getNombre() + ": " + camino);
    }

    private static void probarFloydWarshall(Controlador controlador) {
        System.out.println("\n--- PRUEBA: Floyd-Warshall ---");
        int[][] distancias = AlgoritmosGrafo.floydWarshall(controlador.getParadas(), controlador.getListaAdyacencia());
        System.out.println("Matriz de distancias mínimas:");
        for (int[] fila : distancias) {
            System.out.println(Arrays.toString(fila));
        }
    }


}

