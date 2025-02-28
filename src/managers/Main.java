package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {

        File file = new File("data.csv");
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(historyManager, file);
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Первая задача", "-", Status.NEW, 1,Duration.ofMinutes(5),Instant.ofEpochSecond(50000));
        Task task1 = new Task("Вторая задача", "-", Status.NEW, 2,Duration.ofMinutes(4),Instant.ofEpochSecond(10));
        Epic epic = new Epic("Первый эпик", "-", Status.NEW, 3, Duration.ofMinutes(10),Instant.ofEpochSecond(10));
        SubTask subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 5, epic, Duration.ofMinutes(20),Instant.ofEpochSecond(10));
        SubTask subTask1 = new SubTask("Первый сабтаск", "-", Status.NEW, 5, epic, Duration.ofMinutes(20),Instant.ofEpochSecond(10));

        fileBackedTaskManager.addNewTask(task);
        fileBackedTaskManager.addNewTask(task1);
        fileBackedTaskManager.addNewSubTask(subTask);
        fileBackedTaskManager.addNewSubTask(subTask1);
        fileBackedTaskManager.addNewEpic(epic);


        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

        System.out.println(task.getStartTime());

        System.out.println(taskManager.getTaskPriotity());

        Managers.loadFrom(file);
    }
}
