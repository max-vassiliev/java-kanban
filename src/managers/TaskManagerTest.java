package managers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected static TaskManager taskManager;

    protected TaskManagerTest(TaskManager taskManager) { //TODO удалить, если не нужно
        this.taskManager = taskManager;
    }

//    @BeforeEach
//    public void createTaskManager(TaskManager taskManager1) {
//        taskManager = taskManager1;
//    }

    // добавить задачу
    public void addTask() {
        Task task = new Task("Task1", "Description task 1", "NEW");
        final int taskId = taskManager.add(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    };

    // добавить эпик без подзадач
    public void addEpicWithOutSubtasks() {
        Epic epic = new Epic("Epic1", "Description epic1");
        final int epicId = taskManager.add(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(0), "Эпики не совпадают");
    }

    // добавить эпик с подзадачами
    public void addEpicWithSubtasks() {
        Epic epic = new Epic("Epic2", "Description epic2");
        Subtask subtask1 = new Subtask("Epic2 Subtask1",
                                        "Description Epic2 Subtask1",
                                        "NEW",
                                        "Epic2");
        Subtask subtask2 = new Subtask("Epic2 Subtask2",
                                        "Description Epic2 Subtask2",
                                        "NEW",
                                        "Epic2");

        final int idEpic = taskManager.add(epic);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);
        final List<Integer> relatedSubtaskIds = new ArrayList<>(List.of(idSubtask1, idSubtask2));

        final Epic savedEpic = taskManager.getEpic(idEpic);
        final Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        final Subtask savedSubtask2 = taskManager.getSubtask(idSubtask2);
        final List<Integer> savedRelatedSubtasks = savedEpic.getRelatedSubtasks();

        assertNotNull(savedEpic, "Не найден эпик");
        assertNotNull(savedSubtask1, "Не найдена подзадача 1");
        assertNotNull(savedSubtask2, "Не найдена подзадача 2");
        assertNotNull(savedRelatedSubtasks, "Не найден список подзадач");

        assertEquals(epic, savedEpic, "Эпики не совпадают");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают");
        assertEquals(relatedSubtaskIds, savedRelatedSubtasks, "Списки подзадач не совпадают");

        final List<Epic> epics = taskManager.getEpics();
        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(0), "Эпики не совпадают");

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают");


    }

}