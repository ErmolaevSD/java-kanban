package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<SubTask> subTasks;

    public Epic(String nameTask, String descriptionTask, Status status, Integer id) {
        super(nameTask, descriptionTask, status, id);
        subTasks = new ArrayList<>();
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}