package entries;

import entries.Task;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(int id, String title, String description, String status) {
        super(id, title, description, status);
    }
}
