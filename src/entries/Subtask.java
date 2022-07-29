package entries;

public class Subtask extends Task {
    protected String relatedEpicTitle;
    protected int relatedEpicId;

    public Subtask(int id, String title, String description, String status, String relatedEpicTitle) {
        super(id, title, description, status);
        this.relatedEpicTitle = relatedEpicTitle;
    }

    
}
