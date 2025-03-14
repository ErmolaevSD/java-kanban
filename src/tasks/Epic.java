package tasks;

import com.google.gson.annotations.Expose;
import exception.ManagerSaveException;
import exception.ManagerTimeException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<SubTask> subTasks;
    private Instant endTime;

    public Epic(String nameTask, String descriptionTask, Status status, Integer id, Duration duration, Instant startTime) {
        super(nameTask, descriptionTask, status, id, duration, startTime);
        subTasks = new ArrayList<>();

    }

    public String stringToFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", getId(), TaskType.EPIC, getName(), getStatus(), getDescription(),getDuration(),getStartTime());
    }

    public void setStartTime() {
        setStartTime(Instant.MAX);
        for (SubTask subTask : subTasks) {
            try {
                if (getStartTime().isAfter(subTask.getStartTime())) {
                    setStartTime(subTask.getStartTime());
                }
            } catch (ManagerTimeException e) {
                throw new ManagerSaveException(e.getMessage());
            }
        }
    }

    public void setEndTime() {
        for (SubTask subTask : subTasks) {
            try {
                if (subTask.getStartTime().isAfter(getStartTime())) {
                    setStartTime(subTask.getStartTime());
                    setDuration(subTask.getDuration());
                }
            } catch (ManagerTimeException e) {
                throw new ManagerTimeException(e.getMessage());
            }
        }
        endTime = getStartTime().plus(getDuration());
    }

    public List<SubTask> getSubTasks() {
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