package managers;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private static final String HOME = "resources/";

    private Path history;

    public FileBackedTaskManager(Path history) {
        this.history = history;
    }


    static void main(String[] args) {

        FileBackedTaskManager manager = new FileBackedTaskManager(Paths.get(HOME, "history.csv"));


    }

}
