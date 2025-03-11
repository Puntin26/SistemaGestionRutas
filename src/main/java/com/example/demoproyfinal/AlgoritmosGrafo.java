package com.example.demoproyfinal;

import java.util.List;
import java.util.*;

/**
 * En esta clase se epcansulan los algoritmos clasicos que se piden en el proyecto
 */
public class AlgoritmosGrafo {

    //Dijksta:
    //Queremos retornar la ruta mas corta en distancia desde el origen hasta el destino con una lista de adyacencia
    public static List<Parada> dijkstra(Map<Parada, List<Ruta>> listaAdyacencia, Parada origen, Parada destino){
        //distancias minimas acumuladas desde el inicio y previo para reconstruir el caminoo
        Map<Parada, Integer> distancia = new HashMap<>();
        Map<Parada, Parada> previo = new HashMap<>();

        //inicia las listancias con lo maximo y el previo con null y pone la distancia en el origen en 0
        for(Parada p : listaAdyacencia.keySet()){
            distancia.put(p, Integer.MAX_VALUE);
            previo.put(p, null);
        }

        distancia.put(origen, 0);

        //queue de prioridad que ordena paradas dependiendo de su distancia, asi ordeno la cola
        PriorityQueue<Parada> cola = new PriorityQueue<>(Comparator.comparingInt(distancia::get));
        cola.add(origen);

        while(!cola.isEmpty()){

            Parada u = cola.poll();//(parada con menor distancia)
            //parada == destino, se termina
            if(u.equals(destino)){
                break;
            }

            //cuerpo y funcionalidad Dijkstra
            List<Ruta> rutas = listaAdyacencia.get(u);
            for(Ruta r : rutas){
                Parada a = r.getDestino();
                int peso = r.getDistancia();
                int nuevaDistancia = distancia.get(u) + peso;
                if(nuevaDistancia < distancia.get(a)){
                    distancia.put(a, nuevaDistancia);
                    previo.put(a, u);
                    cola.remove(a);
                    cola.add(a);
                }
            }
        }
        //ahora se construye la ruta final desde destino a origen con previo
        List<Parada> camino = new ArrayList<>();
        Parada actual = destino;
        while(actual != null){
            camino.add(actual);
            actual = previo.get(actual);
        }
        //como el camino esta en orden inverso aqui se revierte
        Collections.reverse(camino);

        //si no hay coneccion (no hay ruta) devuelve un empty list(equivalente a null
        if(distancia.get(destino) == Integer.MAX_VALUE){
            return Collections.emptyList();
        }

        return camino;
    }

    // Floyd-Warshall
    // Calcula las distancias minimas entre todas las parejas de paradas.
    // Retorna la matriz de distancias, donde la fila i y columna j representanla distancia minima de i a j.
    public static int[][] floydWarshall(List<Parada> paradas,Map<Parada,List<Ruta>> listaAdyacencia) {

        int n = paradas.size();
        // crear un indice para cada parada
        Map<Parada, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indexMap.put(paradas.get(i), i);
        }


        // matriz de distancias, inicializar con maximo  valor
        int[][] dist =new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE / 2);
            dist[i][i] = 0; // distancia de la misma parada
        }

        // llenar las distancias directas segun las rutas
        for (Parada p : paradas) {
            int i = indexMap.get(p);
            if (listaAdyacencia.get(p) != null) {
                for (Ruta r : listaAdyacencia.get(p)) {
                    int j = indexMap.get(r.getDestino());
                    int peso = r.getDistancia();
                    dist[i][j] = Math.min(dist[i][j], peso);
                }
            }
        }


        //  Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    //Faltarian:
    //Prim:
    //grafo com NO dirigido (para MST), se toma la ruta con menor "distancia" como peso.
    //Retorna el conjunto de aristas que forman el arbol de expansion minima
    //mejor para grafos densos

    //kruskall
    //Similar a Prim, pero se ordenan primero todas las aristas
    // y se va tomando la de menor peso que no forme ciclo, se usaria Union-Find
    //mejor para grafos dispersos

}
