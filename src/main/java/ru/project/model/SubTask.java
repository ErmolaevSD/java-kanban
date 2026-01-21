package ru.project.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
public class SubTask extends Task {

    private final Integer parentTask;

    public SubTask(String nameTask, String descriptionTask, Integer parentTask, Duration duration, Instant startTime) {
        super(nameTask, descriptionTask);
        this.status = Status.NEW;
        this.parentTask = parentTask;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", parentTask=" + parentTask +
                '}';
    }
}