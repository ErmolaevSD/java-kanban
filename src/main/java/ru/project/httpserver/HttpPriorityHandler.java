package ru.project.httpserver;

import com.sun.net.httpserver.HttpExchange;
import ru.project.exception.IntersectionTaskException;
import ru.project.exception.NotIntegerIdException;
import ru.project.exception.NotTaskException;
import ru.project.service.TaskManager;

import java.io.IOException;

public class HttpPriorityHandler extends BaseHttpHandler {

    public HttpPriorityHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    sortingPriorityTask(exchange);
                    break;
                default:
                    sendText(exchange, "Обработка метода %s не предусмотрена".formatted(method), 500);
                    break;
            }
        } catch (NotTaskException e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException | NotIntegerIdException e) {
            sendText(exchange, e.getMessage(), 406);
        }
    }

    private void sortingPriorityTask(HttpExchange exchange) throws IOException {
        String jsonPriorityTask = gsonBuilder.toJson(taskManager.getTaskPriotity());
        sendSuccess(exchange, jsonPriorityTask);
    }
}
