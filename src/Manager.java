import entries.Epic;
import entries.Subtask;
import entries.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private Integer nextId = 1;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // ДОБАВИТЬ

    // добавить задачу
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    // добавить эпик
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
    }

    // добавить подзадачу
    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        setEpicSubtaskRelation(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getRelatedEpicId());
        checkEpicStatus(relatedEpic);
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
        ArrayList<Task> tasksList= new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            Task task = tasks.get(id);
            tasksList.add(task);
        }
        return tasksList;
    }

    // получить список всех эпиков
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            Epic epic = epics.get(id);
            epicsList.add(epic);
        }
        return epicsList;
    }

    // получить список всех подзадач
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            Subtask subtask = subtasks.get(id);
            subtasksList.add(subtask);
        }
        return subtasksList;
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
