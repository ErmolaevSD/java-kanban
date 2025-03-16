package httpServer;

import com.sun.net.httpserver.HttpExchange;
import exception.ErrorResponse;
import exception.IntersectionTaskException;
import exception.NotIntegerIdException;
import exception.NotTaskException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

public class HttpPrioritiHandler extends BaseHttpHandler {

    public HttpPrioritiHandler(TaskManager taskManager) {
        BaseHttpHandler.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] paths = exchange.getRequestURI().getPath().split("/");

        try {
            if (method.equals("GET") && paths.length == 2) {
                TreeSet<Task> historyTask = taskManager.getTaskPriotity();
                String jsonTasks = gsonBuilder.toJson(historyTask);
                sendText(exchange, jsonTasks, 200);
            } else {
                ErrorResponse errorMessage = new ErrorResponse("Обработка данного метода не предусмотренна", 500, exchange.getRequestURI().getPath());
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
