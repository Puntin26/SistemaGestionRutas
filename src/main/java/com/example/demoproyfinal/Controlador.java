package com.example.demoproyfinal;

import java.util.ArrayList;

public class Controlador {

    ArrayList<Parada> paradas;
    ArrayList<Ruta> rutas;

    public Controlador() {
        this.paradas = new ArrayList<>();
        this.rutas = new ArrayList<>();
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
