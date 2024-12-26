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

    @Override
    public String toString() {
        return super.toString();
    }
}