package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private final HttpClient httpClient;
    private final HttpResponse.BodyHandler<String> handler;
    URI clientURI;

    public KVTaskClient(String uri) {
        clientURI = URI.create(uri.substring(0, uri.length() - 9));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(uri)).GET().build();
        handler = HttpResponse.BodyHandlers.ofString();
        this.httpClient = HttpClient.newHttpClient();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, handler);
            if (httpResponse.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(httpResponse.body());
                apiToken = jsonElement.getAsString();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String json) {
        String path = clientURI + "/save/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(path);
        //собрать httprequest
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(request, handler);
            System.out.println("код ответа на запрос: " + httpResponse.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        String response;
        String path = clientURI + "/load/" + key + "?API_TOKEN=" + apiToken;
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(path)).GET().build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, handler);
            System.out.println("код ответа на запрос: " + httpResponse.statusCode());
            response = httpResponse.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}