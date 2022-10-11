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

    @Test @Override
    public void updateTask() {
        super.updateTask();
    }

    @Test @Override
    public void updateEpic() {
        super.updateEpic();
    }

    @Test @Override
    public void updateSubtask() {
        super.updateSubtask();
    }

    @Test @Override
    public void deleteTask() {
        super.deleteTask();
    }

    @Test @Override
    public void deleteEpic() {
        super.deleteEpic();
    }

    @Test @Override
    public void deleteSubtask() {
        super.deleteSubtask();
    }

    @Test @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Test @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
    }

    @Test @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
    }
}