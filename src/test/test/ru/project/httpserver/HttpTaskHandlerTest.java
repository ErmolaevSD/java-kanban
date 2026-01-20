package ru.project.httpserver;

import org.junit.jupiter.api.Test;
import ru.project.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskHandlerTest extends BaseHttpTest {

    private final URI URI_TASKS = URI.create("http://localhost:8088/tasks");

    @Test
    void testCreateTask_shouldIsOk() throws IOException, InterruptedException {

        Task task = new Task("Name Task", "Description Task");
        String jsonTask = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI_TASKS)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task createdTask = taskManager.findTaskById(0);

        assertEquals(0, createdTask.getId());
        assertEquals("Name Task", createdTask.getName());
        assertEquals("Description Task", createdTask.getDescription());
    }

    @Test
    void testGetTaskyId_shouldIsOk() throws IOException, InterruptedException {

        Task task = new Task("Name Task", "Description Task");
        taskManager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_TASKS + "/0"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task findTask = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertEquals(findTask, taskManager.findTaskById(0));
    }

    @Test
    void testDeleteTask_shouldIsOk() throws IOException, InterruptedException {

        Task task = new Task("Name Task", "Description Task");
        taskManager.createTask(task);

        assertEquals(1, taskManager.getListTask().size());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_TASKS + "/0"))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(taskManager.getListTask().isEmpty());
    }
}