import Managers.InMemoryHistoryManager;
import Managers.Managers;
import Managers.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

public class main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        Task task1 = taskManager.addNewTask(new Task("Сделать домашнее задание", "Качествено", Status.NEW, null));
        System.out.println(task1.hashCode());
        System.out.println();
    }
}
//
//        Task task1 = taskManager.addNewTask(new Task("Сделать домашнее задание", "Качествено", Status.NEW, null));
//        Task task2 = taskManager.addNewTask(new Task("Приготовить кофе", "Качествено", Status.NEW, null));
//
//        Epic epic1 = taskManager.addNewEpic(new Epic("Уборка квартиры", "Клининг", Status.NEW, null));
//        Epic epic2 = taskManager.addNewEpic(new Epic("Починить машину", "капитал", Status.NEW, null));
//
//        SubTask subTask1 = taskManager.addNewSubTask(new SubTask("Вымыть пол", "Средство", Status.DONE, null, epic1));
//        SubTask subTask2 = taskManager.addNewSubTask(new SubTask("Вымыть стекло", "Средство", Status.NEW, null,epic1));
//        SubTask subTask3 = taskManager.addNewSubTask(new SubTask("Ремонт двигателя", "Тотал", Status.IN_PROGRESS, null, epic2));
//
//
//        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
//        inMemoryHistoryManager.add(task2);
//        inMemoryHistoryManager.add(epic1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//        inMemoryHistoryManager.add(task1);
//
////
////
//        System.out.println(inMemoryHistoryManager.getHistory());
//

//
//        System.out.println("Замена задачи");
//        Tasks.Epic epic3 = new Tasks.Epic("Сделать домашнее молоко", "Качественно", Tasks.Status.DONE, 3);
//        inMemoryTaskManager.updateEpic(epic3);
//        System.out.println(inMemoryTaskManager.printListEpicTask());
//
//
//        System.out.println("Статусы");
//        System.out.println(epic1);
//        System.out.println(epic2);
//
//        System.out.println();
//        System.out.println("Удаление Такс");
//        inMemoryTaskManager.deleteNameTask(2);
//        System.out.println(inMemoryTaskManager.printListTask());
//        inMemoryTaskManager.deleteEpicTask(epic2);
//        System.out.println(inMemoryTaskManager.printListEpicTask());
//
//        System.out.println("Удаление 'эпика'");
//        inMemoryTaskManager.deleteEpicTask(epic1);
//        System.out.println(inMemoryTaskManager.printListEpicTask());
//        System.out.println(inMemoryTaskManager.printListSubTask());
////
//    }
//}
