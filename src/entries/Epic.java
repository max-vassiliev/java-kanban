package entries;

import entries.Task;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> relatedSubtasks;

    public Epic(int id, String title, String description, String status) {
        super(id, title, description, status);
        relatedSubtasks = new ArrayList<>();
    }

    // добавить ID связанной подзадачи
    public void addRelatedSubtask(int subtaskId) {
        this.relatedSubtasks.add(subtaskId);
    }

    // удалить ID подзадачи из списка связанных подзадач
    public void removeRelatedSubtask(int subtaskId) {
        for (Integer id : relatedSubtasks) {
            if (id == subtaskId) {
                relatedSubtasks.remove(id);
            }
        }
    }

    public ArrayList<Integer> getRelatedSubtasks() {
        return relatedSubtasks;
    }

    public void setRelatedSubtasks(ArrayList<Integer> relatedSubtasks) {
        this.relatedSubtasks = relatedSubtasks;
    }

    @Override
    public String toString() {
        return "entries.Epic{" +
                "relatedSubtasks=" + relatedSubtasks +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
