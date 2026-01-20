package ru.project.service;

import org.junit.jupiter.api.Test;
import ru.project.exception.IntersectionTaskException;
import ru.project.model.Epic;
import ru.project.model.Status;
import ru.project.model.SubTask;
import ru.project.model.Task;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends AbstractManagerTest {

    @Test
    void testCreateTask() {
        Task createdTask = taskManager.createTask(task);

        assertNotNull(createdTask);
    }

    @Test
    void testCreateEpic() {
        Epic createdEpic = taskManager.createEpic(epic);

        assertNotNull(createdEpic);
    }

    @Test
    void testCreateSubTask() {
        taskManager.createEpic(epic);
        SubTask createdSubTask = taskManager.createSubTask(subTask);

        assertNotNull(createdSubTask);
    }

    @Test
    void testGetListTask() {
        taskManager.createTask(task);
        Collection<Task> listTask = taskManager.getListTask().values();

        assertEquals(1, listTask.size());
    }

    @Test
    void testGetListEpicTask() {
        taskManager.createEpic(epic);

        Collection<Epic> listEpic = taskManager.getListEpicTask().values();

        assertEquals(1, listEpic.size());
    }

    @Test
    void testGetListSubTask() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        assertEquals(1, taskManager.getListSubTask().size());
    }

    @Test
    void testDeleteTask() {
        taskManager.createTask(task);
        boolean isTasks = taskManager.getListTask().isEmpty();
        taskManager.deleteAllTask();
        boolean emptyTask = taskManager.getListTask().isEmpty();

        assertFalse(isTasks);
        assertTrue(emptyTask);
    }

    @Test
    void testDeleteEpic() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        assertEquals(1, taskManager.getListEpicTask().size());

        taskManager.deleteEpicTask(epic);

        assertEquals(0, taskManager.getListEpicTask().size());
    }

    @Test
    void testDeleteSubTask() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(subTask);

        assertEquals(0, taskManager.getListSubTask().size());
    }

    @Test
    void testFindTaskById() {
        taskManager.createTask(task);
        String createdTask = task.toString();
        String findTask = taskManager.findTaskById(task.getId()).toString();

        assertEquals(createdTask, findTask);
    }

    @Test
    void testFindEpicTaskById() {
        taskManager.createEpic(epic);
        String nameTask = epic.toString();
        String findTask = taskManager.findEpicTaskById(epic.getId()).toString();

        assertEquals(nameTask, findTask);
    }

    @Test
    void testFindSubTaskById() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        String nameTask = subTask.toString();
        String findTask = taskManager.findSubTaskById(subTask.getId()).toString();

        assertEquals(nameTask, findTask);
    }

    @Test
    void testUpdateTask() {
        taskManager.createTask(task);
        String nameTask = task.toString();

        assertEquals(nameTask, taskManager.findTaskById(task.getId()).toString());

        task.setName("Новый такс");
        taskManager.updateTask(task);
        String newNameTask = task.toString();

        assertEquals(newNameTask, taskManager.findTaskById(task.getId()).toString());
    }

    @Test
    void testNewStatus() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        boolean statusNew = epic.getStatus().equals(Status.NEW);

        assertTrue(statusNew);
    }

    @Test
    void testGetIntersectionTaskException() {
        taskManager.createEpic(epic);

        assertThrows(IntersectionTaskException.class, () -> taskManager.createEpic(epic1));
    }

    @Test
    void getSortedTask() {
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic);

        Collection<Epic> epicList = taskManager.getListEpicTask().values();
        assertEquals(epic2, epicList.stream().toList().getFirst());
        assertEquals(epic, epicList.stream().toList().getLast());

        taskManager.getTaskPriotity();
    }
}