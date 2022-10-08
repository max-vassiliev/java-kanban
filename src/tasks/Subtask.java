package tasks;

public class Subtask extends Task {
    protected String relatedEpicTitle;
    protected int relatedEpicId;

    public Subtask(String title, String description, String status, String relatedEpicTitle) {
        super(title, description, status);
        this.relatedEpicTitle = relatedEpicTitle;
    }

    public Subtask(String title, String description, String status, int relatedEpicId) {
        super(title, description, status);
        this.relatedEpicId = relatedEpicId;
    }

    // TODO проверить работу
    public Subtask(String title, String description, String status,
                   String relatedEpicTitle, String startTime, String duration) {
        super(title, description, status, startTime, duration);
        this.relatedEpicTitle = relatedEpicTitle;
    }

    // TODO проверить работу
    public Subtask(String title, String description, String status,
                   int relatedEpicId, String startTime, String duration) {
        super(title, description, status, startTime, duration);
        this.relatedEpicId = relatedEpicId;
    }




    public String getRelatedEpicTitle() {
        return relatedEpicTitle;
    }

    public void setRelatedEpicTitle(String relatedEpicTitle) {
        this.relatedEpicTitle = relatedEpicTitle;
    }

    public int getRelatedEpicId() {
        return relatedEpicId;
    }

    public void setRelatedEpicId(int relatedEpicId) {
        this.relatedEpicId = relatedEpicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", relatedEpicTitle='" + relatedEpicTitle + '\'' +
                ", relatedEpicId=" + relatedEpicId +
                ", duration=" + duration + '\'' +
                ", startTime=" + startTime + '\'' +
                '}';
    }
}
