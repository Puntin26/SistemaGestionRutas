package com.example.demoproyfinal;

import java.util.*;

public class Controlador {

    private ArrayList<Parada> paradas;
    private ArrayList<Ruta> rutas;
    Map<Parada, List<Ruta>> listaAdyacencia = new HashMap<>();
    private static Controlador controlador = null;

    private Controlador() {
        super();
        this.paradas = new ArrayList<>();
        this.rutas = new ArrayList<>();
        this.listaAdyacencia = new HashMap<>();
    }

    public static Controlador getInstance() {
        if (controlador == null) {
            controlador = new Controlador();
        }return controlador;
    }

    public ArrayList<Parada> getParadas() {
        return paradas;
    }

    public void setParadas(ArrayList<Parada> paradas) {
        this.paradas = paradas;
    }

    public ArrayList<Ruta> getRutas() {
        return rutas;
    }

    public void setRutas(ArrayList<Ruta> rutas) {
        this.rutas = rutas;
    }

    public void insertarParada(Parada parada) {
        if (parada == null || parada.getNombre() == null || parada.getNombre().trim().isEmpty()) {
            System.out.println("Error: El nombre de la parada no puede estar vacío.");
            return;
        }

        if (paradas.stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(parada.getNombre()))) {
            System.out.println("Error: Ya existe una parada con el nombre '" + parada.getNombre() + "'.");
            return;
        }

        this.paradas.add(parada);
        this.listaAdyacencia.put(parada, new ArrayList<>());
        System.out.println("Parada '" + parada.getNombre() + "' agregada correctamente.");
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

        this.rutas.add(ruta);
        this.listaAdyacencia.get(ruta.getOrigen()).add(ruta);
        System.out.println("Ruta agregada correctamente.");
    }

    public void eliminarParada(Parada parada) {
        if (parada == null || !paradas.contains(parada)) {
            System.out.println("Error: La parada no existe en el sistema.");
            return;
        }

        paradas.remove(parada);
        rutas.removeIf(ruta -> ruta.getOrigen().equals(parada) || ruta.getDestino().equals(parada));
        listaAdyacencia.remove(parada);
        //System.out.println("Parada '" + parada.getNombre() + "' eliminada correctamente.");
    }

    public void eliminarRuta(Ruta ruta) {
        if (ruta == null || !rutas.contains(ruta)) {
            System.out.println("Error: La ruta no existe en el sistema.");
            return;
        }

        rutas.remove(ruta);
        listaAdyacencia.get(ruta.getOrigen()).remove(ruta);
       // System.out.println("Ruta eliminada correctamente.");
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
        //System.out.println("Parada modificada correctamente.");
    }

    public void modificarRuta(Ruta ruta, int nuevaDistancia, float nuevoCosto, Date nuevoTiempo) {
        if (ruta == null || !rutas.contains(ruta)) {
            System.out.println("Error: La ruta no existe en el sistema.");
            return;
        }

        if (nuevaDistancia <= 0 || nuevoCosto <= 0) {
            System.out.println("Error: La distancia y el costo deben ser valores positivos.");
            return;
        }

        ruta.setDistancia(nuevaDistancia);
        ruta.setCosto(nuevoCosto);
        ruta.setTiempo(nuevoTiempo);
        //System.out.println("Ruta modificada correctamente.");
    }

    public Map<Parada, List<Ruta>> getListaAdyacencia() {
        return listaAdyacencia;
    }

}
