package tests;

import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import tests.InMemoryTaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.HistoryManagerTest;

class InMemoryHistoryManagerTest extends HistoryManagerTest {

    protected InMemoryHistoryManagerTest() {
        super(new InMemoryHistoryManager(), new InMemoryTaskManager());
    }

    @BeforeEach
    public void create() {
        new InMemoryTaskManagerTest();
    }

    @Test @Override
    void addTask() {
        super.addTask();
    }

    @Test @Override
    void addTasks() {
        super.addTasks();
    }

    @Test @Override
    void addTasksWithDuplicate() {
        super.addTasksWithDuplicate();
    }

    @Test @Override
    void shouldSaveTenTasksIfMoreAreAdded() {
        super.shouldSaveTenTasksIfMoreAreAdded();
    }

    @Test @Override
    void removeFirst() {
        super.removeFirst();
    }

    @Test @Override
    void removeLast() {
        super.removeLast();
    }

    @Test @Override
    void removeFromMiddle() {
        super.removeFromMiddle();
    }

    @Test @Override
    void shouldReturnEmptyListIfNoTasksInHistory() {
        super.shouldReturnEmptyListIfNoTasksInHistory();
    }

}