package tests.sprint06;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class BackupTestR6 {

    public static Task createTask3() {
        return new Task("Task3",
                "Description task3",
                "NEW"
        );
    }

    public static Epic createEpic3() {
        return new Epic("Epic3",
                "Description epic3"
        );
    }

    public static Subtask createEpic3Subtask1() {
        return new Subtask("Epic3 SubTask1",
                "Description epic3 subTask1",
                "NEW",
                "Epic3"
        );
    }
}
