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
                "15.10.2022, 14:00",        // TODO вернуть на ноябрь
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

    public static Task updateTask2(Task task) {
        Task update = new Task("Task2",
                "Description task2",
                "NEW",
                "15.11.2022, 14:00",        // TODO меняем на ноябрь
                "00:45"
        );

        update.setId(task.getId());
        update.setType(task.getType());
        update.setBackupStartTime(task.getBackupStartTime());
        update.setBackupDuration(task.getBackupDuration());
        return update;
    }

    public static Subtask updateEpic1Subtask1(Subtask subtask) {
        Subtask update = new Subtask("Epic1 SubTask1",
                "Description epic1 subTask1",
                "IN_PROGRESS",              // TODO поменяли статус
                "Epic1",
                "18.10.2022, 14:00",      // TODO сдвинули на день раньше
                "01:15"
        );

        update.setId(subtask.getId());
        update.setEpicId(subtask.getEpicId());
        update.setType(subtask.getType());
        update.setBackupStartTime(subtask.getBackupStartTime());
        update.setBackupDuration(subtask.getBackupDuration());
        update.setEpicStartTime(subtask.isEpicStartTime());
        update.setEpicEndTime(subtask.isEpicEndTime());
        return update;
    }

    public static Subtask updateEpic1Subtask3(Subtask subtask) {
        Subtask update = new Subtask("Epic1 SubTask3",
                "Description epic1 subTask3",
                "IN_PROGRESS",              // TODO поменяли статус
                "Epic1",
                "17.10.2022, 10:00",      // TODO сдвинули на день раньше
                "01:15"
        );

        update.setId(subtask.getId());
        update.setEpicId(subtask.getEpicId());
        update.setType(subtask.getType());
        update.setBackupStartTime(subtask.getBackupStartTime());
        update.setBackupDuration(subtask.getBackupDuration());
        update.setEpicStartTime(subtask.isEpicStartTime());
        update.setEpicEndTime(subtask.isEpicEndTime());
        return update;
    }

    public static Subtask updateEpic1Subtask2(Subtask subtask) {
        Subtask update = new Subtask("Epic1 SubTask2",
                "Description epic1 subTask2",
                "IN_PROGRESS",              // TODO поменяли статус
                "Epic1",
                "19.10.2022, 10:00",      // TODO сдвинули на день раньше
                "01:15"
        );

        update.setId(subtask.getId());
        update.setEpicId(subtask.getEpicId());
        update.setType(subtask.getType());
        update.setBackupStartTime(subtask.getBackupStartTime());
        update.setBackupDuration(subtask.getBackupDuration());
        update.setEpicStartTime(subtask.isEpicStartTime());
        update.setEpicEndTime(subtask.isEpicEndTime());
        return update;
    }
}
