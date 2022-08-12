package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> relatedSubtasks;

    public Epic(String title, String description) {
        super(title, description);
        relatedSubtasks = new ArrayList<>();
    }

    // добавить ID связанной подзадачи
    public void addRelatedSubtask(int subtaskId) {
        this.relatedSubtasks.add(subtaskId);
    }

    // удалить ID подзадачи из списка связанных подзадач
    public void removeRelatedSubtask(Integer subtaskId) {
        relatedSubtasks.remove(subtaskId);
    }

    public ArrayList<Integer> getRelatedSubtasks() {
        return relatedSubtasks;
    }

    public void setRelatedSubtasks(ArrayList<Integer> relatedSubtasks) {
        this.relatedSubtasks = relatedSubtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "relatedSubtasks=" + relatedSubtasks +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
