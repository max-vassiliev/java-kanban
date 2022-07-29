package entries;

public class Subtask extends Task {
    protected String relatedEpicTitle;
    protected int relatedEpicId;

    public Subtask(int id, String title, String description, String status, String relatedEpicTitle) {
        super(id, title, description, status);
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


}
