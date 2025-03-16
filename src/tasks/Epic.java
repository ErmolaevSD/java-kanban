package tasks;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subTasks = new ArrayList<>();
    private Instant endTime;

    public Epic(String nameTask, String descriptionTask, Status status, Integer id, Duration duration, Instant startTime) {
        super(nameTask, descriptionTask, status, id, duration, startTime);
    }

    @Override
    public Instant getEndTime() {
        endTime = getStartTime().plus(getDuration());
        setStartTime(endTime);
        return getStartTime().plus(getDuration());
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String stringToFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", getId(), TaskType.EPIC, getName(), getStatus(), getDescription(),getDuration(),getStartTime());
    }

    public List<Integer> getSubTasksID() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "nameTask='" + getName() + '\'' +
                ", descriptionTask='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}