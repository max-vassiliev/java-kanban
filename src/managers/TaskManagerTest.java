package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;       //TODO проверить импорты
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected static TaskManager taskManager;
    protected static Task task1;
    protected static Epic epic1;
    protected static Epic epic2;
    protected static Subtask subtask1;
    protected static Subtask subtask2;
    protected static Subtask subtask3;

    protected TaskManagerTest(TaskManager taskManager) {
        TaskManagerTest.taskManager = taskManager;
    }

    // ТЕСТЫ 1 — Стандартное поведение

    // добавить задачу
    @Test
    public void addTask() {
        createTask1();
        final int idTask1 = taskManager.add(task1);
        final Task savedTask1 = taskManager.getTask(idTask1);

        assertNotNull(savedTask1, "Задача не найдена");
        assertEquals(task1, savedTask1, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    };

    // добавить эпик без подзадач
    @Test
    public void addEpicWithOutSubtasks() {
        createEpic1();
        final int epicId = taskManager.add(epic1);
        final Epic savedEpic1 = taskManager.getEpic(epicId);

        assertNotNull(savedEpic1, "Эпик не найден");
        assertEquals(epic1, savedEpic1, "Эпики не совпадают");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают");
    }

    // добавить эпик с подзадачами
    @Test
    public void addEpicWithSubtasks() {
        createEpic1();
        createSubtask1();
        createSubtask2();

        final int idEpic1 = taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);

        final Epic savedEpic1 = taskManager.getEpic(idEpic1);
        final Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        final Subtask savedSubtask2 = taskManager.getSubtask(idSubtask2);

        assertNotNull(savedEpic1, "Не найден эпик");
        assertNotNull(savedSubtask1, "Не найдена подзадача 1");
        assertNotNull(savedSubtask2, "Не найдена подзадача 2");
        assertEquals(epic1, savedEpic1, "Эпики не совпадают");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают");

        final int savedEpicSubtask1 = savedSubtask1.getEpicId();
        final int savedEpicSubtask2 = savedSubtask2.getEpicId();

        assertEquals(idEpic1, savedEpicSubtask1, "ID эпиков не совпадают");
        assertEquals(idEpic1, savedEpicSubtask2, "ID эпиков не совпадают");

        final List<Subtask> savedRelatedSubtasks = taskManager.getSubtasksInEpic(idEpic1);
        assertNotNull(savedRelatedSubtasks, "Не найден список подзадач");

        assertEquals(savedSubtask1, savedRelatedSubtasks.get(0), "Подзадачи не совпадают");
        assertEquals(savedSubtask2, savedRelatedSubtasks.get(1), "Подзадачи не совпадают");

        final List<Epic> epics = taskManager.getEpics();
        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают");

        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают");
    }

    // обновить задачу
    @Test
    public void updateTask() {
        createTask1();
        final int idTask1 = taskManager.add(task1);
        final Task savedTask1 = taskManager.getTask(idTask1);
        savedTask1.setStatus(Status.DONE);

        taskManager.update(savedTask1);

        final List<Task> tasks = taskManager.getTasks();
        assertEquals(savedTask1, tasks.get(0), "Задачи не совпадают");
    }

    // обновить эпик без подзадач
    @Test
    public void updateEpic() {
        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        final Epic savedEpic1 = taskManager.getEpic(idEpic1);
        savedEpic1.setDescription("Epic1 Description2");

        taskManager.update(savedEpic1);

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(savedEpic1, epics.get(0), "Эпики не совпадают");
    }

    // обновить подзадачу
    @Test
    public void updateSubtask() {
        createEpic1();
        createSubtask1();
        createSubtask2();

        final int idEpic1 = taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);
        taskManager.add(subtask2);

        final Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        savedSubtask1.setStatus(Status.DONE);

        taskManager.update(savedSubtask1);
        final List<Subtask> subtasks = taskManager.getSubtasks();
        final Epic savedEpic = taskManager.getEpic(idEpic1);

        assertEquals(savedSubtask1, subtasks.get(0), "Подзадачи не совпадают");
        assertEquals(savedEpic.getStatus(), Status.IN_PROGRESS, "Неверный статус эпика");
    }

    // удалить задачу
    @Test
    public void deleteTask() {
        createTask1();
        final int idTask1 = taskManager.add(task1);

        taskManager.getTask(idTask1);
        taskManager.delete(task1);

        List<Task> tasks = taskManager.getTasks();
        List<Task> tasksInHistory = taskManager.getHistory();

        assertEquals(0, tasks.size());
        assertEquals(0, tasksInHistory.size());
    }

    // удалить эпик
    @Test
    public void deleteEpic() {
        createEpic1();
        createSubtask1();
        createSubtask2();

        final int idEpic1 = taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);

        final Epic savedEpic = taskManager.getEpic(idEpic1);
        taskManager.getSubtask(idSubtask1);
        taskManager.getSubtask(idSubtask2);

        taskManager.delete(savedEpic);

        // TODO добавить prioritized tasks

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Task> history = taskManager.getHistory();

        assertEquals(0, epics.size());
        assertEquals(0, subtasks.size());
        assertEquals(0, history.size());
    }

    // удалить подзадачу
    @Test
    public void deleteSubtask() {
        createEpic1();
        createSubtask1();
        createSubtask2();

        final int idEpic1 = taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);

        final Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        taskManager.getSubtask(idSubtask2);
        taskManager.getEpic(idEpic1);

        taskManager.delete(savedSubtask1);

        List<Subtask> epicSubtasks = taskManager.getSubtasksInEpic(idEpic1);
        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Task> history = taskManager.getHistory();

        // TODO добавить prioritized tasks

        assertEquals(1, subtasks.size(), "Остались лишние подзадачи");
        assertEquals(1, epicSubtasks.size(), "Остались лишние подзадачи");
        assertEquals(epicSubtasks, subtasks, "Cписки подзадач не совпадают");
        assertEquals(2, history.size(), "Остались лишние задачи в истории");

    }

    // удалить все задачи
    @Test
    public void deleteAllTasks() {
        createTask1();
        Task task2 = new Task("Task2",
                        "Description task 2",
                        "NEW");

        final int idTask1 = taskManager.add(task1);
        final int idTask2 = taskManager.add(task2);

        taskManager.getTask(idTask1);
        taskManager.getTask(idTask2);

        taskManager.deleteAllTasks();

        //TODO добавить prioritizedTasks

        List<Task> tasks = taskManager.getTasks();
        List<Task> history = taskManager.getHistory();

        assertEquals(0, tasks.size(), "Остались лишние задачи в списке задач");
        assertEquals(0, history.size(), "Остались лишние задачи в истории");
    }

    // удалить все эпики
    @Test
    public void deleteAllEpics() {
        createEpic1();
        createEpic2();
        createSubtask1();
        createSubtask2();
        createSubtask3();

        final int idEpic1 = taskManager.add(epic1);
        final int idEpic2 = taskManager.add(epic2);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);
        final int idSubtask3 = taskManager.add(subtask3);

        taskManager.getEpic(idEpic1);
        taskManager.getEpic(idEpic2);
        taskManager.getSubtask(idSubtask1);
        taskManager.getSubtask(idSubtask2);
        taskManager.getSubtask(idSubtask3);

        taskManager.deleteAllEpics();

        // TODO добавить prioritized tasks

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Task> history = taskManager.getHistory();

        assertEquals(0, epics.size(), "Остались лишние эпики в списке эпиков");
        assertEquals(0, subtasks.size(), "Остались лишние подзадачи в списке подзадач");
        assertEquals(0, history.size(), "Остались лишние задачи в истории");
    }

    // удалить все подзадачи
    @Test
    public void deleteAllSubtasks() {
        createEpic1();
        createEpic2();
        createSubtask1();
        createSubtask2();
        createSubtask3();

        final int idEpic1 = taskManager.add(epic1);
        final int idEpic2 = taskManager.add(epic2);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);
        final int idSubtask3 = taskManager.add(subtask3);

        taskManager.getEpic(idEpic1);
        taskManager.getEpic(idEpic2);
        taskManager.getSubtask(idSubtask1);
        taskManager.getSubtask(idSubtask2);
        taskManager.getSubtask(idSubtask3);

        taskManager.deleteAllSubtasks();

        // TODO добавить prioritized tasks

        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Epic> epics = taskManager.getEpics();
        List<Task> history = taskManager.getHistory();

        assertEquals(0, subtasks.size(), "Остались лишние подзадачи");
        assertEquals(2, history.size(), "Остались лишние задачи в истории");

        assertEquals(2, epics.size(), "Количество эпиков не соответствует ожидаемому");
        assertEquals(Status.NEW, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(Status.NEW, epics.get(1).getStatus(), "Неверный статус эпика");
    }


    // ТЕСТЫ 2 — Пустой список задач

    // TODO добавить тесты


    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    // создать задачи по шаблону

    protected void createTask1() {
        task1 = new Task("Task1",
                "Description task 1",
                "NEW");
    }

    protected void createEpic1() {
        epic1 = new Epic("Epic1", "Description Epic1");
    }

    protected void createEpic2() {
        epic2 = new Epic("Epic2", "Description Epic2");
    }

    protected void createSubtask1() {
        subtask1 = new Subtask("Epic1 Subtask1",
                "Description Epic1 Subtask1",
                "NEW",
                "Epic1");
    }

    protected void createSubtask2() {
        subtask2 = new Subtask("Epic1 Subtask2",
                "Description Epic1 Subtask2",
                "NEW",
                "Epic1");
    }

    protected void createSubtask3() {
        subtask3 = new Subtask("Epic2 Subtask3",
                "Description Epic2 Subtask3",
                "NEW",
                "Epic2");
    }
}