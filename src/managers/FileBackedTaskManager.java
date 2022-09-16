package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO возможно, исключение можно будет убрать
public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private static final String HOME = "resources";
    private static final String COLUMN_SEPARATOR = ",";
    private static final String ROW_SEPARATOR = "\n";
    private static final String TASKS_HISTORY_SEPARATOR = "\n\n";

    private final Path historyPath;

    public FileBackedTaskManager(Path historyPath) throws IOException {
        this.historyPath = historyPath;
        File docFile = new File(String.valueOf(historyPath));
        String content = loadFromFile(docFile);

        if (content != null) {
            String[] split = content.split(TASKS_HISTORY_SEPARATOR);
            String tasksData = split[0];
            String historyData = split[1];

            // получаем задачи из файла и добавляем в менеджер
            String[] taskRows = tasksData.split(ROW_SEPARATOR);
            for (int i = 1; i < taskRows.length; i++) {
                fromString(taskRows[i]); // TODO тут что-то еще нужно для истории
            }

            // получаем историю из файла и добавляем в менеджер
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

        FileBackedTaskManager manager = new FileBackedTaskManager(Paths.get(HOME, historyFile1));



    }

    // TODO возможно, исключение можно будет убрать
    String loadFromFile(File file) throws IOException {
        try {
            String content = Files.readString(Path.of(String.valueOf(file)));
            return content;
        } catch (IOException e) {
            System.out.println("Ошибка в методе loadFromFile"); // TODO переписать строку
            return null;
        }
    }
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
//        String[] split = content.split(TASKS_HISTORY_SEPARATOR);
//        String tasksData = split[0];
//        String historyData = split[1];
//
//        String[] taskRows = tasksData.split(ROW_SEPARATOR);
//
//        for (int i = 1; i <= taskRows.length; i++) {
//            String[] data = taskRows[i].split(COLUMN_SEPARATOR);
//
//        }
//
//
//        return null;

    // TODO новый метод быть верный
    private Task fromString(String taskRow) {
        Task taskFromString;
        String[] data = taskRow.split(COLUMN_SEPARATOR);
        int id = Integer.parseInt(data[0]);
        TaskType type = TaskType.valueOf(data[1]);

        switch (type) {
            case TASK:
                Task task = new Task(data[2], data[4], data[3]);
                task.setId(id);         // TODO возможно, поменять на checkId();
                addTask(task);          // TODO возможно, поменять на tasks.put(id, task);
                taskFromString = task;
                break;
            case EPIC:
                Epic epic = new Epic(data[2], data[4]);
                epic.setId(id);         // TODO
                addEpic(epic);          // TODO
                taskFromString = epic;
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(data[5]);
                Subtask subtask = new Subtask(data[2], data[4], data[3], epicId);
                subtask.setId(id);      // TODO
                addSubtask(subtask);    // TODO
                taskFromString = subtask;
                break;
            default:
                taskFromString = null;
                break;
        }
        return taskFromString;
    }

    //TODO новый метод
    static List<Integer> historyFromString(String historyData) {
        String[] split = historyData.split(COLUMN_SEPARATOR);
        List<Integer> history = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            history.add(Integer.parseInt(split[i]));
        }

        return history;
    }


    // TODO [Вариант 1] возможно, удалить
//     private void tasksFromString(String tasksData) {
//         String[] taskRows = tasksData.split(ROW_SEPARATOR);
//
//         for (int i = 1; i <= taskRows.length; i++) {
//             String[] data = taskRows[i].split(COLUMN_SEPARATOR);
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

}
