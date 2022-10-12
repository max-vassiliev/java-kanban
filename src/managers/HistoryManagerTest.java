package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {

    protected static HistoryManager historyManager;
    protected static TaskManager taskManager;
    protected static Task task1;
    protected static Epic epic1;
    protected static Subtask subtask1;
    protected static Subtask subtask2;

    protected HistoryManagerTest(HistoryManager historyManager, TaskManager taskManager) {
        HistoryManagerTest.historyManager = historyManager;
    }

    // ---------------------------------------------
    //  ШАБЛОНЫ ЗАДАЧ
    // ---------------------------------------------

    protected void createTask1() {
        task1 = new Task("Task1",
                         "Description task 1",
                         "NEW");
        task1.setId(1);
    }

    protected void createEpic1() {
        epic1 = new Epic("Epic1", "Description Epic1");
        epic1.setId(2);
    }

    protected void createSubtask1() {
        subtask1 = new Subtask("Epic1 Subtask1",
                               "Description Epic1 Subtask1",
                               "NEW",
                               "Epic1");
        subtask1.setId(3);
    }

    protected void createSubtask2() {
        subtask2 = new Subtask("Epic1 Subtask2",
                               "Description Epic1 Subtask2",
                               "NEW",
                               "Epic1");
        subtask2.setId(4);
    }


    // ---------------------------------------------
    // ТЕСТЫ
    // ---------------------------------------------

    // ДОБАВИТЬ

    // добавить в историю
    @Test
    void addTask() {
        createTask1();
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    // добавить несколько задач
    @Test
    void addTasks() {
        createTask1();
        createEpic1();
        createSubtask1();

        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>(List.of(task1, epic1, subtask1));

        assertEquals(3, history.size(), "Неверное количество задач в истории");
        assertEquals(history, expectedHistory, "Неверный порядок задач в истории");
    }

    // добавить несколько задач, одну продублировать
    @Test
    void addTasksWithDuplicate() {
        createTask1();
        createEpic1();
        createSubtask1();

        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(task1);    // повтор

        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>(List.of(epic1, subtask1, task1));

        assertEquals(3, history.size(), "Неверное количество задач в истории");
        assertEquals(expectedHistory, history, "Неверный порядок задач в истории");
    }

    // добавить много задач сверх лимита
    @Test
    void shouldSaveTenTasksIfMoreAreAdded() {
        createTask1();
        createEpic1();
        createSubtask1();
        createSubtask2();

        Task task2 = new Task("Task2","Description task 2", "NEW");
        Task task3 = new Task("Task3","Description task 3", "NEW");
        Task task4 = new Task("Task4","Description task 4", "NEW");
        Task task5 = new Task("Task5","Description task 5", "NEW");
        Task task6 = new Task("Task6","Description task 6", "NEW");
        Task task7 = new Task("Task7","Description task 7", "NEW");
        Task task8 = new Task("Task8","Description task 8", "NEW");
        task2.setId(5);
        task3.setId(6);
        task4.setId(7);
        task5.setId(8);
        task6.setId(9);
        task7.setId(10);
        task8.setId(11);

        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);

        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>(List.of(epic1, subtask1, subtask2,
                                                                    task2, task3, task4, task5,
                                                                    task6, task7, task8));

        assertEquals(10, history.size(), "Неверное количество задач в истории");
        assertEquals(expectedHistory, history, "Неверный список задач в истории");

    }

    // УДАЛИТЬ

    // удалить первую задачу
    @Test
    void removeFirst() {
        createTask1();
        createEpic1();
        createSubtask1();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(task1.getId());

        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>(List.of(epic1, subtask1));

        assertEquals(2, history.size(), "Неверное количество задач в истории");
        assertEquals(expectedHistory, history, "Неверный список задач в истории");
    }

    // удалить последнюю задачу
    @Test
    void removeLast() {
        createTask1();
        createEpic1();
        createSubtask1();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(subtask1.getId());

        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>(List.of(task1, epic1));

        assertEquals(2, history.size(), "Неверное количество задач в истории");
        assertEquals(expectedHistory, history, "Неверный список задач в истории");
    }

    // удалить из середины
    @Test
    void removeFromMiddle() {
        createTask1();
        createEpic1();
        createSubtask1();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(epic1.getId());

        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>(List.of(task1, subtask1));

        assertEquals(2, history.size(), "Неверное количество задач в истории");
        assertEquals(expectedHistory, history, "Неверный список задач в истории");
    }

    // ДОПОЛНИТЕЛЬНО

    // пустая история задач
    @Test
    void shouldReturnEmptyListIfNoTasksInHistory() {
        final List<Task> history = historyManager.getHistory();
        final List<Task> expectedHistory = new ArrayList<>();

        assertNotNull(history, "История не пустая.");
        assertEquals(expectedHistory, history, "История не пустая");
    }

}