package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileBackedTaskManagerTest extends AbstractManagerTest {

    @Test
    void loadFromFile() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);
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