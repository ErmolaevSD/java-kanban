package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        task1 = new Task("Первая задача", "-", Status.NEW, 2, Duration.ofMinutes(10), Instant.ofEpochSecond(1000000));
        task = new Task("Первая задача", "-", Status.NEW, 1, Duration.ofMinutes(100), Instant.ofEpochSecond(10));
        epic = new Epic("Первый эпик", "-", Status.NEW, 3, Duration.ofMinutes(5), Instant.ofEpochSecond(1000));
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 4, epic,Duration.ofMinutes(10), Instant.ofEpochSecond(10000));
        subTask1 = new SubTask("Второй сабтаск", "-", Status.NEW, 5, epic,Duration.ofMinutes(15), Instant.ofEpochSecond(50000));
    }
}

