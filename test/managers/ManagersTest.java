package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @BeforeEach
    void unit() {
        try {
            File file = Files.createTempFile("data-",".csv").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Test
    void getFileBackedTaskManager() {
        Path filePath;
        try {
            filePath = Files.createTempFile("data-", "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TaskManager actually = Managers.getFileBackedTaskManager(filePath.toFile());
        Assertions.assertInstanceOf(FileBackedTaskManager.class, actually);
    }
}