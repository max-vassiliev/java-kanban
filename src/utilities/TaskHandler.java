package utilities;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

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
                try {
                    response = handlePostRequest(uri, body);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "DELETE":
                response = handleDeleteRequest(uri);
                break;
        }

        if (response == null) {
            httpExchange.sendResponseHeaders(statusCode, 0);
            return;
        }
        statusCode = 200;

        httpExchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // обработка запросов GET
    private String handleGetRequest(String uri) {
        // получить все задачи в порядке приоритета
        if ("/tasks/".equals(uri)) {
            List<Task> allTasks = taskManager.getAll();
            return gson.toJson(allTasks);
        }

        String[] parts = uri.split("/");
        String type = parts[2];

        // получить задачу
        if (uri.contains("?id=")) {
            int id = parseId(parts[3]);

            if ("task".equals(type)) {
                Task task = taskManager.getTask(id);
                return gson.toJson(task);
            };
            if ("epic".equals(type)) {
                Epic epic = taskManager.getEpic(id);
                return gson.toJson(epic);
            }
            if ("subtask".equals(type)) {
                Subtask subtask = taskManager.getSubtask(id);
                return gson.toJson(subtask);
            }
            if ("epic-subtasks".equals(type)) {
                List<Subtask> subtasksInEpic = taskManager.getSubtasksInEpic(id);
                return gson.toJson(subtasksInEpic);
            }

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

        // получить список задачи в порядке приоритета
        if ("priority".equals(type)) {
            List<Task> priority = taskManager.getPrioritizedTasks();
            return gson.toJson(priority);
        }

        return null;
    }

    // обработка запросов POST
    private String handlePostRequest(String uri, String body) throws InterruptedException {
        String[] parts = uri.split("/");
        String type = parts[2];

        // обновить задачу
        if (uri.contains("?id=")) {
            int id = parseId(parts[3]);

            if ("task".equals(type)) {
                Task task = gson.fromJson(body, Task.class);
                taskManager.update(task);
                Task taskUpdated = taskManager.getTask(id);
                return gson.toJson(taskUpdated);
            }
            if ("epic".equals(type)) {
                Epic epic = gson.fromJson(body, Epic.class);
                taskManager.update(epic);
                Epic epicUpdated = taskManager.getEpic(id);
                return gson.toJson(epicUpdated);
            }
            if ("subtask".equals(type)) {
                Subtask subtask = gson.fromJson(body, Subtask.class);
                taskManager.update(subtask);
                Subtask subtaskUpdated = taskManager.getSubtask(id);
                return gson.toJson(subtaskUpdated);
            }
        }

        // добавить задачу
        if ("task".equals(type)) {
            Task task = gson.fromJson(body, Task.class);
            int id = taskManager.add(task);
            Task taskSaved = taskManager.getTask(id);
            return gson.toJson(taskSaved);
        }
        if ("epic".equals(type)) {
            Epic epic = gson.fromJson(body, Epic.class);
            int id = taskManager.add(epic);
            Epic epicSaved = taskManager.getEpic(id);
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
        return null;
    }

    // получить ID из строки
    private int parseId(String str) {
        String[] parts = str.split("=");
        return Integer.parseInt(parts[1]);
    }
}
