package ru.project.httpserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.project.exception.IntersectionTaskException;
import ru.project.exception.NotIntegerIdException;
import ru.project.exception.NotTaskException;
import ru.project.service.TaskManager;
import ru.project.model.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static java.util.Objects.isNull;

public class HttpSubtasksHandler extends BaseHttpHandler {

    public HttpSubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] paths = exchange.getRequestURI().getPath().split("/");

        logger.info("Поступил HTTP-запрос по URL={}, method={}", exchange.getRequestURI(), method);

        try {
            switch (method) {
                case "GET":
                    getSubTasks(exchange, paths);
                    break;
                case "POST":
                    postSubTask(exchange);
                    break;
                case "DELETE":
                    deleteSubTask(exchange, paths);
                    break;
                default:
                    sendError(exchange, "Обработка данного метода", 500);
                    break;
            }
        } catch (NotTaskException | NotIntegerIdException e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException e) {
            sendText(exchange, e.getMessage(), 406);
        }
    }

    private void getSubTasks(HttpExchange exchange, String[] paths) throws IOException {

        logger.info("Вызван method ={}, по URL={}, path={} ", exchange.getRequestMethod(), exchange.getRequestURI(), paths);

        if (paths.length == 2) {
            Collection<SubTask> allTasks = taskManager.getListSubTask().values();
            String jsonTasks = gsonBuilder.toJson(allTasks);
            sendSuccess(exchange, jsonTasks);
        } else if (paths.length == 3) {
            int idSubTask = Integer.parseInt(paths[2]);
            SubTask subTask = taskManager.findSubTaskById(idSubTask);
            String jsonTask = gsonBuilder.toJson(subTask);
            sendText(exchange, jsonTask, 200);
        }
    }

    private void deleteSubTask(HttpExchange exchange, String[] paths) throws IOException {
        int idTasks = Integer.parseInt(paths[2]);
        taskManager.deleteSubTask(taskManager.findSubTaskById(idTasks));
        sendText(exchange, "", 204);
    }

    private void postSubTask(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {

            String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (isNull(bodyString)) {
                sendError(exchange, "Тело запроса не может быть пустым", 400);
            }

            JsonElement jsonElement = JsonParser.parseString(bodyString);

            if (!jsonElement.isJsonObject()) {
                sendError(exchange, "Некорректный JSON формат", 400);
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            SubTask subTask = gsonBuilder.fromJson(jsonObject, SubTask.class);

            if (isNull(subTask)) {
                sendError(exchange, "Ошибка десериализации SubTask", 400);
            }

            if (subTask == null) {
                sendError(exchange, "Не удалось десериализовать SubTask", 400);
                return;
            }

            if (isNull(subTask.getId())) {
                SubTask createdSubTask = taskManager.createSubTask(subTask);

                if (createdSubTask == null) {
                    sendError(exchange, "Не удалось создать SubTask", 500);
                    return;
                }

                String jsonTask = gsonBuilder.toJson(createdSubTask);
                sendSuccess(exchange, jsonTask);

            } else {
                SubTask updateSubTask = taskManager.updateSub(subTask);

                if (updateSubTask == null) {
                    sendError(exchange, "SubTask с ID " + subTask.getId() + " не найден", 404);
                    return;
                }

                String jsonTask = gsonBuilder.toJson(updateSubTask);
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
