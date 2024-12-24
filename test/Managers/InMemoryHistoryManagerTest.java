package Managers;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task;
    private Task task1;
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    void unit() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("Первая задача", "-", Status.NEW, null);
        task1 = new Task("Первая задача", "-", Status.NEW, null);
        epic = new Epic("Первый эпик", "-", Status.NEW, 3);
        epic1 = new Epic("Первый эпик", "-", Status.NEW, 3);
        subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 4, epic);
        subTask1 = new SubTask("Второй сабтаск", "-", Status.NEW, 5, epic);
        subTask2 = new SubTask("Второй сабтаск", "-", Status.NEW, 5, epic);
    }

    @Test
    void testAdd() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);

        String history = historyManager.getHistory().toString();
        String historyDone = "[Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}, Tasks.Task{nameTask='Первая задача', descriptionTask='-', status=NEW}, Tasks.Task{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";
        assertEquals(history, historyDone);
    }

    @Test
    void getHistory() {
        int historySize = 10;
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(epic);

        assertEquals(historySize, historyManager.getHistory().size());

        historyManager.add(epic);
        historyManager.add(epic);

        assertEquals(historySize, historyManager.getHistory().size());
    }
}