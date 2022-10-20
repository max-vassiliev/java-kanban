package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;
import utilities.ManagerSaveException;
import utilities.TaskIdComparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String HOME = "resources";
    private static final String BACKUP_FILE = "backup-s8.csv";
    private static final String ITEM_SEPARATOR = ",";
    private static final String LINE_SEPARATOR = "\n";
    private static final String SECTION_SEPARATOR = "\n\n";
    private static final String BACKUP_FILE_HEADING = "id,type,name,status,description,starttime," +
                                                      "duration,epic,epicStartTime,epicEndTime\n";
    private final String historyPath;


    public FileBackedTaskManager(String historyPath) {
        this.historyPath = historyPath;
        load(historyPath);
    }

    public static void main(String[] args) {
        FileBackedTaskManager taskManager = loadFromFile(new File(HOME, BACKUP_FILE));
    }


    // ЧТЕНИЕ ФАЙЛА

    // восстановить менеджер задач из файла с резервной копией
    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            return new FileBackedTaskManager(file.getPath());
        } catch (NullPointerException e) {
            System.out.println("Ошибка при загрузке сохраненных данных");
            return null;
        }
    }

    // получить содержимое файла
    String read(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Файл не найден");
            return null;
        }
    }

    // загрузить и обработать данные
    public void load(String path) {
        String content = read(path);
        if (content.isBlank()) return;
        StringBuilder sb = new StringBuilder(content);

        // восстанавливаем список задач и историю просмотров
        try {
            String[] split = content.split(SECTION_SEPARATOR);
            int sectionBreak = content.indexOf(SECTION_SEPARATOR);
            sb.delete(0, sectionBreak + SECTION_SEPARATOR.length());

            String tasksData = split[0];
            String historyData = sb.toString();

            // получаем задачи и добавляем в менеджер задач
            String[] taskRows = tasksData.split(LINE_SEPARATOR);
            for (int i = 1; i < taskRows.length; i++) {
                fromString(taskRows[i]);
            }

            // получаем историю и добавляем в менеджер истории
            if (!historyData.isBlank()) {
                List<Integer> history = historyFromString(historyData);
                for (Integer id : history) {
                    if (tasks.containsKey(id)) {
                        getTask(id);
                    } else if (epics.containsKey(id)) {
                        this.getEpic(id);
                    } else if (subtasks.containsKey(id)) {
                        getSubtask(id);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке сохраненных задач");
        }
    }

    // создать задачу из строки
    private Task fromString(String taskRow) throws IOException {
        Task taskFromString;
        String[] data = taskRow.split(ITEM_SEPARATOR);
        int id = Integer.parseInt(data[0]);
        TaskType type = TaskType.valueOf(data[1]);
        LocalDateTime startTime = parseStartTime(data[5]);
        Duration duration = parseDuration(data[6]);

        super.checkNextId(id);
        switch (type) {
            case TASK:
                Task task = new Task(data[2], data[3], data[4], startTime, duration);
                task.setId(id);
                task.setType(type);
                tasks.put(id, task);
                prioritizedTasks.add(task);
                taskFromString = task;
                break;
            case EPIC:
                Epic epic = new Epic(data[2], data[3], data[4], startTime, duration);
                epic.setId(id);
                epic.setType(type);
                epics.put(id, epic);
                taskFromString = epic;
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(data[7]);
                boolean isEpicStartTime = Boolean.parseBoolean(data[8]);
                boolean isEpicEndTime = Boolean.parseBoolean(data[9]);
                Epic relatedEpic = epics.get(epicId);
                Subtask subtask = new Subtask(data[2], data[3], data[4], epicId,
                                              startTime, duration, isEpicStartTime, isEpicEndTime);
                subtask.setId(id);
                subtask.setType(type);
                relatedEpic.addRelatedSubtask(id);
                setEpicTiming(relatedEpic, subtask);
                if (isEpicStartTime) relatedEpic.setStartTimeSubtask(subtask.getId());
                if (isEpicEndTime) relatedEpic.setEndTimeSubtask(subtask.getId());
                subtasks.put(id, subtask);
                prioritizedTasks.add(subtask);
                taskFromString = subtask;
                break;
            default:
                taskFromString = null;
                break;
        }
        return taskFromString;
    }

    // восстановить список задач в истории просмотров из резервной копии
    static List<Integer> historyFromString(String historyData) {
        if (historyData == null) return null;

        String[] split = historyData.split(ITEM_SEPARATOR);
        List<Integer> history = new ArrayList<>();

        for (String id : split) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }

    // восстановить время начала
    LocalDateTime parseStartTime(String str) {
        if (str.equals("null")) {
            return null;
        } else {
            return LocalDateTime.parse(str);
        }
    }

    // восстановить продолжительность
    Duration parseDuration(String str) {
        if (str.equals("null")) {
            return null;
        } else {
            return Duration.parse(str);
        }
    }

    // ЗАПИСЬ В ФАЙЛ

    // сохранить резервную копию менеджера
    public void save() {
        List<Task> sortedTasks = sortTasks();

        try(FileWriter fw = new FileWriter(historyPath)) {
            fw.write(BACKUP_FILE_HEADING);
            for (Task task : sortedTasks) {
                fw.write(toString(task) + LINE_SEPARATOR);
            }
            fw.write('\n');
            fw.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при автосохранении");
        }
    }

    // сохранить задачу в строку
    private String toString(Task task) {
        String result;
        switch (task.getType()) {
            case TASK:
            case EPIC:
                result = String.format("%s,".repeat(7),
                        task.getId(), task.getType(), task.getTitle(),
                        task.getStatus(), task.getDescription(),
                        task.getStartTime(), task.getDuration());
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                result = String.format("%s,".repeat(9) + "%s",
                        subtask.getId(), subtask.getType(), subtask.getTitle(),
                        subtask.getStatus(), subtask.getDescription(),
                        subtask.getStartTime(), subtask.getDuration(),
                        subtask.getEpicId(), subtask.getIsEpicStartTime(), subtask.getIsEpicEndTime());
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    // получить список ID задач в менеджере истории
    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();

        if (history.isEmpty()) return "";

        for (Task task : history) {
            sb.append(task.getId()).append(ITEM_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    // ---------------------------------------------
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ---------------------------------------------

    // отсортировать задачи по ID
    public List<Task> sortTasks() {
        List<Task> sortedTasks = new ArrayList<>();
        TaskIdComparator taskIdComparator = new TaskIdComparator();

        sortedTasks.addAll(tasks.values());
        sortedTasks.addAll(epics.values());
        sortedTasks.addAll(subtasks.values());
        sortedTasks.sort(taskIdComparator);
        return sortedTasks;
    }


    // ---------------------------------------------
    // ПЕРЕОПРЕДЕЛЕННЫЕ МЕТОДЫ
    // ---------------------------------------------

    // ДОБАВИТЬ

    // добавить задачу
    @Override
    public int add(Task task) {
        super.add(task);
        save();
        return task.getId();
    }

    // добавить эпик
    @Override
    public int add(Epic epic) {
        super.add(epic);
        save();
        return epic.getId();
    }

    // добавить подзадачу
    @Override
    public int add(Subtask subtask) {
        super.add(subtask);
        save();
        return subtask.getId();
    }

    // ОБНОВИТЬ

    // обновить задачу
    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    // обновить эпик
    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    // обновить подзадачу
    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    // ПОЛУЧИТЬ

    // получить задачу
    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    // получить эпик
    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    // получить подзадачу
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    // УДАЛИТЬ

    // удалить задачу
    @Override
    public void delete(Task task) {
        super.delete(task);
        save();
    }

    // удалить эпик
    @Override
    public void delete(Epic epic) {
        super.delete(epic);
        save();
    }

    // удалить подзадачу
    @Override
    public void delete(Subtask subtask) {
        super.delete(subtask);
        save();
    }

    // УДАЛИТЬ ВСЕ ЗАДАЧИ ОДНОГО ТИПА

    // удалить все задачи
    @Override
    public void deleteAllTasks(){
        super.deleteAllTasks();
        save();
    }

    // удалить все эпики
    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    // удалить все подзадачи
    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}