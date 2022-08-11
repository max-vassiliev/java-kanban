import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tests.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Тестируем приложение

        // РАУНД 1

        // Раунд 1: создаем объекты
        Task task1 = Round1.createTask1();
        Task task2 = Round1.createTask2();
        Epic epic1 = Round1.createEpic1();
        Epic epic2 = Round1.createEpic2();
        Subtask epic1Subtask1 = Round1.createEpic1Subtask1();
        Subtask epic1Subtask2 = Round1.createEpic1Subtask2();
        Subtask epic2Subtask1 = Round1.createEpic2Subtask1();

        // Раунд 1: добавляем объекты в менеджер, генерируем ID
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(epic1Subtask1);
        manager.addSubtask(epic1Subtask2);
        manager.addSubtask(epic2Subtask1);

        // Раунд 1: печатаем все объекты
        printEntries(1, manager);
        System.out.println();

        // РАУНД 2

        // Раунд 2: меняем статусы некоторых объектов
        task1 = Round2.updateTask1(task1); // DONE
        epic1Subtask1 = Round2.updateEpic1Subtask1(epic1Subtask1); // IN_PROGRESS
        epic2Subtask1 = Round2.updateEpic2Subtask1(epic2Subtask1); // DONE

        // Раунд 2: добавляем обновления в программу
        manager.updateTask(task1);
        manager.updateSubtask(epic1Subtask1);
        manager.updateSubtask(epic2Subtask1);

        // Раунд 2: печатаем обновленные объекты и родственные эпики
        System.out.println("-----------------------------");
        System.out.println("РАУНД 2");
        System.out.println("-----------------------------");
        System.out.println("Задача '" + task1.getTitle() + "':");
        System.out.println(manager.getTask(task1.getId()));
        System.out.println();
        System.out.println("Подзадача '" + epic1Subtask1.getTitle() + "':");
        System.out.println(manager.getSubtask(epic1Subtask1.getId()));
        Epic relatedEpic1 = manager.getEpic(epic1Subtask1.getRelatedEpicId());
        System.out.println("Входит в эпик '" + relatedEpic1.getTitle() + "':");
        System.out.println(manager.getEpic(epic1Subtask1.getRelatedEpicId()));
        System.out.println();
        System.out.println("Подзадача '" + epic2Subtask1.getTitle() + "':");
        System.out.println(manager.getSubtask(epic2Subtask1.getId()));
        Epic relatedEpic2 = manager.getEpic(epic2Subtask1.getRelatedEpicId());
        System.out.println("Входит в эпик '" + relatedEpic2.getTitle() + "':");
        System.out.println(manager.getEpic(epic2Subtask1.getRelatedEpicId()));
        System.out.println();

        // ИСТОРИЯ ПРОСМОТРОВ - ТЕСТ 1

        // печатаем историю
        System.out.println("-----------------------------");
        System.out.println("ИСТОРИЯ ПРОСМОТРОВ");
        System.out.println("-----------------------------");
        System.out.println("История просмотров [Тест 1]");
        System.out.println("Всего " + (manager.getHistory().size()) + " задач:");
        System.out.println(manager.getHistory());

        // ИСТОРИЯ ПРОСМОТРОВ - ТЕСТ 2

        // получаем задачи, добавляем их в историю просмотров
        manager.getEpic(3);
        manager.getEpic(4);
        manager.getTask(2);
        manager.getSubtask(7);

        // печатаем историю просмотров
        System.out.println("-----------------------------");
        System.out.println("История просмотров [Тест 2]");
        System.out.println("Всего " + (manager.getHistory().size()) + " задач:");
        System.out.println(manager.getHistory());

        // ИСТОРИЯ ПРОСМОТРОВ - ТЕСТ 3

        // получаем задачу, добавляем ее в историю просмотров
        manager.getTask(1);

        // печатаем историю просмотров
        System.out.println("-----------------------------");
        System.out.println("История просмотров [Тест 3]");
        System.out.println("Всего " + (manager.getHistory().size()) + " задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n \n");

        // РАУНД 3

        // Раунд 3: удаляем одну задачу и один эпик
        manager.deleteTask(task1);
        manager.deleteEpic(epic1);

        // Раунд 3: смотрим, что осталось
        printEntries(3, manager);

    }

    public static void printEntries(int roundNumber, TaskManager manager) {
        System.out.println("-----------------------------");
        System.out.println("РАУНД " + roundNumber);
        System.out.println("-----------------------------");
        System.out.println("Список задач:");
        System.out.println(manager.getTasks());
        System.out.println("-----------------------------");
        System.out.println("Список эпиков:");
        System.out.println(manager.getEpics());
        System.out.println("-----------------------------");
        System.out.println("Список подзадач:");
        System.out.println(manager.getSubtasks());
    }
}
