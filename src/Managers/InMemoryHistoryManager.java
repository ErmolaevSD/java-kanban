package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> viewedTask = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (!(viewedTask.size() == 10)) {
            viewedTask.add(task);
        } else {
            viewedTask.removeFirst();
            viewedTask.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewedTask;
    }
}
