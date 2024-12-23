package Managers;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        taskManager.addNewTask(task);
        int idTask = task.getId();

        String nameTask = "[Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}]";
        String name = taskManager.printListTask().toString();

        assertNotNull(idTask);
        assertEquals(name, nameTask);
    }

    @Test
    void testAddNewEpic() {
        taskManager.addNewEpic(epic);
        int idTask = epic.getId();

        String nameTask = "[Tasks.Task{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";
        String name = taskManager.printListEpicTask().toString();

        assertNotNull(idTask);
        assertEquals(name, nameTask);
    }

    @Test
    void addNewSubTask() {
        taskManager.addNewSubTask(subTask);
        int idTask = subTask.getId();

        String nameTask = "[Tasks.Task{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}]";
        String name = taskManager.printListSubTask().toString();

        assertNotNull(idTask);
        assertEquals(name, nameTask);
    }

    @Test
    void testPrintAllSubtasksByEpic() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);

        String listSubTaskByEpic = taskManager.findEpicTask(epic.getId()).getSubTasks().toString();
        String nameSubTask = "[Tasks.Task{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}, Tasks.Task{nameTask='Второй сабтаск', descriptionTask='-', status=NEW}]";

        assertEquals(listSubTaskByEpic, nameSubTask);
    }

    @Test
    void printListTask() {
        taskManager.addNewTask(task);

        String listTask = taskManager.printListTask().toString();
        String nameTask = "[Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}]";

        assertEquals(listTask, nameTask);
    }

    @Test
    void printListEpicTask() {
        taskManager.addNewEpic(epic);

        String listTask = taskManager.printListEpicTask().toString();
        String nameTask = "[Tasks.Task{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";

        assertEquals(listTask, nameTask);
    }

    @Test
    void testPrintListSubTask() {
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);
        String nameSubTask = "[Tasks.Task{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}, Tasks.Task{nameTask='Второй сабтаск', descriptionTask='-', status=NEW}]";

        assertEquals(taskManager.printListSubTask().toString(), nameSubTask);
    }

    @Test
    void deleteNameTask() {
        taskManager.addNewTask(task);
        boolean isTasks = taskManager.printListTask().isEmpty();

        List<Task> nullTask = taskManager.deleteAllTask();
        List<Task> taskIsEmpty = new ArrayList<>();

        assertFalse(isTasks);
        assertEquals(nullTask, taskIsEmpty);
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