package com.example.demoproyfinal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



}
