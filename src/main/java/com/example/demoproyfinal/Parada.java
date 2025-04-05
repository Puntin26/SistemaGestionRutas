package com.example.demoproyfinal;

import java.util.Date;

public class Parada {

    String nombre;

    public Parada(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
