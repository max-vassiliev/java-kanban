package managers;

import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private static final String HOME = "resources/";

    private Path history;

    
    static void main(String[] args) {

    }

}
