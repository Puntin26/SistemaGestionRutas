package com.example.demoproyfinal;

import java.util.*;

/**
 * En esta clase se encapsulan los algoritmos clasicos del proyecto.
 */
public class AlgoritmosGrafo {


    public static List<Parada> dijkstra(Map<Parada,List<Ruta>> listaAdy, Parada origen, Parada destino) {

        Map<Parada,Integer> distancia = new HashMap<>();
        Map<Parada,Parada>  previo    = new HashMap<>();

        /* inicializar */
        for (Parada p : listaAdy.keySet()) {
            distancia.put(p, Integer.MAX_VALUE);
            previo.put(p, null);
        }
        distancia.put(origen, 0);

        PriorityQueue<Parada> cola = new PriorityQueue<>(Comparator.comparingInt(distancia::get));
        cola.add(origen);

        while (!cola.isEmpty()) {
            Parada u = cola.poll();
            if (u.equals(destino)) break;

            List<Ruta> rutas = listaAdy.get(u);
            if (rutas == null) continue;

            for (Ruta r : rutas) {
                Parada a = r.getDestino();
                int w = r.getDistancia();

                if (!distancia.containsKey(a)) distancia.put(a, Integer.MAX_VALUE);

                int nueva = distancia.get(u) + w;
                if (nueva < distancia.get(a)) {
                    distancia.put(a, nueva);
                    previo.put(a, u);
                    cola.remove(a);
                    cola.add(a);
                }
            }
        }

        List<Parada> camino = new ArrayList<>();
        Parada actual = destino;
        while (actual != null) {
            camino.add(actual);
            actual = previo.get(actual);
        }
        Collections.reverse(camino);

        if (distancia.get(destino) == Integer.MAX_VALUE) return Collections.emptyList();
        return camino;
    }


    public static int[][] floydWarshall(List<Parada> paradas, Map<Parada,List<Ruta>> listaAdy) {

        int n = paradas.size();
        Map<Parada,Integer> idx = new HashMap<>();
        for (int i=0;i<n;i++) idx.put(paradas.get(i),i);

        int[][] dist = new int[n][n];
        for (int i=0;i<n;i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE/2);
            dist[i][i] = 0;
        }

        for (Parada p : paradas) {
            int i = idx.get(p);
            if (listaAdy.get(p)!=null) {
                for (Ruta r : listaAdy.get(p)) {
                    int j = idx.get(r.getDestino());
                    dist[i][j] = Math.min(dist[i][j], r.getDistancia());
                }
            }
        }

        for (int k=0;k<n;k++)
            for (int i=0;i<n;i++)
                for (int j=0;j<n;j++)
                    if (dist[i][k]+dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k]+dist[k][j];

        return dist;
    }

    /* ---------------- Prim, Kruskal (pendiente) ---------------- */
}
