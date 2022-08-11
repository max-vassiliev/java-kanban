package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY = 10;

    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyReversed = new ArrayList<>(history);
        Collections.reverse(historyReversed);
        return historyReversed;
    }

}
