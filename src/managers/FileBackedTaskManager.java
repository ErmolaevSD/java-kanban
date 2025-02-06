package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public void save() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        Collection<Task> tasks = listTask.values();
        tasks.addAll(listEpicTask.values());
        tasks.addAll(listSubTask.values());

        for (Task task: tasks) {
            String taskString = taskToString(task);
            writeTaskToFile(taskString);
        }
    }

    private String taskToString(Task task) {
        return task.toString();
    }

    private void writeTaskToFile (String task) {
        try {
            FileWriter fileWriter = new FileWriter(task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task addNewTask(Task task) {
        Task newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Epic newEpic = super.addNewEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        SubTask newSubTask = super.addNewSubTask(subTask);
        save();
        return newSubTask;
    }

    @Override
    public Task deleteNameTask(Integer id) {
        Task newTask = super.deleteNameTask(id);
        save();
        return newTask;
    }

    @Override
    public Epic deleteEpicTask(Epic epic) {
        Epic newEpic = super.deleteEpicTask(epic);
        save();
        return newEpic;
    }

    @Override
    public SubTask deleteSubTask(SubTask subTask) {
        SubTask newSubTask =  super.deleteSubTask(subTask);
        save();
        return newSubTask;
    }

    @Override
    public List<Task> deleteAllTask() {
        List<Task> newTasks = super.deleteAllTask();
        save();
        return newTasks;
    }

    @Override
    public Map<Integer, Epic> deleteAllEpicTask() {
        Map<Integer,Epic> newEpics = super.deleteAllEpicTask();
        save();
        return newEpics;
    }

    @Override
    public Map<Integer, SubTask> deleteAllSubTask() {
        Map<Integer,SubTask> newSubTasks = super.deleteAllSubTask();
        save();
        return newSubTasks;
    }

    @Override
    public Task updateTask(Task newTask) {
        return super.updateTask(newTask);
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        return super.updateEpic(newEpic);
    }

    @Override
    public SubTask updateSub(SubTask newSub) {
        return super.updateSub(newSub);
    }
}
