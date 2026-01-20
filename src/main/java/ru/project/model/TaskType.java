package ru.project.model;

public enum TaskType {
    TASK("TASK"),
    EPIC("EPIC"),
    SUBTASK("SUBTASK");

    private final String taskType;

    TaskType(String taskType) {
        this.taskType = taskType;
    }
}
