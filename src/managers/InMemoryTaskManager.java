package managers;

import tasks.*;
import utilities.TaskPriorityComparator;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Integer nextId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(new TaskPriorityComparator());


    // ДОБАВИТЬ

    // добавить задачу
    @Override
    public int add(Task task) {
        int id = nextId++;
        task.setId(id);
        task.setType(TaskType.TASK);
        checkOverlap(task);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return id;
    }

    // добавить эпик
    @Override
    public int add(Epic epic) {
        int id = nextId++;
        epic.setId(id);
        epic.setType(TaskType.EPIC);
        checkEpicStatus(epic);
        epics.put(epic.getId(), epic);
        return id;
    }

    // добавить подзадачу
    @Override
    public int add(Subtask subtask) {
        int id = nextId++;
        subtask.setId(id);
        subtask.setType(TaskType.SUBTASK);
        checkOverlap(subtask);
        setEpicSubtaskRelation(subtask);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        Epic relatedEpic = epics.get(subtask.getEpicId());
        checkEpicStatus(relatedEpic);
        return id;
    }

    // ОБНОВИТЬ

    // обновить задачу
    @Override
    public void update(Task task) {
        checkTaskInMap(task.getId());
        tasks.put(task.getId(), task);
        checkOverlap(task);
        updatePrioritizedTask(task);
    }

    // обновить эпик
    @Override
    public void update(Epic epic) {
        checkEpicInMap(epic.getId());
        epics.put(epic.getId(), epic);
    }

    // обновить подзадачу
    @Override
    public void update(Subtask subtask) {
        checkSubtaskInMap(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        Epic relatedEpic = epics.get(subtask.getEpicId());
        checkEpicStatus(relatedEpic);
        checkOverlap(subtask);
        updateEpicTiming(relatedEpic, subtask);
        updatePrioritizedTask(subtask);
    }

    // ПОЛУЧИТЬ ЗАДАЧУ

    // получить задачу
    @Override
    public Task getTask(int id) {
        checkTaskInMap(id);
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    // получить эпик
    @Override
    public Epic getEpic(int id) {
        checkEpicInMap(id);
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    // получить подзадачу
    @Override
    public Subtask getSubtask(int id) {
        checkSubtaskInMap(id);
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    // ПОЛУЧИТЬ СПИСОК ЗАДАЧ

    // получить список всех задач
    @Override
    public List<Task> getTasks() {
        checkIfTaskMapIsEmpty();
        return new ArrayList<>(tasks.values());
    }

    // получить список всех эпиков
    @Override
    public List<Epic> getEpics() {
        checkIfEpicMapIsEmpty();
        return new ArrayList<>(epics.values());
    }

    // получить список всех подзадач
    @Override
    public List<Subtask> getSubtasks() {
        checkIfSubtaskMapIsEmpty();
        return new ArrayList<>(subtasks.values());
    }

    // получить список всех подзадач одного эпика
    @Override
    public List<Subtask> getSubtasksInEpic(int id) {
        checkEpicInMap(id);
        checkIfSubtaskMapIsEmpty();
        Epic epic = this.getEpic(id);
        ArrayList<Subtask> subtasksList = new ArrayList<>();

        for (Integer subtaskId : epic.getRelatedSubtasks()) {
            Subtask subtask = getSubtask(subtaskId);
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    // УДАЛИТЬ

    // удалить задачу
    @Override
    public void delete(Task task) {
        checkTaskInMap(task.getId());
        prioritizedTasks.remove(task);
        historyManager.remove(task.getId());
        tasks.remove(task.getId());
    }

    // удалить эпик
    @Override
    public void delete(Epic epic) {
        checkEpicInMap(epic.getId());
        for (Integer subtaskId : epic.getRelatedSubtasks()){
            prioritizedTasks.remove(subtasks.get(subtaskId));
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
        historyManager.remove(epic.getId());
        epics.remove(epic.getId());
    }

    // удалить подзадачу
    @Override
    public void delete(Subtask subtask) {
        checkSubtaskInMap(subtask.getId());
        Epic relatedEpic = epics.get(subtask.getEpicId());
        prioritizedTasks.remove(subtask);
        relatedEpic.removeRelatedSubtask(subtask.getId());
        resetEpicStartEndTime(relatedEpic);
        checkEpicStatus(relatedEpic);
        historyManager.remove(subtask.getId());
        subtasks.remove(subtask.getId());
    }


    // УДАЛИТЬ ВСЕ ЗАДАЧИ ОДНОГО ТИПА

    // удалить все задачи
    @Override
    public void deleteAllTasks() {
        checkIfTaskMapIsEmpty();
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }

    // удалить все эпики
    @Override
    public void deleteAllEpics() {
        checkIfEpicMapIsEmpty();
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
        }
        subtasks.clear();
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    // удалить все подзадачи
    @Override
    public void deleteAllSubtasks() {
        checkIfSubtaskMapIsEmpty();
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
        }
        for (int id : epics.keySet()) {
            Epic epic = epics.get(id);
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
            epic.removeAllRelatedSubtasks();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ

    // Показать историю просмотров

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Получить список задач в порядке приоритета
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    // проверить счетчик ID при восстановлении задач из резервной копии
    protected void checkNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    // для эпика — проверить статус
    protected void checkEpicStatus(Epic epic) {
        int statusDone = 0;
        int statusNew = 0;

        if (epic.getRelatedSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer subtaskId : epic.getRelatedSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (Status.NEW.equals(subtask.getStatus())) {
                statusNew += 1;
            } else if (Status.DONE.equals(subtask.getStatus())) {
                statusDone += 1;
            }
        }

        if (statusNew == epic.getRelatedSubtasks().size()) {
            epic.setStatus(Status.NEW);
        } else if (statusDone == epic.getRelatedSubtasks().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    // для эпика — связать подзадачу с эпиком
    protected void setEpicSubtaskRelation(Subtask subtask) {
        if (subtask.getEpicId() != 0) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.addRelatedSubtask(subtask.getId());
            return;
        }
        for (Integer epicId : epics.keySet()) {
            Epic epic = epics.get(epicId);
            if (epic.getTitle().equals(subtask.getEpicTitle())) {
                subtask.setEpicId(epicId);
                if (!epic.getRelatedSubtasks().contains(subtask.getId())) {
                    epic.addRelatedSubtask(subtask.getId());
                    setEpicTiming(epic, subtask);
                }
            }
        }
    }

    // для эпика — рассчитать время начала, завершения и продолжительность
    protected void setEpicTiming(Epic epic, Subtask subtask) {
        if (subtask.getStartTime() == null) {
            return;
        }

        // время начала
        if (epic.getStartTime() == null) {
            epic.setStartTime(subtask.getStartTime());
            epic.setStartTimeSubtask(subtask.getId());
            subtask.setEpicStartTime(true);
        } else if (subtask.getStartTime().isBefore(epic.getStartTime())) {
            Subtask oldStartTimeSubtask = subtasks.get(epic.getStartTimeSubtask());
            oldStartTimeSubtask.setEpicStartTime(false);
            epic.setStartTime(subtask.getStartTime());
            epic.setStartTimeSubtask(subtask.getId());
            subtask.setEpicStartTime(true);
        }

        // время завершения
        if (epic.getEndTime().isEmpty()) {
            epic.setEndTime(subtask.getEndTime().get());
            epic.setEndTimeSubtask(subtask.getId());
            subtask.setEpicEndTime(true);
        } else if (subtask.getEndTime().get().isAfter(epic.getEndTime().get())) {
            Subtask priorEndTimeSubtask = subtasks.get(epic.getEndTimeSubtask());
            priorEndTimeSubtask.setEpicEndTime(false);
            epic.setEndTime(subtask.getEndTime().get());
            epic.setEndTimeSubtask(subtask.getId());
            subtask.setEpicEndTime(true);
        }

        // продолжительность
        epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime().get()));
    }

    // для эпика — обновить время начала, завершения и продолжительность
    protected void updateEpicTiming(Epic epic, Subtask subtask) {
        if (subtask.getStartTime() == null) {
            return;
        } else if (subtask.isEpicStartTime() && subtask.getStartTime().isBefore(epic.getStartTime())) {
            epic.setStartTime(subtask.getStartTime());
        } else if (subtask.isEpicEndTime() && subtask.getEndTime().get().isAfter(epic.getEndTime().get())) {
            epic.setEndTime(subtask.getEndTime().get());
        } else {
            resetEpicStartEndTime(epic);
        }
    }

    // для эпика — переопределить первую и последнюю подзадачи, если меняется порядок
    protected void resetEpicStartEndTime(Epic epic) {
        if (epic.getStartTime() == null || epic.getEndTime().isEmpty()) {
            return;
        }

        List<Subtask> epicSubtasks = new ArrayList<>();
        Subtask oldStartTimeSubtask = subtasks.get(epic.getStartTimeSubtask());
        Subtask oldEndTimeSubtask = subtasks.get(epic.getEndTimeSubtask());

        oldStartTimeSubtask.setEpicStartTime(false);
        oldEndTimeSubtask.setEpicEndTime(false);

        for (int subtaskId : epic.getRelatedSubtasks()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        epicSubtasks.sort(new TaskPriorityComparator());

        Subtask newStartTimeSubtask = epicSubtasks.get(0);
        Subtask newEndTimeSubtask = epicSubtasks.get(epicSubtasks.size() - 1);

        int i = epicSubtasks.size() - 1;
        while (i >= 0 && newEndTimeSubtask.getStartTime() == null) {
            newEndTimeSubtask = epicSubtasks.get(i - 1);
            i--;
        }

        newStartTimeSubtask.setEpicStartTime(true);
        newEndTimeSubtask.setEpicEndTime(true);
        epic.setStartTimeSubtask(newStartTimeSubtask.getId());
        epic.setEndTimeSubtask(newEndTimeSubtask.getId());
        epic.setStartTime(newStartTimeSubtask.getStartTime());
        epic.setEndTime(newEndTimeSubtask.getEndTime().get());
        epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime().get()));
    }

    // проверить задачи на пересечения
    protected void checkOverlap(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        boolean noOverlap = true;

        for (Task taskToCheck : prioritizedTasks) {
            if (isOverlap(task, taskToCheck)) {
                System.out.println("Не удалось сохранить время для задачи '" + task.getTitle() +
                                    "' — пересечение по времени с задачей '" + taskToCheck.getTitle() + "'" +
                                    ". Выберите другое время." +
                                    "\n\n" + task.getTitle() +
                                    "\nНачало: " + task.getStartTime().format(Task.DATE_TIME_FORMATTER) +
                                    "\nЗавершение: " + task.getEndTime().get().format(Task.DATE_TIME_FORMATTER) +
                                    "\n" + taskToCheck.getTitle() +
                                    "\nНачало: " + taskToCheck.getStartTime().format(Task.DATE_TIME_FORMATTER) +
                                    "\nЗавершение: " + taskToCheck.getEndTime().get()
                                                            .format(Task.DATE_TIME_FORMATTER) + "\n"
                );
                task.setStartTime(task.getBackupStartTime());
                task.setDuration(task.getBackupDuration());
                noOverlap = false;
            }
        }
        if (noOverlap) {
            task.setBackupStartTime(task.getStartTime());
            task.setBackupDuration(task.getDuration());
        }
    }

    // условия для определения пересечения по времени
    private boolean isOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null || task1.equals(task2)) {
            return false;
        }
        return  // задачи начинаются в одно время
               (task1.getStartTime().equals(task2.getStartTime()) ||
                // task1 начинается, пока не завершилась task2
               (task1.getStartTime().isAfter(task2.getStartTime()) &&
                task1.getStartTime().isBefore(task2.getEndTime().get())) ||
                // task2 начинается, пока не завершилась task1
               (task2.getStartTime().isAfter(task1.getStartTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime().get()))
        );
    }

    // обновить задачу в списке приоритетных задач
    protected void updatePrioritizedTask(Task task) {
        if (prioritizedTasks.contains(task)) {
            prioritizedTasks.remove(task);
            checkOverlap(task);
            prioritizedTasks.add(task);
        }
    }

    // проверить, что список задач не пуст
    protected void checkIfTaskMapIsEmpty() {
        if (tasks.isEmpty()){
            throw new NullPointerException("Список задач пуст");
        }
    }

    // проверить, что список эпиков не пуст
    protected void checkIfEpicMapIsEmpty() {
        if (epics.isEmpty()){
            throw new NullPointerException("Список эпиков пуст");
        }
    }

    // проверить, что список подзадач не пуст
    protected void checkIfSubtaskMapIsEmpty() {
        if (subtasks.isEmpty()) {
            throw new NullPointerException("Список подзадач пуст");
        }
    }

    // проверить, что задача есть в списке задач
    protected void checkTaskInMap(int id) {
        if (tasks.isEmpty()){
            throw new NullPointerException("Список задач пуст");
        } else if (!tasks.containsKey(id)) {
            throw new NullPointerException("Задачи нет в списке");
        }
    }

    // проверить, что эпик есть в списке эпиков
    protected void checkEpicInMap(int id) {
        if (epics.isEmpty()) {
            throw new NullPointerException("Список эпиков пуст");
        } else if (!epics.containsKey(id)) {
            throw new NullPointerException("Эпика нет в списке");
        }
    }

    // проверить, что подзадача есть в списке подзадач
    protected void checkSubtaskInMap(int id) {
        if (subtasks.isEmpty()) {
            throw new NullPointerException("Список подзадач пуст");
        } else if (!subtasks.containsKey(id)) {
            throw new NullPointerException("Подзадачи нет в списке");
        }
    }

}