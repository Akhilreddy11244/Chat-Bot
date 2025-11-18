package com.chatbot.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private DatabaseConnection db;

    public UserDAO(DatabaseConnection db) {
        this.db = db;
    }

    // Register user
    public boolean register(String name, String email, String password) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection con = db.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Login user
    public boolean login(String email, String password) {
        String sql = "SELECT id FROM users WHERE email=? AND password=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();  // true if match found

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }
}
