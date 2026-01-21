package ru.project.httpserver;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import ru.project.model.Epic;
import ru.project.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpPriorityHandlerTest extends BaseHttpTest {

    private final URI URI_PRIORITY = URI.create("http://localhost:8088/prioritized");


    @Test
    void testGetPriorityTask() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Parent Epic",
                "Description Parent Epic",
                Instant.ofEpochSecond(10000),
                Duration.ofMinutes(1));

        Epic epic2 = new Epic("Parent Epic",
                "Description Parent Epic",
                Instant.ofEpochSecond(10),
                Duration.ofMinutes(1));

        Task task = new Task("Name Task", "Description Task");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI_PRIORITY)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> listTask = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertNotNull(listTask);
        assertEquals(listTask, taskManager.getTaskPriotity());
        assertEquals(epic2, listTask.getFirst());
    }
}