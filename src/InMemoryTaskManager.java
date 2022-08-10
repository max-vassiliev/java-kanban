import entries.Epic;
import entries.Status;
import entries.Subtask;
import entries.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private Integer nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // ДОБАВИТЬ

    // добавить задачу
    @Override
    public int addTask(Task task) {
        int newId = nextId++;
        Status status = convertStatus(task.getStatusRaw());

        task.setId(newId);
        task.setStatus(status);
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
        Status status = convertStatus(subtask.getStatusRaw());

        subtask.setId(newId);
        subtask.setStatus(status);
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
        Status status = convertStatus(task.getStatusRaw());
        task.setStatus(status);
        tasks.put(task.getId(), task);
    }

    // обновить эпик
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // обновить подзадачу
    @Override
    public void updateSubtask (Subtask subtask) {
        Status status = convertStatus(subtask.getStatusRaw());
        subtask.setStatus(status);
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
    }

    // ПОЛУЧИТЬ ЗАДАЧУ

    // получить задачу
    @Override
    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    // получить эпик
    @Override
    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    // получить подзадачу
    @Override
    public Subtask getSubtask (int subtaskId) {
        return subtasks.get(subtaskId);
    }

    // ПОЛУЧИТЬ СПИСОК ЗАДАЧ

    // получить список всех задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // получить список всех эпиков
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // получить список всех подзадач
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получить список всех подзадач одного эпика
    @Override
    public ArrayList<Subtask> getSubtasksInEpic(int epicId) {
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

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    // перевести статус задачи в enum
    private Status convertStatus(String status) {
        return Status.valueOf(status);
    }

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