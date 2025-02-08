package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    @Override
    public Task addNewTask(Task task) {
        Task newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public Task deleteTask(Task task) {
        Task newTask = super.deleteTask(task);
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
        SubTask newSubTask = super.deleteSubTask(subTask);
        save();
        return newSubTask;
    }

    @Override
    public List<Task> deleteAllTask() {
        List<Task> newListTask = super.deleteAllTask();
        save();
        return newListTask;
    }

    @Override
    public Map<Integer, Epic> deleteAllEpicTask() {
        Map<Integer, Epic> newListEpic = super.deleteAllEpicTask();
        save();
        return newListEpic;
    }

    @Override
    public Map<Integer, SubTask> deleteAllSubTask() {
        Map<Integer, SubTask> newListSubTask = super.deleteAllSubTask();
        save();
        return newListSubTask;
    }

    @Override
    public Task updateTask(Task newTask) {
        Task newTasks = super.updateTask(newTask);
        save();
        return newTasks;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        Epic newEpics = super.updateEpic(newEpic);
        save();
        return newEpics;
    }

    @Override
    public SubTask updateSub(SubTask newSub) {
        SubTask newSubTask = super.updateSub(newSub);
        save();
        return newSubTask;
    }

    @Override
    public Epic addNewEpic(Epic task) {
        Epic newTask = super.addNewEpic(task);
        save();
        return newTask;
    }

    @Override
    public SubTask addNewSubTask(SubTask task) {
        SubTask newTask = super.addNewSubTask(task);
        save();
        return newTask;
    }

    private void save() {
        List<String> tasks = new ArrayList<>();
        tasks.add("id,type,name,status,description,epic\n");

        for (Task task : listTask.values()) {
            tasks.add(task.stringToFile());
        }

        for (Epic task : listEpicTask.values()) {
            tasks.add(task.stringToFile());
        }

        for (SubTask task : listSubTask.values()) {
            tasks.add(task.stringToFile());
        }
        taskToFile(tasks);
    }

    /* Проходимся по списку задач -> каждую записываем в файл */
    private void taskToFile(List<String> tasks) {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {
            for (String task : tasks) {
                fileWriter.write(task);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    static FileBackedTaskManager loadFromFile (File file) {
//
//    }

}


