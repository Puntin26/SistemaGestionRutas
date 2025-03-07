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

    public void insertarParada(Parada parada){
        this.paradas.add(parada);
        this.listaAdyacencia.put(parada, new ArrayList<>());
    }

    public void insertarRuta(Ruta ruta){
        this.rutas.add(ruta);
        this.listaAdyacencia.get(ruta.getOrigen()).add(ruta);
    }

    public void eliminarRuta(Ruta ruta){
        this.rutas.remove(ruta);

        listaAdyacencia.get(ruta.getOrigen()).remove(ruta);
    }

    public void eliminarParada(Parada parada){
        this.paradas.remove(parada);

        rutas.removeIf(ruta -> ruta.getOrigen().equals(parada) || ruta.getDestino().equals(parada));
        listaAdyacencia.remove(parada);

        listaAdyacencia.values().forEach(lista -> lista.removeIf(ruta -> ruta.getOrigen().equals(parada) || ruta.getDestino().equals(parada)));

    }

    public void modificarParada(Parada parada, String nuevoNombre) {
        if (paradas.contains(parada)) {
            parada.setNombre(nuevoNombre);
        } else {
            System.out.println("La parada no existe en el sistema.");
        }
    }

    public void modificarRuta(Ruta ruta, int nuevaDistancia, float nuevoCosto, Date nuevoTiempo) {
        if (rutas.contains(ruta)) {
            ruta.setDistancia(nuevaDistancia);
            ruta.setCosto(nuevoCosto);
            ruta.setTiempo(nuevoTiempo);
        } else {
            System.out.println("La ruta no existe en el sistema.");
        }
    }





}
