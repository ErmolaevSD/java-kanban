package ru.project.httpHandler;

import com.sun.net.httpserver.HttpExchange;
import ru.project.exception.IntersectionTaskException;
import ru.project.exception.NotIntegerIdException;
import ru.project.exception.NotTaskException;
import ru.project.service.TaskManager;
import ru.project.model.Task;

import java.io.IOException;
import java.util.List;

public class HttpHistoryHandler extends BaseHttpHandler {

    public HttpHistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        logger.info("На сервер поступил HTTP - запрос: method=[{}], path=[{}]", exchange.getRequestMethod(), exchange.getRequestURI());


        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    getListHistoryTask(exchange);
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

    private void getListHistoryTask(HttpExchange exchange) throws IOException {
        List<Task> taskListHistory = historyManager.getHistory();
        String jsonElements = gsonBuilder.toJson(taskListHistory);
        sendSuccess(exchange, jsonElements);
    }
}
