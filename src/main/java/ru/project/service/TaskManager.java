package ru.project.service;

import ru.project.model.Epic;
import ru.project.model.SubTask;
import ru.project.model.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    Map<Integer, Epic> getListEpicTask();

    Map<Integer, SubTask> getListSubTask();

    Map<Integer, Task> getListTask();

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    List<SubTask> getSubTasks(List<Integer> subTasksByID);

    void deleteTask(Task task);

    void deleteEpicTask(Epic epic);

    void deleteSubTask(SubTask subTask);

    Task findTaskById(Integer id);

    Epic findEpicTaskById(Integer id);

    SubTask findSubTaskById(Integer id);

    void deleteAllTask();

    void deleteAllEpicTask();

    void deleteAllSubTask();

    Task updateTask(Task newTask);

    Epic updateEpic(Epic newEpic);

    SubTask updateSub(SubTask newSub);

    void newStatus(Epic epic);

    TreeSet<Task> getTaskPriotity();

    HistoryManager getHistoryManager();
}
