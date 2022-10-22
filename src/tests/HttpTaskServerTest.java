package tests;

import com.google.gson.*;
import http.KVServer;
import managers.HTTPTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HTTPTaskManager taskManager;
    private static final String HOST = "http://localhost:8080";
    private static final String SERVER_URI = "http://localhost:8078/";
    private final Gson gson = new Gson();
    private static final int STATUS_OK = 200;

    private Task task1;
    private Task task2;
    private static Epic epic1;
    private static Epic epic2;
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static Subtask subtask3;
    private KVServer kvServer;



    @BeforeEach
    public void startKVServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить KVServer");
        }
    }

    @BeforeEach
    public void initializeTest() {
        taskManager = new HTTPTaskManager(SERVER_URI);
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
    //  ТЕСТЫ — Запросы GET
    // ---------------------------------------------

    // ПОЛУЧИТЬ

    // получить задачу: GET /tasks/task/?id=
    @Test
    public void getTask() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask = HttpRequest.newBuilder()
                .uri(urlAddTask)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        Task task1Saved = null;

        try {
            HttpResponse<String> response = client.send(requestAddTask, HttpResponse.BodyHandlers.ofString());
            String taskSavedJson = response.body();
            task1Saved = gson.fromJson(taskSavedJson, Task.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задачи");
        }

        int id = task1Saved.getId();
        task1.setId(id);

        URI urlGetTask = URI.create(HOST + "/tasks/task/?id=" + id);
        HttpRequest requestGetTask = HttpRequest.newBuilder()
                .uri(urlGetTask)
                .GET()
                .build();

        Task task1Received = null;

        try {
            HttpResponse<String> response = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());
            String taskSavedJson = response.body();
            task1Received = gson.fromJson(taskSavedJson, Task.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении задачи");
        }

        assertNotNull(task1Received);
        assertEquals(task1, task1Received, "Задачи не совпадают");

    }

    // получить эпик: GET /tasks/epic/?id=
    @Test
    public void getEpic() {
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();

        String epic1Json = gson.toJson(epic1);
        URI urlAddTEpic = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAdd = HttpRequest.newBuilder()
                .uri(urlAddTEpic)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        Epic epic1Saved = null;

        try {
            HttpResponse<String> response = client.send(requestAdd, HttpResponse.BodyHandlers.ofString());
            String epicSavedJson = response.body();
            epic1Saved = gson.fromJson(epicSavedJson, Epic.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении эпика");
        }

        int id = epic1Saved.getId();
        epic1.setId(id);

        URI urlGet = URI.create(HOST + "/tasks/epic/?id=" + id);
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();

        Epic epic1Received = null;

        try {
            HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
            String epicSavedJson = response.body();
            epic1Received = gson.fromJson(epicSavedJson, Epic.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении эпика");
        }

        assertNotNull(epic1Received);
        assertEquals(epic1, epic1Received, "Эпики не совпадают");
    }

    // получить подзадачу: GET /tasks/subtask/?id=
    @Test
    public void getSubtask() {
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();
        createSubtask1();

        String epic1Json = gson.toJson(epic1);
        URI urlAddTEpic = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic = HttpRequest.newBuilder()
                .uri(urlAddTEpic)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddSubtask = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask = HttpRequest.newBuilder()
                .uri(urlAddSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Subtask subtask1Saved = null;

        try {
            client.send(requestAddEpic, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> response = client.send(requestAddSubtask, HttpResponse.BodyHandlers.ofString());
            String subtaskSavedJson = response.body();
            subtask1Saved = gson.fromJson(subtaskSavedJson, Subtask.class);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении эпика и подзадачи");
        }

        int id = subtask1Saved.getId();
        subtask1.setId(id);

        URI urlGetSubtask = URI.create(HOST + "/tasks/subtask/?id=" + id);
        HttpRequest requestGetSubtask = HttpRequest.newBuilder()
                .uri(urlGetSubtask)
                .GET()
                .build();

        Subtask subtask1Received = null;

        try {
            HttpResponse<String> response = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());
            String subtaskSavedJson = response.body();
            subtask1Received = gson.fromJson(subtaskSavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении подзадачи");
        }

        assertNotNull(subtask1Received);
        assertEquals(subtask1, subtask1Received, "Подзадачи не совпадают");
    }

    // ПОЛУЧИТЬ ВСЕ

    // получить все задачи: GET /tasks/task/
    @Test
    public void getTasks() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createTask2();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String task2Json = gson.toJson(task2);
        URI urlAddTask2 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask2 = HttpRequest.newBuilder()
                .uri(urlAddTask2)
                .POST(HttpRequest.BodyPublishers.ofString(task2Json))
                .build();

        Task task1Saved = null;
        Task task2Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseTask2 = client.send(requestAddTask2, HttpResponse.BodyHandlers.ofString());
            String task2SavedJson = responseTask2.body();
            task2Saved = gson.fromJson(task2SavedJson, Task.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idTask2 = task2Saved.getId();
        task2.setId(idTask2);

        URI urlGetTasks = URI.create(HOST + "/tasks/task/");
        HttpRequest requestGetTasks = HttpRequest.newBuilder()
                .uri(urlGetTasks)
                .GET()
                .build();

        String tasksSavedJson = null;

        try {
            HttpResponse<String> response = client.send(requestGetTasks, HttpResponse.BodyHandlers.ofString());
            tasksSavedJson = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении задач");
        }

        assertNotNull(tasksSavedJson);

        JsonArray jsonArray = JsonParser.parseString(tasksSavedJson).getAsJsonArray();
        List<Task> tasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            Task task = gson.fromJson(jsonElement, Task.class);
            tasksReceived.add(task);
        }

        assertEquals(2, tasksReceived.size(), "Сохранилось неверное количество задач");
        assertEquals(task1, tasksReceived.get(0), "Задачи не совпадают");
        assertEquals(task2, tasksReceived.get(1), "Задачи не совпадают");
    }

    // получить все эпики: GET /tasks/epic/
    @Test
    public void getEpics() {
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();
        createEpic2();
        createSubtask1();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String epic2Json = gson.toJson(epic2);
        URI urlAddEpic2 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic2 = HttpRequest.newBuilder()
                .uri(urlAddEpic2)
                .POST(HttpRequest.BodyPublishers.ofString(epic2Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Epic epic1Saved = null;
        Epic epic2Saved = null;

        try {
            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseEpic2 = client.send(requestAddEpic2, HttpResponse.BodyHandlers.ofString());
            String epic2SavedJson = responseEpic2.body();
            epic2Saved = gson.fromJson(epic2SavedJson, Epic.class);

            client.send(requestAddSubtask1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении эпиков и подзадачи");
        }

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idEpic2 = epic2Saved.getId();
        epic2.setId(idEpic2);

        URI urlGetEpics = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestGetEpics = HttpRequest.newBuilder()
                .uri(urlGetEpics)
                .GET()
                .build();

        String epicsSavedJson = null;

        try {
            HttpResponse<String> response = client.send(requestGetEpics, HttpResponse.BodyHandlers.ofString());
            epicsSavedJson = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении эпиков");
        }

        assertNotNull(epicsSavedJson);

        JsonArray jsonArray = JsonParser.parseString(epicsSavedJson).getAsJsonArray();
        List<Epic> epicsReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            Epic epic = gson.fromJson(jsonElement, Epic.class);
            epicsReceived.add(epic);
        }

        assertEquals(2, epicsReceived.size(), "Сохранилось неверное количество эпиков");
        assertEquals(epic1, epicsReceived.get(0), "Эпики не совпадают");
        assertEquals(epic2, epicsReceived.get(1), "Эпики не совпадают");
    }

    // получить все подзадачи: GET /tasks/subtask/
    @Test
    public void getSubtasks() {
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();
        createEpic2();
        createSubtask1();
        createSubtask2();
        createSubtask3();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String epic2Json = gson.toJson(epic2);
        URI urlAddEpic2 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic2 = HttpRequest.newBuilder()
                .uri(urlAddEpic2)
                .POST(HttpRequest.BodyPublishers.ofString(epic2Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        String subtask2Json = gson.toJson(subtask2);
        URI urlAddESubtask2 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask2 = HttpRequest.newBuilder()
                .uri(urlAddESubtask2)
                .POST(HttpRequest.BodyPublishers.ofString(subtask2Json))
                .build();

        String subtask3Json = gson.toJson(subtask3);
        URI urlAddESubtask3 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask3 = HttpRequest.newBuilder()
                .uri(urlAddESubtask3)
                .POST(HttpRequest.BodyPublishers.ofString(subtask3Json))
                .build();

        Subtask subtask1Saved = null;
        Subtask subtask2Saved = null;
        Subtask subtask3Saved = null;

        try {
            client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            client.send(requestAddEpic2, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);

            HttpResponse<String> responseSubtask2 = client.send(requestAddSubtask2,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask2SavedJson = responseSubtask2.body();
            subtask2Saved = gson.fromJson(subtask2SavedJson, Subtask.class);

            HttpResponse<String> responseSubtask3 = client.send(requestAddSubtask3,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask3SavedJson = responseSubtask3.body();
            subtask3Saved = gson.fromJson(subtask3SavedJson, Subtask.class);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении эпиков и подзадач");
        }

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        int idSubtask2 = subtask2Saved.getId();
        subtask2.setId(idSubtask2);

        int idSubtask3 = subtask3Saved.getId();
        subtask3.setId(idSubtask3);

        URI urlGetSubtasks = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestGetSubtasks = HttpRequest.newBuilder()
                .uri(urlGetSubtasks)
                .GET()
                .build();

        String subtasksSavedJson = null;

        try {
            HttpResponse<String> response = client.send(requestGetSubtasks, HttpResponse.BodyHandlers.ofString());
            subtasksSavedJson = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении подзадач");
        }

        assertNotNull(subtasksSavedJson);

        JsonArray jsonArray = JsonParser.parseString(subtasksSavedJson).getAsJsonArray();
        List<Subtask> subtasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
            subtasksReceived.add(subtask);
        }

        assertEquals(3, subtasksReceived.size(), "Сохранилось неверное количество подзадач");
        assertEquals(subtask1, subtasksReceived.get(0), "Подзадачи не совпадают");
        assertEquals(subtask2, subtasksReceived.get(1), "Подзадачи не совпадают");
        assertEquals(subtask3, subtasksReceived.get(2), "Подзадачи не совпадают");
    }

    // получить все подзадачи эпика: GET /tasks/epic-subtasks/?id=
    @Test
    public void getSubtaskInEpic() {
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();
        createSubtask1();
        createSubtask2();

        String epic1Json = gson.toJson(epic1);
        URI urlAddTEpic = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic = HttpRequest.newBuilder()
                .uri(urlAddTEpic)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddSubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddSubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        String subtask2Json = gson.toJson(subtask2);
        URI urlAddSubtask2 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask2 = HttpRequest.newBuilder()
                .uri(urlAddSubtask2)
                .POST(HttpRequest.BodyPublishers.ofString(subtask2Json))
                .build();

        Epic epic1Saved = null;
        Subtask subtask1Saved = null;
        Subtask subtask2Saved = null;

        try {
            HttpResponse<String> responseEpic1 = client.send(requestAddEpic, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);

            HttpResponse<String> responseSubtask2 = client.send(requestAddSubtask2,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask2SavedJson = responseSubtask2.body();
            subtask2Saved = gson.fromJson(subtask2SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении эпика и подзадач");
        }

        int epic1id = epic1Saved.getId();
        subtask1.setId(subtask1Saved.getId());
        subtask2.setId(subtask2Saved.getId());

        URI urlGetSubtasksInEpic = URI.create(HOST + "/tasks/epic-subtasks/?id=" + epic1id);
        HttpRequest requestGetSubtasksInEpic = HttpRequest.newBuilder()
                .uri(urlGetSubtasksInEpic)
                .GET()
                .build();

        String subtasksSavedJson = null;

        try {
            HttpResponse<String> response = client.send(requestGetSubtasksInEpic, HttpResponse.BodyHandlers.ofString());
            subtasksSavedJson = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении подзадачи");
        }

        assertNotNull(subtasksSavedJson);

        JsonArray jsonArray = JsonParser.parseString(subtasksSavedJson).getAsJsonArray();
        List<Subtask> subtasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
            subtasksReceived.add(subtask);
        }

        assertEquals(subtask1, subtasksReceived.get(0), "Подзадачи не совпадают");
        assertEquals(subtask2, subtasksReceived.get(1), "Подзадачи не совпадают");
    }

    // получить все задачи всех типов: GET /tasks/
    @Test
    public void getAll() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Epic epic1Saved = null;
        Subtask subtask1Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                    HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задачи, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String allSavedJson = null;

        try {
            HttpResponse<String> response = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            allSavedJson = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка задач всех типов");
        }

        assertNotNull(allSavedJson);

        JsonArray jsonArray = JsonParser.parseString(allSavedJson).getAsJsonArray();
        List<Task> allTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                allTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                allTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                allTasksReceived.add(subtask);
            }
        }

        assertEquals(3, allTasksReceived.size(), "Получили неверное количество задач");

        assertEquals(task1, allTasksReceived.get(0), "Задачи не совпадают");
        assertEquals(epic1, allTasksReceived.get(1), "Эпики не совпадают");
        assertEquals(subtask1, allTasksReceived.get(2), "Подзадачи не совпадают");
    }

    // получить историю просмотров: GET /tasks/history/
    @Test
    public void getHistory() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Epic epic1Saved = null;
        Subtask subtask1Saved = null;

        // Отправляем в менеджер задачу, эпик и подзадачу.
        // При добавлении HttpTaskServer вызовет методы getTask(), getEpic и getSubtask() —
        // в результате задачи окажутся в истории по порядку: task1, epic1, subtask1.
        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                    HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задачи, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        // Еще раз получаем task1, чтобы поменялся порядок в истории.
        // Задача task1 переместится в конец: epic1, subtask1, task1.
        URI urlGetTask1 = URI.create(HOST + "/tasks/task/?id=" + idTask1);
        HttpRequest requestGetTask1 = HttpRequest.newBuilder()
                .uri(urlGetTask1)
                .GET()
                .build();
        try {
            client.send(requestGetTask1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении задачи");
        }

        // запрашиваем историю просмотров
        URI urlGetHistory = URI.create(HOST + "/tasks/history/");
        HttpRequest requestGetHistory = HttpRequest.newBuilder()
                .uri(urlGetHistory)
                .GET()
                .build();
        String historySavedJson = null;
        try {
            HttpResponse<String> responseHistory = client.send(requestGetHistory,
                                                               HttpResponse.BodyHandlers.ofString());
            historySavedJson = responseHistory.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении истории просмотров");
        }

        assertNotNull(historySavedJson);

        JsonArray jsonArray = JsonParser.parseString(historySavedJson).getAsJsonArray();
        List<Task> history = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                history.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                history.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                history.add(subtask);
            }
        }

        assertEquals(3, history.size(), "Получили неверное количество задач");

        assertEquals(epic1, history.get(0), "Эпики не совпадают");
        assertEquals(subtask1, history.get(1), "Подзадачи не совпадают");
        assertEquals(task1, history.get(2), "Задачи не совпадают");
    }

    // получить все задачи всех типов: GET /tasks/
    @Test
    public void getPrioritizedTasks() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();      // первая по приоритету; дата: 15.10.2022
        createTask2();      // здесь время начала и продолжительность обнулим
        createEpic1();
        createSubtask1();   // вторая по приоритету; дата: 16.10.2022

        // в шаблоне задачи task2 обнуляем время начала и продолжительность
        task2.setStartTime((LocalDateTime) null);
        task2.setDuration((Duration) null);

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String task2Json = gson.toJson(task2);
        URI urlAddTask2 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask2 = HttpRequest.newBuilder()
                .uri(urlAddTask2)
                .POST(HttpRequest.BodyPublishers.ofString(task2Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Task task2Saved = null;
        Subtask subtask1Saved = null;

        // Добавляем task1, task2, epic1 и subtask1.
        // В список приоритетов должны попасть три задачи
        // в следующем порядке: task1, subtask1, task2.

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseTask2 = client.send(requestAddTask2, HttpResponse.BodyHandlers.ofString());
            String task2SavedJson = responseTask2.body();
            task2Saved = gson.fromJson(task2SavedJson, Task.class);

            client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задач, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idTask2 = task2Saved.getId();
        task2.setId(idTask2);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        URI urlGetPrioritized = URI.create(HOST + "/tasks/priority/");
        HttpRequest requestGetPrioritized = HttpRequest.newBuilder()
                .uri(urlGetPrioritized)
                .GET()
                .build();

        String prioritizedTasksJson = null;

        try {
            HttpResponse<String> response = client.send(requestGetPrioritized, HttpResponse.BodyHandlers.ofString());
            prioritizedTasksJson = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка задач в порядке приоритета");
        }

        assertNotNull(prioritizedTasksJson);

        JsonArray jsonArray = JsonParser.parseString(prioritizedTasksJson).getAsJsonArray();
        List<Task> prioritizedTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                prioritizedTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) { // для проверки, не должно срабатывать
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                prioritizedTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                prioritizedTasksReceived.add(subtask);
            }
        }

        assertEquals(3, prioritizedTasksReceived.size(), "Получили неверное количество задач");

        assertEquals(task1, prioritizedTasksReceived.get(0), "Должна быть задача " + task1);
        assertEquals(subtask1, prioritizedTasksReceived.get(1), "Должна быть подзадача " + subtask1);
        assertEquals(task2, prioritizedTasksReceived.get(2), "Должна быть задача " + task2);
    }


    // ---------------------------------------------
    //  ТЕСТЫ — Запросы POST
    // ---------------------------------------------

    // ДОБАВИТЬ

    // добавить задачу: POST /tasks/task/
    @Test
    public void addTask() {
        URI url = URI.create(HOST + "/tasks/task/");
        HttpClient client = HttpClient.newHttpClient();

        createTask1();

        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCode = response.statusCode();
            assertEquals(STATUS_OK, receivedStatusCode, "Код не совпадает");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // добавить эпик: POST /tasks/epic/
    @Test
    public void addEpicWithOutSubtasks() {
        URI url = URI.create(HOST + "/tasks/epic/");
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();

        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCode = response.statusCode();
            assertEquals(STATUS_OK, receivedStatusCode, "Код не совпадает");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // добавить подзадачу: POST /tasks/subtask/
    @Test
    public void addSubtask() {
        URI urlEpic = URI.create(HOST + "/tasks/epic/");
        URI urlSubtask = URI.create(HOST + "/tasks/subtask/");
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();
        createSubtask1();

        String jsonEpic1 = gson.toJson(epic1);
        String jsonSubtask1 = gson.toJson(subtask1);

        HttpRequest requestEpic1 = HttpRequest.newBuilder()
                .uri(urlEpic)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic1))
                .build();

        HttpRequest requestSubtask1 = HttpRequest.newBuilder()
                .uri(urlSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask1))
                .build();

        try {
            HttpResponse<String> responseEpic1 = client.send(requestEpic1, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCodeEpic1 = responseEpic1.statusCode();
            assertEquals(STATUS_OK, receivedStatusCodeEpic1, "Код не совпадает");

            HttpResponse<String> responseSubtask1 = client.send(requestSubtask1, HttpResponse.BodyHandlers.ofString());
            int receivedStatusCodeSubtask1 = responseSubtask1.statusCode();
            assertEquals(STATUS_OK, receivedStatusCodeSubtask1, "Код не совпадает");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // ОБНОВИТЬ

    // обновить задачу:  POST /tasks/task/?id=
    @Test
    public void updateTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI urlAddTask = URI.create(HOST + "/tasks/task/");

        createTask1();

        String task1ToAdd = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(task1ToAdd);
        HttpRequest requestAdd = HttpRequest.newBuilder().uri(urlAddTask).POST(body).build();
        Task task1Saved = null;
        try {
            HttpResponse<String> response = client.send(requestAdd, HttpResponse.BodyHandlers.ofString());
            String taskSavedJson = response.body();
            task1Saved = gson.fromJson(taskSavedJson, Task.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задачи");
        }

        assertNotNull(task1Saved);

        int id = task1Saved.getId();
        task1Saved.setDescription("New description for Task1");
        URI urlUpdateTask = URI.create(HOST + "/tasks/task/?id=" + id);

        String task1ToUpdate = gson.toJson(task1Saved);
        final HttpRequest.BodyPublisher bodyUpdate = HttpRequest.BodyPublishers.ofString(task1ToUpdate);
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(urlUpdateTask).POST(bodyUpdate).build();
        Task task1Updated = null;

        try {
            HttpResponse<String> response = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
            String taskSavedJson = response.body();
            task1Updated = gson.fromJson(taskSavedJson, Task.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при обновлении задачи");
        }

        assertNotNull(task1Updated);
        assertEquals(task1Saved, task1Updated, "Задача обновлена неправильно");
    }

    // обновить эпик: POST /tasks/epic/?id=
    @Test
    public void updateEpic() {
        HttpClient client = HttpClient.newHttpClient();
        URI urlAddEpic = URI.create(HOST + "/tasks/epic/");

        createEpic1();

        String epic1ToAdd = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyAdd = HttpRequest.BodyPublishers.ofString(epic1ToAdd);
        HttpRequest requestAdd = HttpRequest.newBuilder().uri(urlAddEpic).POST(bodyAdd).build();
        Task epic1Saved = null;
        try {
            HttpResponse<String> response = client.send(requestAdd, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = response.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении эпика");
        }

        assertNotNull(epic1Saved);

        int id = epic1Saved.getId();
        epic1Saved.setDescription("New description for Epic1");
        URI urlUpdateEpic = URI.create(HOST + "/tasks/epic/?id=" + id);

        String epic1ToUpdate = gson.toJson(epic1Saved);
        final HttpRequest.BodyPublisher bodyUpdate = HttpRequest.BodyPublishers.ofString(epic1ToUpdate);
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(urlUpdateEpic).POST(bodyUpdate).build();
        Task epic1Updated = null;

        try {
            HttpResponse<String> response = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = response.body();
            epic1Updated = gson.fromJson(epic1SavedJson, Epic.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при обновлении эпика");
        }

        assertNotNull(epic1Updated);
        assertEquals(epic1Saved, epic1Updated, "Эпик обновлен неправильно");
    }

    // обновить подзадачу: POST /tasks/subtask/?id=
    @Test
    public void updateSubtask() {
        URI urlEpic = URI.create(HOST + "/tasks/epic/");
        URI urlSubtask = URI.create(HOST + "/tasks/subtask/");
        HttpClient client = HttpClient.newHttpClient();

        createEpic1();
        createSubtask1();

        String jsonEpic1 = gson.toJson(epic1);
        String jsonSubtask1 = gson.toJson(subtask1);

        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlEpic)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic1))
                .build();

        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask1))
                .build();

        Subtask subtask1Saved = null;

        try {
            client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = client.send(requestAddSubtask1, HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = response.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(subtask1Saved);
        int id = subtask1Saved.getId();
        subtask1Saved.setStatus(Status.DONE);

        URI urlUpdateSubtask1 = URI.create(HOST + "/tasks/subtask/?id=" + id);
        String subtask1UpdateJson = gson.toJson(subtask1Saved);
        HttpRequest requestUpdate = HttpRequest.newBuilder()
                .uri(urlUpdateSubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1UpdateJson))
                .build();

        Subtask subtask1Updated = null;

        try {
            HttpResponse<String> response = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
            String subtaskSavedJson = response.body();
            subtask1Updated = gson.fromJson(subtaskSavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при обновлении задачи");
        }

        assertNotNull(subtask1Updated);
        assertEquals(subtask1Saved, subtask1Updated, "Задача обновлена неправильно");
    }



    // ---------------------------------------------
    //  ТЕСТЫ — Запросы DELETE
    // ---------------------------------------------

    // УДАЛИТЬ ПО ID

    // удалить задачу: DELETE /tasks/task/?id=
    @Test
    public void deleteTask() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Epic epic1Saved = null;
        Subtask subtask1Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                    HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задач, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        URI urlDeleteTask1 = URI.create(HOST + "/tasks/task/?id=" + idTask1);
        HttpRequest requestDeleteTask1 = HttpRequest.newBuilder()
                .uri(urlDeleteTask1)
                .DELETE()
                .build();

        try {
            client.send(requestDeleteTask1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при удалении задачи");
        }

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String remainingTasksJson = null;

        try {
            HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            remainingTasksJson = responseGetAll.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка оставшихся задач");
        }

        assertNotNull(remainingTasksJson);

        JsonArray jsonArray = JsonParser.parseString(remainingTasksJson).getAsJsonArray();
        List<Task> remainingTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                remainingTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                remainingTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                remainingTasksReceived.add(subtask);
            }
        }

        assertEquals(2, remainingTasksReceived.size(), "Получили неверное количество задач");

        assertEquals(epic1, remainingTasksReceived.get(0), "Должен быть эпик " + epic1);
        assertEquals(subtask1, remainingTasksReceived.get(1), "Должна быть подзадача " + subtask1);
    }

    // удалить эпик: DELETE /tasks/epic/?id=
    @Test
    public void deleteEpic() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Epic epic1Saved = null;
        Subtask subtask1Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задач, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        URI urlDeleteEpic1 = URI.create(HOST + "/tasks/epic/?id=" + idEpic1);
        HttpRequest requestDeleteEpic1 = HttpRequest.newBuilder()
                .uri(urlDeleteEpic1)
                .DELETE()
                .build();

        try {
            client.send(requestDeleteEpic1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при удалении эпика");
        }

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String remainingTasksJson = null;

        try {
            HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            remainingTasksJson = responseGetAll.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка оставшихся задач");
        }

        assertNotNull(remainingTasksJson);

        JsonArray jsonArray = JsonParser.parseString(remainingTasksJson).getAsJsonArray();
        List<Task> remainingTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                remainingTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                remainingTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                remainingTasksReceived.add(subtask);
            }
        }

        assertEquals(1, remainingTasksReceived.size(), "Получили неверное количество задач");
        assertEquals(task1, remainingTasksReceived.get(0), "Должна быть задача " + task1);
    }

    // удалить подзадачу: DELETE /tasks/subtask/?id=
    @Test
    public void deleteSubtask() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Epic epic1Saved = null;
        Subtask subtask1Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                                                                HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задач, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        URI urlDeleteSubtask1 = URI.create(HOST + "/tasks/subtask/?id=" + idSubtask1);
        HttpRequest requestDeleteSubtask1 = HttpRequest.newBuilder()
                .uri(urlDeleteSubtask1)
                .DELETE()
                .build();

        try {
            client.send(requestDeleteSubtask1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при удалении подзадачи");
        }

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String remainingTasksJson = null;

        try {
            HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            remainingTasksJson = responseGetAll.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка оставшихся задач");
        }

        assertNotNull(remainingTasksJson);

        JsonArray jsonArray = JsonParser.parseString(remainingTasksJson).getAsJsonArray();
        List<Task> remainingTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                remainingTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                remainingTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                remainingTasksReceived.add(subtask);
            }
        }

        assertEquals(2, remainingTasksReceived.size(), "Получили неверное количество задач");

        assertEquals(task1, remainingTasksReceived.get(0), "Должен быть задача " + task1);
        assertEquals(epic1, remainingTasksReceived.get(1), "Должен быть эпик " + epic1);
    }

    // УДАЛИТЬ ВСЕ

    // удалить все задачи: DELETE /tasks/task/
    @Test
    public void deleteTasks() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createTask2();
        createEpic1();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String task2Json = gson.toJson(task2);
        URI urlAddTask2 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask2 = HttpRequest.newBuilder()
                .uri(urlAddTask2)
                .POST(HttpRequest.BodyPublishers.ofString(task2Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;
        Task task2Saved = null;
        Epic epic1Saved = null;
        Subtask subtask1Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseTask2 = client.send(requestAddTask2, HttpResponse.BodyHandlers.ofString());
            String task2SavedJson = responseTask2.body();
            task2Saved = gson.fromJson(task2SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseSubtask1 = client.send(requestAddSubtask1,
                    HttpResponse.BodyHandlers.ofString());
            String subtask1SavedJson = responseSubtask1.body();
            subtask1Saved = gson.fromJson(subtask1SavedJson, Subtask.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задач, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idTask2 = task2Saved.getId();
        task2.setId(idTask2);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idSubtask1 = subtask1Saved.getId();
        subtask1.setId(idSubtask1);

        URI urlDeleteTasks = URI.create(HOST + "/tasks/task/");
        HttpRequest requestDeleteTasks = HttpRequest.newBuilder()
                .uri(urlDeleteTasks)
                .DELETE()
                .build();

        try {
            client.send(requestDeleteTasks, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при удалении задач");
        }

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String remainingTasksJson = null;

        try {
            HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            remainingTasksJson = responseGetAll.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка оставшихся задач");
        }

        assertNotNull(remainingTasksJson);

        JsonArray jsonArray = JsonParser.parseString(remainingTasksJson).getAsJsonArray();
        List<Task> remainingTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                remainingTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                remainingTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                remainingTasksReceived.add(subtask);
            }
        }

        assertEquals(2, remainingTasksReceived.size(), "Получили неверное количество задач");

        assertEquals(epic1, remainingTasksReceived.get(0), "Должен быть эпик " + epic1);
        assertEquals(subtask1, remainingTasksReceived.get(1), "Должна быть подзадача " + subtask1);
    }


    // удалить все эпики: DELETE /tasks/epic/
    @Test
    public void deleteEpics() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createEpic2();
        createSubtask1();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String epic2Json = gson.toJson(epic2);
        URI urlAddEpic2 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic2 = HttpRequest.newBuilder()
                .uri(urlAddEpic2)
                .POST(HttpRequest.BodyPublishers.ofString(epic2Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        Task task1Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            client.send(requestAddEpic2, HttpResponse.BodyHandlers.ofString());
            client.send(requestAddSubtask1,HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задач, эпика и подзадачи");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        URI urlDeleteEpics = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestDeleteTasks = HttpRequest.newBuilder()
                .uri(urlDeleteEpics)
                .DELETE()
                .build();

        try {
            client.send(requestDeleteTasks, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при удалении эпиков");
        }

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String remainingTasksJson = null;

        try {
            HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            remainingTasksJson = responseGetAll.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка оставшихся задач");
        }

        assertNotNull(remainingTasksJson);

        JsonArray jsonArray = JsonParser.parseString(remainingTasksJson).getAsJsonArray();
        List<Task> remainingTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                remainingTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                remainingTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                remainingTasksReceived.add(subtask);
            }
        }

        assertEquals(1, remainingTasksReceived.size(), "Получили неверное количество задач");
        assertEquals(task1, remainingTasksReceived.get(0), "Должна быть задача " + task1);
    }


    // удалить все подзадачи: DELETE /tasks/subtask/
    @Test
    public void deleteSubtasks() {
        HttpClient client = HttpClient.newHttpClient();

        createTask1();
        createEpic1();
        createEpic2();
        createSubtask1();
        createSubtask2();
        createSubtask3();

        String task1Json = gson.toJson(task1);
        URI urlAddTask1 = URI.create(HOST + "/tasks/task/");
        HttpRequest requestAddTask1 = HttpRequest.newBuilder()
                .uri(urlAddTask1)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        String epic1Json = gson.toJson(epic1);
        URI urlAddEpic1 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic1 = HttpRequest.newBuilder()
                .uri(urlAddEpic1)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        String epic2Json = gson.toJson(epic2);
        URI urlAddEpic2 = URI.create(HOST + "/tasks/epic/");
        HttpRequest requestAddEpic2 = HttpRequest.newBuilder()
                .uri(urlAddEpic2)
                .POST(HttpRequest.BodyPublishers.ofString(epic2Json))
                .build();

        String subtask1Json = gson.toJson(subtask1);
        URI urlAddESubtask1 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask1 = HttpRequest.newBuilder()
                .uri(urlAddESubtask1)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        String subtask2Json = gson.toJson(subtask2);
        URI urlAddESubtask2 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask2 = HttpRequest.newBuilder()
                .uri(urlAddESubtask2)
                .POST(HttpRequest.BodyPublishers.ofString(subtask2Json))
                .build();

        String subtask3Json = gson.toJson(subtask3);
        URI urlAddESubtask3 = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestAddSubtask3 = HttpRequest.newBuilder()
                .uri(urlAddESubtask3)
                .POST(HttpRequest.BodyPublishers.ofString(subtask3Json))
                .build();

        Task task1Saved = null;
        Epic epic1Saved = null;
        Epic epic2Saved = null;

        try {
            HttpResponse<String> responseTask1 = client.send(requestAddTask1, HttpResponse.BodyHandlers.ofString());
            String task1SavedJson = responseTask1.body();
            task1Saved = gson.fromJson(task1SavedJson, Task.class);

            HttpResponse<String> responseEpic1 = client.send(requestAddEpic1, HttpResponse.BodyHandlers.ofString());
            String epic1SavedJson = responseEpic1.body();
            epic1Saved = gson.fromJson(epic1SavedJson, Epic.class);

            HttpResponse<String> responseEpic2 = client.send(requestAddEpic2, HttpResponse.BodyHandlers.ofString());
            String epic2SavedJson = responseEpic2.body();
            epic2Saved = gson.fromJson(epic2SavedJson, Epic.class);

            client.send(requestAddSubtask1, HttpResponse.BodyHandlers.ofString());
            client.send(requestAddSubtask2, HttpResponse.BodyHandlers.ofString());
            client.send(requestAddSubtask3, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при добавлении задачи, эпиков и подзадач");
        }

        int idTask1 = task1Saved.getId();
        task1.setId(idTask1);

        int idEpic1 = epic1Saved.getId();
        epic1.setId(idEpic1);

        int idEpic2 = epic2Saved.getId();
        epic2.setId(idEpic2);

        URI urlDeleteSubtasks = URI.create(HOST + "/tasks/subtask/");
        HttpRequest requestDeleteSubtasks = HttpRequest.newBuilder()
                .uri(urlDeleteSubtasks)
                .DELETE()
                .build();

        try {
            client.send(requestDeleteSubtasks, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при удалении подзадач");
        }

        URI urlGetAll = URI.create(HOST + "/tasks/");
        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(urlGetAll)
                .GET()
                .build();

        String remainingTasksJson = null;

        try {
            HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
            remainingTasksJson = responseGetAll.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении списка оставшихся задач");
        }

        assertNotNull(remainingTasksJson);

        JsonArray jsonArray = JsonParser.parseString(remainingTasksJson).getAsJsonArray();
        List<Task> remainingTasksReceived = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            if ("TASK".equals(type)) {
                Task task = gson.fromJson(jsonObject, Task.class);
                remainingTasksReceived.add(task);
            }
            if ("EPIC".equals(type)) {
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                remainingTasksReceived.add(epic);
            }
            if ("SUBTASK".equals(type)) {
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                remainingTasksReceived.add(subtask);
            }
        }

        assertEquals(3, remainingTasksReceived.size(), "Получили неверное количество задач");

        assertEquals(task1, remainingTasksReceived.get(0), "Должна быть задача " + task1);
        assertEquals(epic1, remainingTasksReceived.get(1), "Должен быть эпик " + epic1);
        assertEquals(epic2, remainingTasksReceived.get(2), "Должен быть эпик " + epic1);
    }


    // ---------------------------------------------
    //  ШАБЛОНЫ ЗАДАЧ
    // ---------------------------------------------

    private void createTask1() {
        task1 = new Task("Task1",
                "Description task 1",
                "NEW",
                "15.10.2022, 14:00",
                "01:00");
    }

    private void createTask2() {
        task2 = new Task("Task2",
                "Description task 2",
                "NEW",
                "15.11.2022, 14:00",
                "01:00");
    }

    private void createEpic1() {
        epic1 = new Epic("Epic1", "Description Epic1");
    }

    private void createEpic2() {
        epic2 = new Epic("Epic2", "Description Epic2");
    }

    private void createSubtask1() {
        subtask1 = new Subtask("Epic1 Subtask1",
                "Description Epic1 Subtask1",
                "NEW",
                "Epic1",
                "16.10.2022, 12:00",
                "00:30"
        );
    }

    private void createSubtask2() {
        subtask2 = new Subtask("Epic1 Subtask2",
                "Description Epic1 Subtask2",
                "NEW",
                "Epic1",
                "17.10.2022, 12:00",
                "00:30"
        );
    }

    private void createSubtask3() {
        subtask3 = new Subtask("Epic2 Subtask3",
                "Description Epic2 Subtask3",
                "NEW",
                "Epic2",
                "18.10.2022, 14:00",
                "00:30"
        );
    }
}