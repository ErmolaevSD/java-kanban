package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
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
        String nameTask = "[Task{nameTask='Первая задача', descriptionTask='-', status=NEW}]";
        String name = taskManager.getListTask().toString();

        assertEquals(nameTask, name);
    }

    @Test
    void testAddNewEpic() {
        taskManager.addNewEpic(epic);
        String nameTask = "[Epic{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";
        String name = taskManager.getListEpicTask().toString();

        assertEquals(nameTask, name);
    }

    @Test
    void testAddNewSubTask() {
        taskManager.addNewSubTask(subTask);
        String nameTask = "[SubTask{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}]";
        String name = taskManager.getListSubTask().toString();

        assertEquals(nameTask,name);
    }

    @Test
    void testGetAllSubtasksByEpic() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);

        String listSubTaskByEpic = taskManager.findEpicTask(epic.getId()).getSubTasks().toString();
        String nameSubTask = "[SubTask{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}, SubTask{nameTask='Второй сабтаск', descriptionTask='-', status=NEW}]";

        assertEquals(nameSubTask, listSubTaskByEpic);
    }

    @Test
    void testGetListTask() {
        taskManager.addNewTask(task);

        String listTask = taskManager.getListTask().toString();
        String nameTask = "[Task{nameTask='Первая задача', descriptionTask='-', status=NEW}]";

        assertEquals(nameTask, listTask);
    }

    @Test
    void testGetListEpicTask() {
        taskManager.addNewEpic(epic);

        String listTask = taskManager.getListEpicTask().toString();
        String nameTask = "[Epic{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";

        assertEquals(nameTask, listTask);
    }

    @Test
    void testGetListSubTask() {
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);
        String nameSubTask = "[SubTask{nameTask='Первый сабтаск', descriptionTask='-', status=NEW}, SubTask{nameTask='Второй сабтаск', descriptionTask='-', status=NEW}]";

        assertEquals(nameSubTask,taskManager.getListSubTask().toString());
    }

    @Test
    void testDeleteNameTask() {
        taskManager.addNewTask(task);
        boolean isTasks = taskManager.getListTask().isEmpty();

        List<Task> nullTask = taskManager.deleteAllTask();
        List<Task> taskIsEmpty = new ArrayList<>();

        assertFalse(isTasks);
        assertEquals(taskIsEmpty, nullTask);
    }

    @Test
    void testDeleteEpicTask() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.addNewSubTask(subTask1);


        assertNotNull(taskManager.getListEpicTask().toString());
        assertNotNull(taskManager.findEpicTask(epic.getId()).getSubTasks().toString());

        taskManager.deleteEpicTask(epic);
        String epicNull = "[]";
        ArrayList<SubTask> subTasks = new ArrayList<>();

        assertEquals(epicNull, taskManager.getListEpicTask().toString());
        assertEquals(subTasks, taskManager.getListSubTask());
    }

    @Test
    void testDeleteSubTask() {
        taskManager.addNewSubTask(subTask);
        taskManager.deleteSubTask(subTask);
        String epicNull = "[]";


        assertEquals(epicNull, taskManager.getListSubTask().toString());
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
        String newNameTask = "[Task{nameTask='Новый такс', descriptionTask='-', status=NEW}]";
        String oldNameTask = "Task{nameTask='Первая задача', descriptionTask='-', status=NEW}";

        assertEquals(oldNameTask, taskManager.findTask(task.getId()).toString());

        task.setName("Новый такс");

        taskManager.updateTask(task);

        assertEquals(newNameTask, taskManager.getListTask().toString());

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