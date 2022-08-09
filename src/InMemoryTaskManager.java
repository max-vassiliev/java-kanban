import entries.Epic;
import entries.Subtask;
import entries.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager {
    private Integer nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // ДОБАВИТЬ

    // добавить задачу
    public int addTask(Task task) {
        int newId = nextId++;
        task.setId(newId);
        tasks.put(task.getId(), task);
        return newId;
    }

    // добавить эпик
    public int addEpic(Epic epic) {
        int newId = nextId++;
        epic.setId(newId);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
        return newId;
    }

    // добавить подзадачу
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
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // обновить эпик
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // обновить подзадачу
    public void updateSubtask (Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
    }

    // ПОЛУЧИТЬ

    // получить задачу
    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    // получить эпик
    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    // получить подзадачу
    public Subtask getSubtask (int subtaskId) {
        return subtasks.get(subtaskId);
    }

    // получить список всех задач
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // получить список всех эпиков
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // получить список всех подзадач
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получить список всех подзадач одного эпика
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
    public void deleteTask(Task task) {
        tasks.remove(task.getId());
    }

    // удалить эпик
    public void deleteEpic(Epic epic) {
        for (Integer subtaskId : epic.getRelatedSubtasks()){
            subtasks.remove(subtaskId);
        }
        epics.remove(epic.getId());
    }

    // удалить подзадачу
    public void deleteSubtask(Subtask subtask) {
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        relatedEpic.removeRelatedSubtask(subtask.getId());
        checkEpicStatus(relatedEpic);
        subtasks.remove(subtask.getId());
    }

    // удалить все задачи
    public void deleteAllTasks(){
        tasks.clear();
    }

    // удалить все эпики
    public void deleteAllEpics() {
        epics.clear();
    }

    // удалить все подзадачи
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    // проверить статус эпика
    private void checkEpicStatus(Epic epic) {
        int done = 0;
        int inProgress = 0;

        if (epic.getRelatedSubtasks().isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        for (Integer subtaskId : epic.getRelatedSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus().equals("DONE")) {
                done += 1;
            } else if (subtask.getStatus().equals("IN_PROGRESS")) {
                inProgress += 1;
            }
        }

        if (done == epic.getRelatedSubtasks().size()) {
            epic.setStatus("DONE");
        } else if (inProgress > 0 || (done > 0 && done < epic.getRelatedSubtasks().size())) {
            epic.setStatus("IN_PROGRESS");
        } else {
            epic.setStatus("NEW");
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