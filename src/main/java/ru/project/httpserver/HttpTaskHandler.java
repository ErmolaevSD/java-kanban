package ru.project.httpserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.project.exception.IntersectionTaskException;
import ru.project.exception.NotIntegerIdException;
import ru.project.exception.NotTaskException;
import ru.project.service.TaskManager;
import ru.project.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static java.util.Objects.isNull;

public class HttpTaskHandler extends BaseHttpHandler {

    public HttpTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            switch (method) {
                case "GET":
                    getTasks(exchange, paths);
                    break;
                case "POST":
                    postTasks(exchange);
                    break;
                case "DELETE":
                    deleteTasks(exchange, paths);
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

    private void getTasks(HttpExchange exchange, String[] paths) throws IOException {

        if (paths.length == 2) {
            Collection<Task> allTasks = taskManager.getListTask().values();
            String jsonTasks = gsonBuilder.toJson(allTasks);
            sendText(exchange, jsonTasks, 200);
        } else if (paths.length == 3) {
            int idTasks = Integer.parseInt(paths[2]);
            Task task = taskManager.findTaskById(idTasks);
            String jsonTask = gsonBuilder.toJson(task);
            sendText(exchange, jsonTask, 200);
        }
    }

    private void deleteTasks(HttpExchange exchange, String[] paths) throws IOException {
        int idTasks = Integer.parseInt(paths[2]);
        taskManager.deleteTask(taskManager.findTaskById(idTasks));
        sendText(exchange, "", 200);
    }

    private void postTasks(HttpExchange exchange) throws IOException {

        try (InputStream inputStream = exchange.getRequestBody()) {
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (isNull(body) || body.trim().isEmpty()) {
                sendError(exchange, "Тело запроса не может быть пустым", 400);
                return;
            }

            JsonElement jsonElement = JsonParser.parseString(body);

            if (!jsonElement.isJsonObject()) {
                sendError(exchange, "Некорректный JSON формат", 400);
                return;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Task task = gsonBuilder.fromJson(jsonObject, Task.class);

            if (isNull(task)) {
                sendError(exchange, "Не удалось десериализовать эпик", 400);
                return;
            }

            if (isNull(task.getId())) {
                Task createdTask = taskManager.createTask(task);

                if (isNull(createdTask)) {
                    sendError(exchange, "Не удалось создать таск", 500);
                    return;
                }

                String jsonTask = gsonBuilder.toJson(createdTask);
                sendSuccess(exchange, jsonTask);
            } else {
                Task updateTask = taskManager.updateTask(task);

                if (isNull(updateTask)) {
                    sendError(exchange, "Эпик с ID " + task.getId() + " не найден", 404);
                    return;
                }

                String jsonTask = gsonBuilder.toJson(updateTask);
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
