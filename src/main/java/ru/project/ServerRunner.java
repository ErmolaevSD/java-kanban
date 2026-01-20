package ru.project;

import ru.project.httpserver.HttpTaskServer;
import ru.project.service.Managers;
import ru.project.service.TaskManager;

import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(8088, "localhost", taskManager);
        httpTaskServer.startTaskServer();
    }
}
