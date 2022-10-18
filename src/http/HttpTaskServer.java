package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class HttpTaskServer {

    private static final int PORT = 8080; // TODO возможно, перенести куда-то еще
    private static final String FILE = "resources/backup-s8.csv"; // TODO удалить, когда удалим FileBackedTaskManager
    public static TaskManager taskManager;
    private HttpServer httpServer;
    private static final Gson gson = new Gson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        new HttpTaskServer();
    }

    public HttpTaskServer() {
        taskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager(FILE);
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler());
            httpServer.start(); // TODO добавить метод остановки сервера

            //TODO не знаю, нужно ли
            System.out.println("HTTP-сервер запущен на " + PORT + " порту");
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера");
        }
    }

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            int statusCode = 404;
            String response = null;
            String method = httpExchange.getRequestMethod();
            String uri = httpExchange.getRequestURI().toString();

            switch (method) {
                case "GET":
                    response = handleGetRequest(uri);
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    response = handlePostRequest(uri, body);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(uri);
                    break;
            }

            if (response != null) statusCode = 200;
            httpExchange.sendResponseHeaders(statusCode, 0);

            if (response == null) return;

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        // обработка запросов GET
        private String handleGetRequest(String uri) {
            // получить все задачи в порядке приоритета
            if ("/tasks/".equals(uri)) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                return gson.toJson(prioritizedTasks);
            }

            String[] parts = uri.split("/");
            String type = parts[2];

            // получить задачу
            if (uri.contains("?id=")) {
                int id = parseId(parts[3]);
                Task task = null;

                if ("task".equals(type)) task = taskManager.getTask(id);
                if ("epic".equals(type)) task = taskManager.getEpic(id);
                if ("subtask".equals(type)) task = taskManager.getSubtask(id);

                return gson.toJson(task);
            }

            // получить все задачи одного типа
            if ("task".equals(type)) {
                List<Task> tasks = taskManager.getTasks();
                return gson.toJson(tasks);
            }
            if ("epic".equals(type)) {
                List<Epic> epics = taskManager.getEpics();
                return gson.toJson(epics);
            }
            if ("subtask".equals(type)) {
                List<Subtask> subtasks = taskManager.getSubtasks();
                return gson.toJson(subtasks);
            }

            // получить историю просмотров
            if ("history".equals(type)) {
                List<Task> history = taskManager.getHistory();
                return gson.toJson(history);
            }

            return null;
        }

        // обработка запросов POST
        private String handlePostRequest(String uri, String body) {
            String[] parts = uri.split("/");
            String type = parts[2];

            // обновить задачу
            if (uri.contains("?id=")) {
                int id = parseId(parts[3]);

                if ("task".equals(type)) {
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.update(task);
                    Task taskUpdated = taskManager.getTask(id);  // TODO можно ли без get?
                    return gson.toJson(taskUpdated);
                }
                if ("epic".equals(type)) {
                    Epic epic = gson.fromJson(body, Epic.class);
                    taskManager.update(epic);
                    Epic epicUpdated = taskManager.getEpic(id);  // TODO можно ли без get?
                    return gson.toJson(epicUpdated);
                }
                if ("subtask".equals(type)) {
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    taskManager.update(subtask);
                    Subtask subtaskUpdated = taskManager.getSubtask(id);  // TODO можно ли без get?
                    return gson.toJson(subtaskUpdated);
                }
            }

            // добавить задачу
            if ("task".equals(type)) {
                Task task = gson.fromJson(body, Task.class);
                int id = taskManager.add(task);
                Task taskSaved = taskManager.getTask(id);  // TODO убрать, если не нужно get
                return gson.toJson(taskSaved);
            }
            if ("epic".equals(type)) {
                Epic epic = gson.fromJson(body, Epic.class);
                int id = taskManager.add(epic);
                Epic epicSaved = taskManager.getEpic(id);  // TODO убрать, если не нужен get
                return gson.toJson(epicSaved);
            }
            if ("subtask".equals(type)) {
                Subtask subtask = gson.fromJson(body, Subtask.class);
                int id = taskManager.add(subtask);
                Subtask subtaskSaved = taskManager.getSubtask(id);
                return gson.toJson(subtaskSaved);
            }

            return null;
        }

        // обработка запросов DELETE
        private String handleDeleteRequest(String uri) {
            String response = null;
            String[] parts = uri.split("/");
            String type = parts[2];

            // удалить задачу
            if (uri.contains("?id=")) {
                int id = parseId(parts[3]);

                if ("task".equals(type)) {
                    Task task = taskManager.getTask(id);
                    taskManager.delete(task);
                    return "Удалили задачу";
                }
                if ("epic".equals(type)) {
                    Epic epic = taskManager.getEpic(id);
                    taskManager.delete(epic);
                    return "Удалили эпик";
                }
                if ("subtask".equals(type)) {
                    Subtask subtask = taskManager.getSubtask(id);
                    taskManager.delete(subtask);
                    return "Удалили подзадачу";
                }
            }

            // удалить все
            if ("task".equals(type)) {
                taskManager.deleteAllTasks();
                return "Все задачи удалены";
            }
            if ("epic".equals((type))) {
                taskManager.deleteAllEpics();
                return "Все эпики удалены";
            }
            if ("subtask".equals(type)) {
                taskManager.deleteAllSubtasks();
                return "Все подзадачи удалены";
            }

            return response;
        }

        // получить ID из строки
        private int parseId(String str) {
            String[] parts = str.split("=");
            return Integer.parseInt(parts[1]);
        }
    }
}
