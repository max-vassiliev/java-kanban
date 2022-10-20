package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {

    private final HttpClient client;
    private final URI serverUri;
    private String apiToken;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    public KVTaskClient(String uri) {
        serverUri = URI.create(uri);
        client = HttpClient.newHttpClient();

        URI registerUri = URI.create(serverUri + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(registerUri)
                .GET()
                .build();

        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET);

        try {
            HttpResponse<String> response = client.send(request, bodyHandler);
            apiToken = response.body();
        } catch (IOException | InterruptedException ignored) {
        }
    }

    public void put(String key, String json) {
        // Запрос: POST /save/<ключ>?API_TOKEN=
        URI uri = URI.create(serverUri + "save/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET);

        try {
            HttpResponse<String> response = client.send(request, bodyHandler);
            System.out.println("Код ответа: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при сохранении данных на сервере");
        }
    }

    public String load(String key) {
        // Запрос: GET /load/<ключ>?API_TOKEN=
        URI uri = URI.create(serverUri + "load/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET);

        try {
            HttpResponse<String> response = client.send(request, bodyHandler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
