package ru.project.service;

import ru.project.model.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int id);

    void deleteTaskHistory(int id);

    List<Task> getHistory();
}