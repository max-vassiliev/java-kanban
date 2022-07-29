import entries.Epic;
import entries.Subtask;
import entries.Task;

import java.util.HashMap;

public class Manager {
    private Integer nextId = 1;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    

}
