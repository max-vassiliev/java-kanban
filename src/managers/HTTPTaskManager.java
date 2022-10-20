package managers;

import com.google.gson.*;
import http.HttpTaskServer;
import http.KVTaskClient;

import java.util.ArrayList;
import java.util.List;

import tasks.*;

public class HTTPTaskManager extends FileBackedTaskManager {

    private KVTaskClient taskClient;

    private final HttpTaskServer httpTaskServer;

    Gson gson = new Gson();

    public HTTPTaskManager(String uri) {
        super(uri);
        httpTaskServer = new HttpTaskServer(this);
    }

    public static void main(String[] args) {
        new HTTPTaskManager("http://localhost:8078/");
    }

    public void stopHttpTaskServer() {
        httpTaskServer.stop();
    }

    @Override
    public void load(String serverUri) {
        taskClient = new KVTaskClient(serverUri);
        Gson gson1 = new Gson();

        // получить данные с сервера
        String tasksJson = taskClient.load("tasks");
        String epicsJson = taskClient.load("epics");
        String subtasksJson = taskClient.load("subtasks");
        String historyJson = taskClient.load("history");

        // обработать данные и загрузить в менеджер
        loadTasksFromJson(tasksJson, TaskType.TASK, gson1);
        loadTasksFromJson(epicsJson, TaskType.EPIC, gson1);
        loadTasksFromJson(subtasksJson, TaskType.SUBTASK, gson1);
        loadHistoryFromJson(historyJson);
    }

    // получить задачи из JSON
    private void loadTasksFromJson(String data, TaskType type, Gson gson1) {
        if (data == null || data.isEmpty()) return;
        JsonArray jsonArray = JsonParser.parseString(data).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject()) continue;
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            int id = jsonObject.get("id").getAsInt();
            checkNextId(id);

            switch (type) {
                case TASK:
                    Task task = gson1.fromJson(jsonObject, Task.class);
                    tasks.put(id, task);
                    prioritizedTasks.add(task);
                    break;
                case EPIC:
                    Epic epic = gson1.fromJson(jsonObject, Epic.class);
                    epics.put(id, epic);
                    prioritizedTasks.add(epic);
                    break;
                case SUBTASK:
                    Subtask subtask = gson1.fromJson(jsonObject, Subtask.class);
                    subtasks.put(id, subtask);
                    break;
            }
        }
    }

    // восстановить историю просмотров
    private void loadHistoryFromJson(String data) {
        if (data == null || data.isEmpty()) return;
        JsonArray jsonArray = JsonParser.parseString(data).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject()) continue;
            int id = jsonElement.getAsInt();

            if (tasks.containsKey(id)) {
                getTask(id);
                continue;
            }
            if (epics.containsKey(id)) {
                getEpic(id);
                continue;
            }
            if (subtasks.containsKey(id)) {
                getSubtask(id);
            }
        }
    }

    // сохранить состояние менеджера на сервер
    @Override
    public void save() {
        if (!tasks.isEmpty()) {
            String tasksJson = gson.toJson(getTasks());
            taskClient.put("tasks", tasksJson);
        }
        if (!epics.isEmpty()) {
            String epicsJson = gson.toJson(getEpics());
            taskClient.put("epics", epicsJson);
        }
        if (!subtasks.isEmpty()) {
            String subtasksJson = gson.toJson(getSubtasks());
            taskClient.put("subtasks", subtasksJson);
        }
        if (!getHistory().isEmpty()) {
            String historyJson = getHistoryJson();
            taskClient.put("history", historyJson);
        }
    }

    // ---------------------------------------------
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ---------------------------------------------


    // преобразовать список истории в элемент JSON со списком ID
    private String getHistoryJson() {
        List<Task> history = getHistory();
        if (history.isEmpty()) return null;

        List<Integer> historyIds = new ArrayList<>();
        for (Task task : history) {
            historyIds.add(task.getId());
        }
        return gson.toJson(historyIds);
    }
}
