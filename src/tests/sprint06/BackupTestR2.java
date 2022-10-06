package tests.sprint06;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class BackupTestR2 {

    public static Task createTask2() {
        return new Task("Task2",
                "Description task2",
                "NEW"
        );
    }

    public static Epic createEpic1() {
        return new Epic("Epic1",
                "Description epic1"
        );
    }

    public static Subtask createEpic1Subtask1() {
        return new Subtask("Epic1 SubTask1",
                "Description epic1 subTask1",
                "NEW",
                "Epic1"
        );
    }

    public static Subtask createEpic2Subtask1() {
        return new Subtask("Epic2 SubTask1",
                "Description epic2 subTask1",
                "NEW",
                "Epic2"
        );
    }
}
