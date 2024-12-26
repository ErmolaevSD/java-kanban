package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {

    Epic epic;
    SubTask subTask;
    List<SubTask> listSubTask;

    @BeforeEach
    public void unit() {
        epic= new Epic("Купить квартиру", "-", Status.NEW, 1);
        subTask = new SubTask("Накопить", "1000", Status.NEW, 2,epic);
    }

    @Test
    void getSubTasks() {
        List<SubTask> listSub = epic.getSubTasks();

        for (SubTask subTask1: listSub) {
            if (subTask1.equals(subTask)) {
                listSubTask.add(subTask1);
            }
            assertEquals(listSub, listSubTask);
        }
    }
}