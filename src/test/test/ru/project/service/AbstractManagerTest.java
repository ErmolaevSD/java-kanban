package ru.project.service;

import org.junit.jupiter.api.BeforeEach;
import ru.project.model.Epic;
import ru.project.model.SubTask;
import ru.project.model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public abstract class AbstractManagerTest {

    protected File file;
    protected Task task;
    protected Epic epic;
    protected Epic epic1;
    protected Epic epic2;
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

        epic2 = new Epic("Купить квартиру", "-", Instant.ofEpochSecond(100000), Duration.ofMinutes(2));
        epic = new Epic("Купить квартиру", "-", Instant.ofEpochSecond(10), Duration.ofMinutes(10));
        epic1 = new Epic("Купить квартиру", "-", Instant.ofEpochSecond(120), Duration.ofMinutes(10));
        subTask = new SubTask("Накопить", "-", 0, Duration.ofMinutes(2), Instant.ofEpochSecond(20000000));
        subTask1 = new SubTask("Накопить", "-", 0, Duration.ofMinutes(1), Instant.ofEpochSecond(100));
        task = new Task("Приготовить кофе", "-");
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(Instant.ofEpochSecond(10));
    }
}

