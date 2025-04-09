package com.example.demoproyfinal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RutaDAO {

    //private static final String URL = "jdbc:mariadb://localhost:3308/mi_base_datos";
    private static final String URL = "jdbc:mariadb://localhost:3307/db_empty";
    private static final String USER = "root";
    private static final String PASSWORD = "example";

    public List<Ruta> obtenerrutas() {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT origen, destino, distancia, costo, tiempo, cantTransbordos FROM rutas";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                String origenNombre = rs.getString("origen");
                String destinoNombre = rs.getString("destino");
                int distancia = rs.getInt("distancia");
                float costo = rs.getFloat("costo");
                int tiempo = rs.getInt("tiempo");
                int cantTransbordos = rs.getInt("cantTransbordos");

                // Crear objetos Parada a partir de los nombres
                Parada origen = new Parada(origenNombre);
                Parada destino = new Parada(destinoNombre);

                // Crear la ruta con los datos obtenidos
                Ruta ruta = new Ruta(origen, destino, distancia, costo, tiempo);
                ruta.setCantTransbordos(cantTransbordos);

                // Agregar la ruta a la lista
                rutas.add(ruta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rutas;
    }

    public void insertarRuta(Ruta ruta) {
        // Suponiendo que las columnas de la tabla son: origen, destino, distancia, costo, tiempo, cantTransbordos
        String sql = "INSERT INTO rutas (origen, destino, distancia, costo, tiempo, cantTransbordos) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Usamos los nombres de las paradas para 'origen' y 'destino'
            statement.setString(1, ruta.getOrigen().getNombre());
            statement.setString(2, ruta.getDestino().getNombre());
            statement.setInt(3, ruta.getDistancia());
            statement.setFloat(4, ruta.getCosto());
            statement.setInt(5, ruta.getTiempo());
            statement.setInt(6, ruta.getCantTransbordos());

            statement.executeUpdate();
            System.out.println("Ruta insertada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarRuta(Ruta ruta) {
        String sql = "DELETE FROM rutas WHERE origen = ? AND destino = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ruta.getOrigen().getNombre());
            statement.setString(2, ruta.getDestino().getNombre());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ruta eliminada correctamente.");
            } else {
                System.out.println("No se encontró la ruta a eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarRutasPorParada(Parada parada) {
        String sql = "DELETE FROM rutas WHERE origen = ? OR destino = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, parada.getNombre());
            statement.setString(2, parada.getNombre());
            int rowsAffected = statement.executeUpdate();
            System.out.println("Se eliminaron " + rowsAffected + " rutas asociadas a la parada " + parada.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarRutasPorParada(String oldName, String newName) {
        String sql = "UPDATE rutas SET " +
                "origen = CASE WHEN origen = ? THEN ? ELSE origen END, " +
                "destino = CASE WHEN destino = ? THEN ? ELSE destino END " +
                "WHERE origen = ? OR destino = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, oldName);
            statement.setString(2, newName);
            statement.setString(3, oldName);
            statement.setString(4, newName);
            statement.setString(5, oldName);
            statement.setString(6, oldName); // Aquí se actualiza si el oldName aparece en destino

            int rowsAffected = statement.executeUpdate();
            System.out.println("Se actualizaron " + rowsAffected + " rutas asociadas a la parada modificada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Se actualizan los valores de distancia, costo, tiempo y cantTransbordos para la ruta identificada por origen y destino.
    public void actualizarRuta(Ruta ruta) {
        String sql = "UPDATE rutas SET distancia = ?, costo = ?, tiempo = ?, cantTransbordos = ? WHERE origen = ? AND destino = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ruta.getDistancia());
            statement.setFloat(2, ruta.getCosto());
            statement.setInt(3, ruta.getTiempo());
            statement.setInt(4, ruta.getCantTransbordos());
            statement.setString(5, ruta.getOrigen().getNombre());
            statement.setString(6, ruta.getDestino().getNombre());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ruta actualizada correctamente.");
            } else {
                System.out.println("No se encontró la ruta a actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
