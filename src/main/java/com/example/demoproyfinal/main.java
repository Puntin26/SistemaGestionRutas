package com.example.demoproyfinal;

import java.util.ArrayList;
import java.util.Date;

public class main {
    public static void main(String[] args) {

        Controlador controlador = new Controlador();
        controlador.paradas = new ArrayList<>();
        controlador.rutas = new ArrayList<>();
        Ruta ruta = new Ruta(10, 50.0F, new Date());
        Parada parada = new Parada("Parada");

        controlador.paradas.add(parada);
        controlador.rutas.add(ruta);

        System.out.println(controlador.paradas);










    }

}
