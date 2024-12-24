package Managers;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private SubTask subTask1;

    @BeforeEach
    public void unit() {
        task = new Task("Первая задача", "-", Status.NEW, 1);
        epic = new Epic("Первый эпик", "-", Status.NEW, 3);
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 4, epic);
        subTask1 = new SubTask("Второй сабтаск", "-", Status.NEW, 5, epic);

        taskManager = Managers.getDefault();
    }

    @Test
    void testAddNewTask() {
        taskManager.addNewTask(task);
        String nameTask = "[Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}]";
        String name = taskManager.printListTask().toString();

        assertEquals(name, nameTask);
    }

    @Test
    void testAddNewEpic() {
        taskManager.addNewEpic(epic);
        String nameTask = "[Tasks.Task{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";
        String name = taskManager.printListEpicTask().toString();

        assertEquals(name, nameTask);
    }

    @Test
    void testAddNewSubTask() {
        taskManager.addNewSubTask(subTask);
        String nameTask = "[Tasks.Task{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}]";
        String name = taskManager.printListSubTask().toString();

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
    void testPrintListTask() {
        taskManager.addNewTask(task);

        String listTask = taskManager.printListTask().toString();
        String nameTask = "[Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}]";

        assertEquals(listTask, nameTask);
    }

    @Test
    void testPrintListEpicTask() {
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
    void testDeleteNameTask() {
        taskManager.addNewTask(task);
        boolean isTasks = taskManager.printListTask().isEmpty();

        List<Task> nullTask = taskManager.deleteAllTask();
        List<Task> taskIsEmpty = new ArrayList<>();

        assertFalse(isTasks);
        assertEquals(nullTask, taskIsEmpty);
    }

    @Test
    void testDeleteEpicTask() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);

        assertNotNull(taskManager.printListEpicTask().toString());
        assertNotNull(taskManager.findEpicTask(epic.getId()).getSubTasks().toString());

        taskManager.deleteEpicTask(epic);
        String epicNull = "[]";

        assertEquals(epicNull, taskManager.printListEpicTask().toString());
        assertEquals(epicNull, taskManager.printListEpicTask().toString());
    }

    @Test
    void testDeleteSubTask() {
        taskManager.addNewSubTask(subTask);
        taskManager.deleteSubTask(subTask);
        String epicNull = "[]";

        assertEquals(epicNull, taskManager.printListSubTask().toString());
    }

    @Test
    void testFindTask() {
        taskManager.addNewTask(task);
        String nameTask = task.toString();
        String findTask = taskManager.findTask(task.getId()).toString();

        assertEquals(nameTask, findTask);

    }

    @Test
    void testFindEpicTask() {
        taskManager.addNewEpic(epic);
        String nameTask = epic.toString();
        String findTask = taskManager.findEpicTask(epic.getId()).toString();

        assertEquals(nameTask, findTask);
    }

    @Test
    void findSubTask() {
        taskManager.addNewSubTask(subTask);
        String nameTask = subTask.toString();
        String findTask = taskManager.findSubTask(subTask.getId()).toString();

        assertEquals(nameTask, findTask);
    }

    @Test
    void updateTask() {
        taskManager.addNewTask(task);
        String newNameTask = "[Tasks.Task{nameTask='Новый такс', descriptionTask='-', status=NEW}]";
        String oldNameTask = "Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}";

        assertEquals(oldNameTask, taskManager.findTask(task.getId()).toString());

        task.setName("Новый такс");

        taskManager.updateTask(task);

        assertEquals(newNameTask, taskManager.printListTask().toString());

    }

    @Test
    void updateEpic() {
        taskManager.addNewEpic(epic);
        String newNameTask = "Tasks.Task{nameTask='Новый такс', descriptionTask='-', status=NEW}";
        String oldNameTask = "Tasks.Task{nameTask='Первый эпик', descriptionTask='-', status=NEW}";

        assertEquals(oldNameTask, taskManager.findEpicTask(epic.getId()).toString());

        epic.setName("Новый такс");
        taskManager.updateEpic(epic);

        assertEquals(newNameTask, taskManager.findEpicTask(epic.getId()).toString());
    }

    @Test
    void updateSub() {
        taskManager.addNewSubTask(subTask);
        SubTask newTask = new SubTask("Новый такс", "-", Status.NEW, 1, epic);
        String newNameTask = "Tasks.Task{nameTask='Новый такс', descriptionTask='-', status=NEW}";
        String oldNameTask = "Tasks.Task{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}";

        assertEquals(oldNameTask, taskManager.findSubTask(subTask.getId()).toString());

        taskManager.updateSub(newTask);

        assertEquals(newNameTask, taskManager.findSubTask(subTask.getId()).toString());
    }

    @Test
    void newStatus() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        boolean statusNew = epic.getStatus().equals(Status.NEW);

        assertTrue(statusNew);

        taskManager.findSubTask(subTask.getId()).setStatus(Status.DONE);
        taskManager.addNewSubTask(subTask1);

        boolean statusIn_Progress = epic.getStatus().equals(Status.IN_PROGRESS);
        assertTrue(statusIn_Progress);
    }
}