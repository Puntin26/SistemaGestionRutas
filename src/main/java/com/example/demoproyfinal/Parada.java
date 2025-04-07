package com.example.demoproyfinal;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parada)) return false;
        Parada parada = (Parada) o;
        return Objects.equals(nombre, parada.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
