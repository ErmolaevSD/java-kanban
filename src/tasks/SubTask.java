package tasks;

import java.time.Duration;
import java.time.Instant;

public class SubTask extends Task {

    private final Integer parentTask;

    public SubTask(String nameTask, String descriptionTask, Status status, Integer id, Integer parentTask, Duration duration, Instant startTime) {
        super(nameTask, descriptionTask, status, id, duration, startTime);
        this.parentTask = parentTask;

    }

    public Integer getParentTask() {
        return parentTask;
    }

    public String stringToFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", getId(), TaskType.SUBTASK, getName(), getStatus(),
                getDescription(),getId(),getDuration(),getStartTime());
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