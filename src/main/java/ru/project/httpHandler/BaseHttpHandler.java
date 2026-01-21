package ru.project.httpHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.project.service.HistoryManager;
import ru.project.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import static java.util.Objects.isNull;

public abstract class BaseHttpHandler implements HttpHandler {

    protected static final Logger logger = LoggerFactory.getLogger(BaseHttpHandler.class);
    protected final TaskManager taskManager;
    protected final HistoryManager historyManager;

    protected final Gson gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .serializeNulls()
            .create();

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.historyManager = this.taskManager.getHistoryManager();
    }

    protected void sendText(HttpExchange exchange, String data, Integer errorCode) throws IOException {
        byte[] resp;
        if (data == null) {
            resp = new byte[0];
        } else {
            resp = data.getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");

        try {
            if (errorCode == 204) {
                exchange.sendResponseHeaders(errorCode, -1);
            } else if (resp.length == 0) {
                exchange.sendResponseHeaders(errorCode, 0);
            } else {
                exchange.sendResponseHeaders(errorCode, resp.length);
                try (var os = exchange.getResponseBody()) {
                    os.write(resp);
                    os.flush();
                }
            }
        } finally {
            try {
                exchange.close();
            } catch (Exception e) {
                System.err.println("[sendText] Error closing exchange: " + e.getMessage());
            }
        }
    }

    protected void sendSuccess(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 200);
        logger.info("HTTP - запрос: method=[{}], path=[{}] успешно обработан", exchange.getRequestMethod(), exchange.getRequestURI());

    }

    protected void sendError(HttpExchange exchange, String errorMessage, int statusCode) throws IOException {
        String errorJson = String.format("{\"error\": \"%s\", \"statusCode\": %d}",
                errorMessage.replace("\"", "\\\""), statusCode);
        sendText(exchange, errorJson, statusCode);
    }

    public static class InstantTypeAdapter extends TypeAdapter<Instant> {

        @Override
        public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
            if (isNull(instant)) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(instant.toString());
            }
        }

        @Override
        public Instant read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return Instant.parse(jsonReader.nextString());
        }
    }

    public static class DurationTypeAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            if (isNull(duration)) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(duration.toString());
            }
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return Duration.parse(jsonReader.nextString());
        }
    }
}