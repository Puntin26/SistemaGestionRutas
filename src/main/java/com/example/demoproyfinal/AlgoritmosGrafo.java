package com.example.demoproyfinal;

import java.util.List;
import java.util.*;

/**
 * En esta clase se epcansulan los algoritmos clasicos que se piden en el proyecto
 */
public class AlgoritmosGrafo {

    //Dijksta:
    //Queremos retornar la ruta mas corta en distancia desde el origen hasta el destino con una lista de adyacencia
    public static List<Parada> dijkstra(Map<Parada, List<Ruta>> listaadyacencia, Parada origen, Parada destino){
        //distancias minimas acumuladas desde el inicio y previo para reconstruir el caminoo
        Map<Parada, Integer> distancia = new HashMap<>();
        Map<Parada, Parada> previo = new HashMap<>();

        //inicia las listancias con lo maximo y el previo con null y pone la distancia en el origen en 0
        for(Parada p : listaadyacencia.keySet()){
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
            List<Ruta> rutas = listaadyacencia.get(u);
            for(Ruta r : rutas){
                Parada a = r.getDestino();
                int peso = r.getDistancia();
                int nuevaDistancia = distancia.get(u) + peso;
                if(nuevaDistancia < distancia.get(a)){
                    distancia.put(a, nuevaDistancia);
                    previo.put(a, u);
                    cola.remove(a);
                    cola.add(u);
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

}
