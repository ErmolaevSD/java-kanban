package ru.project.httpserver;

import org.junit.jupiter.api.Test;
import ru.project.model.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpEpicHandlerTest extends BaseHttpTest {

    private final URI URI_EPICS = URI.create("http://localhost:8088/epics");

    @Test
    public void testAddEpic_shouldIsOk() throws IOException, InterruptedException {

        Epic createdEpic = new Epic("Name Epic", "Description Epic", Instant.now(), Duration.ofMinutes(1));
        String jsonEpic = gson.toJson(createdEpic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI_EPICS)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getListEpicTask().size());
    }

    @Test
    public void testUpdateEpic_shouldIsOk() throws IOException, InterruptedException {

        Epic createdEpic = new Epic("Name Epic", "Description Epic", Instant.now(), Duration.ofMinutes(1));
        String jsonEpic = gson.toJson(createdEpic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI_EPICS)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .header("Content-Type", "application/json")
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Name Epic", taskManager.getListEpicTask().get(0).getName());

        Epic updateEpic = new Epic("Name Epic_Update", "Description Epic_Update", Instant.now(), Duration.ofMinutes(1));
        updateEpic.setId(0);
        String updateEpicJson = gson.toJson(updateEpic);

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI_EPICS)
                .POST(HttpRequest.BodyPublishers.ofString(updateEpicJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responseUpdate = httpClient.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseUpdate.statusCode());
        assertEquals("Name Epic_Update", taskManager.getListEpicTask().get(0).getName());
    }

    @Test
    public void testGetEpic_shouldIsOk() throws IOException, InterruptedException {

        Epic epic = new Epic("Name Epic", "Description Epic", Instant.now(), Duration.ofMinutes(1));
        taskManager.createEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_EPICS + "/0"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Epic getEpic = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(getEpic, taskManager.findEpicTaskById(0));
    }

    @Test
    public void deleteEpic_shouldIsOk() throws IOException, InterruptedException {

        Epic epic = new Epic("Name Epic", "Description Epic", Instant.now(), Duration.ofMinutes(1));
        taskManager.createEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_EPICS + "/0"))
                .DELETE()
                .build();

        assertEquals(1, taskManager.getListEpicTask().size());

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(0, taskManager.getListEpicTask().size());
    }
}