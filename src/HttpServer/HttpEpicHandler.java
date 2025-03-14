package HttpServer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import exception.ErrorResponse;
import exception.IntersectionTaskException;
import exception.NotIntegerIdException;
import exception.NotTaskException;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

public class HttpEpicHandler extends BaseHttpHandler {


    public HttpEpicHandler(TaskManager taskManager) {
        super.taskManager = taskManager;
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
                    ErrorResponse errorMessage = new ErrorResponse("Обработка данного метода не предусмотренна", 500, exchange.getRequestURI().getPath());
                    String jsonTask = gsonBuilder.toJson(errorMessage);
                    sendText(exchange, jsonTask, 500);
            }
        } catch (NotTaskException e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException | NotIntegerIdException e) {
            sendText(exchange, e.getMessage(), 406);
        } catch (Exception e) {
            sendText(exchange, e.getMessage(), 500);
        }
//        finally {
//            exchange.close();
//        }
    }

    private void getEpic(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 2) {
            try {
                List<Epic> allEpic = taskManager.getListEpicTask();
                String jsonTasks = gsonBuilder.toJson(allEpic);
                sendText(exchange, jsonTasks, 200);
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

        } else if (paths.length == 3) {
            int idEpic = 0;
            try {
                idEpic = Integer.parseInt(paths[2]);
            } catch (NumberFormatException e) {
                throw new NotIntegerIdException("В качестве id введено некорректное значение.");
            }
                Epic epic = taskManager.findEpicTask(idEpic);
                String jsonTask = gsonBuilder.toJson(epic);
                sendText(exchange, jsonTask, 200);
        } else if (paths.length == 4 && paths[3].equals("subtasks")) {
            int idEpic = Integer.parseInt(paths[2]);
            List<SubTask> subTasksByEpic = taskManager.getSubTasks(taskManager.findEpicTask(idEpic));
            String jsonSubTasksByEpic = gsonBuilder.toJson(subTasksByEpic);
            sendText(exchange, jsonSubTasksByEpic, 200);
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
