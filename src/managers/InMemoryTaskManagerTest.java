package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest {

    protected InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @BeforeEach
    public void create() {
        new InMemoryTaskManagerTest();
    }

    @Test @Override
    public void addTask() {
        super.addTask();
    }

    @Test @Override
    public void addEpicWithOutSubtasks() {
        super.addEpicWithOutSubtasks();
    }

    @Test @Override
    public void addEpicWithSubtasks() {
        super.addEpicWithSubtasks();
    }
}