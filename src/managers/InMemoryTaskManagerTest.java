package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {

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

    @Test @Override
    public void shouldCalculateEpicTimingFromSubtasksWithTiming() {
        super.shouldCalculateEpicTimingFromSubtasksWithTiming();
    }

    @Test @Override
    void shouldUpdateEpicTimingIfSubtaskTimeUpdated() {
        super.shouldUpdateEpicTimingIfSubtaskTimeUpdated();
    }

    @Test @Override
    void shouldAddTasksWithoutTimingAsNonPriority() {
        super.shouldAddTasksWithoutTimingAsNonPriority();
    }

    @Test @Override
    void shouldPreventOverlapIfTask1EndsAfterTask2Starts() {
        super.shouldPreventOverlapIfTask1EndsAfterTask2Starts();
    }

    @Test @Override
    void shouldPreventOverlapIfTask1StartsBeforeTask2Ends() {
        super.shouldPreventOverlapIfTask1StartsBeforeTask2Ends();
    }

    @Test @Override
    void shouldPreventOverlapIfTasksStartAtOneTime() {
        super.shouldPreventOverlapIfTasksStartAtOneTime();
    }

    @Test @Override
    void shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress() {
        super.shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress();
    }

    @Test @Override
    void shouldPreventOverlapOnUpdate() {
        super.shouldPreventOverlapOnUpdate();
    }

    // ---------------------------------------------
    // ТЕСТЫ 2 — Пустой список задач
    // ---------------------------------------------

    @Test @Override
    void shouldReturnNullWhenUpdatingTaskInEmptyList() {
        super.shouldReturnNullWhenUpdatingTaskInEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenUpdatingEpicInEmptyList() {
        super.shouldReturnNullWhenUpdatingEpicInEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskInEmptyList() {
        super.shouldReturnNullWhenUpdatingSubtaskInEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenGettingTaskFromEmptyList() {
        super.shouldReturnNullWhenGettingTaskFromEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenGettingEpicFromEmptyList() {
        super.shouldReturnNullWhenGettingEpicFromEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenGettingSubtaskFromEmptyList() {
        super.shouldReturnNullWhenGettingSubtaskFromEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenGettingAllTasksIfListIsEmpty() {
        super.shouldReturnNullWhenGettingAllTasksIfListIsEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenGettingAllEpicsIfListIsEmpty() {
        super.shouldReturnNullWhenGettingAllEpicsIfListIsEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty() {
        super.shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty() {
        super.shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty() {
        super.shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingTaskFromEmptyList() {
        super.shouldReturnNullWhenDeletingTaskFromEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingEpicFromEmptyList() {
        super.shouldReturnNullWhenDeletingEpicFromEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskFromEmptyList() {
        super.shouldReturnNullWhenDeletingSubtaskFromEmptyList();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingAllTasksIfListIsEmpty() {
        super.shouldReturnNullWhenDeletingAllTasksIfListIsEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingAllEpicIfListIsEmpty() {
        super.shouldReturnNullWhenDeletingAllEpicIfListIsEmpty();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingAllSubtasksIfListIsEmpty() {
        super.shouldReturnNullWhenDeletingAllSubtasksIfListIsEmpty();
    }

    @Test @Override
    void shouldReturnEmptyListIfHistoryIsEmpty() {
        super.shouldReturnEmptyListIfHistoryIsEmpty();
    }

    @Test @Override
    void shouldReturnEmptyListIfPriorityTaskListIsEmpty() {
        super.shouldReturnEmptyListIfPriorityTaskListIsEmpty();
    }

    // ---------------------------------------------
    // ТЕСТЫ 3 — Несуществующий идентификатор задачи
    // ---------------------------------------------

    @Test @Override
    void shouldReturnNullWhenUpdatingTaskWithInvalidId() {
        super.shouldReturnNullWhenUpdatingTaskWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenUpdatingEpicWithInvalidId() {
        super.shouldReturnNullWhenUpdatingEpicWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskWithInvalidId() {
        super.shouldReturnNullWhenUpdatingSubtaskWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenGettingTaskWithInvalidId() {
        super.shouldReturnNullWhenGettingTaskWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenGettingEpicWithInvalidId() {
        super.shouldReturnNullWhenGettingEpicWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenGettingSubtaskWithInvalidId() {
        super.shouldReturnNullWhenGettingSubtaskWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId() {
        super.shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingTaskWithInvalidId() {
        super.shouldReturnNullWhenDeletingTaskWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingEpicWithInvalidId() {
        super.shouldReturnNullWhenDeletingEpicWithInvalidId();
    }

    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskWithInvalidId() {
        super.shouldReturnNullWhenDeletingSubtaskWithInvalidId();
    }

}