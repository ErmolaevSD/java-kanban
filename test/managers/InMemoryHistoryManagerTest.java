package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends AbstractManagerTest{

    @Test
    void testAdd() {
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

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

        taskManager.addNewTask(task);
        taskManager.addNewSubTask(subTask);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.add(epic);
        historyManager.add(task);

        assertEquals(historySize, historyManager.getHistory().size());
    }

    @Test
    void testRemoveHistory() {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = taskManager.getHistoryManager();

        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);

        taskManager.findTask(task.getId());
        taskManager.findEpicTask(epic.getId());

        assertEquals(2, historyManager.getHistory().size());

        taskManager.deleteTask(task);

        assertEquals(1, historyManager.getHistory().size());
    }
}