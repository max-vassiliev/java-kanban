package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected String epicTitle;
    protected int epicId;
    protected boolean epicStartTime = false;         // TODO проверить нужность
    protected boolean epicEndTime = false;           // TODO проверить нужность

    public Subtask(String title, String description, String status, String relatedEpicTitle) {
        super(title, description, status);
        this.epicTitle = relatedEpicTitle;
    }

    public Subtask(String title, String description, String status, int relatedEpicId) {
        super(title, description, status);
        this.epicId = relatedEpicId;
    }

    public Subtask(String title, String description, String status,
                   String relatedEpicTitle, String startTime, String duration) {
        super(title, description, status, startTime, duration);
        this.epicTitle = relatedEpicTitle;
    }

    public Subtask(String title, String description, String status,
                   int relatedEpicId, String startTime, String duration) {
        super(title, description, status, startTime, duration);
        this.epicId = relatedEpicId;
    }

    // TODO возможно, убрать коснтруктор
    public Subtask(String title, String status, String description,
                   int relatedEpicId, LocalDateTime startTime, Duration duration) {
        super(title, status, description, startTime, duration);
        this.epicId = relatedEpicId;
    }

    //TODO проверить
    public Subtask(String title, String status, String description, int relatedEpicId,
                   LocalDateTime startTime, Duration duration, boolean epicStartTime, boolean epicEndTime) {
        super(title, status, description, startTime, duration);
        this.epicStartTime = epicStartTime;
        this.epicEndTime = epicEndTime;
        this.epicId = relatedEpicId;
    }

    public String getEpicTitle() {
        return epicTitle;
    }

    public void setEpicTitle(String epicTitle) {
        this.epicTitle = epicTitle;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public boolean isEpicStartTime() {
        return epicStartTime;
    }

    public void setEpicStartTime(boolean epicStartTime) {
        this.epicStartTime = epicStartTime;
    }

    public boolean isEpicEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(boolean epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    // TODO проверить работу
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Task task = (Task) o;
//        return id.equals(task.id);
//    }
//
//    // TODO проверить
//    @Override
//    public int hashCode() {
//        return id.hashCode();
//    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicTitle='" + epicTitle + '\'' +
                ", epicId=" + epicId +
                ", duration=" + duration + '\'' +
                ", startTime=" + startTime + '\'' +
                '}';
    }
}
