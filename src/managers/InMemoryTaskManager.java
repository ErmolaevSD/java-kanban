package managers;

import tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    protected int identificationID = 0;
    protected final Map<Integer, Task> listTask = new HashMap<>();
    protected final Map<Integer, Epic> listEpicTask = new HashMap<>();
    protected final Map<Integer, SubTask> listSubTask = new HashMap<>();
    IdGenerator idGenerator = new IdGenerator();
    private final HistoryManager historyManager;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setIdentificationID(int identificationID) {
        this.identificationID = identificationID;
    }

    @Override
    public Task addNewTask(Task task) {
        Integer id = idGenerator.getIdentificationID();
        task.setId(id);
        listTask.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Integer id = idGenerator.getIdentificationID();
        epic.setId(id);
        listEpicTask.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        Integer id = idGenerator.getIdentificationID();
        subTask.setId(id);
        Epic parentsTask = subTask.getParentTask();
        parentsTask.getSubTasks().add(subTask);
        listSubTask.put(subTask.getId(), subTask);
        newStatus(parentsTask);
        return subTask;
    }

    @Override
    public List<SubTask> getSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public List<SubTask> getAllSubtasks(Epic epic) {
        return getSubTasks(epic);
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
        historyManager.remove(task.getId());
        historyManager.deleteTaskHistory(task.getId());
        return listTask.remove(task.getId());
    }

    @Override
    public Epic deleteEpicTask(Epic epic) {
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
        return listEpicTask.remove(epic.getId());
    }

    @Override
    public SubTask deleteSubTask(SubTask subTask) {
        Integer id = subTask.getId();
        Epic parentsTask = subTask.getParentTask();
        listSubTask.remove(id);
        newStatus(parentsTask);
        historyManager.remove(subTask.getId());
        historyManager.deleteTaskHistory(subTask.getId());
        return listSubTask.remove(id);
    }

    @Override
    public Task findTask(Integer id) {
        historyManager.add(listTask.get(id));
        return listTask.get(id);
    }

    @Override
    public Epic findEpicTask(Integer id) {
        historyManager.add(listEpicTask.get(id));
        return listEpicTask.get(id);
    }

    @Override
    public SubTask findSubTask(Integer id) {
        historyManager.add(listSubTask.get(id));
        return listSubTask.get(id);
    }

    @Override
    public List<Task> deleteAllTask() {
        listTask.clear();
        return new ArrayList<>(listTask.values());
    }

    @Override
    public Map<Integer, Epic> deleteAllEpicTask() {
        listEpicTask.clear();
        listSubTask.clear();
        return listEpicTask;
    }

    @Override
    public Map<Integer, SubTask> deleteAllSubTask() {
        listSubTask.clear();
        return listSubTask;
    }

    @Override
    public Task updateTask(Task newTask) {
        if (listTask.containsKey(newTask.getId())) {
            Task existingTask = listTask.get(newTask.getId());
            existingTask.setName(newTask.getName());
            existingTask.setDescription(newTask.getDescription());
            existingTask.setStatus(newTask.getStatus());
        }
        return listTask.put(newTask.getId(), newTask);
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        if (listEpicTask.containsKey(newEpic.getId())) {
            Task existingTask = listEpicTask.get(newEpic.getId());
            existingTask.setName(newEpic.getName());
            existingTask.setDescription(newEpic.getDescription());
            existingTask.setStatus(newEpic.getStatus());
        }
        newStatus(newEpic);
        return listEpicTask.put(newEpic.getId(), newEpic);
    }

    @Override
    public SubTask updateSub(SubTask newSub) {
        if (listSubTask.containsKey(newSub.getId())) {
            Task existingTask = listSubTask.get(newSub.getId());
            existingTask.setName(newSub.getName());
            existingTask.setDescription(newSub.getDescription());
            existingTask.setStatus(newSub.getStatus());
        }
        return listSubTask.put(newSub.getId(), newSub);
    }

    @Override
    public Status newStatus(Epic epic) {
        int valueNew = 0;
        int valueDone = 0;
        List<SubTask> subTasksByEpic = new ArrayList<>();
        for (SubTask subTask : listSubTask.values()) {
            if (subTask.getParentTask().equals(epic)) {
                subTasksByEpic.add(subTask);
            }
        }
        for (SubTask subTask1 : subTasksByEpic) {
            if (subTask1 == null) {
                return epic.getStatus();
            } else if (subTask1.getStatus() == Status.NEW) {
                valueNew++;
            } else if (subTask1.getStatus() == Status.DONE) {
                valueDone++;
            }
        }
        if (valueNew == subTasksByEpic.size()) {
            return epic.setStatus(Status.NEW);
        } else if (valueDone == subTasksByEpic.size()) {
            return epic.setStatus(Status.DONE);
        }
        return epic.setStatus(Status.IN_PROGRESS);
    }
}