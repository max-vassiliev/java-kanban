package tests;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private static TaskManager taskManager;

    private static Epic epic;
    private static Subtask subtask1;
    private static Subtask subtask2;

    @BeforeEach
    public void createTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void createEpic() {
        epic = new Epic("Epic1", "Description epic");
        taskManager.add(epic);
    }

    // ---------------------------------------------
    //  ШАБЛОНЫ ПОДЗАДАЧ
    // ---------------------------------------------

    public void createSubtask1() {
        subtask1 = new Subtask("Subtask1",
                               "Description subtask 1",
                               "NEW",
                               "Epic1");
    }

    public void createSubtask2() {
        subtask2 = new Subtask("Subtask2",
                               "Description subtask 2",
                               "NEW",
                               "Epic1");
    }

    // ---------------------------------------------
    //  ТЕСТЫ
    // ---------------------------------------------

    // пустой список подзадач
    @Test
    public void shouldHaveStatusNewWhenNoSubtasks() {
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    // всех подзадачи со статусом NEW
    @Test
    public void shouldHaveStatusNewIfAllSubtasksAreNew() {
        createSubtask1();
        createSubtask2();
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    // все подзадачи со статусом DONE
    @Test
    public void shouldHaveStatusDoneIfAllSubtasksAreDone() {
        createSubtask1();
        createSubtask2();
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.DONE);
    }

    // подзадачи со статусами NEW и DONE
    @Test
    public void shouldHaveStatusInProgressIfSubtasksAreNewOrDone() {
        createSubtask1();
        createSubtask2();
        subtask1.setStatus(Status.DONE);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    // подзадачи со статусом IN_PROGRESS
    @Test
    public void shouldHaveStatusInProgressIfAllSubtasksAreInProgress() {
        createSubtask1();
        createSubtask2();
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}