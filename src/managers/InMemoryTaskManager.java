package managers;

import exception.IntersectionTaskException;
import exception.NotTaskException;
import tasks.*;
import java.util.*;

import static java.util.Objects.isNull;

public class InMemoryTaskManager implements TaskManager {

    protected int identificationID = 0;
    protected final Map<Integer, Task> listTask = new HashMap<>();
    protected final Map<Integer, Epic> listEpicTask = new HashMap<>();
    protected final Map<Integer, SubTask> listSubTask = new HashMap<>();
    IdGenerator idGenerator = new IdGenerator();
    private final HistoryManager historyManager;
    protected TreeSet<Task> sortedTask = new TreeSet<>((o1, o2) -> {
        long firstStartTime = o1.getStartTime().toEpochMilli();
        long twoStartTime = o2.getStartTime().toEpochMilli();
        return (int) (firstStartTime - twoStartTime);
    });

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setIdentificationID(int identificationID) {
        this.identificationID = identificationID;
    }

    @Override
    public Task addNewTask(Task task) {
        try {
            validTask(task);
        } catch (IntersectionTaskException e) {
            throw new IntersectionTaskException(e.getMessage());
        }
        Integer id = idGenerator.getIdentificationID();
        task.setId(id);
        listTask.put(task.getId(), task);
        getTaskPriotity();
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        try {
            validTask(epic);
        } catch (IntersectionTaskException e) {
            throw new IntersectionTaskException(e.getMessage());
        }
        Integer id = idGenerator.getIdentificationID();
        epic.setId(id);
        epic.setStartTime();
        epic.getStartTime();
        listEpicTask.put(epic.getId(), epic);
        getTaskPriotity();
        return epic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        try {
            validTask(subTask);
        } catch (IntersectionTaskException e) {
            throw new IntersectionTaskException(e.getMessage());
        }
        Integer id = idGenerator.getIdentificationID();
        subTask.setId(id);
        Epic parentsTask = subTask.getParentTask();
        parentsTask.getSubTasks().add(subTask);
        parentsTask.setStartTime();
        parentsTask.setEndTime();
        listSubTask.put(subTask.getId(), subTask);
        newStatus(parentsTask);
        getTaskPriotity();
        return subTask;
    }

    @Override
    public List<SubTask> getSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public List<SubTask> getAllSubtasks(Epic epic) {
        return new ArrayList<>(getSubTasks(epic));
    }

    @Override
    public List<Task> getListTask() {
        return new ArrayList<>(listTask.values());
    }

    @Override
    public List<Epic> getListEpicTask() {
        return new ArrayList<>(listEpicTask.values());
    }

    @Override
    public List<SubTask> getListSubTask() {
        return new ArrayList<>(listSubTask.values());
    }

    @Override
    public Task deleteTask(Task task) {
        if (isNull(listTask.get(task.getId()))) {
            String errorMessage = String.format("Задача с id %s не найдена", task.getId());
            throw new NotTaskException(errorMessage);
        }
        historyManager.remove(task.getId());
        historyManager.deleteTaskHistory(task.getId());
        getTaskPriotity();
        return listTask.remove(task.getId());
    }

    @Override
    public Epic deleteEpicTask(Epic epic) {
        if (isNull(listEpicTask.get(epic.getId()))) {
            String errorMessage = String.format("Эпик с id %s не найдена", epic.getId());
            throw new NotTaskException(errorMessage);
        }
        List<SubTask> subTasksListByEpic = epic.getSubTasks();
        List<SubTask> toRemove = new ArrayList<>();
        for (SubTask subTask : listSubTask.values()) {
            if (subTasksListByEpic.contains(subTask)) {
                toRemove.add(subTask);
            }
        }
        for (SubTask subTask : toRemove) {
            listSubTask.remove(subTask.getId());
            historyManager.remove(subTask.getId());
        }
        historyManager.remove(epic.getId());
        historyManager.deleteTaskHistory(epic.getId());
        getTaskPriotity();
        return listEpicTask.remove(epic.getId());
    }

    @Override
    public SubTask deleteSubTask(SubTask subTask) {
        Integer id = subTask.getId();
        Epic parentsTask = subTask.getParentTask();
        listSubTask.remove(id);
        newStatus(parentsTask);
        parentsTask.setStartTime();
        historyManager.remove(subTask.getId());
        historyManager.deleteTaskHistory(subTask.getId());
        getTaskPriotity();
        return listSubTask.remove(id);
    }

    @Override
    public Task findTask(Integer id) {
        if (isNull(listTask.get(id))) {
            String errorMessage = String.format("Задача с id %s не найдена", id);
            throw new NotTaskException(errorMessage);
        }
        historyManager.add(listTask.get(id));
        return listTask.get(id);
    }

    @Override
    public Epic findEpicTask(Integer id) {
        if (isNull(listEpicTask.get(id))) {
            String errorMessage = String.format("Эпик с id %s не найден", id);
            throw new NotTaskException(errorMessage);
        }
        historyManager.add(listEpicTask.get(id));
        return listEpicTask.get(id);
    }

