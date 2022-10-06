import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tests.sprint05.HistoryTestR1;
import tests.sprint05.HistoryTestR4;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        /* -------------------------------
           ТЕСТ
           цель: проверить, как работает история просмотров
           ------------------------------- */

        /* РАУНД 1
           цель: проверить, что задачи при вызове
           попадают в историю просмотров  */

       /*  создаем задачи:
           — две стандартные задачи
           — один эпик с тремя подзадачами
           — один эпик без подзадач  */
        Task task1 = HistoryTestR1.createTask1();
        Task task2 = HistoryTestR1.createTask2();
        Epic epic1 = HistoryTestR1.createEpic1();
        Subtask subtask1 = HistoryTestR1.createEpic1Subtask1();
        Subtask subtask2 = HistoryTestR1.createEpic1Subtask2();
        Subtask subtask3 = HistoryTestR1.createEpic1Subtask3();
        Epic epic2 = HistoryTestR1.createEpic2();

        // добавляем задачи в менеджер, генерируем ID
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(epic1);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.add(subtask3);
        taskManager.add(epic2);

        // запрашиваем задачи в произвольном порядке
        taskManager.getTask(task1.getId());         // Задача 1
        taskManager.getEpic(epic1.getId());         // Эпик 1
        taskManager.getSubtask(subtask3.getId());   // Подзадача 3
        taskManager.getSubtask(subtask1.getId());   // Подзадача 1
        taskManager.getEpic(epic2.getId());         // Эпик 2
        taskManager.getTask(task2.getId());         // Задача 2
        taskManager.getSubtask(subtask2.getId());   // Подзадача 2

        // печатаем результат
        printHistoryTest(1, taskManager);
        /*
        Ожидаемый порядок вывода:
        Задач в истории: 7
		    Задача 1
		    Эпик 1
		    Подзадача 3
		    Подзадача 1
		    Эпик 2
		    Задача 2
		    Подзадача 2
        */


        /* РАУНД 2
           цель: проверить, что при повторном вызове
           меняется положение задач в истории просмотров
           и что в истории нет повторов */

        // запрашиваем задачи
        taskManager.getEpic(epic1.getId());         // Эпик 1
        taskManager.getSubtask(subtask1.getId());   // Подзадача 1
        taskManager.getSubtask(subtask2.getId());   // Подзадача 2
        taskManager.getTask(task1.getId());         // Задача 1

        // печатаем результат
        printHistoryTest(2, taskManager);
        /*
        Ожидаемый порядок вывода:
        Задач в истории: 7
		    Подзадача 3
		    Эпик 2
		    Задача 2
		    Эпик 1
		    Подзадача 1
		    Подзадача 2
		    Задача 1
        */


        /* РАУНД 3
           цель: еще раз убедиться, что при повторном вызове
           задачи попадают в конец истории просмотров */

        // повторно запрашиваем некоторые задачи из Раунда 2
        taskManager.getSubtask(subtask1.getId());   // Подзадача 1
        taskManager.getEpic(epic1.getId());         // Эпик 1

        // печатаем результат
        printHistoryTest(3, taskManager);
        /*
        Ожидаемый порядок вывода:
        Задач в истории: 7
		    Подзадача 3
            Эпик 2
            Задача 2
            Подзадача 2
            Задача 1
            Подзадача 1
            Эпик 1
        */


        /* РАУНД 4
           цель: проверить, что история просмотров
           будет хранить не более 10 задач */

        // сейчас есть семь задач
        // создаем еще четыре и добавляем в менеджер
        Task bonusTask1 = HistoryTestR4.createBonusTask1();
        Task bonusTask2 = HistoryTestR4.createBonusTask2();
        Task bonusTask3 = HistoryTestR4.createBonusTask3();
        Task bonusTask4 = HistoryTestR4.createBonusTask4();
        taskManager.add(bonusTask1);
        taskManager.add(bonusTask2);
        taskManager.add(bonusTask3);
        taskManager.add(bonusTask4);

        // запрашиваем новые задачи
        taskManager.getTask(bonusTask4.getId());    //  Еще задача 4
        taskManager.getTask(bonusTask2.getId());    //  Еще задача 2
        taskManager.getTask(bonusTask1.getId());    //  Еще задача 1
        taskManager.getTask(bonusTask3.getId());    //  Еще задача 3
        taskManager.getTask(bonusTask4.getId());    //  Еще задача 4 (повтор)

        // печатаем результат
        printHistoryTest(4, taskManager);
        /*
        Ожидаемый порядок вывода:
        Задач в истории: 10
		    Эпик 2
		    Задача 2
		    Подзадача 2
		    Задача 1
		    Подзадача 1
		    Эпик 1
		    Еще задача 2
		    Еще задача 1
		    Еще задача 3
		    Еще задача 4
        */


        /* РАУНД 5
           цель: проверить, что при удалении
           задача пропадет из истории просмотров */

        // удаляем задачу
        taskManager.delete(task1);  // Задача 1

        // печатаем результат
        printHistoryTest(5, taskManager);
        /*
        Ожидаемый порядок вывода:
        Задач в истории: 9
		    Эпик 2
		    Задача 2
		    Подзадача 2
		    Подзадача 1
		    Эпик 1
		    Еще задача 2
		    Еще задача 1
		    Еще задача 3
		    Еще задача 4
		- - -
		Пропадет из истории:
		    Задача 1
        */


        /* РАУНД 6
           цель: проверить, что при удалении эпика
           из истории пропадет сам эпик и его подзадачи */

        // удаляем эпик, где есть подзадачи
        taskManager.delete(epic1);      // Эпик 1

        // печатаем результат
        printHistoryTest(6, taskManager);
        /*
        Ожидаемый порядок вывода:
        Задач в истории: 6
		    Эпик 2
		    Задача 2
		    Еще задача 2
		    Еще задача 1
		    Еще задача 3
		    Еще задача 4
		- - -
		Пропадут из истории:
		    Подзадача 2
		    Подзадача 1
		    Эпик 1
        */

    }

    public static void printHistoryTest(int roundNumber, TaskManager taskManager) {
        System.out.println("-----------------------------");
        System.out.println("РАУНД " + roundNumber);
        System.out.println("-----------------------------");
        System.out.println("Задач в истории: " + taskManager.getHistory().size());
        System.out.println(taskManager.getHistory());
        System.out.println("----------------------------- \n \n");
    }
}
