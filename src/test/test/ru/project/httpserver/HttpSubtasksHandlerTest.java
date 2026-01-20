package ru.project.httpserver;

import org.junit.jupiter.api.Test;
import ru.project.model.Epic;
import ru.project.model.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpSubtasksHandlerTest extends BaseHttpTest {

    private final URI URI_SUBTASK = URI.create("http://localhost:8088/subtasks");

    @Test
    void testCreatedSubTask_shouldIsOk() throws IOException, InterruptedException {

        Epic parentEpic = new Epic("Parent Epic",
                "Description Parent Epic",
                Instant.ofEpochSecond(10),
                Duration.ofMinutes(1));
        taskManager.createEpic(parentEpic);

        SubTask subTask = new SubTask("Name SubTask",
                "Description SubTask",
                parentEpic.getId(),
                Duration.ofMinutes(1),
                Instant.ofEpochSecond(10000)
        );

        String createSubTask = gson.toJson(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI_SUBTASK)
                .POST(HttpRequest.BodyPublishers.ofString(createSubTask))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        SubTask createdSubTask = gson.fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getListSubTask().size());
        assertEquals(createdSubTask, taskManager.findSubTaskById(1));
    }

    @Test
    void testGetSubTask_shouldIsOk() throws IOException, InterruptedException {

        Epic parentEpic = new Epic("Parent Epic",
                "Description Parent Epic",
                Instant.ofEpochSecond(10),
                Duration.ofMinutes(1));
        taskManager.createEpic(parentEpic);

        SubTask subTask = new SubTask("Name SubTask",
                "Description SubTask",
                parentEpic.getId(),
                Duration.ofMinutes(1),
                Instant.ofEpochSecond(10000)
        );
        taskManager.createSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_SUBTASK + "/1"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        SubTask findSubTask = gson.fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(findSubTask, taskManager.findSubTaskById(1));
    }

    @Test
    void testDeleteSubTask_shouldIsOk() throws IOException, InterruptedException {

        Epic parentEpic = new Epic("Parent Epic",
                "Description Parent Epic",
                Instant.ofEpochSecond(10),
                Duration.ofMinutes(1));
        taskManager.createEpic(parentEpic);

        SubTask subTask = new SubTask("Name SubTask",
                "Description SubTask",
                parentEpic.getId(),
                Duration.ofMinutes(1),
                Instant.ofEpochSecond(10000)
        );
        taskManager.createSubTask(subTask);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URI_SUBTASK + "/1"))
                .DELETE()
                .build();

        assertEquals(1, taskManager.getListSubTask().size());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        assertTrue(taskManager.getListTask().isEmpty());
    }
}