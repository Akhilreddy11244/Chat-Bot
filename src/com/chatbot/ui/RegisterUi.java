package com.chatbot.ui;

import com.chatbot.db.*;

import javax.swing.*;
import java.awt.*;

public class RegisterUi {

    private JFrame frame;
    private JTextField nameField, emailField;
    private JPasswordField passwordField;

    private UserDAO userDAO;

    public RegisterUi() {
        DatabaseConnection db = new DatabaseConnection(loadProps());
        userDAO = new UserDAO(db);

        initUI();
    }

    private void initUI() {
        frame = new JFrame("Register");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2));

        frame.add(new JLabel("Name:"));
        nameField = new JTextField();
        frame.add(nameField);

        frame.add(new JLabel("Email:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        JButton registerBtn = new JButton("Create Account");
        frame.add(registerBtn);

        registerBtn.addActionListener(e -> registerUser());

        frame.setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());

        if (userDAO.register(name, email, pass)) {
            JOptionPane.showMessageDialog(frame, "Registration successful!");
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Email already exists!");
        }
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
}
