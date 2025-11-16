package com.chatbot.logic;

import com.chatbot.db.ChatHistoryDAO;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatBotService {

    private final String apiKey;
    private final ChatHistoryDAO dao;
    private String extractText(String json) {
        String key = "\"text\":";
        int index = json.indexOf(key);
        if (index == -1) return json; // fallback
        int start = json.indexOf("\"", index + key.length()) + 1;
        int end = json.indexOf("\"", start);
        if (start == -1 || end == -1) return json; // fallback
        String extracted = json.substring(start, end);
        return extracted.replace("\\n", "\n").replace("\\\"", "\"");
    }

    public ChatBotService() {
        try {
            Properties props = new Properties();
            InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");
            props.load(in);

            apiKey = props.getProperty("gemini.api.key");
            dao = new ChatHistoryDAO(props);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }

    public String getBotResponse(String userMsg) throws Exception {

        dao.saveUserMessage(userMsg);

        String model = "models/gemini-2.0-flash:generateContent";

        String endpoint =
                "https://generativelanguage.googleapis.com/v1/" + model + "?key=" + apiKey;

        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);

        // Request JSON for Gemini
        String json = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"parts\": [\n" +
                "        { \"text\": \"" + escape(userMsg) + "\" }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(json.getBytes(StandardCharsets.UTF_8));
        }

        BufferedReader reader;

        if (con.getResponseCode() >= 200 && con.getResponseCode() < 300) {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
        }

        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        String jsonResponse = response.toString();

        // Extract clean response text (simple JSON parsing)
        String cleanText = extractText(jsonResponse);

        dao.saveBotResponse(cleanText);

        return cleanText;
    }


    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }
}
