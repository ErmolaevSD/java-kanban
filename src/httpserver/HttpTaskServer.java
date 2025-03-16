package httpserver;

import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    int port;
    String hostname;
    HttpServer httpServer;

    public HttpTaskServer(int port, String hostname, TaskManager taskManager) throws IOException {
        this.port = port;
        this.hostname = hostname;

        httpServer = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        httpServer.createContext("/tasks", new HttpTaskHandler(taskManager));
        httpServer.createContext("/epics", new HttpEpicHandler(taskManager));
        httpServer.createContext("/subtasks", new HttpSubtasksHandler(taskManager));
        httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new HttpPrioritiHandler(taskManager));
    }
    public void startTaskServer() {
        System.out.printf("Сервер запущен и доступен по адресу: http://%s:%d\n", hostname, port);
        httpServer.start();
    }

    public void stopTaskServer(int delay) {
        System.out.println("Работа сервера завершена");
        httpServer.stop(delay);
    }
}
