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

        task1 = new Task("Первая задача", "-", Status.NEW, 2, Duration.ofMinutes(15), Instant.now().minusSeconds(10000));
        task = new Task("Первая задача", "-", Status.NEW, 1, Duration.ofMinutes(1), Instant.now().minusSeconds(100));
        epic = new Epic("Первый эпик", "-", Status.NEW, 3, Duration.ofMinutes(5), Instant.now().minusSeconds(50));
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 4, epic,Duration.ofMinutes(10), Instant.now().minusSeconds(6000));
    }
}
