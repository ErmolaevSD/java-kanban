package httpserver;

import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionTaskException;
import exception.NotIntegerIdException;
import exception.NotTaskException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HttpHistoryHandler extends BaseHttpHandler {

    public HttpHistoryHandler(TaskManager taskManager) {
        BaseHttpHandler.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            if (method.equals("GET") && paths.length == 2) {
                List<Task> historyTask = historyManager.getHistory();
                String jsonTasks = gsonBuilder.toJson(historyTask);
                sendText(exchange, jsonTasks, 200);
            } else {
                String errorMessage = "Обработка данного метода " + method + " не предусмотренна";
                String jsonTask = gsonBuilder.toJson(errorMessage);
                sendText(exchange, jsonTask, 500);
            }
        } catch (NotTaskException e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException | NotIntegerIdException e) {
            sendText(exchange, e.getMessage(), 406);
        } finally {
            exchange.close();
        }
    }
}
