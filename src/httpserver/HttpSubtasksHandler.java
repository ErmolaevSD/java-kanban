package httpserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionTaskException;
import exception.NotIntegerIdException;
import exception.NotTaskException;
import managers.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Objects.isNull;

public class HttpSubtasksHandler extends BaseHttpHandler {

    public HttpSubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            switch (method) {
                case "GET":
                    getSubTasks(exchange, paths);
                case "POST":
                    postSubTask(exchange);
                case "DELETE":
                    deleteSubTask(exchange, paths);
                default:
                    String errorMessage = "Обработка данного метода " + method + " не предусмотренна";
                    String jsonTask = gsonBuilder.toJson(errorMessage);
                    sendText(exchange, jsonTask, 500);
            }
        } catch (NotTaskException | NotIntegerIdException e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException e) {
            sendText(exchange, e.getMessage(), 406);

        } finally {
            exchange.close();
        }
    }

    private void getSubTasks(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 2) {
            List<SubTask> allTasks = taskManager.getListSubTask();
            String jsonTasks = gsonBuilder.toJson(allTasks);
            sendText(exchange, jsonTasks, 200);
        } else if (paths.length == 3) {
            int idSubTask = Integer.parseInt(paths[2]);
            SubTask subTask = taskManager.findSubTask(idSubTask);
            String jsonTask = gsonBuilder.toJson(subTask);
            sendText(exchange, jsonTask, 200);
        }
    }

    private void deleteSubTask(HttpExchange exchange, String[] paths) throws IOException {
            int idTasks = Integer.parseInt(paths[2]);
            SubTask deteteTask = taskManager.deleteSubTask(taskManager.findSubTask(idTasks));
            String jsonTask = gsonBuilder.toJson(deteteTask);
            sendText(exchange, jsonTask, 200);
    }

    private void postSubTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String bode = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(bode);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        SubTask subTask = gsonBuilder.fromJson(jsonObject, SubTask.class);

        if (isNull(subTask.getId())) {
            SubTask createdTasks = taskManager.addNewSubTask(subTask);
            String jsonTask = gsonBuilder.toJson(createdTasks);
            sendText(exchange, jsonTask, 201);
        } else {
            SubTask updateTask = taskManager.updateSub(subTask);
            String jsonTask = gsonBuilder.toJson(updateTask);
            sendText(exchange, jsonTask, 201);
        }
    }
}
