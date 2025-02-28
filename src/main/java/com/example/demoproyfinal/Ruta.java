package com.example.demoproyfinal;

import java.util.Date;

public class Ruta {
    Parada origen;
    Parada destino;
    int distancia;
    float costo;
    Date tiempo;
    int cantTransbordos;

    public Ruta(Parada origen, Parada destino, int distancia, float costo, Date tiempo) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.costo = costo;
        this.tiempo = tiempo;
        this.cantTransbordos = 0;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public int getCantTransbordos() {
        return cantTransbordos;
    }

    public void setCantTransbordos(int cantTransbordos) {
        this.cantTransbordos = cantTransbordos;
    }

    @Override
    public String toString() {
        return "Ruta{Origen: " + origen.getNombre() + ", Destino: " + destino.getNombre() +
                ", Distancia: " + distancia + " km, Costo: $" + costo + ", Tiempo: " + tiempo + "}";
    }
}
