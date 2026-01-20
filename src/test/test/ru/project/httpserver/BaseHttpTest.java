package ru.project.httpserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.project.service.HistoryManager;
import ru.project.service.Managers;
import ru.project.service.TaskManager;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;

public class BaseHttpTest {

    protected TaskManager taskManager;
    protected HttpTaskServer httpTaskServer;
    protected Gson gson;
    protected HttpClient httpClient;
    protected HistoryManager historyManager;


    @BeforeEach
    public void setUp() throws IOException, InterruptedException {

        taskManager = Managers.getDefault();
        historyManager = taskManager.getHistoryManager();
        httpTaskServer = new HttpTaskServer(8088, "localhost", taskManager);
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new BaseHttpHandler.InstantTypeAdapter())
                .registerTypeAdapter(Duration.class, new BaseHttpHandler.DurationTypeAdapter())
                .serializeNulls()
                .create();
        httpClient = HttpClient.newHttpClient();

        httpTaskServer.startTaskServer();
        Thread.sleep(3000);
    }

    @AfterEach
    public void shutDown() {
        taskManager.deleteAllTask();
        taskManager.deleteAllEpicTask();
        taskManager.deleteAllSubTask();
        httpTaskServer.stopTaskServer(1);
    }
}
