package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // добавить задачу

    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    // обновить задачу

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask (Subtask subtask);

    // получить задачу

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    Subtask getSubtask (int subtaskId);

    // получить список задач

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Subtask> getSubtasksInEpic(int epicId);

    // удалить задачу

    void deleteTask(Task task);

    void deleteEpic(Epic epic);

    void deleteSubtask(Subtask subtask);

    // удалить все задачи одного типа

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // показать историю просмотров
    List<Task> getHistory();
}
