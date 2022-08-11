package managers;

import managers.InMemoryTaskManager;

public class Managers {

    // получить менеджер по умолчанию
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

}
