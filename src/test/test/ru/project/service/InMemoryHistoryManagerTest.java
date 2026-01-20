//package managers;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class InMemoryHistoryManagerTest extends AbstractManagerTest {
//
//    @Test
//    void testAddTask() {
//        taskManager.createTask(task);
//        taskManager.createEpic(epic);
//
//        historyManager.addTask(task);
//        historyManager.addTask(task1);
//        historyManager.addTask(epic);
//
//        String history = historyManager.getHistory().toString();
//        String historyDone = "[Task{nameTask='Первая задача', descriptionTask='-', status=NEW}, Task{nameTask='Первая задача', descriptionTask='-', status=NEW}, Epic{nameTask='Первый эпик', descriptionTask='-', status=NEW}]";
//        assertEquals(historyDone, history);
//    }
//
//
//    @Test
//    void getHistory() {
//        int historySize = 3;
//
//        taskManager.createTask(task);
//        taskManager.createSubTask(subTask);
//
//        historyManager.addTask(task);
//        historyManager.addTask(epic);
//        historyManager.addTask(subTask);
//        historyManager.addTask(epic);
//        historyManager.addTask(task);
//
//        assertEquals(historySize, historyManager.getHistory().size());
//    }
//
//    @Test
//    void testRemoveHistory() {
//
//        TaskManager taskManager = Managers.getDefault();
//        HistoryManager historyManager = taskManager.getHistoryManager();
//
//        taskManager.createTask(task);
//        taskManager.createEpic(epic);
//
//        taskManager.findTaskById(task.getId());
//        taskManager.findEpicTaskById(epic.getId());
//
//        assertEquals(2, historyManager.getHistory().size());
//
//        taskManager.deleteTask(task);
//
//        assertEquals(1, historyManager.getHistory().size());
//    }
//}