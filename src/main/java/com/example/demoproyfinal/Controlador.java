package com.example.demoproyfinal;

import java.util.*;


public class Controlador {

    private ArrayList<Parada> paradas;
    private ArrayList<Ruta>   rutas;
    private Map<Parada, List<Ruta>> listaAdyacencia;

    private static Controlador controlador = null;

    private Controlador() {
        paradas         = new ArrayList<>();
        rutas           = new ArrayList<>();
        listaAdyacencia = new HashMap<>();
    }

    public static Controlador getInstance() {
        if (controlador == null) controlador = new Controlador();
        return controlador;
    }

    public void reconstruirListaAdyacencia() {
        listaAdyacencia = new HashMap<>();

        /* 1. crear entrada vacía para TODAS las paradas  */
        for (Parada p : paradas) {
            listaAdyacencia.put(p, new ArrayList<>());
        }
        /* 2. colocar cada ruta en la lista del origen    */
        for (Ruta r : rutas) {
            listaAdyacencia.get(r.getOrigen()).add(r);
        }
    }

    public ArrayList<Parada> getParadas()                       { return paradas; }
    public void setParadas(ArrayList<Parada> paradas)           { this.paradas = paradas; }

    public ArrayList<Ruta>  getRutas()                          { return rutas; }
    public void setRutas(ArrayList<Ruta> rutas)                 { this.rutas = rutas; }

    public Map<Parada, List<Ruta>> getListaAdyacencia()         { return listaAdyacencia; }

    public void insertarParada(Parada parada) {
        if (parada == null || parada.getNombre() == null || parada.getNombre().trim().isEmpty()) {
            System.out.println("Error: El nombre de la parada no puede estar vacío.");
            return;
        }
        if (paradas.stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(parada.getNombre()))) {
            System.out.println("Error: Ya existe una parada con el nombre '" + parada.getNombre() + "'.");
            return;
        }
        paradas.add(parada);
        listaAdyacencia.put(parada, new ArrayList<>());
        System.out.println("Parada '" + parada.getNombre() + "' agregada correctamente.");
    }

    public void eliminarParada(Parada parada) {
        if (parada == null || !paradas.contains(parada)) {
            System.out.println("Error: La parada no existe en el sistema.");
            return;
        }
        paradas.remove(parada);
        rutas.removeIf(r -> r.getOrigen().equals(parada) || r.getDestino().equals(parada));
        listaAdyacencia.remove(parada);
    }

    public void eliminarParadaCompletamente(Parada parada) {
        paradas.remove(parada);
        listaAdyacencia.remove(parada);
        listaAdyacencia.values().forEach(l -> l.removeIf(r ->
                r.getOrigen().equals(parada) || r.getDestino().equals(parada)));
        rutas.removeIf(r -> r.getOrigen().equals(parada) || r.getDestino().equals(parada));
    }

    public void modificarParada(Parada parada, String nuevoNombre) {
        if (parada == null || !paradas.contains(parada)) {
            System.out.println("Error: La parada no existe en el sistema.");
            return;
        }
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            System.out.println("Error: El nuevo nombre no puede estar vacío.");
            return;
        }
        if (paradas.stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(nuevoNombre))) {
            System.out.println("Error: Ya existe una parada con el nombre '" + nuevoNombre + "'.");
            return;
        }
        parada.setNombre(nuevoNombre);
    }

    public void insertarRuta(Ruta ruta) {
        if (ruta == null || ruta.getOrigen() == null || ruta.getDestino() == null) {
            System.out.println("Error: La ruta no puede ser nula y debe tener origen y destino válidos.");
            return;
        }
        if (!paradas.contains(ruta.getOrigen()) || !paradas.contains(ruta.getDestino())) {
            System.out.println("Error: Las paradas de origen y destino deben existir en el sistema.");
            return;
        }
        if (ruta.getDistancia() <= 0 || ruta.getCosto() <= 0) {
            System.out.println("Error: La distancia y el costo deben ser valores positivos.");
            return;
        }
        rutas.add(ruta);
        listaAdyacencia.get(ruta.getOrigen()).add(ruta);
        System.out.println("Ruta agregada correctamente.");
    }

    public void eliminarRuta(Ruta ruta) {
        if (ruta == null || !rutas.contains(ruta)) {
            System.out.println("Error: La ruta no existe en el sistema.");
            return;
        }
        rutas.remove(ruta);
        listaAdyacencia.get(ruta.getOrigen()).remove(ruta);
    }

    public void modificarRuta(Ruta ruta, int nuevaDistancia, float nuevoCosto, int nuevoTiempo) {
        if (ruta == null || !rutas.contains(ruta)) {
            System.out.println("Error: La ruta no existe en el sistema.");
            return;
        }
        if (nuevaDistancia <= 0 || nuevoCosto <= 0 || nuevoTiempo <= 0) {
            System.out.println("Error: La distancia y el costo deben ser valores positivos.");
            return;
        }
        ruta.setDistancia(nuevaDistancia);
        ruta.setCosto(nuevoCosto);
        ruta.setTiempo(nuevoTiempo);
    }
}
