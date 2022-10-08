package tests.sprint07;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class TestSprint07Round01 {

    public static Task createTask1() {
        return new Task("Task1",
                "Description task1",
                "NEW",
                "15.10.2022, 13:45",
                "01:00"
        );
    }

    public static Task createTask2() {
        return new Task("Task2",
                "Description task2",
                "NEW",
                "15.11.2022, 15:00",
                "00:45"
        );
    }

    public static Task createTask3() {
        return new Task("Task3",
                "Description task3",
                "NEW"
//                "12.10.2022, 12:00",
//                "00:30"
        );
    }

    public static Task createTask4() {
        return new Task("Task4",
                "Description task4",
                "NEW"
        );
    }

    public static Epic createEpic1() {
        return new Epic("Epic 1",
                "Description epic1"
        );
    }

    public static Subtask createEpic1Subtask1() {
        return new Subtask("Epic1 Subtask1",
                            "Description epic1 subtask1",
                            "NEW",
                            "Epic 1",
                            "16.10.2022, 12:00",
                            "00:30"
        );
    }

    public static Subtask createEpic1Subtask2() {
        return new Subtask("Epic1 Subtask2",
                "Description epic1 subtask2",
                "NEW",
                "Epic 1",
                "17.10.2022, 12:00",
                "00:30"
        );
    }

    public static Subtask createEpic1Subtask3() {
        return new Subtask("Epic1 Subtask3",
                "Description epic1 subtask3",
                "NEW",
                "Epic 1"
//                "18.10.2022, 12:00",
//                "00:30"
        );
    }

}
