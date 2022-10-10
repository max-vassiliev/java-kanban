package tasks;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private static InMemoryTaskManager taskManager;

    private static Epic epic;
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static Subtask subtask3;  // TODO проверить необходимость поля

    @BeforeAll
    public static void createTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void createEpic() {
        epic = new Epic("Epic1", "Description epic");
        taskManager.add(epic);
    }

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

    // TODO удалить, если не используется
    public void createSubtask3() {
        subtask3 = new Subtask("Subtask3",
                "Description subtask 3",
                "NEW",
                "Epic1");
    }


    @Test
    public void shouldHaveStatusNewWhenNoSubtasks() {
        // Статус эпика — NEW, если список подзадач пустой
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void shouldHaveStatusNewIfAllSubtasksAreNew() {
        // Статус эпика — NEW, если у всех подзадач статус NEW
        createSubtask1();
        createSubtask2();
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void shouldHaveStatusDoneIfAllSubtasksAreDone() {
        // Статус эпика — DONE, если у всех подзадач статус DONE
        createSubtask1();
        createSubtask2();
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    public void shouldHaveStatusInProgressIfSubtasksAreNewOrDone() {
        // Статус эпика — IN_PROGRESS, если у подзадач статусы NEW и DONE
        createSubtask1();
        createSubtask2();
        subtask1.setStatus(Status.DONE);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void shouldHaveStatusInProgressIfAllSubtasksAreInProgress() {
        // Статус эпика — IN_PROGRESS, если у подзадач статус IN_PROGRESS
        createSubtask1();
        createSubtask2();
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}