package httpserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionTaskException;
import exception.NotIntegerIdException;
import exception.NotTaskException;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Objects.isNull;

public class HttpEpicHandler extends BaseHttpHandler {

    public HttpEpicHandler(TaskManager taskManager) {
        BaseHttpHandler.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            switch (method) {
                case GET:
                    getEpic(exchange, paths);
                    break;
                case POST:
                    postEpic(exchange);
                    break;
                case DELETE:
                    deleteEpic(exchange, paths);
                    break;
                default:
                    String errorMessage = "Обработка данного метода " + method + " не предусмотренна";
                    String jsonTask = gsonBuilder.toJson(errorMessage);
                    sendText(exchange, jsonTask, 500);
            }
        } catch (NotTaskException | NotIntegerIdException  e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException e) {
            sendText(exchange, e.getMessage(), 406);
        } finally {
            exchange.close();
        }
    }

    private void getEpic(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 2) {
            List<Epic> allEpic = taskManager.getListEpicTask();
            String jsonTasks = gsonBuilder.toJson(allEpic);
            sendText(exchange, jsonTasks, 200);

        } else if (paths.length == 3) {
            int idEpic;
            try {
                idEpic = Integer.parseInt(paths[2]);
            } catch (NumberFormatException e) {
                throw new NotIntegerIdException("В качестве id введено некорректное значение.");
            }
            Epic epic = taskManager.findEpicTask(idEpic);
            String jsonTask = gsonBuilder.toJson(epic);
            sendText(exchange, jsonTask, 200);
        } else if (paths.length == 4 && paths[3].equals("subtasks")) {
            try {
                int idEpic = Integer.parseInt(paths[2]);
                List<SubTask> subTasksByEpic = taskManager.getSubTasks(taskManager.findEpicTask(idEpic).getSubTasksID());
                String jsonSubTasksByEpic = gsonBuilder.toJson(subTasksByEpic);
                sendText(exchange, jsonSubTasksByEpic, 200);
            } catch (NotTaskException e) {
                throw new NotTaskException(e.getMessage());
            }
        }
    }



    private void deleteEpic(HttpExchange exchange, String[] paths) throws IOException {
            int idEpic = Integer.parseInt(paths[2]);
            Epic deteteTask = taskManager.deleteEpicTask(taskManager.findEpicTask(idEpic));
            String jsonTask = gsonBuilder.toJson(deteteTask);
            sendText(exchange, jsonTask, 200);
    }

    private void postEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String bode = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(bode);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic epic = gsonBuilder.fromJson(jsonObject, Epic.class);

        if (isNull(epic.getId())) {
            Epic createdTasks = taskManager.addNewEpic(epic);
            String jsonTask = gsonBuilder.toJson(createdTasks);
            sendText(exchange, jsonTask, 201);
        } else {
            Epic updateTask = taskManager.updateEpic(epic);
            String jsonTask = gsonBuilder.toJson(updateTask);
            sendText(exchange, jsonTask, 201);
        }
    }
}
