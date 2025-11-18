package com.chatbot.ui;
import com.chatbot.db.DatabaseConnection;
import com.chatbot.db.UserDAO;
import javax.swing.*;
import java.awt.*;

public class LoginUi {

    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;

    private UserDAO userDAO;

    public LoginUi() {
        DatabaseConnection db = new DatabaseConnection(loadProps());
        userDAO = new UserDAO(db);

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Login");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));

        frame.add(new JLabel("Email:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        frame.add(loginBtn);
        frame.add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        frame.setVisible(true);
    }

    void login() {
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());

        if (userDAO.login(email, pass)) {
            JOptionPane.showMessageDialog(frame, "Login Successful!");
            frame.dispose();
            new ChatBotUi();   // <-- open chatbot
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials!");
        }
    }

    void register() {
        new RegisterUi(); // open register screen
    }

    private java.util.Properties loadProps() {
        try {
            java.util.Properties props = new java.util.Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new LoginUi();
    }
}
