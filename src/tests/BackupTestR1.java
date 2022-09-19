package tests;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class BackupTestR1 {

    public static Task createTask1() {
        return new Task("Task1",
                "Description task1",
                "NEW"
        );
    }

    public static Epic createEpic2() {
        return new Epic("Epic2",
                "Description epic2"
        );
    }

    public static Subtask createEpic2Subtask2() {
        return new Subtask("Epic2 SubTask2",
                "Description epic2 subtask2",
                "DONE",
                "Epic2"
        );
    }
}
