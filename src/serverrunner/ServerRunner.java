package serverrunner;

import httpserver.HttpTaskServer;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(8080, "localhost", taskManager);
        httpTaskServer.startTaskServer();
    }
}
