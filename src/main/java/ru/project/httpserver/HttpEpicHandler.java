package ru.project.httpserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.project.exception.IntersectionTaskException;
import ru.project.exception.NotIntegerIdException;
import ru.project.exception.NotTaskException;
import ru.project.service.TaskManager;
import ru.project.model.Epic;
import ru.project.model.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

public class HttpEpicHandler extends BaseHttpHandler {

    public HttpEpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            switch (method) {
                case "GET":
                    getEpic(exchange, paths);
                    break;
                case "POST":
                    postEpic(exchange);
                    break;
                case "DELETE":
                    deleteEpic(exchange, paths);
                    break;
                default:
                    sendError(exchange, "Обработка метода не предусмотрена", 500);
            }
        } catch (NotTaskException | NotIntegerIdException e) {
            sendError(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException e) {
            sendError(exchange, e.getMessage(), 406);
        }
    }

    private void getEpic(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 2) {
            Collection<Epic> allEpic = taskManager.getListEpicTask().values();
            String jsonTasks = gsonBuilder.toJson(allEpic);
            sendSuccess(exchange, jsonTasks);
        } else if (paths.length == 3) {
            Epic epic = taskManager.findEpicTaskById(Integer.parseInt(paths[2]));
            String jsonTask = gsonBuilder.toJson(epic);
            sendSuccess(exchange, jsonTask);
        } else if (paths.length == 4 && paths[3].equals("subtasks")) {
            int idEpic = Integer.parseInt(paths[2]);
            List<SubTask> subTasksByEpic = taskManager.getSubTasks(taskManager.findEpicTaskById(idEpic).getSubTasks());
            String jsonSubTasksByEpic = gsonBuilder.toJson(subTasksByEpic);
            sendSuccess(exchange, jsonSubTasksByEpic);
        }
    }

    private void deleteEpic(HttpExchange exchange, String[] paths) throws IOException {

        int idEpic = Integer.parseInt(paths[2]);
        taskManager.deleteEpicTask(taskManager.findEpicTaskById(idEpic));
        sendText(exchange, "", 204);
    }

    private void postEpic(HttpExchange exchange) throws IOException {
        try (InputStream requestBody = exchange.getRequestBody()) {

            String bodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            if (bodyString == null || bodyString.trim().isEmpty()) {
                sendError(exchange, "Тело запроса не может быть пустым", 400);
                return;
            }

            JsonElement jsonElement = JsonParser.parseString(bodyString);

            if (!jsonElement.isJsonObject()) {
                sendError(exchange, "Некорректный JSON формат", 400);
                return;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Epic epic = gsonBuilder.fromJson(jsonObject, Epic.class);

            if (epic == null) {
                sendError(exchange, "Не удалось десериализовать эпик", 400);
                return;
            }

            if (isNull(epic.getId())) {
                Epic createdEpic = taskManager.createEpic(epic);

                if (createdEpic == null) {
                    sendError(exchange, "Не удалось создать эпик", 500);
                    return;
                }

                String jsonTask = gsonBuilder.toJson(createdEpic);
                sendSuccess(exchange, jsonTask);

            } else {
                Epic updatedEpic = taskManager.updateEpic(epic);

                if (updatedEpic == null) {
                    sendError(exchange, "Эпик с ID " + epic.getId() + " не найден", 404);
                    return;
                }

                String jsonTask = gsonBuilder.toJson(updatedEpic);
                sendSuccess(exchange, jsonTask);
            }

        } catch (com.google.gson.JsonSyntaxException e) {
            sendError(exchange, "Некорректный JSON: " + e.getMessage(), 400);
        } catch (Exception e) {
            System.err.println("Ошибка в postEpic: " + e.getMessage());
            sendError(exchange, "Ошибка при обработке запроса: " + e.getMessage(), 400);
        }
    }
}