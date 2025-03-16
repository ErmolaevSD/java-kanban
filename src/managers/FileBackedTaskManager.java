package managers;

import exception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
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
        tasks.add("id,type,name,status,description,epic,duration,startTime\n");

        for (Task task : listTask.values()) {
            tasks.add(task.stringToFile());
        }

        for (Epic task : listEpicTask.values()) {
            tasks.add(task.stringToFile());
        }

        for (SubTask task : listSubTask.values()) {
            tasks.add(task.stringToFile());
        }
        try {
            taskToFile(tasks);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    /* Проходимся по списку задач -> каждую записываем в файл */
    private void taskToFile(List<String> tasks) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {
            for (String task : tasks) {
                fileWriter.write(task);
            }
        }
    }

    public void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            List<String> stringTask = new ArrayList<>();
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                stringTask.add(line);
            }
            int newId = 0;
            for (String string : stringTask) {
                if (string.equals("id,type,name,status,description,epic,duration,startTime")) {
                    continue;
                }
                String[] lineIsTask = string.trim().split(",");
                TaskType taskType = TaskType.valueOf(lineIsTask[1]);
                Status status;
                String stringStatus = lineIsTask[3];

                switch (stringStatus) {
                    case "NEW" -> status = Status.NEW;
                    case "DONE" -> status = Status.DONE;
                    case "IN_PROGRESS" -> status = Status.IN_PROGRESS;
                    default -> throw new RuntimeException("Ошибка извлечения статуса задачи из файла");
                }

                switch (taskType) {
                    case TASK ->
                            listTask.put(Integer.parseInt(lineIsTask[0]), new Task(lineIsTask[2], lineIsTask[4], status, Integer.parseInt(lineIsTask[0]), Duration.parse(lineIsTask[5]), Instant.parse(lineIsTask[6])));
                    case EPIC ->
                            listEpicTask.put(Integer.parseInt(lineIsTask[0]), new Epic(lineIsTask[2], lineIsTask[4], status, Integer.parseInt(lineIsTask[0]),Duration.parse(lineIsTask[5]), Instant.parse(lineIsTask[6])));
                    case SUBTASK ->
                            listSubTask.put(Integer.parseInt(lineIsTask[0]), new SubTask(lineIsTask[2], lineIsTask[4], status, Integer.parseInt(lineIsTask[0]), Integer.valueOf(lineIsTask[5]),Duration.parse(lineIsTask[6]), Instant.parse(lineIsTask[7])));
                }

                int id = Integer.parseInt(lineIsTask[0]);

                if (newId < id) {
                    newId = id;
                }
                setIdentificationID(newId);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла с задачами");
        }
    }
}