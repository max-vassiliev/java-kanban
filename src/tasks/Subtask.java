package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected String epicTitle;
    protected int epicId;
    protected boolean isEpicStartTime = false;
    protected boolean isEpicEndTime = false;

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

    // конструктор для чтения из файла
    public Subtask(String title, String status, String description, int relatedEpicId,
                   LocalDateTime startTime, Duration duration, boolean isEpicStartTime, boolean isEpicEndTime) {
        super(title, status, description, startTime, duration);
        this.isEpicStartTime = isEpicStartTime;
        this.isEpicEndTime = isEpicEndTime;
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

    public boolean getIsEpicStartTime() {
        return isEpicStartTime;
    }

    public void setIsEpicStartTime(boolean isEpicStartTime) {
        this.isEpicStartTime = isEpicStartTime;
    }

    public boolean getIsEpicEndTime() {
        return isEpicEndTime;
    }

    public void setIsEpicEndTime(boolean isEpicEndTime) {
        this.isEpicEndTime = isEpicEndTime;
    }

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
