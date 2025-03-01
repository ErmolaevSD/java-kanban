package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends AbstractManagerTest{

    @Test
    void testAdd() {
        Managers.getDefault().addNewTask(task);
        Managers.getDefault().addNewTask(task1);
        Managers.getDefault().addNewEpic(epic);

        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);

        String history = historyManager.getHistory().toString();
        String historyDone = "[Task{nameTask='Первая задача', descriptionTask='-', status=NEW}, Task{nameTask='Первая задача', descriptionTask='-', status=NEW}, Epic{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";
        assertEquals(historyDone, history);
    }


    @Test
    void getHistory() {
        int historySize = 3;

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.add(epic);
        historyManager.add(task);

        assertEquals(historySize, historyManager.getHistory().size());
    }

    @Test
    void testRemoveHistory() {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);

        assertEquals(3, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(task1);

        assertEquals(2, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(task1);

        assertEquals(2, historyManager.getHistory().size());

        inMemoryTaskManager.deleteTask(subTask);

        assertEquals(2, historyManager.getHistory().size());
    }
}