package HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class HttpTaskHandler extends BaseHttpHandler {

    TaskManager taskManager;
    Gson gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public HttpTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String metod = exchange.getRequestMethod();
        String[] paths = path.split("/");

        try {
            switch (metod) {
                case "GET":
                    getTasks(exchange, paths);
                case "POST":
                    postTasks(paths);
                case "DELETE":
                    deleteTasks(exchange,paths);
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void getTasks(HttpExchange exchange, String[] paths) throws IOException {
            if (paths.length == 2) {
                List<Task> allTasks = taskManager.getListTask();
                String jsonTasks = gsonBuilder.toJson(allTasks);
                sendText(exchange, jsonTasks);

            } else if (paths.length == 3) {
                int idTasks = Integer.parseInt(paths[2]);
                Task task = taskManager.findTask(idTasks);
                String jsonTask = gsonBuilder.toJson(task);
                sendText(exchange, jsonTask);
            }
        }

    private void deleteTasks(HttpExchange exchange, String[] paths) {
    }

    private void postTasks(String[] paths) {
    }























}
