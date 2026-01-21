package ru.project.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.project.exception.IntersectionTaskException;
import ru.project.exception.NotFoundException;
import ru.project.model.Epic;
import ru.project.model.Status;
import ru.project.model.SubTask;
import ru.project.model.Task;

import java.time.Instant;
import java.util.*;

import static java.util.Objects.isNull;

@Setter
@Getter
@RequiredArgsConstructor
public class InMemoryTaskManager implements TaskManager {

    private final Logger logger = LoggerFactory.getLogger(InMemoryTaskManager.class);
    private final HistoryManager historyManager;
    private Map<Integer, Task> listTask = new HashMap<>();
    private Map<Integer, Epic> listEpicTask = new HashMap<>();
    private Map<Integer, SubTask> listSubTask = new HashMap<>();
    private int identificationID = 0;
    private TreeSet<Task> sortedTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int nextID() {
        return identificationID++;
    }

    @Override
    public Task createTask(Task task) {
        logger.info("Создание задачи (Task) {}", task);
        validTask(task);
        task.setId(nextID());
        listTask.put(task.getId(), task);
        logger.info("Успешно создана задача (Task) {}", task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        logger.info("Создание задачи (Epic) {}", epic);
        validTask(epic);
        epic.setId(nextID());
        listEpicTask.put(epic.getId(), epic);
        logger.info("Успешно создана задача (Epic) {}", epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        logger.info("Создание задачи (SubTask) {}", subTask);
        validTask(subTask);
        subTask.setId(nextID());
        Epic parentEpic = listEpicTask.get(subTask.getParentTask());
        listEpicTask.get(parentEpic.getId()).getSubTasks().add(subTask.getId());
        listSubTask.put(subTask.getId(), subTask);
        updateEpicForAddSubTask(parentEpic);
        logger.info("Успешно создана задача (SubTask) {}", subTask);
        return subTask;
    }

    @Override
    public void deleteTask(Task task) {
        logger.info("Удаление задачи (Task) {}", task);
        this.findTaskById(task.getId());
        validTask(task);
        historyManager.remove(task.getId());
        historyManager.deleteTaskHistory(task.getId());
        listTask.remove(task.getId());
        logger.info("Успешно удалена задача (Task) {}", task);
    }

    /**
     * Удаление главной задачи - удаление всех входящих в нее подзадач (SubTask)
     *
     * @param epic - главная задача
     */
    @Override
    public void deleteEpicTask(Epic epic) {
        logger.info("Удаление задачи (Epic) {}", epic);
        this.findEpicTaskById(epic.getId());
        List<SubTask> subTasksListByEpic = getSubTasks(epic.getSubTasks());
        List<SubTask> toRemove = new ArrayList<>();

        listSubTask.values().forEach(subTask -> {
            if (subTasksListByEpic.contains(subTask)) {
                toRemove.add(subTask);
            }
        });

        toRemove.forEach(subTask -> {
            listSubTask.remove(subTask.getId());
            historyManager.remove(subTask.getId());
        });
        historyManager.remove(epic.getId());
        historyManager.deleteTaskHistory(epic.getId());
        listEpicTask.remove(epic.getId());
        logger.info("Успешно удалена задача (Epic) {}", epic);
    }

    /**
     * Удаление подраздачи (SubTask) вызывает перерасчет зависящих данных в главной задаче (Epic)
     *
     * @param subTask - подзадача
     */
    @Override
    public void deleteSubTask(SubTask subTask) {
        logger.info("Удаление задачи (SubTask) {}", subTask);
        this.findSubTaskById(subTask.getId());
        Epic epic = listEpicTask.get(subTask.getParentTask());
        epic.getSubTasks().remove(subTask.getId());
        updateEpicForAddSubTask(epic);
        historyManager.remove(subTask.getId());
        historyManager.deleteTaskHistory(subTask.getId());
        listSubTask.remove(subTask.getId());
        logger.info("Успешно удалена задача (SubTask) {}", subTask);
    }

    @Override
    public Task findTaskById(Integer id) {
        logger.info("Поиск задачи по ID=[{}] (Task)", id);
        Task task = listTask.get(id);
        if (isNull(task)) {
            throw new NotFoundException("Задача (Task) с id %s не найдена".formatted(id));
        } else {
            historyManager.addTask(listTask.get(id));
            logger.info("Задача с ID=[{}] (Task) найдена", id);
            return task;
        }
    }

    @Override
    public Epic findEpicTaskById(Integer id) {
        logger.info("Поиск задачи по ID=[{}] (Epic)", id);
        Epic epic = listEpicTask.get(id);
        if (isNull(epic)) {
            throw new NotFoundException("Задача (Epic) с id %s не найдена".formatted(id));
        } else {
            historyManager.addTask(listEpicTask.get(id));
            logger.info("Задача с ID=[{}] (Epic) найдена", id);
            return epic;
        }
    }

    @Override
    public SubTask findSubTaskById(Integer id) {
        logger.info("Поиск задачи по ID=[{}] (SubTask)", id);
        SubTask subTask = listSubTask.get(id);

        if (isNull(subTask)) {
            throw new NotFoundException("Задача (SubTask) с id %s не найдена".formatted(id));
        } else {
            historyManager.addTask(listSubTask.get(id));
            logger.info("Задача с ID=[{}] (SubTask) найдена", id);
            return subTask;
        }
    }

    @Override
    public void deleteAllTask() {
        logger.info("Запрос на удаление всех задач (Task)");
        listTask.values().forEach(task -> historyManager.remove(task.getId()));
        listTask.clear();
        logger.info("Все задачи (Task) успешно удалены");
    }

    @Override
    public void deleteAllEpicTask() {
        logger.info("Запрос на удаление всех задач (Epic)");
        listEpicTask.values().forEach(epic -> historyManager.remove(epic.getId()));
        listSubTask.values().forEach(subTask -> historyManager.remove(subTask.getId()));
        listEpicTask.clear();
        listSubTask.clear();
        logger.info("Все задачи (Epic) успешно удалены");
    }

    /**
     * Удаление всех подзадач - перерасчет задач (Epic)
     */
    @Override
    public void deleteAllSubTask() {
        logger.info("Запрос на удаление всех задач (SubTask)");
        listSubTask.clear();
        listEpicTask.values().forEach(this::updateEpicForAddSubTask);
        logger.info("Все задачи (SubTask) успешно удалены");
    }

    @Override
    public Task updateTask(Task newTask) {
        logger.info("Обновление задачи (Task) {}", newTask);
        Task existingTask = this.findTaskById(newTask.getId());
        existingTask.setName(newTask.getName());
        existingTask.setDescription(newTask.getDescription());
        existingTask.setStatus(newTask.getStatus());
        existingTask.setStartTime(newTask.getStartTime());
        existingTask.setDuration(newTask.getDuration());
        logger.info("Задача (Task) успешно обновлена {}", existingTask);
        return existingTask;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        logger.info("Обновление задачи (Epic) {}", newEpic);
        Epic existingTask = this.findEpicTaskById(newEpic.getId());
        existingTask.setName(newEpic.getName());
        existingTask.setDescription(newEpic.getDescription());
        updateEpicForAddSubTask(existingTask);
        logger.info("Задача (Epic) успешно обновлена {}", existingTask);
        return existingTask;
    }

    @Override
    public SubTask updateSub(SubTask newSub) {
        logger.info("Обновление задачи (SubTask) {}", newSub);
        SubTask existingTask = this.findSubTaskById(newSub.getId());
        existingTask.setName(newSub.getName());
        existingTask.setDescription(newSub.getDescription());
        existingTask.setStatus(newSub.getStatus());
        existingTask.setStartTime(newSub.getStartTime());
        existingTask.setDuration(newSub.getDuration());
        updateEpicForAddSubTask(listEpicTask.get(existingTask.getParentTask()));
        logger.info("Задача (SubTask) успешно обновлена {}", existingTask);
        return existingTask;
    }

    /**
     * Расчет статуса главной задачи (Epic) исходя из статусов входящих в нее подзадач (SubTask)
     *
     * @param epic - главная задача
     */
    public void newStatus(Epic epic) {
        logger.info("Перерасчет статуса задачи (Epic) {}", epic);
        int valueNew = 0;
        int valueDone = 0;

        List<Status> subTasks = listSubTask.values().stream().filter(subTask -> subTask.getParentTask().equals(epic.getId()))
                .map(Task::getStatus).toList();

        for (Status subTask1 : subTasks) {
            if (subTask1 == Status.NEW) {
                valueNew++;
            } else if (subTask1 == Status.DONE) {
                valueDone++;
            }
        }

        if (valueNew == subTasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (valueDone == subTasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        logger.info("Статус задачи (Epic) обновлен ");
    }

    @Override
    public List<Task> getTaskPriotity() {
        logger.info("Начало сортировки задач по времени начала (приоритет)");
        listTask.forEach((integer, task) -> {
            if (!isNull(task.getStartTime())) {
                sortedTask.add(task);
            }
        });
        listEpicTask.forEach((integer, epic) -> sortedTask.add(epic));
        listSubTask.forEach((integer, subTask) -> sortedTask.add(subTask));
        logger.info("Задачи отсортированы {}", sortedTask);
        return new ArrayList<>(sortedTask);
    }

    private void validTask(Task newTask) throws IntersectionTaskException {
        logger.info("Проверка задачи {} на пересечение по времени", newTask);
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return;
        }

        List<Task> overlappingTasks = sortedTask.stream()
                .filter(task -> task.getStartTime() != null && task.getEndTime() != null)
                .filter(task -> isTasksOverlapping(newTask, task))
                .toList();

        if (!overlappingTasks.isEmpty()) {
            Task firstOverlap = overlappingTasks.getFirst();
            logger.info("Задача {} пересекается с задачей {}", newTask, firstOverlap);
            throw new IntersectionTaskException(
                    String.format("Задача '%s' пересекается с существующей задачей '%s'",
                            newTask.getName(), firstOverlap.getName()));
        } else {
            logger.info("Задача {} не пересекается с другими", newTask);
            sortedTask.add(newTask);
        }
    }

    private boolean isTasksOverlapping(Task task1, Task task2) {
        Instant start1 = task1.getStartTime();
        Instant end1 = task1.getEndTime();
        Instant start2 = task2.getStartTime();
        Instant end2 = task2.getEndTime();

        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }

    /**
     * Обновление начального времени главной задачи (Epic) исходя из расчетов его подразач (SubTask)
     *
     * @param epic - главная задача
     */
    private void setStartTime(Epic epic) {
        logger.info("Обновление времени начала задачи (Epic) исходя из подзадач (SubTask) {}", epic);
        List<SubTask> subTasks = getSubTasks(epic.getSubTasks());

        if (!(subTasks.isEmpty())) {
            epic.setStartTime(Instant.MIN);
            subTasks.forEach(subTask -> {
                if (subTask.getStartTime().isAfter(epic.getStartTime())) {
                    epic.setStartTime(subTask.getStartTime());
                }
            });
        }
        logger.info("Начальное время задачи {} успешно обновлено", epic);
    }

    /**
     * Аналогично методу setStartTime только расчет времени окончания
     *
     * @param epic - главная задача
     */
    private void setEndTime(Epic epic) {
        logger.info("Обновление времени окончания задачи (Epic) исходя из подзадач (SubTask) {}", epic);
        List<SubTask> subTasks = getSubTasks(epic.getSubTasks());

        subTasks.forEach(subTask -> {
            if (subTask.getEndTime().isAfter(epic.getEndTime())) {
                epic.setEndTime(subTask.getEndTime());
            }
        });
        logger.info("Время окончания задачи {} успешно обновлено", epic);
    }

    @Override
    public List<SubTask> getSubTasks(List<Integer> subTaskById) {
        logger.info("Получение списка подзадач (SubTaks) {}", subTaskById);
        List<SubTask> subTasksEpic = new ArrayList<>();
        for (Integer i : subTaskById) {
            if (listSubTask.containsKey(i)) {
                subTasksEpic.add(listSubTask.get(i));
            }
        }
        logger.info("Получены подзадачи (SubTasks) {}", subTasksEpic);
        return subTasksEpic;
    }

    private void updateEpicForAddSubTask(Epic epic) {
        setStartTime(epic);
        setEndTime(epic);
        newStatus(epic);
    }
}
