package tests;

import entries.Subtask;
import entries.Task;

public class Round2 {

    // МЕНЯЕМ СТАТУСЫ НЕКОТОРЫХ ЗАДАЧ

    // в задаче task1 меняем статус на DONE
    public static Task updateTask1(Task task) {
        int taskId = task.getId();
        return new Task(taskId,
                "Позвонить бабушке",
                "Дозвониться до бабушки. Узнать, как дела.",
                "DONE"); // новый статус
    }

    // в подзадаче 1 эпика 1 меняем статус на IN_PROGRESS
    public static Subtask updateEpic1Subtask1(Subtask subtask) {
        int subtaskId = subtask.getId();
        int relatedEpicId = subtask.getRelatedEpicId();
        Subtask subtaskUpdate = new Subtask(subtaskId,
                "Купить продукты",
                "Узнать у бабушки, что купить. Сходить в магазин.",
                "IN_PROGRESS", // новый статус
                "Навестить бабушку");
        subtaskUpdate.setRelatedEpicId(relatedEpicId);
        return subtaskUpdate;
    }

    // в подзадаче 1 эпика 2 меняем статус на DONE
    public static Subtask updateEpic2Subtask1(Subtask subtask) {
        int subtaskId = subtask.getId();
        int relatedEpicId = subtask.getRelatedEpicId();
        Subtask subtaskUpdate = new Subtask(subtaskId,
                "Сдать ДЗ",
                "Доделать финальный проект и отправить на проверку.",
                "DONE", // новый статус
                "Спринт 3");
        subtaskUpdate.setRelatedEpicId(relatedEpicId);
        return subtaskUpdate;
    }
}
