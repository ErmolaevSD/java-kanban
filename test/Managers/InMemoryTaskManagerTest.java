package Managers;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private Task task;
    private Task task1;
    private Epic epic;
    private SubTask subTask;
    private SubTask subTask1;
    private Map<Integer, Task> listTask;
    private Map<Integer, Epic> listEpicTask;
    private Map<Integer, SubTask> listSubTask;

    @BeforeEach
    public void unit() {
        task = new Task("Первая задача", "-", Status.NEW,null);
        task1 = new Task("Вторая задача", "-", Status.NEW,2);
        epic = new Epic("Первый эпик", "-", Status.NEW,3);
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW,4, epic);
        subTask1 = new SubTask("Второй сабтаск", "-", Status.NEW,5, epic);

        taskManager = Managers.getDefault();
    }

    @Test
    void testAddNewTask() {
        listTask = new HashMap<>();
        taskManager.addNewTask(task);
        int id = 1;
        int idTask = task.getId();
        boolean lists = listTask.equals(task);

        assertEquals(id, idTask);
        assertTrue(lists);

//        public Task addNewTask(Task task) {
//            Integer id = idGenerator.getIdentificatorID();
//            task.setId(id);
//            listTask.put(task.getId(), task);
//            return task;




    }

    @Test
    void addNewEpic() {
    }

    @Test
    void addNewSubTask() {
    }

    @Test
    void printAllSubtasks() {
    }

    @Test
    void printListTask() {
    }

    @Test
    void printListEpicTask() {
    }

    @Test
    void printListSubTask() {
    }

    @Test
    void deleteNameTask() {
    }

    @Test
    void deleteEpicTask() {
    }

    @Test
    void deleteSubTask() {
    }

    @Test
    void findTask() {
    }

    @Test
    void findEpicTask() {
    }

    @Test
    void findSubTask() {
    }

    @Test
    void deleteAllTask() {
    }

    @Test
    void deleteAllEpicTask() {
    }

    @Test
    void deleteAllSubTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSub() {
    }

    @Test
    void getListSubTask() {
    }

    @Test
    void getIdentificatorID() {
    }

    @Test
    void newStatus() {
    }
}