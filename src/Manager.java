import entries.Epic;
import entries.Subtask;
import entries.Task;

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

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

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
