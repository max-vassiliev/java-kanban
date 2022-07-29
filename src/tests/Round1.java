package tests;

import entries.Epic;
import entries.Subtask;
import entries.Task;

public class Round1 {

    // СОЗДАЕМ ЗАДАЧИ РАЗНЫХ ТИПОВ

    // создать задачу task1
    public static Task createTask1() {
        return new Task(0,
                "Позвонить бабушке",
                "Дозвониться до бабушки. Узнать, как дела.",
                "NEW");
    }

    // создать задачу task2
    public static Task createTask2() {
        return new Task(0,
                "Написать маме",
                "Написать маме сообщение о том, как дела у бабушки.",
                "NEW");
    }

    // создать эпик epic1
    public static Epic createEpic1() {
        return new Epic(0,
                "Навестить бабушку",
                "Съездить к бабушке, привезти продукты, помочь по дому.",
                null);
    }

    // создать эпик epic2
    public static Epic createEpic2() {
        return new Epic(0,
                "Спринт 3",
                "Пройти теорию, послушать вебинары, сдать проект.",
                null
        );
    }

    // создать для эпика 1 подзадачу 1
    public static Subtask createEpic1Subtask1() {
        return new Subtask(0,
                "Купить продукты",
                "Узнать у бабушки, что купить. Сходить в магазин.",
                "NEW",
                "Навестить бабушку"
        );
    }

    // создать для эпика 1 подзадачу 2
    public static Subtask createEpic1Subtask2() {
        return new Subtask(0,
                "Съездить к бабушке",
                "Привезти бабушке продукты, вместе попить чай, помочь по дому.",
                "NEW",
                "Навестить бабушку"
        );
    }

    // создать для эпика 2 подзадачу 1
    public static Subtask createEpic2Subtask1() {
        return new Subtask(0,
                "Сдать ДЗ",
                "Доделать финальный проект и отправить на проверку.",
                "NEW",
                "Спринт 3"
        );
    }
}
