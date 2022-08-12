package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY = 10;

    private final LinkedList<Task> history = new LinkedList<>();

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
        List<Task> historyReversed = new ArrayList<>(history);
        Collections.reverse(historyReversed);
        return historyReversed;
    }

}
