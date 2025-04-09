package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    private static class Node<T> {

        private Node<T> next;
        private Node<T> prev;
        private Task task;

        public Node(Node<T> prev, Task task, Node<T> next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public void add(Task task) {
        if (!(history.containsKey(task.getId()))) {
            if (head == null) {
                linkFirst(task);
            } else {
                linkLast(task);
            }
        } else {
            remove(task.getId());
            linkLast(task);
        }
    }

    private void linkFirst(Task task) {
        Node<Task> firstNode = new Node<>(null, task, null);
        history.put(task.getId(), firstNode);
        head = firstNode;
        tail = firstNode;
    }

    private void linkLast(Task task) {
        Node<Task> node = new Node<>(null, task, null);
        if (isNull(tail) && isNull(head)) {
            tail = node;
            node.prev = null;
            node.next = null;
        } else {
            tail.next = node;
            node.prev = tail;
            this.tail = node;
        }
        history.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
        }
    }

    @Override
    public void deleteTaskHistory(int id) {
        history.remove(id);
    }

    public void removeNode(Node<Task> node) {
        if (!(node == null)) {
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;
            node.task = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node) {
                head = next;
                head.prev = null;
            } else if (tail == node) {
                tail.next = null;
                tail = prev;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    private List<Task> getTask() {
        List<Task> historyTask = new ArrayList<>();
        Node<Task> counter = head;

        while (!(counter == null)) {
            historyTask.add(counter.task);
            counter = counter.next;
        }
        return historyTask;
    }
}
