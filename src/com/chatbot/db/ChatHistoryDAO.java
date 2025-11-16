package com.chatbot.db;
import java.sql.*;
import java.util.*;

public class ChatHistoryDAO {
    private final DatabaseConnection db;

    public ChatHistoryDAO(Properties props) {
        this.db = new DatabaseConnection(props);
    }

    public void saveUserMessage(String msg) {
        String sql = "INSERT INTO chat_history (user_message, bot_response) VALUES (?, NULL)";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, msg);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error saving user message: " + e.getMessage());
        }
    }

    public void saveBotResponse(String botResponse) {
        String sql = "UPDATE chat_history SET bot_response = ? ORDER BY id DESC LIMIT 1";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, botResponse);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error saving bot response: " + e.getMessage());
        }
    }
}
