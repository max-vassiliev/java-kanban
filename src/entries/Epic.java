package entries;

import entries.Task;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> relatedSubtasks;

    public Epic(int id, String title, String description, String status) {
        super(id, title, description, status);
        relatedSubtasks = new ArrayList<>();
    }

    public void addRelatedSubtask(int subtaskId) {
        this.relatedSubtasks.add(subtaskId);
    }

    public void removeRelatedSubtask(int subtaskId) {
        for (Integer id : relatedSubtasks) {
            if (id == subtaskId) {
                relatedSubtasks.remove(id);
            }
        }
    }
    

}
