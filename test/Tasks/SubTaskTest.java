package Tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SubTaskTest {

    Epic epic;
    SubTask subTask;

    @BeforeEach
    public void util() {
        epic= new Epic("Купить квартиру", "-", Status.NEW, 1);
        subTask = new SubTask("Накопить", "1000", Status.NEW, 2,epic);
    }

    @Test
    void testGetParentTask() {
        Epic parentsEpic = subTask.getParentTask();
        assertEquals(epic, parentsEpic);
    }
}