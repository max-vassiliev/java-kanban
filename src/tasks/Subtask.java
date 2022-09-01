package tasks;

public class Subtask extends Task {
    protected String relatedEpicTitle;
    protected int relatedEpicId;

    public Subtask(String title, String description, String status, String relatedEpicTitle) {
        super(title, description, status);
        this.relatedEpicTitle = relatedEpicTitle;
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
                '}';
    }
}
