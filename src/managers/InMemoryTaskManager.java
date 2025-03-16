package managers;

import exception.IntersectionTaskException;
import exception.ManagerSaveException;
import exception.ManagerTimeException;
import exception.NotTaskException;
import tasks.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class InMemoryTaskManager implements TaskManager {

    protected int identificationID = 0;
    protected final Map<Integer, Task> listTask = new HashMap<>();
    protected final Map<Integer, Epic> listEpicTask = new HashMap<>();
    protected final Map<Integer, SubTask> listSubTask = new HashMap<>();
    private final HistoryManager historyManager;
    protected TreeSet<Task> sortedTask = new TreeSet<>(InMemoryTaskManager::compare);

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private static int compare(Task o1, Task o2) {
        long firstStartTime = o1.getStartTime().toEpochMilli();
        long twoStartTime = o2.getStartTime().toEpochMilli();
        return (int) (firstStartTime - twoStartTime);
    }

    public int nextID(){
        return identificationID++;
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
        task.setId(nextID());
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
        epic.setId(nextID());
        setStartTime(epic);
        setEndTime(epic);
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
        int id = nextID();
        subTask.setId(id);
        Integer parentsTask = subTask.getParentTask();
        Epic parentsEpic = listEpicTask.get(parentsTask);
        listEpicTask.get(parentsEpic.getId()).getSubTasksID().add(id);
        listSubTask.put(subTask.getId(), subTask);
        setStartTime(parentsEpic);
        setEndTime(parentsEpic);
        newStatus(parentsEpic);
        getTaskPriotity();
        return subTask;
    }

    @Override
    public List<SubTask> getSubTasks(List<Integer> subTaskById) {
        List<SubTask> subTasksEpic = new ArrayList<>();
        for (Integer i: subTaskById) {
            if (listSubTask.containsKey(i)) {
                subTasksEpic.add(listSubTask.get(i));
            }
        }
        return subTasksEpic;
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(listSubTask.values());
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
        List<Integer> subTasksByEpic = epic.getSubTasksID();
        List<SubTask> subTasksListByEpic = getSubTasks(subTasksByEpic);
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
        Integer parentsTask = subTask.getParentTask();
        Epic epic = listEpicTask.get(parentsTask);
        listSubTask.remove(id);
        epic.getSubTasksID().remove(id);
        newStatus(epic);
        setStartTime(epic);
        setEndTime(epic);
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
            setStartTime(epic);
            setEndTime(epic);
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
            setStartTime(existingTask);
            setEndTime(existingTask);
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
            setEndTime(listEpicTask.get(existingTask.getParentTask()));
            setStartTime(listEpicTask.get(existingTask.getParentTask()));
            newStatus(listEpicTask.get(existingTask.getParentTask()));
        }
        return listSubTask.put(newSub.getId(), newSub);
    }

    @Override
    public Status newStatus(Epic epic) {
        int valueNew = 0;
        int valueDone = 0;

        List<Status> subTasks = listSubTask.values().stream().filter(subTask -> subTask.getParentTask().equals(epic.getId()))
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

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void validTask(Task newTask) throws IntersectionTaskException {
        List<Task> collect = sortedTask.stream().filter(task -> (newTask.getStartTime().isAfter(task.getStartTime())) && (newTask.getStartTime().isBefore(task.getEndTime())) ||
                (newTask.getEndTime().isAfter(task.getStartTime())) && (newTask.getEndTime().isBefore(task.getEndTime())) || (newTask.getStartTime().isBefore(task.getStartTime())) && (newTask.getEndTime().isAfter(task.getEndTime()))).toList();

        if (!collect.isEmpty()) {
            throw new IntersectionTaskException("Задача: |" + collect.getFirst().getName() + " | пересекается c существующей задачей.");
        }
    }

    public void setStartTime(Epic epic) {
        List<Integer> subTaskByEpicId = epic.getSubTasksID();
        List<SubTask> subTasks = getSubTasks(subTaskByEpicId);

        if (subTasks.isEmpty()) {
        } else {
            epic.setStartTime(Instant.MIN);
            for (Integer id : subTaskByEpicId) {
                for (SubTask subTask : subTasks) {
                    try {
                        if (subTask.getStartTime().isAfter(epic.getStartTime())) {
                            epic.setStartTime(subTask.getStartTime());
                        }
                    } catch (ManagerTimeException e) {
                        throw new ManagerSaveException(e.getMessage());
                    }
                }
            }
        }
    }

    public void setEndTime(Epic epic) {
        List<Integer> subTaskByEpicId = epic.getSubTasksID();
        List<SubTask> subTasks = getSubTasks(subTaskByEpicId);

        for (Integer id : subTaskByEpicId) {
            for (SubTask subTask : subTasks) {
                try {
                    if (subTask.getStartTime().isAfter(epic.getStartTime())) {
                        epic.setStartTime(subTask.getStartTime());
                        epic.setDuration(subTask.getDuration());
                        epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
                    } else {
                        epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
                    }
                } catch (ManagerTimeException e) {
                    throw new ManagerTimeException(e.getMessage());
                }
            }
        }
    }
}
