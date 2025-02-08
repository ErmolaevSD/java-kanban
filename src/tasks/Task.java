package tasks;

import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status;
    private Integer id;

    public Task(String name, String description, Status status, Integer id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Status setStatus(Status status) {
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

    public String stringToFile() {
        return String.format("%s,%s,%s,%s,%s,\n",id, TaskType.TASK, name, status, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + name + '\'' +
                ", descriptionTask='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}