package tasks;

import exception.ManagerTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
class TaskTest {

    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    public void unit() {
        task1 = new Task("Приготовить кофе", "Добавить молоко", Status.NEW, 1, Duration.ofMinutes(20), Instant.ofEpochSecond(10));

        task2 = new Task("Убрать квартиру", "Качественно", Status.NEW, 1,Duration.ofMinutes(5),Instant.ofEpochSecond(100));

        task3 = new Task("Убрать квартиру", "Качественно", Status.NEW, 1,Duration.ofMinutes(5),null);
    }

    @Test
    void testSetName() {
        String name = "Приготовить молоко";
        task1.setName(name);
        String newName = task1.getName();

        assertEquals(name, newName);
    }

    @Test
    void testSetDescription() {
        String description = "Приготовить молоко";
        task1.setDescription(description);
        String newDescription = task1.getDescription();

        assertEquals(description, newDescription);
    }

    @Test
    void testSetId() {
        int id = 2;
        task1.setId(id);
        int newId = task1.getId();

        assertEquals(id, newId);
    }

    @Test
    void testGetName() {
        String name = "Приготовить кофе";
        String task1Name = task1.getName();

        assertEquals(name, task1Name);
    }

    @Test
    void testGetDescription() {
        String description = "Добавить молоко";
        String task1Description = task1.getDescription();

        assertEquals(description,task1Description);
    }

    @Test
    void testGetStatus() {
        Status status = Status.NEW;
        Status task1Status = task1.getStatus();

        assertEquals(status, task1Status);
    }

    @Test
    void testSetStatus() {
        Status status = Status.DONE;
        task1.setStatus(status);
        Status newStatus = task1.getStatus();

        assertEquals(newStatus, status);
    }

    @Test
    void testGetId() {
        int id = 1;
        int taskId = task1.getId();

        assertEquals(id, taskId);
    }

    @Test
    void testEquals() {
        boolean equal = task1.equals(task2);

        assertTrue(equal);
    }

    @Test
    void testHashCode() {
        int hashCode = 1;
        int hashCodeTask2 = task1.hashCode();

        assertEquals(hashCode, hashCodeTask2);
    }

    @Test
    void testToString() {
        String toString = "Task{nameTask='Приготовить кофе', descriptionTask='Добавить молоко', status=NEW}";
        String string = task1.toString();

        assertEquals(toString, string);
    }

    @Test
    void testGetEndTimeWithStartTimeNull() {
        assertThrows(ManagerTimeException.class, () -> task3.getEndTime());
    }

    @Test
    void testGetEndTime() {
        Instant expected = Instant.ofEpochSecond(1210);
        Instant actually = task1.getEndTime();

        assertEquals(expected, actually);
    }
}