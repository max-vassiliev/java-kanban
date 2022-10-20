package managers;

public class Managers {

    // получить дефолтный менеджер задач
    public static TaskManager getDefault(String uri) {
        return new HTTPTaskManager(uri);
    }

    // получить менеджер задач, сохраняющий данные в оперативной памяти
    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    // получить менеджер задач, сохраняющий данные в файл
    public static TaskManager getFileBackedTaskManager(String path) {
        return new FileBackedTaskManager(path);
    }


    // получить менеджер задач, сохраняющий данные на сервере
    public static TaskManager getHTTPTaskManager(String uri) {
        return new HTTPTaskManager(uri);
    }

    // получить менеджер истории просмотров
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
