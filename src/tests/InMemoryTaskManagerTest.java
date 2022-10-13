package tests;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    protected InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @BeforeEach
    public void create() {
        new InMemoryTaskManagerTest();
    }


    // ---------------------------------------------
    // ТЕСТЫ 1 — Стандартное поведение
    // ---------------------------------------------

    // ДОБАВИТЬ

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

    // ОБНОВИТЬ

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

    // УДАЛИТЬ

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

    // ДАТА, ВРЕМЯ, ПРОДОЛЖИТЕЛЬНОСТЬ

    // рассчитать время эпика по подзадачам, где указано время
    @Test @Override
    public void shouldCalculateEpicTimingFromSubtasksWithTiming() {
        super.shouldCalculateEpicTimingFromSubtasksWithTiming();
    }

    // время эпика меняется при изменении времени подзадачи
    @Test @Override
    void shouldUpdateEpicTimingIfSubtaskTimeUpdated() {
        super.shouldUpdateEpicTimingIfSubtaskTimeUpdated();
    }

    // задачи без времени должны добавляться в конец списка по приоритету
    @Test @Override
    void shouldAddTasksWithoutTimingAsNonPriority() {
        super.shouldAddTasksWithoutTimingAsNonPriority();
    }

    // ПЕРЕСЕЧЕНИЕ ЗАДАЧ

    // пересечение задач: новая задача начинается до того, как завершилась первая
    @Test @Override
    void shouldPreventOverlapIfTask1EndsAfterTask2Starts() {
        super.shouldPreventOverlapIfTask1EndsAfterTask2Starts();
    }

    // пересечение задач: новая задача завершается после того, как началась первая
    @Test @Override
    void shouldPreventOverlapIfTask1StartsBeforeTask2Ends() {
        super.shouldPreventOverlapIfTask1StartsBeforeTask2Ends();
    }

    // пересечение задач: новая задача начинается одновременно с первой
    @Test @Override
    void shouldPreventOverlapIfTasksStartAtOneTime() {
        super.shouldPreventOverlapIfTasksStartAtOneTime();
    }

    // пересечение задач: новая задача начинается и завершается, пока идет первая
    @Test @Override
    void shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress() {
        super.shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress();
    }

    // пересечение задач при обновлении одной из них — в случае пересечения сохраняется старое время начала
    @Test @Override
    void shouldPreventOverlapOnUpdate() {
        super.shouldPreventOverlapOnUpdate();
    }


    // ---------------------------------------------
    // ТЕСТЫ 2 — Пустой список задач
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingTaskInEmptyList() {
        super.shouldReturnNullWhenUpdatingTaskInEmptyList();
    }

    // обновить эпик в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingEpicInEmptyList() {
        super.shouldReturnNullWhenUpdatingEpicInEmptyList();
    }

    // обновить подзадачу в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskInEmptyList() {
        super.shouldReturnNullWhenUpdatingSubtaskInEmptyList();
    }

    // ПОЛУЧИТЬ

    // получить задачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingTaskFromEmptyList() {
        super.shouldReturnNullWhenGettingTaskFromEmptyList();
    }

    // получить эпик из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingEpicFromEmptyList() {
        super.shouldReturnNullWhenGettingEpicFromEmptyList();
    }

    // получить подзадачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingSubtaskFromEmptyList() {
        super.shouldReturnNullWhenGettingSubtaskFromEmptyList();
    }

    // получить все задачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllTasksIfListIsEmpty() {
        super.shouldReturnNullWhenGettingAllTasksIfListIsEmpty();
    }

    // получить все эпики из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllEpicsIfListIsEmpty() {
        super.shouldReturnNullWhenGettingAllEpicsIfListIsEmpty();
    }

    // получить все подзадачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty() {
        super.shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty();
    }

    // получить все подзадачи эпика, если пусты списки эпиков и подзадач
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty() {
        super.shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty();
    }

    // получить все подзадачи эпика, если пуст список подзадач в менеджере
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty() {
        super.shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty();
    }

    // УДАЛИТЬ

    // удалить задачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingTaskFromEmptyList() {
        super.shouldReturnNullWhenDeletingTaskFromEmptyList();
    }

    // удалить эпик из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingEpicFromEmptyList() {
        super.shouldReturnNullWhenDeletingEpicFromEmptyList();
    }

    // удалить подзадачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskFromEmptyList() {
        super.shouldReturnNullWhenDeletingSubtaskFromEmptyList();
    }

    // удалить все задачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingAllTasksIfListIsEmpty() {
        super.shouldReturnNullWhenDeletingAllTasksIfListIsEmpty();
    }

    // удалить все эпики из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingAllEpicIfListIsEmpty() {
        super.shouldReturnNullWhenDeletingAllEpicIfListIsEmpty();
    }

    // удалить все подзадачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingAllSubtasksIfListIsEmpty() {
        super.shouldReturnNullWhenDeletingAllSubtasksIfListIsEmpty();
    }

    // ДОПОЛНИТЕЛЬНО

    // получить задачи из пустого списка истории
    @Test @Override
    void shouldReturnEmptyListIfHistoryIsEmpty() {
        super.shouldReturnEmptyListIfHistoryIsEmpty();
    }

    // получить задачи из пустого списка приоритетных задач
    @Test @Override
    void shouldReturnEmptyListIfPriorityTaskListIsEmpty() {
        super.shouldReturnEmptyListIfPriorityTaskListIsEmpty();
    }


    // ---------------------------------------------
    // ТЕСТЫ 3 — Несуществующий идентификатор задачи
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingTaskWithInvalidId() {
        super.shouldReturnNullWhenUpdatingTaskWithInvalidId();
    }

    // обновить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingEpicWithInvalidId() {
        super.shouldReturnNullWhenUpdatingEpicWithInvalidId();
    }

    // обновить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskWithInvalidId() {
        super.shouldReturnNullWhenUpdatingSubtaskWithInvalidId();
    }

    // ПОЛУЧИТЬ

    // получить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingTaskWithInvalidId() {
        super.shouldReturnNullWhenGettingTaskWithInvalidId();
    }

    // получить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingEpicWithInvalidId() {
        super.shouldReturnNullWhenGettingEpicWithInvalidId();
    }

    // получить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingSubtaskWithInvalidId() {
        super.shouldReturnNullWhenGettingSubtaskWithInvalidId();
    }

    // получить все подзадачи эпика, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId() {
        super.shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId();
    }

    // УДАЛИТЬ

    // удалить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingTaskWithInvalidId() {
        super.shouldReturnNullWhenDeletingTaskWithInvalidId();
    }

    // удалить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingEpicWithInvalidId() {
        super.shouldReturnNullWhenDeletingEpicWithInvalidId();
    }

    // удалить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskWithInvalidId() {
        super.shouldReturnNullWhenDeletingSubtaskWithInvalidId();
    }

}