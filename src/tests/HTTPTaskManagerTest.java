package tests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import http.KVServer;
import http.KVTaskClient;
import managers.HTTPTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class HTTPTaskManagerTest {

    protected static HTTPTaskManager taskManager;
    protected static String serverUri = "http://localhost:8078/";
    private static final int STATUS_CODE_200 = 200;
    private static final String HOST = "http://localhost:8080/";
    protected KVServer kvServer;
    protected static Task task1;
    protected static Epic epic1;
    protected static Subtask subtask1;
    protected static Subtask subtask2;
    Gson gson = new Gson();


    protected HTTPTaskManagerTest() {
        taskManager = new HTTPTaskManager(serverUri);
    }

    public void initializeTest() {
        new HTTPTaskManagerTest();
    }

    @BeforeEach
    public void startKVServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить KVServer");
        }
    }

    @AfterEach
    public void stopKVServer() {
        kvServer.stop();
    }

    @AfterEach
    public void stopHttpTaskServer() {
        taskManager.stopHttpTaskServer();
    }

    // ---------------------------------------------
    // ТЕСТЫ
    // ---------------------------------------------

    // загрузить задачу с сервера
    @Test
    public void loadTaskFromServer() {
        int id = 1;
        KVTaskClient taskClient = new KVTaskClient(serverUri);

        createTask1();
        task1.setId(id);
        task1.setType(TaskType.TASK);
        List<Task> taskAsList = new ArrayList<>(List.of(task1));
        String json = gson.toJson(taskAsList);
        taskClient.put("tasks", json);

        initializeTest();
        Task uploadedTask = taskManager.getTask(id);
        assertEquals(task1, uploadedTask, "Задача восстановлена неверно");
    }

    // сохранить задачу на сервере
    @Test
    public void saveTaskOnServer() {
        KVTaskClient taskClient = new KVTaskClient(serverUri);

        initializeTest();
        createTask1();
        int id = taskManager.add(task1);
        Task taskInManager = taskManager.getTask(id);

        List<Task> savedTasks = new ArrayList<>();
        String json = taskClient.load("tasks");
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject()) continue;
            Task task = gson.fromJson(jsonElement, Task.class);
            savedTasks.add(task);
        }

        Task taskOnServer = savedTasks.get(0);
        assertEquals(1, savedTasks.size(), "Сохранилось неверное количество задач");
        assertEquals(taskInManager, taskOnServer, "Задача сохранилась неверно");
    }

    @Test
    public void addTask() {
        URI url = URI.create(HOST + "tasks/task/");
        HttpClient client = HttpClient.newHttpClient();

        initializeTest();
        createTask1();

        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCode = response.statusCode();
            assertEquals(STATUS_CODE_200, receivedStatusCode, "Код не совпадает");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // добавить эпик без подзадач
    @Test
    public void addEpicWithOutSubtasks() {
        URI url = URI.create(HOST + "tasks/epic/");
        HttpClient client = HttpClient.newHttpClient();

        initializeTest();
        createEpic1();

        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCode = response.statusCode();
            assertEquals(STATUS_CODE_200, receivedStatusCode, "Код не совпадает");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addEpicWithSubtasks() {
        URI urlEpic = URI.create(HOST + "tasks/epic/");
        URI urlSubtask = URI.create(HOST + "tasks/subtask/");
        HttpClient client = HttpClient.newHttpClient();

        initializeTest();

        createEpic1();
        createSubtask1();
        createSubtask2();

        String jsonEpic1 = gson.toJson(epic1);
        String jsonSubtask1 = gson.toJson(subtask1);
        String jsonSubtask2 = gson.toJson(subtask2);

        HttpRequest requestEpic1 = HttpRequest.newBuilder()
                .uri(urlEpic)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic1))
                .build();

        HttpRequest.BodyPublisher bodySubtask1 = HttpRequest.BodyPublishers.ofString(jsonSubtask1);
        HttpRequest requestSubtask1 = HttpRequest.newBuilder().uri(urlSubtask).POST(bodySubtask1).build();

        HttpRequest.BodyPublisher bodySubtask2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(urlSubtask).POST(bodySubtask2).build();

        try {
            HttpResponse<String> responseEpic1 = client.send(requestEpic1, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCodeEpic1 = responseEpic1.statusCode();
            assertEquals(STATUS_CODE_200, receivedStatusCodeEpic1, "Код не совпадает");

            HttpResponse<String> responseSubtask1 = client.send(requestSubtask1, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCodeSubtask1 = responseSubtask1.statusCode();
            assertEquals(STATUS_CODE_200, receivedStatusCodeSubtask1, "Код не совпадает");

            HttpResponse<String> responseSubtask2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCodeSubtask2 = responseSubtask2.statusCode();
            assertEquals(STATUS_CODE_200, receivedStatusCodeSubtask2, "Код не совпадает");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------------------------
    //  ШАБЛОНЫ ЗАДАЧ
    // ---------------------------------------------

    protected void createTask1() {
        task1 = new Task("Task1",
                "Description task 1",
                "NEW",
                "15.10.2022, 14:00",
                "01:00");
    }

    protected void createEpic1() {
        epic1 = new Epic("Epic1", "Description Epic1");
    }

    protected void createSubtask1() {
        subtask1 = new Subtask("Epic1 Subtask1",
                "Description Epic1 Subtask1",
                "NEW",
                "Epic1",
                "16.10.2022, 12:00",
                "00:30"
        );
    }

    protected void createSubtask2() {
        subtask2 = new Subtask("Epic1 Subtask2",
                "Description Epic1 Subtask2",
                "NEW",
                "Epic1",
                "17.10.2022, 12:00",
                "00:30"
        );
    }
}