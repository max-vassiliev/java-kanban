import entries.Epic;
import entries.Subtask;
import entries.Task;
import tests.*;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

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

    }

}
