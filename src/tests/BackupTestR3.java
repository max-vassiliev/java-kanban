package tests;

import tasks.Subtask;

public class BackupTestR3 {

    public static Subtask updateEpic1Subtask1(Subtask subtask) {
        Subtask update = new Subtask("Epic1 SubTask1",
                "Description epic1 subTask1",
                "DONE",
                "Epic1");

        update.setId(subtask.getId());
        update.setRelatedEpicId(subtask.getRelatedEpicId());
        update.setType(subtask.getType());
        return update;
    }

    public static Subtask updateEpic2Subtask1(Subtask subtask) {
        Subtask update = new Subtask("Epic2 SubTask1",
                "Description epic2 subTask1",
                "IN_PROGRESS",
                "Epic2");

        update.setId(subtask.getId());
        update.setRelatedEpicId(subtask.getRelatedEpicId());
        update.setType(subtask.getType());
        return update;
    }

}
