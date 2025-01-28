package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class main {

    public static void main(String[] args) {

        HistoryManager historyManager;
        InMemoryTaskManager inMemoryTaskManager;
        historyManager = Managers.getDefaultHistory();
        inMemoryTaskManager = new InMemoryTaskManager(historyManager);

        Task task = new Task("Первая задача", "-", Status.NEW, 1);
        Task task1 = new Task("Вторая задача", "-", Status.NEW, 2);
        Epic epic = new Epic("Первый эпик", "-", Status.NEW, 3);
        Epic epic1 = new Epic("Второй эпик", "-", Status.NEW, 4);
        SubTask subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 5, epic);
        SubTask subTask1 = new SubTask("Второй сабтаск", "-", Status.NEW, 6, epic);
        SubTask subTask2 = new SubTask("Третий сабтаск", "-", Status.NEW, 7, epic);

        inMemoryTaskManager.addNewTask(task);
        inMemoryTaskManager.addNewTask(task1);

        inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewEpic(epic1);

        inMemoryTaskManager.addNewSubTask(subTask);
        inMemoryTaskManager.addNewSubTask(subTask1);
        inMemoryTaskManager.addNewSubTask(subTask2);

        System.out.println("Список задач: " + inMemoryTaskManager.printListTask().size());
        System.out.println("Список эпиков: " + inMemoryTaskManager.printListEpicTask().size());
        System.out.println("Список тасков: " + inMemoryTaskManager.printListSubTask().size());

        inMemoryTaskManager.findTask(task.getId());
        inMemoryTaskManager.findTask(task1.getId());
        inMemoryTaskManager.findEpicTask(epic.getId());
        inMemoryTaskManager.findEpicTask(epic1.getId());
        inMemoryTaskManager.findSubTask(subTask.getId());
        inMemoryTaskManager.findSubTask(subTask1.getId());
        inMemoryTaskManager.findSubTask(subTask2.getId());

        System.out.println("Список посмотренных: " + historyManager.getHistory().size());
        System.out.println("Список посмотренных: " + historyManager.getHistory());

        inMemoryTaskManager.findSubTask(subTask.getId());

        System.out.println("Список посмотренных: " + historyManager.getHistory().size());
        System.out.println("Список посмотренных: " + historyManager.getHistory());

        inMemoryTaskManager.deleteTask(task);
        inMemoryTaskManager.deleteEpicTask(epic);

        System.out.println("Список посмотренных: " + historyManager.getHistory().size());
        System.out.println("Список посмотренных: " + historyManager.getHistory());

    }
}
