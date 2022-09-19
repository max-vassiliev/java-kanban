package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Integer nextId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();


    // ДОБАВИТЬ

    // добавить задачу
    @Override
    public int add(Task task) {
        int id = nextId++;

        task.setId(id);
        task.setType(TaskType.TASK);
        tasks.put(task.getId(), task);
        return id;
    }

    // добавить эпик
    @Override
    public int add(Epic epic) {
        int id = nextId++;

        epic.setId(id);
        epic.setType(TaskType.EPIC);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
        return id;
    }

    // добавить подзадачу
    @Override
    public int add(Subtask subtask) {
        int id = nextId++;

        subtask.setId(id);
        subtask.setType(TaskType.SUBTASK);
        setEpicSubtaskRelation(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
        return id;
    }

    // ОБНОВИТЬ

    // обновить задачу
    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    // обновить эпик
    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // обновить подзадачу
    @Override
    public void update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
    }

    // ПОЛУЧИТЬ ЗАДАЧУ

    // получить задачу
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    // получить эпик
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    // получить подзадачу
    @Override
    public Subtask getSubtask(int subtaskId) {
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
    public List<Subtask> getSubtasksInEpic(int id) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        Epic epic = this.getEpic(id);

        for (Integer subtaskId : epic.getRelatedSubtasks()) {
            Subtask subtask = getSubtask(subtaskId);
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    // УДАЛИТЬ

    // удалить задачу
    @Override
    public void delete(Task task) {
        historyManager.remove(task.getId());
        tasks.remove(task.getId());
    }

    // удалить эпик
    @Override
    public void delete(Epic epic) {
        for (Integer subtaskId : epic.getRelatedSubtasks()){
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
        historyManager.remove(epic.getId());
        epics.remove(epic.getId());
    }

    // удалить подзадачу
    @Override
    public void delete(Subtask subtask) {
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        relatedEpic.removeRelatedSubtask(subtask.getId());
        checkEpicStatus(relatedEpic);
        historyManager.remove(subtask.getId());
        subtasks.remove(subtask.getId());
    }


    // УДАЛИТЬ ВСЕ ЗАДАЧИ ОДНОГО ТИПА

    // удалить все задачи
    @Override
    public void deleteAllTasks(){
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    // удалить все эпики
    @Override
    public void deleteAllEpics() {
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    // удалить все подзадачи
    @Override
    public void deleteAllSubtasks() {
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    // ПОКАЗАТЬ ИСТОРИЮ ПРОСМОТРОВ

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    // проверить счетчик ID при восстановлении задач из резервной копии
    protected void checkNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    // проверить статус эпика
    protected void checkEpicStatus(Epic epic) {
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
        if (subtask.getRelatedEpicId() != 0) {
            Epic epic = epics.get(subtask.getRelatedEpicId());
            epic.addRelatedSubtask(subtask.getId());
            return;
        }
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