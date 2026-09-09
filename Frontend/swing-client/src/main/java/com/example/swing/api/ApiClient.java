package com.example.swing.api;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    private final HttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        // Ajout d'un timeout pour éviter que l'application ne bloque
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
        System.out.println("=== ApiClient initialisé ===");
        System.out.println("URL: " + BASE_URL);
        System.out.println("Username: " + USERNAME);
    }

    public Gson getGson() {
        return gson;
    }

    private String getAuthHeader() {
        String credentials = USERNAME + ":" + PASSWORD;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encoded;
    }

    // GET
    public String get(String endpoint) throws IOException, InterruptedException {
        String fullUrl = BASE_URL + endpoint;
        System.out.println("📤 GET " + fullUrl);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Authorization", getAuthHeader())
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("📥 Status: " + response.statusCode());
            
            if (response.statusCode() >= 400) {
                System.err.println("❌ Erreur HTTP " + response.statusCode() + ": " + response.body());
                throw new IOException("Erreur API (code " + response.statusCode() + ") : " + response.body());
            }
            
            String body = response.body();
            if (body == null || body.isEmpty()) {
                System.out.println("⚠️ Réponse vide");
                return "[]";
            }
            
            System.out.println("📥 Response: " + (body.length() > 200 ? body.substring(0, 200) + "..." : body));
            return body;
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Erreur de connexion: " + e.getMessage());
            throw e;
        }
    }

    // POST
    public String post(String endpoint, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);
        System.out.println("📤 POST " + BASE_URL + endpoint);
        System.out.println("   Body: " + json);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", getAuthHeader())
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("📥 Status: " + response.statusCode());
        checkStatus(response);
        return response.body();
    }

    // PUT
    public String put(String endpoint, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);
        System.out.println("📤 PUT " + BASE_URL + endpoint);
        System.out.println("   Body: " + json);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", getAuthHeader())
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("📥 Status: " + response.statusCode());
        checkStatus(response);
        return response.body();
    }

    // DELETE
    public void delete(String endpoint) throws IOException, InterruptedException {
        System.out.println("📤 DELETE " + BASE_URL + endpoint);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", getAuthHeader())
                .timeout(Duration.ofSeconds(10))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("📥 Status: " + response.statusCode());
        checkStatus(response);
    }

    private void checkStatus(HttpResponse<String> response) throws IOException {
        int status = response.statusCode();
        if (status >= 400) {
            throw new IOException("Erreur API (code " + status + ") : " + response.body());
        }
    }
}