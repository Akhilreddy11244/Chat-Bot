package com.chatbot.logic;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ChatBotService {
    private final String apiKey;

    public ChatBotService() {
        try {
            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            apiKey = props.getProperty("gemini.api.key");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load API key");
        }
    }

    public String getBotResponse(String userMsg) throws Exception {
        String endpoint =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=" 
            + apiKey;

        HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);

        String json = "{ \"contents\": [ { \"role\": \"user\", \"parts\": [ { \"text\": \"" 
                      + userMsg.replace("\"", "\\\"") + "\" } ] } ] }";

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder resp = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            resp.append(line);
        }

        return extractText(resp.toString());
    }

    private String extractText(String json) {
        int start = json.indexOf("\"text\":");
        if (start == -1) return "No reply";

        start = json.indexOf("\"", start + 7) + 1;
        int end = json.indexOf("\"", start);

        return json.substring(start, end)
                   .replace("\\n", "\n")
                   .replace("\\\"", "\"");
    }
}
