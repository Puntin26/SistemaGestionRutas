package com.example.demoproyfinal;

import com.example.demoproyfinal.Parada;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParadaDAO {

    //private static final String URL = "jdbc:mariadb://localhost:3308/mi_base_datos";
    private static final String URL = "jdbc:mariadb://localhost:3307/db_empty";
    private static final String USER = "root";
    private static final String PASSWORD = "example";

    public List<Parada> obtenerParadas() {
        List<Parada> paradas = new ArrayList<>();
        String sql = "SELECT nombre FROM paradas";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                paradas.add(new Parada(rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paradas;
    }


    public void insertarParada(Parada parada) {
        String sql = "INSERT INTO paradas (nombre) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, parada.getNombre());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarParada(Parada nombre) {
        String sql = "DELETE FROM paradas WHERE nombre = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre.getNombre());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Parada eliminada correctamente.");
            } else {
                System.out.println("No se encontró la parada a eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarParada(String oldName, String newName) {
        String sql = "UPDATE paradas SET nombre = ? WHERE nombre = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, oldName);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Parada actualizada correctamente.");
            } else {
                System.out.println("No se encontró la parada a actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
