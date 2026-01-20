package ru.project.service;

import ru.project.exception.ManagerSaveException;
import ru.project.model.*;

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
    public Map<Integer, Epic> getListEpicTask() {
        return super.getListEpicTask();
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public void deleteTask(Task task) {
        super.deleteTask(task);
        save();
    }

    @Override
    public void deleteEpicTask(Epic epic) {
        super.deleteEpicTask(epic);
        save();
    }

    @Override
    public void deleteSubTask(SubTask subTask) {
        super.deleteSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpicTask() {
        super.deleteAllEpicTask();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
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
    public Epic createEpic(Epic task) {
        Epic newTask = super.createEpic(task);
        save();
        return newTask;
    }

    @Override
    public SubTask createSubTask(SubTask task) {
        SubTask newTask = super.createSubTask(task);
        save();
        return newTask;
    }

    private void save() {
        List<String> tasks = new ArrayList<>();
        tasks.add("id,type,name,status,description,epic,duration,startTime\n");

        getListTask().values().forEach(task -> tasks.add(task.stringToFile()));
        getListEpicTask().values().forEach(epic -> tasks.add(epic.stringToFile()));
        getListSubTask().values().forEach(subTask -> tasks.add(subTask.stringToFile()));

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
                stringTask.add(fileReader.readLine());
            }

            int newId = 0;
            for (String string : stringTask) {
                if (string.equals("id,type,name,status,description,epic,duration,startTime")) {
                    continue;
                }
                String[] lineIsTask = string.trim().split(",");
                Status status;
                String stringStatus = lineIsTask[3];

                switch (stringStatus) {
                    case "NEW" -> status = Status.NEW;
                    case "DONE" -> status = Status.DONE;
                    case "IN_PROGRESS" -> status = Status.IN_PROGRESS;
                    default -> throw new RuntimeException("Ошибка извлечения статуса задачи из файла");
                }

                TaskType taskType = TaskType.valueOf(lineIsTask[1]);

                switch (taskType) {
                    case TASK -> {
                        Task task = Task.builder()
                                .id(Integer.valueOf(lineIsTask[0]))
                                .name(lineIsTask[2])
                                .description(lineIsTask[4])
                                .status(Status.valueOf(lineIsTask[3]))
                                .startTime(Instant.parse(lineIsTask[7]))
                                .endTime(Instant.parse(lineIsTask[7]).plus(Duration.parse(lineIsTask[6])))
                                .duration(Duration.parse(lineIsTask[6]))
                                .build();

                        getListTask().put(Integer.parseInt(lineIsTask[0]), task);
                    }

                    case EPIC -> {
                        Epic epic = new Epic(lineIsTask[2], lineIsTask[4], Instant.parse(lineIsTask[7]), Duration.parse(lineIsTask[6]));
                        epic.setStatus(Status.valueOf(lineIsTask[3]));
                        getListEpicTask().put(Integer.parseInt(lineIsTask[0]), epic);
                    }

                    case SUBTASK -> {

                        SubTask subTask = new SubTask(
                                lineIsTask[2],
                                lineIsTask[4],
                                Integer.parseInt(lineIsTask[5]),
                                Duration.parse(lineIsTask[6]),
                                Instant.parse(lineIsTask[7])
                        );

                        getListSubTask().put(Integer.parseInt(lineIsTask[0]), subTask);
                    }
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