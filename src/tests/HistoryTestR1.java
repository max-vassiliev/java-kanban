package tests;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class HistoryTestR1 {

    // СОЗДАЕМ ЗАДАЧИ РАЗНЫХ ТИПОВ

    // создаем две задачи

    public static Task createTask1() {
        return new Task("Задача 1",
                "Задача 1",
                "NEW");
    }

    public static Task createTask2() {
        return new Task("Задача 2",
                "Задача 2",
                "NEW");
    }

    // создаем один эпик с тремя подзадачами

    public static Epic createEpic1() {
        return new Epic("Эпик 1",
                "Эпик 1");
    }

    public static Subtask createEpic1Subtask1() {
        return new Subtask("Подзадача 1",
                "Подзадача 1",
                "NEW",
                "Эпик 1"
        );
    }

    public static Subtask createEpic1Subtask2() {
        return new Subtask("Подзадача 2",
                "Подзадача 2",
                "NEW",
                "Эпик 1"
        );
    }

    public static Subtask createEpic1Subtask3() {
        return new Subtask("Подзадача 3",
                "Подзадача 3",
                "NEW",
                "Эпик 1"
        );
    }

    // создаем эпик без подзадач

    public static Epic createEpic2() {
        return new Epic("Эпик 2",
                "Эпик 2");
    }

}
