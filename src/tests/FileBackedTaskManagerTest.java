package tests;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    protected static String HOME = "resources";
    protected static String FILE = "backup-s7.csv";
    protected static Path path = Paths.get(HOME, FILE);


    protected FileBackedTaskManagerTest() {
        super(FileBackedTaskManager.loadFromFile(new File(HOME, FILE)));
    }

    public void initializeTest() {
        new FileBackedTaskManagerTest();
    }

    @BeforeEach
    // очистить содержимое файла
    public void cleanUp()  {
        try (FileWriter fileWriter = new FileWriter(new File(HOME, FILE))) {
            fileWriter.write("");
        } catch (IOException exception) {
            System.out.println("Ошибка при попытке очистить содержимое файла");
        }
    }

    // записать контент в файл перед проведением теста
    private void write(String content) {
        try (FileWriter fileWriter = new FileWriter(new File(HOME, FILE))) {
            fileWriter.write(content);
        } catch (IOException exception) {
            System.out.println("Ошибка при попытке записать контент в файл");
        }
    }

    // считать содержимое файла
    private String readFile() {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении из файла");
            return null;
        }
    }

    // ---------------------------------------------
    // ТЕСТЫ — Чтение из файла
    // ---------------------------------------------

    // чтение из пустого файла
    @Test
    void readFromEmptyFile() {
        initializeTest();

        List<Task> tasks = taskManager.getTasks();
        List<Task> epics = taskManager.getTasks();
        List<Task> subtasks = taskManager.getTasks();
        List<Task> history = taskManager.getHistory();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(0, tasks.size(), "Список задач не пуст");
        assertEquals(0, epics.size(), "Список эпиков не пуст");
        assertEquals(0, subtasks.size(), "Список подзадач не пуст");
        assertEquals(0, history.size(), "История не пустая");
        assertEquals(0, prioritizedTasks.size(), "Список приоритетных задач не пуст");
    }

    // чтение из файла с данными: эпик с подзадачами
    @Test
    void readFileWithEpicWithSubtasks() {
        String content = "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task1,2022-10-15T13:45,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description epic1,2022-10-16T12:00,PT24H30M,\n" +
                "3,SUBTASK,Epic1 Subtask1,NEW,Description epic1 subtask1,2022-10-16T12:00,PT30M,2,true,false\n" +
                "4,SUBTASK,Epic1 Subtask2,NEW,Description epic1 subtask2,2022-10-17T12:00,PT30M,2,false,true\n" +
                "\n" +
                "1,2,3,4";
        write(content);

        initializeTest();

        List<Task> history = taskManager.getHistory();
        List<Task> priorotizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(4, history.size(), "Неверное количество задач в истории");
        assertEquals(3, priorotizedTasks.size(),
                                                 "Неверное количество задач в списке приоритетных задач");

        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, tasks.size(), "Неверное количество задач в списке");
        assertEquals(1, epics.size(), "Неверное количество эпиков в списке");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач в списке");

        Epic savedEpic1 = epics.get(0);
        List<Subtask> subtasksInEpic1 = taskManager.getSubtasksInEpic(savedEpic1.getId());
        assertEquals(2, subtasksInEpic1.size(), "Неверное количество подзадач в эпике");
        assertEquals(subtasks, subtasksInEpic1, "Списки подзадач не совпадают");
    }

    // чтение из файла с данными: эпик без подзадач
    @Test
    void readFileWithEpicWithoutSubtasks() {
        String content = "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task1,2022-10-15T13:45,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description epic1,null,null,\n" +
                "\n" +
                "1,2";
        write(content);

        initializeTest();

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков в списке");

        Epic savedEpic1 = epics.get(0);
        List<Subtask> subtasksInEpic1 = taskManager.getSubtasksInEpic(savedEpic1.getId());
        assertNull(subtasksInEpic1, "Список задач должен быть пустым");
    }

    // чтение из файла с данными: пустая история
    @Test
    void readFileWithEmptyHistory() {
        String content = "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task1,2022-10-15T13:45,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description epic1,null,null,\n\n";
        write(content);

        initializeTest();

        List<Task> history = taskManager.getHistory();
        assertEquals(0, history.size(), "История должна быть пустой");
    }


    // ---------------------------------------------
    // ТЕСТЫ — Запись в файл
    // ---------------------------------------------

    // запись в пустой файл
    @Test
    void writeInEmptyFile() {
        String expectedFinalContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";

        initializeTest();

        createTask1();
        final int idTask1 = taskManager.add(task1);
        taskManager.getTask(idTask1);

        String finalContent = readFile();
        assertEquals(expectedFinalContent, finalContent, "Данные в файл записаны неверно");

    }

    // запись в файл с данными: добавление эпика без подзадач
    @Test
    void writeInFileAddEpicWithoutSubtasks() {
        String backupContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";
        String expectedFinalContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1,2";

        write(backupContent);
        initializeTest();

        createEpic1();
        final int idEpic1 = taskManager.add(epic1);
        taskManager.getEpic(idEpic1);

        String finalContent = readFile();
        assertEquals(expectedFinalContent, finalContent, "Данные в файл записаны неверно");
    }

    // запись в файл с данными: добавить подзадачи в существующий эпик
    @Test
    void writeInFileAddSubtasksToEpic() {
        String backupContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1,2";
        String expectedFinalContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT24H30M,\n" +
                "3,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,2,true,false\n" +
                "4,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,2,false,true\n" +
                "\n" +
                "1,2,3,4";

        write(backupContent);
        initializeTest();

        createSubtask1();
        createSubtask2();
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        List<Epic> epics = taskManager.getEpics();
        Epic epic1 = epics.get(0);
        taskManager.getSubtasksInEpic(epic1.getId());

        String finalContent = readFile();
        assertEquals(expectedFinalContent, finalContent, "Данные в файл записаны неверно");
    }

    // запись в файл: удаление задач из истории
    @Test
    void writeInFileAfterDeletingHistory() {
        String backupContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT24H30M,\n" +
                "3,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,2,true,false\n" +
                "4,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,2,false,true\n" +
                "\n" +
                "1,2,3,4";

        String expectedFinalContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        write(backupContent);
        initializeTest();

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();

        List<Task> history = taskManager.getHistory();
        String finalContent = readFile();

        assertEquals(0, history.size(), "История не пустая");
        assertEquals(expectedFinalContent, finalContent, "Данные в файл записаны неверно");
    }


    // ---------------------------------------------
    // ---------------------------------------------
    // ОБЩИЕ ТЕСТЫ ДЛЯ МЕНЕДЖЕРОВ ЗАДАЧ
    // ---------------------------------------------
    // ---------------------------------------------

    // ---------------------------------------------
    // ТЕСТЫ 1 — Стандартное поведение
    // ---------------------------------------------

    // ДОБАВИТЬ

    // добавить задачу
    @Test @Override
    public void addTask() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";

        initializeTest();
        super.addTask();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // добавить эпик без подзадач
    @Test @Override
    public void addEpicWithOutSubtasks() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1";

        initializeTest();
        super.addEpicWithOutSubtasks();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // добавить эпик с подзадачами
    @Test @Override
    public void addEpicWithSubtasks() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT24H30M,\n" +
                "2,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,false\n" +
                "3,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,1,false,true\n" +
                "\n" +
                "1,2,3";

        initializeTest();
        super.addEpicWithSubtasks();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ОБНОВИТЬ

    // обновить задачу
    @Test @Override
    public void updateTask() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,DONE,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";

        initializeTest();
        super.updateTask();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // обновить эпик без подзадач
    @Test @Override
    public void updateEpic() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Epic1 Description2,null,null,\n" +
                "\n" +
                "1";

        initializeTest();
        super.updateEpic();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // обновить подзадачу
    @Test @Override
    public void updateSubtask() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,IN_PROGRESS,Description Epic1,2022-10-16T12:00,PT24H30M,\n" +
                "2,SUBTASK,Epic1 Subtask1,DONE,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,false\n" +
                "3,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,1,false,true\n" +
                "\n" +
                "2,1";

        initializeTest();
        super.updateSubtask();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // УДАЛИТЬ

    // удалить задачу
    @Test @Override
    public void deleteTask() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.deleteTask();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить эпик
    @Test @Override
    public void deleteEpic() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.deleteEpic();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить подзадачу
    @Test @Override
    public void deleteSubtask() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-17T12:00,PT30M,\n" +
                "3,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,1,true,true\n" +
                "\n" +
                "1,3";

        initializeTest();
        super.deleteSubtask();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить все задачи
    @Test @Override
    public void deleteAllTasks() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.deleteAllTasks();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить все эпики
    @Test @Override
    public void deleteAllEpics() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.deleteAllEpics();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить все подзадачи
    @Test @Override
    public void deleteAllSubtasks() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "2,EPIC,Epic2,NEW,Description Epic2,null,null,\n" +
                "\n" +
                "1,2";

        initializeTest();
        super.deleteAllSubtasks();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ДАТА, ВРЕМЯ, ПРОДОЛЖИТЕЛЬНОСТЬ

    // рассчитать время эпика по подзадачам, где указано время
    @Test @Override
    public void shouldCalculateEpicTimingFromSubtasksWithTiming() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT24H30M,\n" +
                "2,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,false\n" +
                "3,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,1,false,true\n" +
                "4,SUBTASK,Epic1 Subtask3,NEW,Description Epic1 Subtask3,null,null,1,false,false\n" +
                "\n" +
                "1,2,3";

        initializeTest();

        super.shouldCalculateEpicTimingFromSubtasksWithTiming();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // время эпика меняется при изменении времени подзадачи
    @Test @Override
    void shouldUpdateEpicTimingIfSubtaskTimeUpdated() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT71H10M,\n" +
                "2,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,false\n" +
                "3,SUBTASK,Epic1 Subtask2,NEW,Description Epic1 Subtask2,2022-10-17T12:00,PT30M,1,false,false\n" +
                "4,SUBTASK,Epic1 Subtask3,NEW,Description Epic1 Subtask3,2022-10-19T10:00,PT1H10M,1,false,true\n" +
                "\n" +
                "2,3,1,4";

        initializeTest();
        super.shouldUpdateEpicTimingIfSubtaskTimeUpdated();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // задачи без времени должны добавляться в конец списка по приоритету
    @Test @Override
    void shouldAddTasksWithoutTimingAsNonPriority() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Non-Priority Task A,NEW,Description Non-Priority Task A,null,null,\n" +
                "2,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "3,TASK,Non-Priority Task B,NEW,Description Non-Priority Task B,null,null,\n" +
                "4,TASK,Task2,NEW,Description task 2,2022-11-15T14:00,PT1H,\n" +
                "\n";

        initializeTest();
        super.shouldAddTasksWithoutTimingAsNonPriority();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ПЕРЕСЕЧЕНИЕ ЗАДАЧ

    // пересечение задач: новая задача начинается до того, как завершилась первая
    @Test @Override
    void shouldPreventOverlapIfTask1EndsAfterTask2Starts() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,TASK,Task2,NEW,Description task 2,null,null,\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldPreventOverlapIfTask1EndsAfterTask2Starts();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // пересечение задач: новая задача завершается после того, как началась первая
    @Test @Override
    void shouldPreventOverlapIfTask1StartsBeforeTask2Ends() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,TASK,Task2,NEW,Description task 2,null,null,\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldPreventOverlapIfTask1StartsBeforeTask2Ends();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // пересечение задач: новая задача начинается одновременно с первой
    @Test @Override
    void shouldPreventOverlapIfTasksStartAtOneTime() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,TASK,Task2,NEW,Description task 2,null,null,\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldPreventOverlapIfTasksStartAtOneTime();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // пересечение задач: новая задача начинается и завершается, пока идет первая
    @Test @Override
    void shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "2,TASK,Task2,NEW,Description task 2,null,null,\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldPreventOverlapIfTask2StartsAndEndsWhileTask1IsInProgress();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");

    }

    // пересечение задач при обновлении одной из них — в случае пересечения сохраняется старое время начала
    @Test @Override
    void shouldPreventOverlapOnUpdate() {
         String expectedContent =
                 "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                 "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                 "2,TASK,Task2,NEW,Description task 2,2022-10-14T14:00,PT1H,\n" +
                 "\n" +
                 "2";

        initializeTest();
        super.shouldPreventOverlapOnUpdate();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ---------------------------------------------
    // ТЕСТЫ 2 — Пустой список задач
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingTaskInEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenUpdatingTaskInEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // обновить эпик в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingEpicInEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenUpdatingEpicInEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // обновить подзадачу в пустом списке
    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskInEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenUpdatingSubtaskInEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ПОЛУЧИТЬ

    // получить задачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingTaskFromEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingTaskFromEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить эпик из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingEpicFromEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingEpicFromEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить подзадачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingSubtaskFromEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingSubtaskFromEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить все задачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllTasksIfListIsEmpty() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingAllTasksIfListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить все эпики из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllEpicsIfListIsEmpty() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingAllEpicsIfListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить все подзадачи из пустого списка
    @Test @Override
    void shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingAllSubtasksIfListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить все подзадачи эпика, если пусты списки эпиков и подзадач
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenGettingEpicSubtasksIfListsAreEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить все подзадачи эпика, если пуст список подзадач в менеджере
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n";

        initializeTest();
        super.shouldReturnNullWhenGettingEpicSubtasksIfSubtaskListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // УДАЛИТЬ

    // удалить задачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingTaskFromEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenDeletingTaskFromEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить эпик из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingEpicFromEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenDeletingEpicFromEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить подзадачу из пустого списка
    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskFromEmptyList() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnNullWhenDeletingSubtaskFromEmptyList();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить все задачи из пустого списка
    @Test @Override
    void shouldDoNothingWhenDeletingAllTasksIfListIsEmpty() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.shouldDoNothingWhenDeletingAllTasksIfListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить все эпики из пустого списка
    @Test @Override
    void shouldDoNothingWhenDeletingAllEpicIfListIsEmpty() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.shouldDoNothingWhenDeletingAllEpicIfListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить все подзадачи из пустого списка
    @Test @Override
    void shouldDoNothingWhenDeletingAllSubtasksIfListIsEmpty() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n\n";

        initializeTest();
        super.shouldDoNothingWhenDeletingAllSubtasksIfListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ДОПОЛНИТЕЛЬНО

    // получить задачи из пустого списка истории
    @Test @Override
    void shouldReturnEmptyListIfHistoryIsEmpty() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnEmptyListIfHistoryIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить задачи из пустого списка приоритетных задач
    @Test @Override
    void shouldReturnEmptyListIfPriorityTaskListIsEmpty() {
        String expectedContent = "";

        initializeTest();
        super.shouldReturnEmptyListIfPriorityTaskListIsEmpty();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }


    // ---------------------------------------------
    // ТЕСТЫ 3 — Несуществующий идентификатор задачи
    // ---------------------------------------------

    // ОБНОВИТЬ

    // обновить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingTaskWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenUpdatingTaskWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // обновить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingEpicWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenUpdatingEpicWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // обновить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenUpdatingSubtaskWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT30M,\n" +
                "2,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,true\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldReturnNullWhenUpdatingSubtaskWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // ПОЛУЧИТЬ

    // получить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingTaskWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenGettingTaskWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingEpicWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenGettingEpicWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingSubtaskWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT30M,\n" +
                "2,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,true\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldReturnNullWhenGettingSubtaskWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // получить все подзадачи эпика, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenGettingEpicSubtasksWithInvalidEpicId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // УДАЛИТЬ

    // удалить задачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingTaskWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,TASK,Task1,NEW,Description task 1,2022-10-15T14:00,PT1H,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenDeletingTaskWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить эпик, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingEpicWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,null,null,\n" +
                "\n" +
                "1";

        initializeTest();
        super.shouldReturnNullWhenDeletingEpicWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }

    // удалить подзадачу, используя несуществующий ID
    @Test @Override
    void shouldReturnNullWhenDeletingSubtaskWithInvalidId() {
        String expectedContent =
                "id,type,name,status,description,starttime,duration,epic,epicStartTime,epicEndTime\n" +
                "1,EPIC,Epic1,NEW,Description Epic1,2022-10-16T12:00,PT30M,\n" +
                "2,SUBTASK,Epic1 Subtask1,NEW,Description Epic1 Subtask1,2022-10-16T12:00,PT30M,1,true,true\n" +
                "\n" +
                "2";

        initializeTest();
        super.shouldReturnNullWhenDeletingSubtaskWithInvalidId();

        String savedContent = readFile();
        assertEquals(expectedContent, savedContent, "Данные в файл записаны неверно");
    }
}