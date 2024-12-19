package Managers;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    Task addNewTask(Task task);

    Epic addNewEpic(Epic epic);

    SubTask addNewSubTask(SubTask subTask);

    List<SubTask> getSubTasks(Epic epic);

    List<SubTask> printAllSubtasks(Epic epic);

    List<Task> printListTask();

    List<Epic> printListEpicTask();

    List<SubTask> printListSubTask();

    Task deleteNameTask(Integer id);

    Epic deleteEpicTask(Epic epic);

    SubTask deleteSubTask(SubTask subTask);

    Task findTask(Integer id);

    Epic findEpicTask(Integer id);

    SubTask findSubTask(Integer id);

    List<Task> deleteAllTask();

    Map<Integer, Epic> deleteAllEpicTask();

    Map<Integer, SubTask> deleteAllSubTask();

    Task updateTask(Task newTask);

    Epic updateEpic(Epic newEpic);

    SubTask updateSub(SubTask newSub);

    Map<Integer, SubTask> getListSubTask();

    Status newStatus(Epic epic);

    int getIdentificatorID();

}
