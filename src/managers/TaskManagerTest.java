package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected static TaskManager taskManager;
    protected static Task task1;
    protected static Task task2;
    protected static Epic epic1;
    protected static Epic epic2;
    protected static Subtask subtask1;
    protected static Subtask subtask2;
    protected static Subtask subtask3;

    protected TaskManagerTest(TaskManager taskManager) {
        TaskManagerTest.taskManager = taskManager;
    }

    // ---------------------------------------------
    //  ШАБЛОНЫ ЗАДАЧ
    // ---------------------------------------------

    protected void createTask1() {
        task1 = new Task("Task1",
                "Description task 1",
                "NEW",
                "15.10.2022, 14:00",
                "01:00");
    }

    protected void createTask2() {
        task2 = new Task("Task2",
                "Description task 2",
                "NEW",
                "15.11.2022, 14:00",
                "01:00");
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
                "Epic1",
                "16.10.2022, 12:00",
                "00:30"
        );
    }

    protected void createSubtask2() {
        subtask2 = new Subtask("Epic1 Subtask2",
                "Description Epic1 Subtask2",
                "NEW",
                "Epic1",
                "17.10.2022, 12:00",
                "00:30"
        );
    }

    protected void createSubtask3() {
        subtask3 = new Subtask("Epic2 Subtask3",
                "Description Epic2 Subtask3",
                "NEW",
                "Epic2",
                "18.10.2022, 14:00",
                "00:30"
        );
    }


    // ---------------------------------------------
    // ТЕСТЫ 1 — Стандартное поведение
    // ---------------------------------------------

    // ДОБАВИТЬ

    // добавить задачу
    @Test
    public void addTask() {
        createTask1();
        final int idTask1 = taskManager.add(task1);
        final Task savedTask1 = taskManager.getTask(idTask1);

        assertNotNull(savedTask1, "Задача не найдена");
        assertEquals(task1, savedTask1, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getTasks();
        final List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество приоритетных задач");
        assertEquals(task1, prioritizedTasks.get(0), "Задачи не совпадают.");

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

    // ОБНОВИТЬ

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

    // УДАЛИТЬ

    // удалить задачу
    @Test
    public void deleteTask() {
        createTask1();
        final int idTask1 = taskManager.add(task1);

        taskManager.getTask(idTask1);
        taskManager.delete(task1);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTasks();
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());
        List<Task> tasksInHistory = taskManager.getHistory();
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

        List<Task> history = taskManager.getHistory();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(0, history.size(), "История не пустая");
        assertEquals(0, prioritizedTasks.size(), "Список приоритетных задач не пуст");

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpics();
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());


        final NullPointerException exception2 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasks();
                    }
                });

        assertEquals("Список подзадач пуст", exception2.getMessage());
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
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(1, epicSubtasks.size(), "Неверное количество подзадач у эпика");
        assertEquals(epicSubtasks, subtasks, "Cписки подзадач не совпадают");
        assertEquals(2, history.size(), "Неверное количество задач в истории");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество приоритетных задач");

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

        List<Task> history = taskManager.getHistory();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(0, history.size(), "Остались лишние задачи в истории");
        assertEquals(0, prioritizedTasks.size(), "Неверное количество приоритетных задач");

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTasks();
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());

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

        List<Task> history = taskManager.getHistory();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(0, history.size(), "Остались лишние задачи в истории");
        assertEquals(0, prioritizedTasks.size(), "Неверное количество приоритетных задач");

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpics();
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());

        final NullPointerException exception2 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasks();
                    }
                });

        assertEquals("Список подзадач пуст", exception2.getMessage());
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

        List<Epic> epics = taskManager.getEpics();
        List<Task> history = taskManager.getHistory();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(2, history.size(), "Остались лишние задачи в истории");

        assertEquals(2, epics.size(), "Количество эпиков не соответствует ожидаемому");
        assertEquals(Status.NEW, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(Status.NEW, epics.get(1).getStatus(), "Неверный статус эпика");
        assertEquals(0, prioritizedTasks.size(), "Неверное количество приоритетных задач");

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasks();
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // ДАТА, ВРЕМЯ, ПРОДОЛЖИТЕЛЬНОСТЬ

    // рассчитать время эпика по подзадачам, где указано время
    @Test
    void shouldCalculateEpicTimingFromSubtasksWithTiming() {
        createEpic1();
        createSubtask1();
        createSubtask2();
        Subtask subtaskNonPriority = new Subtask("Epic1 Subtask3",
                                                 "Description Epic1 Subtask3",
                                                 "NEW",
                                                 "Epic1"
        );

        final int idEpic1 = taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);
        taskManager.add(subtaskNonPriority);

        Epic savedEpic1 = taskManager.getEpic(idEpic1);
        Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        Subtask savedSubtask2 = taskManager.getSubtask(idSubtask2);
        Duration expectedEpicDuration = Duration.between(savedSubtask1.getStartTime(),
                                                         savedSubtask2.getEndTime().get());

        assertTrue(savedSubtask1.isEpicStartTime(), "Подзадача должна быть начальной для эпика");
        assertTrue(savedSubtask2.isEpicEndTime(), "Подзадача должна быть завершающей для эпика");
        assertEquals(savedSubtask1.getStartTime(), savedEpic1.getStartTime(),
                                                    "Неверное время начала эпика");
        assertEquals(savedSubtask2.getEndTime().get(), savedEpic1.getEndTime().get(),
                                                    "Неверное время завершения эпика");
        assertEquals(expectedEpicDuration, savedEpic1.getDuration(),
                                                    "Неверная продолжительность эпика");
    }

    // время эпика меняется при изменении времени подзадачи
    @Test
    void shouldUpdateEpicTimingIfSubtaskTimeUpdated() {
        createEpic1();
        createSubtask1();
        createSubtask2();
        Subtask subtaskToUpdate = new Subtask("Epic1 Subtask3",
                                              "Description Epic1 Subtask3",
                                              "NEW",
                                              "Epic1"
        );

        final int idEpic1 = taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);
        final int idSubtask2 = taskManager.add(subtask2);
        final int idSubtaskToUpdate = taskManager.add(subtaskToUpdate);

        final Epic epic1Saved = taskManager.getEpic(idEpic1);
        final LocalDateTime epic1SavedStartTime = epic1Saved.getStartTime();
        final LocalDateTime epic1SavedEndTime = epic1Saved.getEndTime().get();
        final Duration epic1SavedDuration = epic1Saved.getDuration();

        Subtask subtask1Saved = taskManager.getSubtask(idSubtask1);
        Subtask subtask2Saved = taskManager.getSubtask(idSubtask2);
        Subtask subtaskToUpdateSaved = taskManager.getSubtask(idSubtaskToUpdate);

        // добавляем время для третьей подзадачи
        // она должна стать последней для эпика
        Subtask subtaskUpdate = new Subtask("Epic1 Subtask3",
                                            "Description Epic1 Subtask3",
                                            "NEW",
                                            "Epic1",
                                            "19.10.2022, 10:00",
                                            "01:10"
        );
        subtaskUpdate.setId(subtaskToUpdateSaved.getId());
        subtaskUpdate.setEpicId(subtaskToUpdateSaved.getEpicId());
        subtaskUpdate.setType(subtaskToUpdateSaved.getType());

        taskManager.update(subtaskUpdate);

        final Epic epic1Updated = taskManager.getEpic(idEpic1);
        final Subtask subtaskToUpdateUpdated = taskManager.getSubtask(idSubtaskToUpdate);

        final LocalDateTime expectedStartTime = subtask1Saved.getStartTime();
        final LocalDateTime expectedEndTimeSaved = subtask2Saved.getEndTime().get();
        final LocalDateTime expectedEndTimeUpdated = subtaskToUpdateUpdated.getEndTime().get();

        assertEquals(epic1SavedStartTime, epic1Updated.getStartTime(),
                                        "Время начала эпика не совпадает");
        assertNotEquals(epic1SavedEndTime, epic1Updated.getEndTime().get(),
                                        "Время завершения эпика не должно совпадать");
        assertNotEquals(epic1SavedDuration, epic1Updated.getDuration(),
                                        "Продолжительность эпиков не должна совпадать");

        assertEquals(expectedStartTime, epic1SavedStartTime,
                                        "Неверное время начала эпика при сохранении");
        assertEquals(expectedEndTimeSaved, epic1SavedEndTime,
                                        "Неверное время завершения эпика при сохранении");
        assertEquals(expectedStartTime, epic1Updated.getStartTime(),
                                        "Неверное время начала эпика при обновлении");
        assertEquals(expectedEndTimeUpdated, epic1Updated.getEndTime().get(),
                                        "Неверное время завершения эпика после обновления");
    }

    // задачи без времени должны добавляться в конец списка по приоритету
    @Test
    void shouldAddTasksWithoutTimingAsNonPriority() {
        createTask1();
        createTask2();
        Task nonPriorityTaskA = new Task("Non-Priority Task A",
                                         "Description Non-Priority Task A",
                                         "NEW"
        );
        Task nonPriorityTaskB = new Task("Non-Priority Task B",
                                         "Description Non-Priority Task B",
                                         "NEW"
        );

        taskManager.add(nonPriorityTaskA);
        taskManager.add(task1);
        taskManager.add(nonPriorityTaskB);
        taskManager.add(task2);

        final List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        final List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2,
                                                                            nonPriorityTaskA,
                                                                            nonPriorityTaskB));

        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Списки задач не совпадают");
    }

    // ПЕРЕСЕЧЕНИЕ ЗАДАЧ

    // пересечение задач: новая задача начинается до того, как завершилась первая
    @Test
    void shouldPreventOverlapIfTask1EndsAfterTask2Starts() {
        // task1 начинается в 14:00 и длится 1 час
        createTask1();
        // task2 начинается в 14:30 и длится 1 час
        task2 = new Task("Task2",
                         "Description task 2",
                         "NEW",
                         "15.10.2022, 14:30",
                         "01:00");

        taskManager.add(task1);
        final int idTask2 = taskManager.add(task2);
        Task savedTask2 = taskManager.getTask(idTask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));

        assertNull(savedTask2.getStartTime(), "Время начала задачи не должно сохраняться");
        assertEquals(expectedPrioritizedTasks, prioritizedTasks,
                                              "Неверный порядок сохранения задач");

    }

    // пересечение задач: новая задача завершается после того, как началась первая
    void shouldPreventOverlapIfTask1StartsBeforeTask2Ends() {
        // task1 начинается в 14:00 и длится 1 час
        createTask1();
        // task2 начинается в 13:30 и длится 1 час
        task2 = new Task("Task2",
                "Description task 2",
                "NEW",
                "15.10.2022, 13:30",
                "01:00");

        taskManager.add(task1);
        final int idTask2 = taskManager.add(task2);
        Task savedTask2 = taskManager.getTask(idTask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));

        assertNull(savedTask2.getStartTime(), "Время начала задачи не должно сохраняться");
        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Неверный порядок сохранения задач");

    }

    // пересечение задач: новая задача начинается одновременно с первой
    @Test
    void shouldPreventOverlapIfTasksStartAtOneTime() {
        // task1 начинается в 14:00 и длится 1 час
        createTask1();
        // task2 начинается в 14:00 и длится 15 минут
        task2 = new Task("Task2",
                         "Description task 2",
                         "NEW",
                         "15.10.2022, 14:00",
                         "00:15");

        taskManager.add(task1);
        final int idTask2 = taskManager.add(task2);
        Task savedTask2 = taskManager.getTask(idTask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));

        assertNull(savedTask2.getStartTime(), "Время начала задачи не должно сохраняться");
        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Неверный порядок сохранения задач");
    }

    // пересечение задач: новая задача начинается и завершается, пока идет первая
    @Test
    void shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress() {
        // task1 начинается в 14:00 и длится 1 час
        createTask1();
        // task2 начинается в 14:15 и длится 30 минут
        task2 = new Task("Task2",
                "Description task 2",
                "NEW",
                "15.10.2022, 14:15",
                "00:30");

        taskManager.add(task1);
        final int idTask2 = taskManager.add(task2);
        Task savedTask2 = taskManager.getTask(idTask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));

        assertNull(savedTask2.getStartTime(), "Время начала задачи не должно сохраняться");
        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Неверный порядок сохранения задач");
    }

    // пересечение задач при обновлении одной из них:
    // в случае пересечения сохраняется старое время начала
    @Test
    void shouldPreventOverlapOnUpdate() {
        // task1 начинается 15 октября в 14:00
        createTask1();
        // task2 начинается на день раньше в то же время
        task2 = new Task("Task2",
                "Description task 2",
                "NEW",
                "14.10.2022, 14:00",
                "01:00");

        taskManager.add(task1);
        final int idTask2 = taskManager.add(task2);
        final Task task2Saved = taskManager.getTask(idTask2);
        final LocalDateTime task2SavedStartTime = task2Saved.getStartTime();
        List<Task> prioritizedTasksSaved = taskManager.getPrioritizedTasks();

        // обновляем дату начала Task2 на 15 октября
        // обе задачи будут начинаться и заканчиваться в одно время
        Task task2Upd = new Task("Task2",
                                 "Description task 2",
                                 "NEW",
                                 "15.10.2022, 14:00",
                                 "01:00");
        task2Upd.setId(idTask2);
        task2Upd.setBackupStartTime(task2Saved.getBackupStartTime());
        task2Upd.setBackupDuration(task2Saved.getBackupDuration());
        task2Upd.setType(task2Saved.getType());

        taskManager.update(task2Upd);

        final Task task2Updated = taskManager.getTask(idTask2);
        final LocalDateTime task2UpdatedStartTime = task2Updated.getStartTime();
        List<Task> prioritizedTasksUpdated = taskManager.getPrioritizedTasks();

        assertEquals(task2SavedStartTime, task2UpdatedStartTime,
                                         "Время начала не совпадает");
        assertEquals(prioritizedTasksSaved, prioritizedTasksUpdated,
                                         "Порядок задач не совпадает ");
    }


    // ---------------------------------------------
    // ТЕСТЫ 2 — Пустой список задач
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу в пустом списке
    @Test
    void shouldReturnNullWhenUpdatingTaskInEmptyList() {
        createTask1();
        task1.setId(1);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.update(task1);
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());
    }

    // обновить эпик в пустом списке
    @Test
    void shouldReturnNullWhenUpdatingEpicInEmptyList() {
        createEpic1();
        epic1.setId(1);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.update(epic1);
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());
    }

    // обновить подзадачу в пустом списке
    @Test
    void shouldReturnNullWhenUpdatingSubtaskInEmptyList() {
        createSubtask1();
        subtask1.setId(1);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.update(subtask1);
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // ПОЛУЧИТЬ

    // получить задачу из пустого списка
    @Test
    void shouldReturnNullWhenGettingTaskFromEmptyList() {
        createTask1();
        task1.setId(1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTask(task1.getId());
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());
    }

    // получить эпик из пустого списка
    @Test
    void shouldReturnNullWhenGettingEpicFromEmptyList() {
        createEpic1();
        epic1.setId(1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(epic1.getId());
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());
    }

    // получить подзадачу из пустого списка
    @Test
    void shouldReturnNullWhenGettingSubtaskFromEmptyList() {
        createSubtask1();
        subtask1.setId(1);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtask(subtask1.getId());
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // получить все задачи из пустого списка
    @Test
    void shouldReturnNullWhenGettingAllTasksIfListIsEmpty() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTasks();
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());
    }

    // получить все эпики из пустого списка
    @Test
    void shouldReturnNullWhenGettingAllEpicsIfListIsEmpty() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpics();
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());
    }

    // получить все подзадачи из пустого списка
    @Test
    void shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasks();
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // получить все подзадачи эпика, если пусты списки эпиков и подзадач
    @Test
    void shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty() {
        createEpic1();
        epic1.setId(1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasksInEpic(epic1.getId());
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());
    }

    // получить все подзадачи эпика, если пуст список подзадач в менеджере
    void shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty() {
        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasksInEpic(idEpic1);
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // УДАЛИТЬ

    // удалить задачу из пустого списка
    @Test
    void shouldReturnNullWhenDeletingTaskFromEmptyList() {
        createTask1();
        task1.setId(1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.delete(task1);
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());
    }

    // удалить эпик из пустого списка
    @Test
    void shouldReturnNullWhenDeletingEpicFromEmptyList() {
        createEpic1();
        epic1.setId(1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.delete(epic1);
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());
    }

    // удалить подзадачу из пустого списка
    @Test
    void shouldReturnNullWhenDeletingSubtaskFromEmptyList() {
        createSubtask1();
        subtask1.setId(1);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.delete(subtask1);
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // удалить все задачи из пустого списка
    @Test
    void shouldReturnNullWhenDeletingAllTasksIfListIsEmpty() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.deleteAllTasks();
                    }
                });

        assertEquals("Список задач пуст", exception.getMessage());
    }

    // удалить все эпики из пустого списка
    @Test
    void shouldReturnNullWhenDeletingAllEpicIfListIsEmpty() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.deleteAllEpics();
                    }
                });

        assertEquals("Список эпиков пуст", exception.getMessage());
    }

    // удалить все подзадачи из пустого списка
    @Test
    void shouldReturnNullWhenDeletingAllSubtasksIfListIsEmpty() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.deleteAllSubtasks();
                    }
                });

        assertEquals("Список подзадач пуст", exception.getMessage());
    }

    // ДОПОЛНИТЕЛЬНО

    // получить задачи из пустого списка истории
    @Test
    void shouldReturnEmptyListIfHistoryIsEmpty() {
        final List<Task> expectedHistory = new ArrayList<>();
        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "Список не должен быть равен null");
        assertEquals(expectedHistory, history, "Список не пустой");
    }

    // получить задачи из пустого списка приоритетных задач
    @Test
    void shouldReturnEmptyListIfPriorityTaskListIsEmpty() {
        final List<Task> expectedList = new ArrayList<>();
        final List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertNotNull(prioritizedTasks, "Список не должен быть равен null");
        assertEquals(expectedList, prioritizedTasks, "Список не пустой");
    }


    // ---------------------------------------------
    // ТЕСТЫ 3 — Несуществующий идентификатор задачи
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу, используя несуществующий ID
    @Test
    void shouldReturnNullWhenUpdatingTaskWithInvalidId() {
        createTask1();
        final int idTask1 = taskManager.add(task1);
        Task savedTask1 = taskManager.getTask(idTask1);
        savedTask1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.update(savedTask1);
                    }
                });

        assertEquals("Задачи нет в списке", exception.getMessage());
    }

    // обновить эпик, используя несуществующий ID
    @Test
    void shouldReturnNullWhenUpdatingEpicWithInvalidId() {
        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        Epic savedEpic1 = taskManager.getEpic(idEpic1);
        savedEpic1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.update(savedEpic1);
                    }
                });

        assertEquals("Эпика нет в списке", exception.getMessage());
    }

    // обновить подзадачу, используя несуществующий ID
    @Test
    void shouldReturnNullWhenUpdatingSubtaskWithInvalidId() {
        createEpic1();
        createSubtask1();
        taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);

        Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        savedSubtask1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.update(savedSubtask1);
                    }
                });

        assertEquals("Подзадачи нет в списке", exception.getMessage());
    }

    // ПОЛУЧИТЬ

    // получить задачу, используя несуществующий ID
    @Test
    void shouldReturnNullWhenGettingTaskWithInvalidId() {
        createTask1();
        final int idTask1 = taskManager.add(task1);
        Task savedTask1 = taskManager.getTask(idTask1);
        savedTask1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTask(savedTask1.getId());
                    }
                });

        assertEquals("Задачи нет в списке", exception.getMessage());
    }

    // получить эпик, используя несуществующий ID
    @Test
    void shouldReturnNullWhenGettingEpicWithInvalidId() {
        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        Epic savedEpic1 = taskManager.getEpic(idEpic1);
        savedEpic1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(savedEpic1.getId());
                    }
                });

        assertEquals("Эпика нет в списке", exception.getMessage());
    }

    // получить подзадачу, используя несуществующий ID
    @Test
    void shouldReturnNullWhenGettingSubtaskWithInvalidId() {
        createEpic1();
        createSubtask1();
        taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);

        Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        savedSubtask1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtask(savedSubtask1.getId());
                    }
                });

        assertEquals("Подзадачи нет в списке", exception.getMessage());
    }

    // получить все подзадачи эпика, используя несуществующий ID
    void shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId() {
        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        Epic savedEpic1 = taskManager.getEpic(idEpic1);
        savedEpic1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtasksInEpic(savedEpic1.getId());
                    }
                });

        assertEquals("Эпика нет в списке", exception.getMessage());
    }

    // УДАЛИТЬ

    // удалить задачу, используя несуществующий ID
    @Test
    void shouldReturnNullWhenDeletingTaskWithInvalidId() {
        createTask1();
        final int idTask1 = taskManager.add(task1);
        Task savedTask1 = taskManager.getTask(idTask1);
        savedTask1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.delete(savedTask1);
                    }
                });

        assertEquals("Задачи нет в списке", exception.getMessage());
    }

    // удалить эпик, используя несуществующий ID
    @Test
    void shouldReturnNullWhenDeletingEpicWithInvalidId() {
        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        Epic savedEpic1 = taskManager.getEpic(idEpic1);
        savedEpic1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.delete(savedEpic1);
                    }
                });

        assertEquals("Эпика нет в списке", exception.getMessage());
    }

    // удалить подзадачу, используя несуществующий ID
    @Test
    void shouldReturnNullWhenDeletingSubtaskWithInvalidId() {
        createEpic1();
        createSubtask1();
        taskManager.add(epic1);
        final int idSubtask1 = taskManager.add(subtask1);

        Subtask savedSubtask1 = taskManager.getSubtask(idSubtask1);
        savedSubtask1.setId(0);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.delete(savedSubtask1);
                    }
                });

        assertEquals("Подзадачи нет в списке", exception.getMessage());
    }
}