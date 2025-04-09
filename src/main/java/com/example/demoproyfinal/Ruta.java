package com.example.demoproyfinal;

import java.util.Date;

public class Ruta {
    Parada origen;
    Parada destino;
    int distancia;
    float costo;
    int tiempo;
    int cantTransbordos;

    public Ruta(Parada origen, Parada destino, int distancia, float costo, int tiempo) {
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

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getCantTransbordos() {
        return cantTransbordos;
    }

    public void setCantTransbordos(int cantTransbordos) {
        this.cantTransbordos = cantTransbordos;
    }

    public Parada getOrigen() {
        return origen;
    }

    public void setOrigen(Parada origen) {
        this.origen = origen;
    }

    public Parada getDestino() {
        return destino;
    }

    public void setDestino(Parada destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return distancia + "km\n";
    }

    public String toString2() {
        return + distancia + " km\n$" + costo +"\n" + tiempo + " m";
    }


}
