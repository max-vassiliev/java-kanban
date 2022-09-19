package tests;

import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class BackupTestBonus {

    public static void backupTestBonus(FileBackedTaskManager taskManager) {

     /* -------------------------------
        ТЕСТ (BONUS)

        Чтобы тест запустился, нужно раскомментировать часть кода
        в конце метода main класса FileBackedTaskManager:

            String backupFileBonus = "backup-bonus.csv";
            FileBackedTaskManager taskManagerBonus = loadFromFile(new File(HOME, backupFileBonus));
            BackupTestBonus.backupTestBonus(taskManagerBonus);
            - конец -

        Тест состоит из шести раундов.
        По умолчанию все раунды заключены в комментарии.

        Чтобы протестировать все раунды, нужно последовательно
        раскомментировать код каждого следующего раунда.
        Перед запуском нового раунда нужно полностью очищать содержимое
        файла с резервной копией (resources/backup-bonus.csv).

        Если не очистить резервную копию перед новым тестом,
        код запустится, но объекты продублируются с разными id.

        Закомментированный код для каждого раунда отделен отбивками:

        *** Раунд N. НАЧАЛО 🚀 ***

        <здесь код, который нужно раскомментировать>

        *** Раунд N. КОНЕЦ 🛑 ***

        ------------------------------- */



/*      --------------------------
        РАУНД 1
        --------------------------

        Задачи:
            — проверить, как менеджер читает пустой файл
            — убедиться, что в файле сохраняются добавленные задачи
            — также убедиться, что в файле сохраняется история просмотров
*/

//      *** Раунд 1. НАЧАЛО 🚀 ***

//        Task task1 = BackupTestR1.createTask1();
//        Epic epic2 = BackupTestR1.createEpic2();
//        Subtask epic2subtask2 = BackupTestR1.createEpic2Subtask2();
//
//        taskManager.add(task1);                             // [id=1] Task1
//        taskManager.add(epic2);                             // [id=2] Epic2
//        taskManager.add(epic2subtask2);                     // [id=3] Epic2 Subtask2
//
//        taskManager.getEpic(epic2.getId());                 // [id=2] Epic2
//        taskManager.getSubtask(epic2subtask2.getId());      // [id=3] Epic2 Subtask2

//      *** Раунд 1. КОНЕЦ 🛑 ***

/*      Ожидаемый вывод
        ------------------
        id,type,name,status,description,epic
        1,TASK,Task1,NEW,Description task1,
        2,EPIC,Epic2,DONE,Description epic2,
        3,SUBTASK,Epic2 SubTask2,DONE,Description epic2 subtask2,2

        2,3
        ------------------
*/



/*      --------------------------
        РАУНД 2
        --------------------------

        Задачи:
            — проверить, как менеджер читает заполненный файл
            — убедиться, что новые задачи добавляются верно
            — убедиться, что в истории просмотров не дублируются id задач
            — проверить работу метода определения статуса эпика
*/

//      *** Раунд 2. НАЧАЛО 🚀 ***

//        Task task2 = BackupTestR2.createTask2();
//        Epic epic1 = BackupTestR2.createEpic1();
//        Subtask epic1subtask1 = BackupTestR2.createEpic1Subtask1();
//        Subtask epic2Subtask1 = BackupTestR2.createEpic2Subtask1();
//
//        taskManager.add(task2);                             // [id=4] Task2
//        taskManager.add(epic1);                             // [id=5] Epic1
//        taskManager.add(epic1subtask1);                     // [id=6] Epic1 Subtask1
//        taskManager.add(epic2Subtask1);                     // [id=7] Epic2 Subtask1
//
//        taskManager.getSubtask(epic1subtask1.getId());      // [id=6] Epic1 Subtask1
//        taskManager.getEpic(epic1.getId());                 // [id=5] Epic1
//        taskManager.getSubtask(epic2Subtask1.getId());      // [id=7] Epic2 Subtask1
//        taskManager.getEpic(epic2.getId());                 // [id=2] Epic2          - повтор
//        taskManager.getSubtask(epic1subtask1.getId());      // [id=6] Epic1 Subtask1 - повтор

//      *** Раунд 2. КОНЕЦ 🛑 ***

/*      Ожидаемый вывод
        ------
        id,type,name,status,description,epic
        1,TASK,Task1,NEW,Description task1,
        2,EPIC,Epic2,IN_PROGRESS,Description epic2,
        3,SUBTASK,Epic2 SubTask2,DONE,Description epic2 subtask2,2
        4,TASK,Task2,NEW,Description task2,
        5,EPIC,Epic1,NEW,Description epic1,
        6,SUBTASK,Epic1 SubTask1,NEW,Description epic1 subTask1,5
        7,SUBTASK,Epic2 SubTask1,NEW,Description epic2 subTask1,2

        3,5,7,2,6
        ------
        Статус эпика Epic2 (id=2) должен измениться с DONE на IN_PROGRESS —
        после того, как к нему добавили подзадачу Subtask1 (id=7) со статусом NEW.
*/



/*      --------------------------
        РАУНД 3
        --------------------------

        Задача: протестировать методы обновления и удаления задач.  */

//      *** Раунд 3. НАЧАЛО 🚀 ***

//        epic1subtask1 = BackupTestR3.updateEpic1Subtask1(epic1subtask1);
//        epic2Subtask1 = BackupTestR3.updateEpic2Subtask1(epic2Subtask1);
//
//        taskManager.update(epic1subtask1);                  // [id=6] Epic1 Subtask1
//        taskManager.update(epic2Subtask1);                  // [id=7] Epic2 Subtask1
//        taskManager.delete(task2);                          // [id=4] Task2

//      *** Раунд 3. КОНЕЦ 🛑 ***

/*      Ожидаемый вывод
        ------
        id,type,name,status,description,epic
        1,TASK,Task1,NEW,Description task1,
        2,EPIC,Epic2,IN_PROGRESS,Description epic2,
        3,SUBTASK,Epic2 SubTask2,DONE,Description epic2 subtask2,2
        5,EPIC,Epic1,DONE,Description epic1,
        6,SUBTASK,Epic1 SubTask1,DONE,Description epic1 subTask1,5
        7,SUBTASK,Epic2 SubTask1,IN_PROGRESS,Description epic2 subTask1,2

        3,5,7,2,6
        ------
        Из истории пропадет задача Task2 (id=4).
        Статус эпика Epic1 (id=5) должен измениться с NEW на DONE —
        после изменения статуса подзадачи Subtask1 (id=6) на статус DONE.

*/



/*      --------------------------
        РАУНД 4
        --------------------------

        Задача: проверить ограничение на максимальное число задач в истории.  */

//      *** Раунд 4. НАЧАЛО 🚀 ***

//        Task bonusTask1 = BackupTestR4.createBonusTask1();
//        Task bonusTask2 = BackupTestR4.createBonusTask2();
//        Task bonusTask3 = BackupTestR4.createBonusTask3();
//        Task bonusTask4 = BackupTestR4.createBonusTask4();
//        Task bonusTask5 = BackupTestR4.createBonusTask5();
//
//        taskManager.add(bonusTask1);                           // [id=8]  BonusTask1
//        taskManager.add(bonusTask2);                           // [id=9]  BonusTask2
//        taskManager.add(bonusTask3);                           // [id=10] BonusTask3
//        taskManager.add(bonusTask4);                           // [id=11] BonusTask4
//        taskManager.add(bonusTask5);                           // [id=12] BonusTask5
//
//        taskManager.getTask(bonusTask1.getId());               // [id=8]  BonusTask1
//        taskManager.getTask(bonusTask2.getId());               // [id=9]  BonusTask2
//        taskManager.getTask(bonusTask3.getId());               // [id=10] BonusTask3
//        taskManager.getTask(bonusTask4.getId());               // [id=11] BonusTask4
//        taskManager.getTask(bonusTask5.getId());               // [id=12] BonusTask5
//        taskManager.getEpic(epic1.getId());                    // [id=5]  Epic1         - повтор
//        taskManager.getTask(bonusTask3.getId());               // [id=10] BonusTask3    - повтор

//      *** Раунд 4. КОНЕЦ 🛑 ***

/*      Ожидаемый вывод
        ------
        id,type,name,status,description,epic
        1,TASK,Task1,NEW,Description task1,
        2,EPIC,Epic2,IN_PROGRESS,Description epic2,
        3,SUBTASK,Epic2 SubTask2,DONE,Description epic2 subtask2,2
        5,EPIC,Epic1,DONE,Description epic1,
        6,SUBTASK,Epic1 SubTask1,DONE,Description epic1 subTask1,5
        7,SUBTASK,Epic2 SubTask1,IN_PROGRESS,Description epic2 subTask1,2
        8,TASK,BonusTask1,NEW,Description bonus task1,
        9,TASK,BonusTask2,NEW,Description bonus task2,
        10,TASK,BonusTask3,NEW,Description bonus task3,
        11,TASK,BonusTask4,NEW,Description bonus task4,
        12,TASK,BonusTask5,NEW,Description bonus task5,

        3,7,2,6,8,9,11,12,5,10
        ------
        При добавлении и вызове пяти новых задач
        текущее количество задач в проекте вырастет до 11.
        Сработает ограничение по количеству задач в истории просмотров —
        в ней будут отображены последние десять задач.
        Эпик Epic1 (id=5) при повторном вызове переместится
        со второй позиции в истории просмотров на девятую (предпоследнюю).
*/



/*      --------------------------
        РАУНД 5
        --------------------------

        Задача: проверить работу методов удаления всех задач.
*/

//      *** Раунд 5. НАЧАЛО 🚀 ***

//        taskManager.deleteAllTasks();
//        taskManager.deleteAllSubtasks();
//        taskManager.deleteAllEpics();

//      *** Раунд 5. КОНЕЦ 🛑 ***

/*      Ожидаемый вывод
        ------
        id,type,name,status,description,epic

        ------
*/



/*      --------------------------
        РАУНД 6
        --------------------------

        Задача: проверить работу менеджера после удаления всех задач.
*/

//      *** Раунд 6. НАЧАЛО 🚀 ***

//        Task task3 = BackupTestR6.createTask3();
//        Epic epic3 = BackupTestR6.createEpic3();
//        Subtask epic3Subtask1 = BackupTestR6.createEpic3Subtask1();
//
//        taskManager.add(task3);                                 // [id=13] Task3
//        taskManager.add(epic3);                                 // [id=14] Epic3
//        taskManager.add(epic3Subtask1);                         // [id=15] Epic3 Subtask 1
//
//        taskManager.getEpic(epic3.getId());                     // [id=14] Epic3
//        taskManager.getTask(task3.getId());                     // [id=13] Task3
//        taskManager.getSubtask(epic3Subtask1.getId());          // [id=15] Epic3 Subtask 1

//      *** Раунд 6. КОНЕЦ 🛑 ***

/*      Ожидаемый вывод
        ------
        id,type,name,status,description,epic
        13,TASK,Task3,NEW,Description task3,
        14,EPIC,Epic3,NEW,Description epic3,
        15,SUBTASK,Epic3 SubTask1,NEW,Description epic3 subTask1,14

	    14,13,15
        ------
*/


    }
}
