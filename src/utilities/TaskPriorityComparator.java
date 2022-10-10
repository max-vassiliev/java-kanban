package utilities;

import tasks.Task;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        if (task1.equals(task2)) {
            return 0;
        } else if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return +1;
        } else if (task1.getStartTime() == null) {
            return 1;
        } else if (task2.getStartTime() == null) {
            return -1;
        }
        return task1.getStartTime().compareTo(task2.getStartTime());
    }
}
