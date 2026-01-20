package ru.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.service.Managers;
import ru.project.service.TaskManager;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    private Task task;
    private TaskManager taskManager;

    @BeforeEach
    public void unit() {

        task = new Task("Приготовить кофе", "-");
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(Instant.ofEpochSecond(10));
        taskManager = Managers.getDefault();
    }

    @Test
    void testCreateTask() {
        taskManager.createTask(task);

        assertNotNull(taskManager.getListTask().get(task.getId()));
    }
}