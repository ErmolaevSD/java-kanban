import java.util.*;

public class TaskManager {

    private int identificatorID = 0;
    private final Map<Integer, Task> listTask = new HashMap<>();
    private final Map<Integer, Epic> listEpicTask = new HashMap<>();
    private final Map<Integer, SubTask> listSubTask = new HashMap<>();
    IdGenerator idGenerator = new IdGenerator();

    public TaskManager() {
    }

    public Task addNewTask(Task task) {
        Integer id = idGenerator.getIdentificatorID();
        task.setId(id);
        listTask.put(task.getId(), task);
        return task;
    }

    public Epic addNewEpic(Epic epic) {
        Integer id = idGenerator.getIdentificatorID();
        epic.setId(id);
        listEpicTask.put(epic.getId(), epic);
        return epic;
    }

    public SubTask addNewSubTask(SubTask subTask) {
        Integer id = idGenerator.getIdentificatorID();
        subTask.setId(id);
        Epic parentsTask = subTask.getParentTask();
        parentsTask.getSubTasks().add(subTask);
        listSubTask.put(subTask.getId(), subTask);
        newStatus(parentsTask);
        return subTask;
    }

    private List<SubTask> getSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    public List<SubTask> printSubtasks(Epic epic) {
        System.out.println("Подзадачи для эпика: " + epic.getName());
        return getSubTasks(epic);
    }

    public List<Task> printListTask() {
        return new ArrayList<>(listTask.values());
    }

    public List<Epic> printListEpicTask() {
        return new ArrayList<>(listEpicTask.values());
    }

    public List<SubTask> printListSubTask() {
        return new ArrayList<>(listSubTask.values());
    }

    public Task deleteNameTask(Integer id) {
        return listTask.remove(id);
    }

    public Epic deleteEpicTask(Epic epic) {
        Integer id = epic.getId();
        List<SubTask> subTasksListByEpic = epic.getSubTasks();
        List<SubTask> toRemove = new ArrayList<>();
        for (SubTask subTask : listSubTask.values()) {
            if (subTasksListByEpic.contains(subTask)) {
                toRemove.add(subTask);
            }
        }
        for (SubTask subTask : toRemove) {
            listSubTask.remove(subTask.getId());
        }
        return listEpicTask.remove(id);
    }

    public SubTask deleteSubTask(SubTask subTask) {
        Integer id = subTask.getId();
        Epic parentsTask = subTask.getParentTask();
        newStatus(parentsTask);
        return listSubTask.remove(id);
    }

    public Task findTask(Integer id) {
        return listTask.get(id);
    }

    public Epic findEpicTask(Integer id) {
        return listEpicTask.get(id);
    }

    public SubTask findSubTask(Integer id) {
        return listSubTask.get(id);
    }

    public List<Task> deleteAllTask() {
        listTask.clear();
        return new ArrayList<>(listTask.values());

    }

    public Map<Integer, Epic> deleteAllEpicTask() {
        listEpicTask.clear();
        listSubTask.clear();
        return listEpicTask;
    }

    public Map<Integer, SubTask> deleteAllSubTask() {
        listSubTask.clear();
        return listSubTask;
    }

    public Task updateTask(Task newTask) {
        if (listTask.containsKey(newTask.getId())) {
            Task existingTask = listTask.get(newTask.getId());
            existingTask.setName(newTask.getName());
            existingTask.setDescription(newTask.getDescription());
            existingTask.setStatus(newTask.getSTATUS());
        }
        return listTask.put(newTask.getId(), newTask);
    }

    public Epic updateEpic(Epic newEpic) {
        if (listEpicTask.containsKey(newEpic.getId())) {
            Task existingTask = listEpicTask.get(newEpic.getId());
            existingTask.setName(newEpic.getName());
            existingTask.setDescription(newEpic.getDescription());
            existingTask.setStatus(newEpic.getSTATUS());
        }
        newStatus(newEpic);
        return listEpicTask.put(newEpic.getId(), newEpic);
    }

    public SubTask updateSub (SubTask newSub) {
        if (listSubTask.containsKey(newSub.getId())) {
            Task existingTask = listSubTask.get(newSub.getId());
            existingTask.setName(newSub.getName());
            existingTask.setDescription(newSub.getDescription());
            existingTask.setStatus(newSub.getSTATUS());
        }
        return listSubTask.put(newSub.getId(), newSub);
    }

    public Map<Integer, SubTask> getListSubTask() {
        return listSubTask;
    }

    private Status newStatus (Epic epic) {
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
                return epic.status;
            } else if (subTask1.status == Status.NEW) {
                valueNew++;
            } else if (subTask1.status == Status.DONE) {
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

    public int getIdentificatorID() {
        return identificatorID;
    }
}