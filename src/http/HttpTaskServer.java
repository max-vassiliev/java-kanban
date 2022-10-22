package http;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import managers.TaskManager;
import utilities.TaskHandler;

import java.io.IOException;


public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler(taskManager));
            httpServer.start();
            System.out.println("HTTP-сервер запущен на порту " + PORT);
            System.out.println("Адрес: http://localhost:" + PORT + "/");
        } catch (IOException e) {
            System.out.println("Ошибка при старте HTTP-сервера");
        }
    }

    // остановить сервер
    public void stop() {
        httpServer.stop(0);
    }

}
