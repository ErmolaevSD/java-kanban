package managers;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public abstract class AbstractManagerTest {

    protected File file;
    protected Task task;
    protected Task task1;
    protected Task task3;
    protected Epic epic;
    protected SubTask subTask;
    protected SubTask subTask1;
    protected TaskManager taskManager;
    protected HistoryManager historyManager;



    @BeforeEach
    void unit() {
        try {
            file = Files.createTempFile("data-", ".csv").toFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getFileBackedTaskManager(file);


        task1 = new Task("Первая задача", "-", Status.NEW, null, Duration.ofMinutes(10), Instant.ofEpochSecond(1000000));
        task = new Task("Первая задача", "-", Status.NEW, null, Duration.ofMinutes(100), Instant.ofEpochSecond(10));
        epic = new Epic("Первый эпик", "-", Status.NEW, null, Duration.ofMinutes(5), Instant.ofEpochSecond(500000));
        taskManager.addNewEpic(epic);
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW, null, epic.getId(),Duration.ofMinutes(10), Instant.ofEpochSecond(100000000));
        task3 = new Task("Пересекается", "Должна быть ошибка", Status.NEW, null, Duration.ofMinutes(20), Instant.ofEpochSecond(500050));
        subTask1 = new SubTask("Второй сабтаск", "-", Status.DONE, null, epic.getId(),Duration.ofMinutes(15), Instant.ofEpochSecond(50000));
//        taskManager.addNewTask(task);
//        taskManager.addNewTask(task1);
//        taskManager.addNewTask(task3);
//        taskManager.addNewEpic(epic);
//        taskManager.addNewSubTask(subTask);
//        taskManager.addNewSubTask(subTask1);


    }
}

