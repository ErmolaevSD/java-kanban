package tasks;

public class SubTask extends Task {

    private final Epic parentTask;

    public SubTask(String nameTask, String descriptionTask, Status status, Integer id, Epic parentTask) {
        super(nameTask, descriptionTask, status, id);
        this.parentTask = parentTask;
    }

    public Epic getParentTask() {
        return parentTask;
    }

    public String stringToFile() {
        return String.format("%s,%s,%s,%s,%s,%s\n", getId(), TaskType.SUBTASK, getName(), getStatus(), getDescription(), getParentTask().getId());
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "nameTask='" + getName() + '\'' +
                ", descriptionTask='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}