package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    // добавить задачу

    int add(Task task);

    int add(Epic epic);

    int add(Subtask subtask);

    // обновить задачу

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    // получить задачу

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    // получить список задач

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Subtask> getSubtasksInEpic(int epicId);

    List<Task> getAll();

    // удалить задачу

    void delete(Task task);

    void delete(Epic epic);

    void delete(Subtask subtask);

    // удалить все задачи одного типа

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // показать историю просмотров
    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
