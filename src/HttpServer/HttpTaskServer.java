package HttpServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;

public class HttpTaskServer {

    public static void main(String[] args) throws IOException {

        int PORT = 8080;

        TaskManager taskManager = Managers.getDefault();

        taskManager.addNewTask(new Task("Task1", "АВБ", Status.NEW, null, Duration.ofMinutes(10), Instant.now()));
        taskManager.addNewTask(new Task("Task2", "АВБ", Status.NEW, null, Duration.ofMinutes(10), Instant.now().plusSeconds(10000)));

        Epic epic1 = new Epic("Epi1", "ABC", Status.NEW, null, Duration.ofMinutes(5), Instant.now().plusSeconds(5000));
        taskManager.addNewEpic(epic1);
        taskManager.addNewSubTask(new SubTask("SubTask1", "ABC", Status.NEW, null, epic1, Duration.ofMinutes(1), Instant.now().plusSeconds(6000)));
        taskManager.addNewSubTask(new SubTask("SubTask2", "ABC", Status.DONE, null, epic1, Duration.ofMinutes(1), Instant.now().plusSeconds(5006)));

        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.start();
        httpServer.createContext("/tasks", new HttpTaskHandler(taskManager));
        httpServer.createContext("/epics", new HttpEpicHandler(taskManager));
        httpServer.createContext("/subtasks", new HttpSubtasksHandler(taskManager));
//        httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
//        httpServer.createContext("/prioritized", new HttpPrioritiHandler(taskManager));
    }
}
