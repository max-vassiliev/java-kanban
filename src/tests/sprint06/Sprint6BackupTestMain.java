package tests.sprint06;

import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tests.sprint06.BackupTestR1;
import tests.sprint06.BackupTestR2;
import tests.sprint06.BackupTestR4;
import tests.sprint06.BackupTestR6;

public class Sprint6BackupTestMain {

    // TODO удалено из из метода main в FileBackedTaskManager

    /*      -------------------------------
        ТЕСТ - Спринт 6 (по техническому заданию)
        -------------------------------      */

//        String backupFile = "backup.csv";
//        FileBackedTaskManager taskManager1 = loadFromFile(new File(HOME, backupFile));
//        Sprint6BackupTestMain.backupTestMain(taskManager1);

    public static void backupTestMain(FileBackedTaskManager taskManager1) {
        Task task1 = BackupTestR1.createTask1();
        Task task2 = BackupTestR2.createTask2();
        Task task3 = BackupTestR6.createTask3();
        Epic epic1 = BackupTestR2.createEpic1();
        Epic epic2 = BackupTestR1.createEpic2();
        Epic epic3 = BackupTestR6.createEpic3();
        Subtask epic1Subtask1 = BackupTestR2.createEpic1Subtask1();
        Subtask epic2Subtask1 = BackupTestR2.createEpic2Subtask1();
        Subtask epic2Subtask2 = BackupTestR1.createEpic2Subtask2();
        Subtask epic3Subtask1 = BackupTestR6.createEpic3Subtask1();
        Task bonusTask1 = BackupTestR4.createBonusTask1();

        // добавляем задачи по порядку
        taskManager1.add(task1);                            // [id=1]  Task1
        taskManager1.add(task2);                            // [id=2]  Task2
        taskManager1.add(task3);                            // [id=3]  Task3
        taskManager1.add(epic1);                            // [id=4]  Epic1
        taskManager1.add(epic2);                            // [id=5]  Epic2
        taskManager1.add(epic3);                            // [id=6]  Epic3
        taskManager1.add(epic1Subtask1);                    // [id=7]  Epic1 Subtask1
        taskManager1.add(epic2Subtask1);                    // [id=8]  Epic2 Subtask1
        taskManager1.add(epic2Subtask2);                    // [id=9]  Epic2 Subtask2
        taskManager1.add(epic3Subtask1);                    // [id=10] Epic3 Subtask1
        taskManager1.add(bonusTask1);                       // [id=11] BonusTask1

        // вызываем задачи по порядку
        taskManager1.getTask(task1.getId());                // [id=1]  Task1
        taskManager1.getTask(task2.getId());                // [id=2]  Task2
        taskManager1.getTask(task3.getId());                // [id=3]  Task3
        taskManager1.getEpic(epic1.getId());                // [id=4]  Epic1
        taskManager1.getEpic(epic2.getId());                // [id=5]  Epic2
        taskManager1.getEpic(epic3.getId());                // [id=6]  Epic3
        taskManager1.getSubtask(epic1Subtask1.getId());     // [id=7]  Epic1 Subtask1
        taskManager1.getSubtask(epic2Subtask1.getId());     // [id=8]  Epic2 Subtask1
        taskManager1.getSubtask(epic2Subtask2.getId());     // [id=9]  Epic2 Subtask2
        taskManager1.getSubtask(epic3Subtask1.getId());     // [id=10] Epic3 Subtask1

        // вызываем некоторые задачи повторно
        taskManager1.getTask(bonusTask1.getId());           // [id=11] BonusTask1
        taskManager1.getEpic(epic2.getId());                // [id=5]  Epic2

        // TODO удалено из метода main в FileBackedTaskManager

//    // создаем новый менеджер из того же файла
//    FileBackedTaskManager taskManager2 = loadFromFile(new File(HOME, backupFile));
//
//    // вызываем еще несколько задач
//        taskManager2.getTask(task1.getId());                // [id=1]  Task1
//        taskManager2.getTask(task2.getId());                // [id=2]  Task2}


    }
}
