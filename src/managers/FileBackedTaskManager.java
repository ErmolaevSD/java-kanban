package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

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

    private void save() {

        Collection<Task> tasks = new ArrayList<>();

        for (Task task : listTask.values()) {
            tasks.add(task);
        }

        for (Epic task : listEpicTask.values()) {
            tasks.add(task);
        }

        for (SubTask task : listSubTask.values()) {
            tasks.add(task);
        }

        for (Task task : tasks) {
            String name = taskToString(task);
            taskToFile(name);
        }
    }

    private String taskToString(Task task) {
        return task.toString();
    }

    private void taskToFile(String string) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


