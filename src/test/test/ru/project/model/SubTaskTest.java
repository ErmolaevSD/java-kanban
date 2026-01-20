package ru.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.service.Managers;
import ru.project.service.TaskManager;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    private Epic epic;
    private SubTask subTask;
    private TaskManager taskManager;

    @BeforeEach
    public void util() {

        epic = new Epic("Купить квартиру", "-", Instant.ofEpochSecond(10), Duration.ofMinutes(10));
        subTask = new SubTask("Накопить", "-", 0, Duration.ofMinutes(2), Instant.ofEpochSecond(20000));
        taskManager = Managers.getDefault();
    }

    @Test
    void testGetParentTask() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        assertEquals(epic.getId(), subTask.getParentTask());
    }
}