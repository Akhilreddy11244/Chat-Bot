package com.chatbot.ui;
import com.chatbot.logic.ChatBotService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChatBotUi {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private ChatBotService service;

    public ChatBotUi() {
        service = new ChatBotService();
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Java Chatbot (Swing)");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");
       
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();          // Close chatbot window
                new LoginUi();            // Return to login screen
            }
        });
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        bottom.add(logoutButton, BorderLayout.WEST);


        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        ActionListener sendListener = e -> {
            String userMsg = inputField.getText().trim();
            if (userMsg.isEmpty()) return;

            chatArea.append("You: " + userMsg + "\n");
            inputField.setText("");

            try {
                String botReply = service.getBotResponse(userMsg);
                chatArea.append("Bot: " + botReply + "\n");
            } catch (Exception ex) {
                chatArea.append("Bot Error: " + ex.getMessage() + "\n");
            }
        };

        sendButton.addActionListener(sendListener);
        inputField.addActionListener(sendListener);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatBotUi::new);
    }
}
