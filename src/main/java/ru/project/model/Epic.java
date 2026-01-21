package ru.project.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String name, String description, Instant startTime, Duration duration) {
        super(name, description);
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                ", subTasks=" + subTasks +
                '}';
    }
}