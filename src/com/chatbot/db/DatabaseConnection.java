package com.chatbot.db;
import java.sql.*;
import java.util.*;

public class DatabaseConnection {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnection(Properties props) {
        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    public Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
