package ru.project;

import com.sun.net.httpserver.HttpServer;
import ru.project.httpHandler.*;
import ru.project.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpTaskServer {

    private final int port;
    public final String hostname;
    public final HttpServer httpServer;

    public HttpTaskServer(int port, String hostname, TaskManager taskManager) throws IOException {
        this.port = port;
        this.hostname = hostname;

        httpServer = HttpServer.create(new InetSocketAddress(hostname, port), 0);

        httpServer.setExecutor(Executors.newFixedThreadPool(10));
        httpServer.createContext("/tasks", new HttpTaskHandler(taskManager));
        httpServer.createContext("/epics", new HttpEpicHandler(taskManager));
        httpServer.createContext("/subtasks", new HttpSubtasksHandler(taskManager));
        httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new HttpPriorityHandler(taskManager));
    }

    public void startTaskServer() {
        httpServer.start();
    }

    public void stopTaskServer(int delay) {
        httpServer.stop(delay);
    }
}