package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    Task addNewTask(Task task);

    Epic addNewEpic(Epic epic);

    SubTask addNewSubTask(SubTask subTask);

    List<SubTask> getSubTasks(List<Integer> subTasksByID);

    List<SubTask> getAllSubtasks();

    List<Task> getListTask();

    List<Epic> getListEpicTask();

    List<SubTask> getListSubTask();

    Task deleteTask(Task task);

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

    Status newStatus(Epic epic);

    TreeSet<Task> getTaskPriotity();

    HistoryManager getHistoryManager();
}
