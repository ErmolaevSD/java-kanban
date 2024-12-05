import java.util.Objects;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected STATUS status;
    protected Integer id;

    public Task(String nameTask, String descriptionTask, STATUS status, Integer id) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.status = status;
        this.id = id;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public STATUS getSTATUS() {
        return status;
    }

    public STATUS setStatus(STATUS status) {
        this.status = status;
        return status;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", status=" + status +
                '}';
    }

    enum STATUS{
        NEW,
        IN_PROGRESS,
        DONE
    }

}