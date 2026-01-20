package ru.project.service;

import java.io.File;


/**
 * Фабрика менеджеров
 * <p>
 * Методы создания нужного менеджера в зависимости от места хранения информации
 */

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public static FileBackedTaskManager loadFrom(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(getDefaultHistory(), file);
        fileBackedTaskManager.loadFromFile(file);
        return fileBackedTaskManager;
    }
}
