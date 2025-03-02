package tasks;

import exception.ManagerTimeException;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status;
    private Integer id;
    private Duration duration;
    private Instant startTime;

    public Task(String name, String description, Status status, Integer id, Duration duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }


    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getStartTime() {
        if (startTime == null) {
            throw new ManagerTimeException("Не задано начальное время");
        } else return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public Instant getEndTime() {
        if (startTime == null) {
            throw new ManagerTimeException("Не задано начальное время");
        } else return startTime.plus(duration);
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
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", id, TaskType.TASK, name, status, description, duration, startTime);
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