    @Override
    public SubTask findSubTask(Integer id) {
        if (isNull(listSubTask.get(id))) {
            String errorMessage = String.format("Подзадача с id %s не найдена", id);
            throw new NotTaskException(errorMessage);
        }
        historyManager.add(listSubTask.get(id));
        return listSubTask.get(id);
    }

    @Override
    public List<Task> deleteAllTask() {
        listTask.clear();
        getTaskPriotity();
        return new ArrayList<>(listTask.values());
    }

    @Override
    public Map<Integer, Epic> deleteAllEpicTask() {
        listEpicTask.clear();
        listSubTask.clear();
        getTaskPriotity();
        return listEpicTask;
    }

    @Override
    public Map<Integer, SubTask> deleteAllSubTask() {
        listSubTask.clear();
        getTaskPriotity();

        for (Epic epic : listEpicTask.values()) {
            epic.setStartTime();
            epic.setEndTime();
        }
        return listSubTask;
    }

    @Override
    public Task updateTask(Task newTask) {
        try {
            validTask(newTask);
        } catch (IntersectionTaskException e) {
            throw new IntersectionTaskException(e.getMessage());
        }
        if (listTask.containsKey(newTask.getId())) {
            Task existingTask = listTask.get(newTask.getId());
            existingTask.setName(newTask.getName());
            existingTask.setDescription(newTask.getDescription());
            existingTask.setStatus(newTask.getStatus());
            existingTask.setStartTime(newTask.getStartTime());
            existingTask.setDuration(newTask.getDuration());
            sortedTask.remove(existingTask);
            getTaskPriotity();
        }
        return listTask.put(newTask.getId(), newTask);
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        try {
            validTask(newEpic);
        } catch (IntersectionTaskException e) {
            throw new IntersectionTaskException(e.getMessage());
        }
        if (listEpicTask.containsKey(newEpic.getId())) {
            Epic existingTask = listEpicTask.get(newEpic.getId());
            existingTask.setName(newEpic.getName());
            existingTask.setDescription(newEpic.getDescription());
            existingTask.setStartTime();
            existingTask.setEndTime();
            existingTask.setStatus(newEpic.getStatus());
        }
        newStatus(newEpic);
        return listEpicTask.put(newEpic.getId(), newEpic);
    }

    @Override
    public SubTask updateSub(SubTask newSub) {
        try {
            validTask(newSub);
        } catch (IntersectionTaskException e) {
            throw new IntersectionTaskException(e.getMessage());
        }
        if (listSubTask.containsKey(newSub.getId())) {
            SubTask existingTask = listSubTask.get(newSub.getId());
            existingTask.setName(newSub.getName());
            existingTask.setDescription(newSub.getDescription());
            existingTask.setStatus(newSub.getStatus());
            existingTask.setStartTime(newSub.getStartTime());
            existingTask.setDuration(newSub.getDuration());
            existingTask.getParentTask().setEndTime();
            existingTask.getParentTask().setStartTime();
            newStatus(existingTask.getParentTask());
        }
        return listSubTask.put(newSub.getId(), newSub);
    }

    @Override
    public Status newStatus(Epic epic) {
        int valueNew = 0;
        int valueDone = 0;

        List<Status> subTasks = listSubTask.values().stream().filter(subTask -> subTask.getParentTask().equals(epic))
                .map(Task::getStatus).toList();

        for (Status subTask1 : subTasks) {
            if (subTask1 == null) {
                return epic.getStatus();
            } else if (subTask1 == Status.NEW) {
                valueNew++;
            } else if (subTask1 == Status.DONE) {
                valueDone++;
            }
        }
        if (valueNew == subTasks.size()) {
            return epic.setStatus(Status.NEW);
        } else if (valueDone == subTasks.size()) {
            return epic.setStatus(Status.DONE);
        }
        return epic.setStatus(Status.IN_PROGRESS);
    }

    @Override
    public TreeSet<Task> getTaskPriotity() {
        listTask.values().stream().filter(task -> task.getStartTime() != null).forEach(task -> sortedTask.add(task));
        listSubTask.values().stream().filter(subTask -> subTask.getStartTime() != null).forEach(subTask -> sortedTask.add(subTask));
        return sortedTask;
    }

    private void validTask(Task newTask) throws IntersectionTaskException {
        List<Task> collect = sortedTask.stream().filter(task -> (task.getStartTime().isBefore(newTask.getStartTime())) && ((task.getEndTime()).isAfter(newTask.getEndTime())) ||
                (task.getEndTime().isAfter(newTask.getStartTime())) && (task.getEndTime().isBefore(newTask.getEndTime()))).toList();

        if (!collect.isEmpty()) {
            throw new IntersectionTaskException("Задача: " + collect + " пересекается c существующей задачей.");
        }
    }
}