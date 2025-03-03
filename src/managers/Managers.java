package managers;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        HistoryManager historyManager = getDefaultHistory();
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(getDefaultHistory(),file);
    }

    public static FileBackedTaskManager loadFrom(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(getDefaultHistory(), file);
        fileBackedTaskManager.loadFromFile(file);
        return fileBackedTaskManager;
    }
}
