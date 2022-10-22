package tests;

import com.google.gson.Gson;
import http.KVServer;
import http.KVTaskClient;
import managers.HTTPTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    private final HTTPTaskManager httpTaskManager;
    protected static String serverUri = "http://localhost:8078/";
    protected KVServer kvServer;


    protected HTTPTaskManagerTest() {
        super(new HTTPTaskManager(serverUri));
        httpTaskManager = (HTTPTaskManager) taskManager;
    }

    public void initializeTest() {
        new HTTPTaskManagerTest();
    }

    @BeforeEach
    public void startKVServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить KVServer");
        }
    }

    @AfterEach
    public void stopKVServer() {
        kvServer.stop();
    }

    @AfterEach
    public void stopHttpTaskServer() {
        httpTaskManager.stopHttpTaskServer();
    }

    // ---------------------------------------------
    // ТЕСТЫ — Сервер
    // ---------------------------------------------

    // загрузить задачу с сервера
    @Test
    public void loadTaskFromServer() {
        int id = 1;
        KVTaskClient taskClient = new KVTaskClient(serverUri);
        Gson gson = new Gson();

        createTask1();
        task1.setId(id);
        task1.setType(TaskType.TASK);
        List<Task> taskAsList = new ArrayList<>(List.of(task1));
        String json = gson.toJson(taskAsList);
        taskClient.put("tasks", json);

        httpTaskManager.load(serverUri);
        Task uploadedTask = taskManager.getTask(id);
        assertEquals(task1, uploadedTask, "Задача восстановлена неверно");
    }

    // сохранить задачу на сервере
    @Test
    public void saveTaskOnServer() {
        initializeTest();
        createTask1();
        int id = taskManager.add(task1);
        Task taskInManager = taskManager.getTask(id);

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        Task taskFromServer = newTaskManager.getTask(id);
        List<Task> tasksOnServer = newTaskManager.getTasks();

        assertEquals(1, tasksOnServer.size(), "Неверное количество задач на сервере");
        assertEquals(taskInManager, taskFromServer, "Задача сохранилась неверно");
    }


    /* ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
       ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

      Тесты TaskManagerTest

       ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
       ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~  */

    // ---------------------------------------------
    // ТЕСТЫ 1 — Стандартное поведение
    // ---------------------------------------------

    // ДОБАВИТЬ

    // добавить задачу
    @Test
    public void addTask() {
        initializeTest();

        super.addTask();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Task> tasks = newTaskManager.getTasks();
        Task task1Uploaded = tasks.get(0);

        // задача task1 создается в методе super.addTask()
        assertEquals(task1, task1Uploaded, "Задачи не совпадают");
    }

    // добавить эпик без подзадач
    @Test
    public void addEpicWithOutSubtasks() {
        initializeTest();
        super.addEpicWithOutSubtasks();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Epic> epics = newTaskManager.getEpics();
        Epic epic1Uploaded = epics.get(0);

        // эпик epic1 создается в родительском методе
        assertEquals(epic1, epic1Uploaded, "Эпики не совпадают");
    }

    @Test
    public void addEpicWithSubtasks() {
        initializeTest();
        super.addEpicWithSubtasks();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Epic> epics = newTaskManager.getEpics();
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        Epic epic1Uploaded = epics.get(0);
        Subtask subtask1Uploaded = subtasks.get(0);
        Subtask subtask2Uploaded = subtasks.get(1);

        // задачи epic1, subtask1 и subtask2 создаются в родительском методе
        assertEquals(epic1, epic1Uploaded, "Эпики не совпадают");
        assertEquals(subtask1, subtask1Uploaded, "Подзадачи не совпадают");
        assertEquals(subtask2, subtask2Uploaded, "Подзадачи не совпадают");
    }

    // ОБНОВИТЬ

    // обновить задачу
    @Test @Override
    public void updateTask() {
        initializeTest();
        super.updateTask();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Task> tasks = newTaskManager.getTasks();
        Task task1Uploaded = tasks.get(0);

        // задача task1 обновляется в родительском методе
        assertEquals(task1, task1Uploaded, "Задачи не совпадают");
    }

    // обновить эпик
    @Test @Override
    public void updateEpic() {
        initializeTest();
        super.updateEpic();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Epic> epics = newTaskManager.getEpics();
        Epic epic1Uploaded = epics.get(0);

        // epic1 обновляется в родительском методе
        assertEquals(epic1, epic1Uploaded, "Эпики не совпадают");
    }

    @Test @Override
    public void updateSubtask() {
        initializeTest();
        super.updateSubtask();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        List<Epic> epics = newTaskManager.getEpics();
        Subtask subtask1Uploaded = subtasks.get(0);
        Epic epic1Uploaded = epics.get(0);

        // subtask1 обновляется в родительском методе
        assertEquals(subtask1, subtask1Uploaded, "Подзадачи не совпадают");
        assertEquals(epic1.getStatus(), epic1Uploaded.getStatus(), "Статус эпиков не совпадает");
    }

    // УДАЛИТЬ

    @Test @Override
    public void deleteTask() {
        initializeTest();
        super.deleteTask();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Task> tasks = newTaskManager.getTasks();
        List<Task> history = newTaskManager.getHistory();
        assertEquals(0, tasks.size());
        assertEquals(0, history.size());
    }

    @Test @Override
    public void deleteEpic() {
        initializeTest();
        super.deleteEpic();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Epic> epics = newTaskManager.getEpics();
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        List<Task> history = newTaskManager.getHistory();
        assertEquals(0, epics.size());
        assertEquals(0, subtasks.size());
        assertEquals(0, history.size());
    }

    @Test @Override
    public void deleteSubtask() {
        initializeTest();
        super.deleteSubtask();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Subtask> epicSubtasks = newTaskManager.getSubtasksInEpic(epic1.getId());
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        List<Task> history = taskManager.getHistory();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(1, epicSubtasks.size(), "Неверное количество подзадач у эпика");
        assertEquals(epicSubtasks, subtasks, "Списки подзадач не совпадают");
        assertEquals(2, history.size(), "Неверное количество задач в истории");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество приоритетных задач");
    }

    // удалить все задачи
    @Test @Override
    public void deleteAllTasks() {
        initializeTest();
        super.deleteAllTasks();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Task> tasks = newTaskManager.getTasks();
        List<Task> history = newTaskManager.getHistory();
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(0, tasks.size(), "Список задач не пустой");
        assertEquals(0, history.size(), "Остались лишние задачи в истории");
        assertEquals(0, prioritizedTasks.size(), "Неверное количество приоритетных задач");
    }

    @Test @Override
    public void deleteAllEpics() {
        initializeTest();
        super.deleteAllEpics();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Epic> epics = newTaskManager.getEpics();
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        List<Task> history = newTaskManager.getHistory();
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(0, epics.size(), "Список эпиков не пустой");
        assertEquals(0, subtasks.size(), "Список подзадач не пустой");
        assertEquals(0, history.size(), "Остались лишние задачи в истории");
        assertEquals(0, prioritizedTasks.size(), "Неверное количество приоритетных задач");
    }

    @Test @Override
    public void deleteAllSubtasks() {
        initializeTest();
        super.deleteAllSubtasks();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        List<Epic> epics = newTaskManager.getEpics();
        List<Task> history = newTaskManager.getHistory();
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(0, subtasks.size(), "Список подзадач не пустой");
        assertEquals(2, history.size(), "Остались лишние задачи в истории");

        assertEquals(2, epics.size(), "Количество эпиков не соответствует ожидаемому");
        assertEquals(Status.NEW, epics.get(0).getStatus(), "Неверный статус эпика");
        assertEquals(Status.NEW, epics.get(1).getStatus(), "Неверный статус эпика");
        assertEquals(0, prioritizedTasks.size(), "Неверное количество приоритетных задач");
    }

    // ДАТА, ВРЕМЯ, ПРОДОЛЖИТЕЛЬНОСТЬ

    // рассчитать время эпика по подзадачам, где указано время
    @Test @Override
    public void shouldCalculateEpicTimingFromSubtasksWithTiming() {
        initializeTest();
        super.shouldCalculateEpicTimingFromSubtasksWithTiming();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);
        List<Epic> epics = newTaskManager.getEpics();
        List<Subtask> subtasks = newTaskManager.getSubtasks();
        Epic savedEpic1 = epics.get(0);
        Subtask savedSubtask1 = subtasks.get(0);
        Subtask savedSubtask2 = subtasks.get(1);

        if (savedEpic1.getEndTime().isEmpty()) fail("Не удалось получить время завершения эпика");
        if (savedSubtask2.getEndTime().isEmpty()) fail("Не удалось получить время завершения подзадачи");

        Duration expectedEpicDuration = Duration.between(savedSubtask1.getStartTime(),
                                                         savedSubtask2.getEndTime().get());

        assertTrue(savedSubtask1.getIsEpicStartTime(),
                "Подзадача должна быть начальной для эпика");
        assertTrue(savedSubtask2.getIsEpicEndTime(),
                "Подзадача должна быть завершающей для эпика");
        assertEquals(savedSubtask1.getStartTime(), savedEpic1.getStartTime(),
                "Неверное время начала эпика");
        assertEquals(savedSubtask2.getEndTime().get(), savedEpic1.getEndTime().get(),
                "Неверное время завершения эпика");
        assertEquals(expectedEpicDuration, savedEpic1.getDuration(),
                "Неверная продолжительность эпика");

    }

    // время эпика меняется при изменении времени подзадачи
    @Test @Override
    void shouldUpdateEpicTimingIfSubtaskTimeUpdated() {
        initializeTest();
        super.shouldUpdateEpicTimingIfSubtaskTimeUpdated();
    }

    // задачи без времени должны добавляться в конец списка по приоритету
    @Test @Override
    void shouldAddTasksWithoutTimingAsNonPriority() {
        initializeTest();
        super.shouldAddTasksWithoutTimingAsNonPriority();
    }

    // ПЕРЕСЕЧЕНИЕ ЗАДАЧ

    // пересечение задач: новая задача начинается до того, как завершилась первая
    @Test @Override
    void shouldPreventOverlapIfTask1EndsAfterTask2Starts() {
        initializeTest();
        super.shouldPreventOverlapIfTask1EndsAfterTask2Starts();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);

        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(expectedPrioritizedTasks, prioritizedTasks,"Неверный порядок сохранения задач");
    }

    // пересечение задач: новая задача завершается после того, как началась первая
    @Test @Override
    void shouldPreventOverlapIfTask1StartsBeforeTask2Ends() {
        initializeTest();
        super.shouldPreventOverlapIfTask1StartsBeforeTask2Ends();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);

        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Неверный порядок сохранения задач");
    }

    // пересечение задач: новая задача начинается одновременно с первой
    @Test @Override
    void shouldPreventOverlapIfTasksStartAtOneTime() {
        initializeTest();
        super.shouldPreventOverlapIfTasksStartAtOneTime();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);

        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Неверный порядок сохранения задач");
    }

    // пересечение задач: новая задача начинается и завершается, пока идет первая
    @Test @Override
    void shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress() {
        initializeTest();
        super.shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress();

        HTTPTaskManager newTaskManager = new HTTPTaskManager(serverUri);

        List<Task> expectedPrioritizedTasks = new ArrayList<>(List.of(task1, task2));
        List<Task> prioritizedTasks = newTaskManager.getPrioritizedTasks();

        assertEquals(expectedPrioritizedTasks, prioritizedTasks, "Неверный порядок сохранения задач");
    }

    // пересечение задач при обновлении одной из них — в случае пересечения сохраняется старое время начала
    @Test @Override
    void shouldPreventOverlapOnUpdate() {
        initializeTest();
        super.shouldPreventOverlapOnUpdate();
    }


    // ---------------------------------------------
    // ТЕСТЫ 2 — Пустой список задач
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingTaskInEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenUpdatingTaskInEmptyList();
    }

    // обновить эпик в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingEpicInEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenUpdatingEpicInEmptyList();
    }

    // обновить подзадачу в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskInEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenUpdatingSubtaskInEmptyList();
    }

    // ПОЛУЧИТЬ

    // получить задачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingTaskFromEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenGettingTaskFromEmptyList();
    }

    // получить эпик из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingEpicFromEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenGettingEpicFromEmptyList();
    }

    // получить подзадачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingSubtaskFromEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenGettingSubtaskFromEmptyList();
    }

    // получить все задачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllTasksIfListIsEmpty() {
        initializeTest();
        super.shouldReturnNullWhenGettingAllTasksIfListIsEmpty();
    }

    // получить все эпики из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllEpicsIfListIsEmpty() {
        initializeTest();
        super.shouldReturnNullWhenGettingAllEpicsIfListIsEmpty();
    }

    // получить все подзадачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty() {
        initializeTest();
        super.shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty();
    }

    // получить все подзадачи эпика, если пусты списки эпиков и подзадач
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty() {
        initializeTest();
        super.shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty();
    }

    // получить все подзадачи эпика, если пуст список подзадач в менеджере
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty() {
        initializeTest();
        super.shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty();
    }

    // УДАЛИТЬ

    // удалить задачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingTaskFromEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenDeletingTaskFromEmptyList();
    }

    // удалить эпик из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingEpicFromEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenDeletingEpicFromEmptyList();
    }

    // удалить подзадачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskFromEmptyList() {
        initializeTest();
        super.shouldReturnNullWhenDeletingSubtaskFromEmptyList();
    }

    // удалить все задачи из пустого списка
    @Test @Override
    void shouldDoNothingWhenDeletingAllTasksIfListIsEmpty() {
        initializeTest();
        super.shouldDoNothingWhenDeletingAllTasksIfListIsEmpty();
    }

    // удалить все эпики из пустого списка
    @Test @Override
    void shouldDoNothingWhenDeletingAllEpicIfListIsEmpty() {
        initializeTest();
        super.shouldDoNothingWhenDeletingAllEpicIfListIsEmpty();
    }

    // удалить все подзадачи из пустого списка
    @Test @Override
    void shouldDoNothingWhenDeletingAllSubtasksIfListIsEmpty() {
        initializeTest();
        super.shouldDoNothingWhenDeletingAllSubtasksIfListIsEmpty();
    }

    // ДОПОЛНИТЕЛЬНО

    // получить задачи из пустого списка истории
    @Test @Override
    void shouldReturnEmptyListIfHistoryIsEmpty() {
        initializeTest();
        super.shouldReturnEmptyListIfHistoryIsEmpty();
    }

    // получить задачи из пустого списка приоритетных задач
    @Test @Override
    void shouldReturnEmptyListIfPriorityTaskListIsEmpty() {
        initializeTest();
        super.shouldReturnEmptyListIfPriorityTaskListIsEmpty();
    }


    // ---------------------------------------------
    // ТЕСТЫ 3 — Несуществующий идентификатор задачи
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingTaskWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenUpdatingTaskWithInvalidId();
    }

    // обновить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingEpicWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenUpdatingEpicWithInvalidId();
    }

    // обновить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenUpdatingSubtaskWithInvalidId();
    }

    // ПОЛУЧИТЬ

    // получить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingTaskWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenGettingTaskWithInvalidId();
    }

    // получить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingEpicWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenGettingEpicWithInvalidId();
    }

    // получить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingSubtaskWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenGettingSubtaskWithInvalidId();
    }

    // получить все подзадачи эпика, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId() {
        initializeTest();
        super.shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId();
    }

    // УДАЛИТЬ

    // удалить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingTaskWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenDeletingTaskWithInvalidId();
    }

    // удалить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingEpicWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenDeletingEpicWithInvalidId();
    }

    // удалить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskWithInvalidId() {
        initializeTest();
        super.shouldReturnNullWhenDeletingSubtaskWithInvalidId();
    }

}