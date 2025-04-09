package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
class SubTaskTest {

    Epic epic;
    SubTask subTask;

    @BeforeEach
    public void util() {
        epic= new Epic("Купить квартиру", "-", Status.NEW, 1, Duration.ofMinutes(5), Instant.now().minusSeconds(100));
        subTask = new SubTask("Накопить", "1000", Status.NEW, 2,1,Duration.ofMinutes(5),Instant.now().minusSeconds(1000));
    }

    @Test
    void testGetParentTask() {
        Integer parentsEpic = subTask.getParentTask();

        assertEquals(epic.getId(), parentsEpic);
    }
}