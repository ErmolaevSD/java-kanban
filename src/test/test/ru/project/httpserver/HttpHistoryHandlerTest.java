package ru.project.httpserver;

import org.junit.jupiter.api.Test;
import ru.project.model.Epic;
import ru.project.model.SubTask;
import ru.project.model.Task;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpHistoryHandlerTest extends BaseHttpTest {

    @Test
    void testGetHistory_shouldIsOk() {

        Epic parentEpic = new Epic("Parent Epic",
                "Description Parent Epic",
                Instant.ofEpochSecond(10),
                Duration.ofMinutes(1));
        taskManager.createEpic(parentEpic);

        SubTask subTask = new SubTask("Name SubTask",
                "Description SubTask",
                parentEpic.getId(),
                Duration.ofMinutes(1),
                Instant.ofEpochSecond(10000));

        Task task = new Task("Name Task", "Description Task");

        taskManager.createSubTask(subTask);
        taskManager.createTask(task);

        assertTrue(historyManager.getHistory().isEmpty());

        taskManager.findEpicTaskById(0);

        assertEquals(1, historyManager.getHistory().size());

        taskManager.findSubTaskById(1);

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(parentEpic, historyManager.getHistory().getFirst());
        assertEquals(subTask, historyManager.getHistory().getLast());

        taskManager.findTaskById(2);
        assertEquals(task, historyManager.getHistory().getLast());

        taskManager.findEpicTaskById(0);
        assertEquals(subTask, historyManager.getHistory().getFirst());
        assertEquals(parentEpic, historyManager.getHistory().getLast());
    }
}