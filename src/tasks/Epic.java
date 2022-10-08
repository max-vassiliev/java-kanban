package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> relatedSubtasks;
    protected LocalDateTime endTime;            // TODO новое поле


    public Epic(String title, String description) {
        super(title, description);
        relatedSubtasks = new ArrayList<>();
    }

    // TODO добавить расчет duration и getEndTime




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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", relatedSubtasks=" + relatedSubtasks +
                ", duration=" + duration + '\'' +
                ", startTime=" + startTime + '\'' +
                ", endTime=" + endTime + '\'' +
                '}';
    }
}
