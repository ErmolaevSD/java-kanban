package httpserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionTaskException;
import exception.NotIntegerIdException;
import exception.NotTaskException;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static java.util.Objects.isNull;

public class HttpTaskHandler extends BaseHttpHandler {

    public HttpTaskHandler(TaskManager taskManager) {
        BaseHttpHandler.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            switch (method) {
                case GET:
                    getTasks(exchange, paths);
                    break;
                case POST:
                    postTasks(exchange);
                    break;
                case DELETE:
                    deleteTasks(exchange, paths);
                    break;
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

    private void getTasks(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 2) {
            List<Task> allTasks = taskManager.getListTask();
            String jsonTasks = gsonBuilder.toJson(allTasks);
            sendText(exchange, jsonTasks, 200);
        } else if (paths.length == 3) {
            int idTasks;
            try {
                idTasks = Integer.parseInt(paths[2]);
            } catch (NumberFormatException e) {
                throw new NotIntegerIdException("В качестве id введено некорректное значение.");
            }
            Task task = taskManager.findTask(idTasks);
            String jsonTask = gsonBuilder.toJson(task);
            sendText(exchange, jsonTask, 200);
        }
    }

    private void deleteTasks(HttpExchange exchange, String[] paths) throws IOException {
        int idTasks;
        try {
            idTasks = Integer.parseInt(paths[2]);
        } catch (NumberFormatException e) {
            throw new NotIntegerIdException("В качестве id введено некорректное значение.");
        }
            Task deteteTask = taskManager.deleteTask(taskManager.findTask(idTasks));
            String jsonTask = gsonBuilder.toJson(deteteTask);
            sendText(exchange, jsonTask, 200);
    }

    private void postTasks(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String bode = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(bode);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task = gsonBuilder.fromJson(jsonObject, Task.class);

        if (isNull(task.getId()) || isNull(taskManager.findTask(task.getId()))) {
            Task createdTasks = taskManager.addNewTask(task);
            String jsonTask = gsonBuilder.toJson(createdTasks);
            sendText(exchange, jsonTask, 201);
        } else {
            Task updateTask = taskManager.updateTask(task);
            String jsonTask = gsonBuilder.toJson(updateTask);
            sendText(exchange, jsonTask, 201);
        }
    }
}
