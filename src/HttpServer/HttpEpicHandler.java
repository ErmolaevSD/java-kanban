package HttpServer;

import com.sun.net.httpserver.HttpExchange;
import exception.ErrorResponse;
import exception.IntersectionTaskException;
import exception.NotTaskException;
import managers.TaskManager;

import java.io.IOException;

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
                case "POST":
                    postEpic(exchange);
                case "DELETE":
                    deleteEpic(exchange, paths);
                default:
                    ErrorResponse errorMessage = new ErrorResponse("Обработка данного метода не предусмотренна", 500, exchange.getRequestURI().getPath());
                    String jsonTask = gsonBuilder.toJson(errorMessage);
                    sendText(exchange, jsonTask, 500);
            }
        } catch (NotTaskException e) {
            sendText(exchange, e.getMessage(), 404);
        } catch (IntersectionTaskException e) {
            sendText(exchange, e.getMessage(), 406);

        } finally {
            exchange.close();
        }

    }

    private void deleteEpic(HttpExchange exchange, String[] paths) {
    }

    private void postEpic(HttpExchange exchange) {

    }

    private void getEpic(HttpExchange exchange, String[] paths) {

    }
}
