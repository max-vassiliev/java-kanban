package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Integer nextId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    // ДОБАВИТЬ

    // добавить задачу
    @Override
    public int addTask(Task task) {
        int newId = nextId++;
        task.setId(newId);
        tasks.put(task.getId(), task);
        return newId;
    }

    // добавить эпик
    @Override
    public int addEpic(Epic epic) {
        int newId = nextId++;
        epic.setId(newId);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
        return newId;
    }

    // добавить подзадачу
    @Override
    public int addSubtask(Subtask subtask) {
        int newId = nextId++;
        subtask.setId(newId);
        setEpicSubtaskRelation(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
        return newId;
    }

    // ОБНОВИТЬ

    // обновить задачу
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // обновить эпик
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // обновить подзадачу
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
    }

    // ПОЛУЧИТЬ ЗАДАЧУ

    // получить задачу
    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    // получить эпик
    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    // получить подзадачу
    @Override
    public Subtask getSubtask (int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    // ПОЛУЧИТЬ СПИСОК ЗАДАЧ

    // получить список всех задач
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // получить список всех эпиков
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // получить список всех подзадач
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получить список всех подзадач одного эпика
    @Override
    public List<Subtask> getSubtasksInEpic(int epicId) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        Epic epic = getEpic(epicId);

        for (Integer subtaskId : epic.getRelatedSubtasks()) {
            Subtask subtask = getSubtask(subtaskId);
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    // УДАЛИТЬ

    // удалить задачу
    @Override
    public void deleteTask(Task task) {
        tasks.remove(task.getId());
    }

    // удалить эпик
    @Override
    public void deleteEpic(Epic epic) {
        for (Integer subtaskId : epic.getRelatedSubtasks()){
            subtasks.remove(subtaskId);
        }
        epics.remove(epic.getId());
    }

    // удалить подзадачу
    @Override
    public void deleteSubtask(Subtask subtask) {
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        relatedEpic.removeRelatedSubtask(subtask.getId());
        checkEpicStatus(relatedEpic);
        subtasks.remove(subtask.getId());
    }

    // УДАЛИТЬ ВСЕ ЗАДАЧИ ОДНОГО ТИПА

    // удалить все задачи
    @Override
    public void deleteAllTasks(){
        tasks.clear();
    }

    // удалить все эпики
    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    // удалить все подзадачи
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // ПОКАЗАТЬ ИСТОРИЮ ПРОСМОТРОВ

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    // проверить статус эпика
    private void checkEpicStatus(Epic epic) {
        int statusDone = 0;
        int statusNew = 0;

        if (epic.getRelatedSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer subtaskId : epic.getRelatedSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (Status.NEW.equals(subtask.getStatus())) {
                statusNew += 1;
            } else if (Status.DONE.equals(subtask.getStatus())) {
                statusDone += 1;
            }
        }

        if (statusNew == epic.getRelatedSubtasks().size()) {
            epic.setStatus(Status.NEW);
        } else if (statusDone == epic.getRelatedSubtasks().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    // связать подзадачу с эпиком
    private void setEpicSubtaskRelation(Subtask subtask) {
        for (Integer epicId : epics.keySet()) {
            Epic epic = epics.get(epicId);
            if (epic.getTitle().equals(subtask.getRelatedEpicTitle())) {
                subtask.setRelatedEpicId(epicId);
                if (!epic.getRelatedSubtasks().contains(subtask.getId())) {
                    epic.addRelatedSubtask(subtask.getId());
                }
            }
        }
    }
}