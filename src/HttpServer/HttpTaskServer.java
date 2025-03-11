package HttpServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;

public class HttpTaskServer {

    public static void main(String[] args) throws IOException {

        int PORT = 8080;

        TaskManager taskManager = Managers.getDefault();

        taskManager.addNewTask(new Task("Реализовать эндпоинты", "АВБ", Status.NEW, null, Duration.ofMinutes(10), Instant.now()));

        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.start();
        httpServer.createContext("/tasks", new HttpTaskHandler(taskManager));
//        httpServer.createContext("/epics", new HttpEpicHandler(taskManager));
//        httpServer.createContext("/subtasks", new HttpSubtasksHandler(taskManager));
//        httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
//        httpServer.createContext("/prioritized", new HttpPrioritiHandler(taskManager));
    }
}
