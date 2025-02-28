package tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {

    Epic epic;
    SubTask subTask;
    SubTask subTask1;
    TaskManager taskManager;


    @BeforeEach
    public void unit() {
        taskManager = Managers.getDefault();
        epic = new Epic("Купить квартиру", "-", Status.NEW, 1, Duration.ofMinutes(1), Instant.ofEpochSecond(10));
        subTask = new SubTask("Накопить", "1000", Status.NEW, 2,epic,Duration.ofMinutes(5),Instant.ofEpochSecond(100));
        subTask1 = new SubTask("Накопить", "1000", Status.NEW, 3,epic,Duration.ofMinutes(5),Instant.ofEpochSecond(1200));
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);
    }

    @Test
    void getSubTasks() {
        Integer expected = 2;
        Integer actually = epic.getSubTasks().size();

        assertEquals(expected, actually);
    }

    @Test
    void epicStartTime() {
epic.setStartTime();
        Instant expected = epic.getStartTime();
        Instant actually = Instant.ofEpochSecond(100);

        assertEquals(expected, actually);
    }

    @Test
    void getEndTime() {
        epic.setEndTime();
        Instant expected = epic.getEndTime();
        Instant actually = Instant.ofEpochSecond(1500);

        assertEquals(expected, actually);
    }
}