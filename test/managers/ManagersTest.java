package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void testGetDefault() {
        TaskManager taskManager = Managers.getDefault();
        assertInstanceOf(InMemoryTaskManager.class, taskManager);
    }

    @Test
    void testGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }
}