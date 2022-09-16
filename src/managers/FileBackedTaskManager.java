package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;
import tests.HistoryTestR4;
import utilities.TaskIdComparator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// TODO возможно, исключение можно будет убрать
public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private static final String HOME = "resources";
    private static final String ITEM_SEPARATOR = ",";
    private static final String LINE_SEPARATOR = "\n";
    private static final String SECTION_SEPARATOR = "\n\n";

    private static final String BACKUP_FILE_HEADING = "id,type,name,status,description,epic\n";

    private final String historyPath;


    public FileBackedTaskManager(String historyPath) {
        this.historyPath = historyPath;
//        File historyFile = new File(historyPath);
        String content = readFile(new File(historyPath));

        // восстанавливаем список задач и историю просмотров
        if (content != null || !content.isBlank()) {                //TODO распределить условия, оставить isBlank()
            String[] split = content.split(SECTION_SEPARATOR);
            String tasksData = split[0];
            String historyData = split[1];

            // получаем задачи и добавляем в менеджер задач
            String[] taskRows = tasksData.split(LINE_SEPARATOR);
            for (int i = 1; i < taskRows.length; i++) {
                fromString(taskRows[i]);
            }

            // получаем историю и добавляем в менеджер истории
            List<Integer> history = historyFromString(historyData);
            for (Integer id : history) {
                if (tasks.containsKey(id)) {
                    getTask(id);
                } else if (epics.containsKey(id)) {
                    getEpic(id);
                } else if (subtasks.containsKey(id)) {
                    getSubtask(id);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        String historyFile1 = "history1.csv";

        FileBackedTaskManager taskManager = loadFromFile(new File(HOME, historyFile1));

        Task bonusTask1 = HistoryTestR4.createBonusTask1();
        taskManager.addTask(bonusTask1);
        taskManager.getTask(bonusTask1.getId());
        taskManager.getSubtask(3);
        taskManager.save();
    }

    static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file.getPath());
    }



    // TODO новый метод
    public void save() throws IOException {
        List<Task> sortedTasks = sortTasks();

        try(FileWriter fw = new FileWriter(new File(HOME, "history2.csv"))) { // TODO поменять файл
            fw.write(BACKUP_FILE_HEADING);
            for (Task task : sortedTasks) {
                fw.write(task.toString() + LINE_SEPARATOR); // TODO преписать метод toString()
            }
            fw.write('\n');
            fw.write(historyToString(historyManager));
        } catch (IOException e) {                   //TODO добавить ManagerSaveException
            System.out.println("Ошибка при сохранении истории");
        }
    }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();

        for (Task task : history) {
            sb.append(task.getId()).append(ITEM_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

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

    // получить содержание файла
    String readFile(File file) {
        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке сохраненных данных");
            return null;
        }
    }


    // TODO новый метод быть верный
    private Task fromString(String taskRow) {
        Task taskFromString;
        String[] data = taskRow.split(ITEM_SEPARATOR);
        int id = Integer.parseInt(data[0]);
        TaskType type = TaskType.valueOf(data[1]);

        switch (type) {
            case TASK:
                Task task = new Task(data[2], data[4], data[3]);
                task.setId(id);         // TODO возможно, поменять на checkId(); — кажется, НЕ НАДО менять
                addTask(task);          // TODO возможно, поменять на tasks.put(id, task);
                taskFromString = task;
                break;
            case EPIC:
                Epic epic = new Epic(data[2], data[4]);
                epic.setId(id);         // TODO — кажется, НЕ НАДО менять
                addEpic(epic);          // TODO — кажется, НЕ НАДО менять
                taskFromString = epic;
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(data[5]);
                Subtask subtask = new Subtask(data[2], data[4], data[3], epicId);
                subtask.setId(id);      // TODO — кажется, НЕ НАДО менять
                addSubtask(subtask);    // TODO — кажется, НЕ НАДО менять
                taskFromString = subtask;
                break;
            default:
                taskFromString = null;
                break;
        }
        return taskFromString;
    }

    // восстановить список задач в истории из списка ID в резервной копии
    static List<Integer> historyFromString(String historyData) {
        String[] split = historyData.split(ITEM_SEPARATOR);
        List<Integer> history = new ArrayList<>();

        for (String id : split) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }
}


    // TODO [Вариант 1] возможно, удалить
//     private void tasksFromString(String tasksData) {
//         String[] taskRows = tasksData.split(LINE_SEPARATOR);
//
//         for (int i = 1; i <= taskRows.length; i++) {
//             String[] data = taskRows[i].split(ITEM_SEPARATOR);
//             int id = Integer.parseInt(data[0]);
//             switch (data[1]) {
//                 case "TASK":
//                     Task task = new Task(data[2], data[4], data[3]);
//                     task.setId(id);
//                     addTask(task);
//                     break;
//                 case "EPIC":
//                     Epic epic = new Epic(data[2], data[4]);
//                     epic.setId(id);
//                     addEpic(epic);
//                     break;
//                 case "SUBTASK":
//                     Subtask subtask = new Subtask(data[2], data[4], data[3], data[5]);
//                     subtask.setId(id);
//                     addSubtask(subtask);
//                     break;
//                 default:
//                     break;
//             }
//         }
//     }


    //TODO [Вариант 2] возможно, удалить
//    private void uploadData(String[] data) {
//        int id = Integer.parseInt(data[0]);
//        switch (data[1]) {
//            case "TASK":
//                Task task = new Task(data[2], data[4], data[3]);
//                task.setId(id);
//                addTask(task);
//                break;
//            case "EPIC":
//                Epic epic = new Epic(data[2], data[4]);
//                epic.setId(id);
//                addEpic(epic);
//                break;
//            case "SUBTASK":
//                Subtask subtask = new Subtask(data[2], data[4], data[3], data[5]);
//                subtask.setId(id);
//                addSubtask(subtask);
//                break;
//            default:
//                break;
//        }
//    }


    //TODO удалить потом
//    public String readFileContentsOrNull(String path) {
//        try {
//            return Files.readString(Path.of(path));
//        } catch (IOException e) {
//            System.out.println("Ошибочка вышла");
//            return null;
//        }
//    }


//        [удаленный код для метода readFile]
//        Reader reader = new FileReader(file);
//        BufferedReader br = new BufferedReader(reader);
//        while (br.ready()) {
//            String line = br.readLine();
//
//            // если в файле ничего нет, возвращаем null
//            if (line.isBlank()) {
//                return null;
//            }
//
//
//
//            System.out.println("Строка: " + line);
//        }
//        String content = Files.readString(Path.of(String.valueOf(file)));


//        if (content.isBlank()) {
//            return null;
//        }
//
//        String[] split = content.split(SECTION_SEPARATOR);
//        String tasksData = split[0];
//        String historyData = split[1];
//
//        String[] taskRows = tasksData.split(LINE_SEPARATOR);
//
//        for (int i = 1; i <= taskRows.length; i++) {
//            String[] data = taskRows[i].split(ITEM_SEPARATOR);
//
//        }
//
//
//        return null;