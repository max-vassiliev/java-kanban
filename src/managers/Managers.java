package managers;

import managers.InMemoryTaskManager;

public class Managers {

    // получить менеджер задач
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // получить менеджер истории просмотров
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
