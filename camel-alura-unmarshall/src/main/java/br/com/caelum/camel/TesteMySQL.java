package br.com.caelum.camel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteMySQL {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:55555/camel";
        String username = "root";
        String password = "";

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
