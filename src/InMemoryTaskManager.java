import java.util.*;

public class InMemoryTaskManager implements TaskManager{

    private int identificatorID = 0;
    private final Map<Integer, Task> listTask = new HashMap<>();
    private final Map<Integer, Epic> listEpicTask = new HashMap<>();
    private final Map<Integer, SubTask> listSubTask = new HashMap<>();
    IdGenerator idGenerator = new IdGenerator();

    List<Task> viewedTask = new ArrayList<>();

    public InMemoryTaskManager() {
    }

    @Override
    public List<Task> getHistory() {
        return viewedTask;
    }

    public void add(Task task) {
        if (!(viewedTask.size() == 10)) {
            viewedTask.add(task);
        } else {
            viewedTask.removeFirst();
            viewedTask.add(task);
        }
    }



















    @Override
    public Task addNewTask(Task task) {
        Integer id = idGenerator.getIdentificatorID();
        task.setId(id);
        listTask.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Integer id = idGenerator.getIdentificatorID();
        epic.setId(id);
        listEpicTask.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        Integer id = idGenerator.getIdentificatorID();
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
    public List<SubTask> printAllSubtasks(Epic epic) {
        System.out.println("Подзадачи для эпика: " + epic.getName());
        return getSubTasks(epic);
    }

    @Override
    public List<Task> printListTask() {
        return new ArrayList<>(listTask.values());
    }

    @Override
    public List<Epic> printListEpicTask() {
        return new ArrayList<>(listEpicTask.values());
    }

    @Override
    public List<SubTask> printListSubTask() {
        return new ArrayList<>(listSubTask.values());
    }

    @Override
    public Task deleteNameTask(Integer id) {
        return listTask.remove(id);
    }

    @Override
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

    @Override
    public SubTask deleteSubTask(SubTask subTask) {
        Integer id = subTask.getId();
        Epic parentsTask = subTask.getParentTask();
        newStatus(parentsTask);
        return listSubTask.remove(id);
    }

    @Override
    public Task findTask(Integer id) {
        add(listTask.get(id));
        return listTask.get(id);
    }

    @Override
    public Epic findEpicTask(Integer id) {
        add(listEpicTask.get(id));
        return listEpicTask.get(id);
    }

    @Override
    public SubTask findSubTask(Integer id) {
        add(listSubTask.get(id));
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
            existingTask.setStatus(newTask.getSTATUS());
        }
        return listTask.put(newTask.getId(), newTask);
    }

    @Override
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

    @Override
    public SubTask updateSub(SubTask newSub) {
        if (listSubTask.containsKey(newSub.getId())) {
            Task existingTask = listSubTask.get(newSub.getId());
            existingTask.setName(newSub.getName());
            existingTask.setDescription(newSub.getDescription());
            existingTask.setStatus(newSub.getSTATUS());
        }
        return listSubTask.put(newSub.getId(), newSub);
    }

    @Override
    public Map<Integer, SubTask> getListSubTask() {
        return listSubTask;
    }

    @Override
    public int getIdentificatorID() {
        return identificatorID;
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



}