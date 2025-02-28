package com.example.demoproyfinal;

import java.util.ArrayList;

public class Controlador {

    private ArrayList<Parada> paradas;
    private ArrayList<Ruta> rutas;
    private static Controlador controlador = null;

    private Controlador() {
        super();
        this.paradas = new ArrayList<>();
        this.rutas = new ArrayList<>();
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
    }

    public void insertarRuta(Ruta ruta){
        this.rutas.add(ruta);
    }



}
