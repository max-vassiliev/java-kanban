package managers;

public class Managers {

    // получить дефолтный менеджер задач
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // получить менеджер задач с записью в файл
    public static TaskManager getFileBackedTaskManager(String path) {
        return new FileBackedTaskManager(path);
    }

    // получить менеджер истории просмотров
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
