package ru.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.service.Managers;
import ru.project.service.TaskManager;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private Epic epic;
    private SubTask subTask;
    private TaskManager taskManager;


    @BeforeEach
    public void unit() {
        taskManager = Managers.getDefault();

        epic = new Epic("Купить квартиру", "-", Instant.ofEpochSecond(10), Duration.ofMinutes(10));
        subTask = new SubTask("Накопить", "-", 0, Duration.ofMinutes(2), Instant.ofEpochSecond(20000000));

    }

    @Test
    void getSubTasks() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        Integer expected = 1;
        Integer actually = epic.getSubTasks().size();

        assertEquals(expected, actually);
    }
}