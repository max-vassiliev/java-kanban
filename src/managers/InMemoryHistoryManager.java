package managers;

import tasks.Task;
import utilities.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY = 10;
    private final LinkedList<Task> history = new LinkedList<>();
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    // добавить задачу в историю просмотров
    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY) {
            history.removeFirst();
            history.add(task);
        } else {
            history.add(task);
        }
    }

    // показать историю просмотров
    @Override
    public List<Task> getHistory() {
        return history;
    }

}
