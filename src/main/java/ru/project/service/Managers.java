package ru.project.service;

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
}