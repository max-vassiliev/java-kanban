package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> relatedSubtasks;

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

    public List<Integer> getRelatedSubtasks() {
        return relatedSubtasks;
    }

    public void setRelatedSubtasks(ArrayList<Integer> relatedSubtasks) {
        this.relatedSubtasks = relatedSubtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", relatedSubtasks=" + relatedSubtasks +
                '}';
    }
}
