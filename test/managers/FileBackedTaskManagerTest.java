package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class FileBackedTaskManagerTest {

    private File file;
    private Task task;
    private Task task1;
    private Epic epic;
    private SubTask subTask;
    private TaskManager taskManager;

    @BeforeEach
    void unit() {
        try {
            file = Files.createTempFile("data-", ".csv").toFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = Managers.getFileBackedTaskManager(file);

        task = new Task("Первая задача", "-", Status.NEW, 1);
        task1 = new Task("Первая задача", "-", Status.NEW, 2);
        epic = new Epic("Первый эпик", "-", Status.NEW, 3);
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 4, epic);
    }

    @Test
    void loadFromFile() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);

        TaskManager load = Managers.loadFrom(file);
        String expected = taskManager.getListTask() + " " + taskManager.getListEpicTask() + " " + taskManager.getListSubTask();
        String actually = load.getListTask() + " " + load.getListEpicTask() + " " + load.getListSubTask();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void addNewTask() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

        TaskManager load = Managers.loadFrom(file);
        String expected = taskManager.getListTask().toString();
        String actually = load.getListTask().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void deleteTask() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);
        taskManager.deleteTask(task);

        TaskManager load = Managers.loadFrom(file);
        Integer expected = taskManager.getListTask().size();
        Integer actually = load.getListTask().size();
        Integer one = 1;
        Assertions.assertEquals(expected, actually);
        Assertions.assertEquals(one,expected);
        Assertions.assertEquals(one,actually);
    }

    @Test
    void deleteEpicTask() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.deleteEpicTask(epic);

        TaskManager load = Managers.loadFrom(file);
        Integer expected = taskManager.getListTask().size() + taskManager.getListEpicTask().size() + taskManager.getListSubTask().size();
        Integer actually = load.getListTask().size() + taskManager.getListEpicTask().size() + taskManager.getListSubTask().size();
        Integer zero = 0;
        Assertions.assertEquals(expected, actually);
        Assertions.assertEquals(zero,expected);
        Assertions.assertEquals(zero,actually);
    }
}