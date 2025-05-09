package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    // добавить задачу в историю просмотров
    void add(Task task);

    // удалить задачу из истории просмотров
    void remove(int id);

    // показать историю просмотров
    List<Task> getHistory();
}
