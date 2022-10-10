package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    protected List<Integer> relatedSubtasks;
    protected LocalDateTime endTime;

    protected int startTimeSubtask;         //TODO проверить поле
    protected int endTimeSubtask;           //TODO проверить поле

    public Epic(String title, String description) {
        super(title, description);
        relatedSubtasks = new ArrayList<>();
    }

    // TODO проверить
    // конструктор для считывания из файла
    public Epic(String title, String statusIn, String description, LocalDateTime startTime, Duration duration) {
        super(title, statusIn, description, startTime, duration);
        relatedSubtasks = new ArrayList<>(); // TODO проверить
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

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (endTime == null) {
            return Optional.empty();
        } else {
            return Optional.of(endTime);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getStartTimeSubtask() {
        return startTimeSubtask;
    }

    public void setStartTimeSubtask(int startTimeSubtask) {
        this.startTimeSubtask = startTimeSubtask;
    }

    public int getEndTimeSubtask() {
        return endTimeSubtask;
    }

    public void setEndTimeSubtask(int endTimeSubtask) {
        this.endTimeSubtask = endTimeSubtask;
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
