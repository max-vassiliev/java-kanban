package entries;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected String statusRaw;
    protected TaskStatus taskStatus;

    public Task(int id, String title, String description, String statusRaw) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusRaw = statusRaw;
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

    public String getStatusRaw() {
        return statusRaw;
    }

    public void setStatusRaw(String statusRaw) {
        this.statusRaw = statusRaw;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "entries.Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", statusRaw='" + statusRaw + '\'' +
                "}";
    }

}
