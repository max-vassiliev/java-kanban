package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;                           // TODO не используется
import java.util.Optional;
import java.util.function.Predicate;                // TODO не используется

public class Task {
    protected Integer id;
    protected String title;
    protected String description;
    protected Status status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime backupStartTime;        // TODO проверить нужность
    protected Duration backupDuration;              // TODO проверить нужность

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    protected static final int MINUTES_IN_HOUR = 60;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, String statusIn) {
        this.title = title;
        this.description = description;
        this.status = Status.valueOf(statusIn);
    }

    // TODO проверить
    // конструктор для считывания из файла
    public Task(String title, String statusIn, String description, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.status = Status.valueOf(statusIn);
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, String statusIn, String startTime, String duration) {
        this.title = title;
        this.description = description;
        this.status = Status.valueOf(statusIn);
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER); // TODO искл - неверный формат ?

        String[] durationSplit = duration.split(":"); // TODO искл - неверный формат ?
        int durationHours = Integer.parseInt(durationSplit[0]);
        int durationMinutes = Integer.parseInt(durationSplit[1]);
        this.duration = Duration.ofMinutes((long) durationHours * MINUTES_IN_HOUR + durationMinutes);
    }

    // рассчитать время завершения задачи
    public Optional<LocalDateTime> getEndTime() {
        return Optional.of(startTime.plusMinutes(duration.toMinutes())); // TODO здесь нужен Optional
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getBackupStartTime() {
        return backupStartTime;
    }

    public void setBackupStartTime(LocalDateTime backupStartTime) {
        this.backupStartTime = backupStartTime;
    }

    public Duration getBackupDuration() {
        return backupDuration;
    }

    public void setBackupDuration(Duration backupDuration) {
        this.backupDuration = backupDuration;
    }

    // TODO проверить работу
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    // TODO проверить
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // TODO возможно переписать — с учетом duration и startTime
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", duration=" + duration + '\'' +
                ", startTime=" + startTime + '\'' +
                '}';
    }
}
