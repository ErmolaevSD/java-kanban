package httpserver;

import com.sun.net.httpserver.HttpExchange;
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

        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());

        try {
            switch (method) {
                case GET:
                    TreeSet<Task> historyTask = taskManager.getTaskPriotity();
                    String jsonTasks = gsonBuilder.toJson(historyTask);
                    sendText(exchange, jsonTasks, 200);
                default:
                String errorMessage = "Обработка данного метода " + method + " не предусмотренна";
                String jsonTask = gsonBuilder.toJson(errorMessage);
                sendText(exchange, jsonTask, 500);
                break;
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
