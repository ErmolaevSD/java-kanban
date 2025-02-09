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

    File file;
    Task task;
    Task task1;
    Epic epic;
    SubTask subTask;
    TaskManager taskManager;

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
        String expected = taskManager.printListTask() + " " + taskManager.printListEpicTask() + " " + taskManager.printListSubTask();
        String actually = load.printListTask() + " " + load.printListEpicTask() + " " + load.printListSubTask();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void addNewTask() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

        TaskManager load = Managers.loadFrom(file);
        String expected = taskManager.printListTask().toString();
        String actually = load.printListTask().toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void deleteTask() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);
        taskManager.deleteTask(task);

        TaskManager load = Managers.loadFrom(file);
        Integer expected = taskManager.printListTask().size();
        Integer actually = load.printListTask().size();
        Integer one = 1;
        Assertions.assertEquals(expected, actually);
        Assertions.assertEquals(expected, one);
        Assertions.assertEquals(actually, one);
    }

    @Test
    void deleteEpicTask() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(subTask);
        taskManager.deleteEpicTask(epic);

        TaskManager load = Managers.loadFrom(file);
        Integer expected = taskManager.printListTask().size() + taskManager.printListEpicTask().size() + taskManager.printListSubTask().size();
        Integer actually = load.printListTask().size() + taskManager.printListEpicTask().size() + taskManager.printListSubTask().size();
        Integer zero = 0;
        Assertions.assertEquals(expected, actually);
        Assertions.assertEquals(expected, zero);
        Assertions.assertEquals(actually, zero);
    }
}