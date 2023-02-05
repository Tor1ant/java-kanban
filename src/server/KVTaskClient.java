package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    Gson gson = new Gson();
    URI uri;
    HttpRequest httpRequest;
    String apiToken;
    HttpClient httpClient;

    public KVTaskClient(String uri) {
        this.uri = URI.create(uri);
        this.httpRequest = HttpRequest.newBuilder().uri(this.uri).GET().build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
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

    }
}
