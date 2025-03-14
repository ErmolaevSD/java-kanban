package HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static java.util.Objects.isNull;

public abstract class BaseHttpHandler implements HttpHandler {

    static TaskManager taskManager;

    static Gson gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
//            .serializeNulls()
            .create();




    protected void sendText(HttpExchange h, String text, Integer errorCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(errorCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    static class InstantTypeAdapter extends TypeAdapter<Instant> {

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

    static class DurationTypeAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            jsonWriter.value(duration.toString());
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

//    static class EpicTypeAdapter extends TypeAdapter<Epic> {
//
//        @Override
//        public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
//            jsonWriter.beginObject();
//            jsonWriter.name("name").value(epic.getName());
//            jsonWriter.name("description").value(epic.getDescription());
//            jsonWriter.name("status").value(epic.getStatus().toString());
//            jsonWriter.name("id").value(epic.getId());
//            jsonWriter.name("duration").value(epic.getDuration().toMinutes());
//            jsonWriter.name("startTime").value(epic.getStartTime().toString());
////            jsonWriter.name("endTime").value(epic.getEndTime().toString());
//            jsonWriter.endObject();
//        }
//
//        @Override
//        public Epic read(JsonReader jsonReader) throws IOException {
//            jsonReader.beginObject();
//            String name = null;
//            String description = null;
//            Status status = null;
//            Integer id = null;
//            Duration duration = null;
//            Instant startTime = null;
//
//            while (jsonReader.hasNext()) {
//                String fieldName = jsonReader.nextName();
//
//                switch (fieldName) {
//                    case "name":
//                        name = jsonReader.nextString();
//                        break;
//                    case "description":
//                        description = jsonReader.nextString();
//                        break;
//                    case "status":
//                        status = statusFromJson(jsonReader.nextString());
//                        break;
//                    case "id":
//                        id = getId(jsonReader);
//                        break;
//                    case "duration":
//                        duration = Duration.ofMinutes(jsonReader.nextInt());
//                        break;
//                    case "startTime":
//                        startTime = Instant.parse(jsonReader.nextString());
//                        break;
//                    default:
//                        jsonReader.skipValue();
//                }
//            }
//            jsonReader.endObject();
//            return new Epic(name, description, status, id, duration, startTime);
//        }
//
//        static class SubTaskTypeAdapter extends TypeAdapter<SubTask> {
//            @Override
//            public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
//                jsonWriter.beginObject();
//                jsonWriter.name("name").value(subTask.getName());
//                jsonWriter.name("description").value(subTask.getDescription());
//                jsonWriter.name("status").value(subTask.getStatus().toString());
//                jsonWriter.name("id").value(subTask.getId());
//                jsonWriter.name("duration").value(subTask.getDuration().toMinutes());
//                jsonWriter.name("startTime").value(subTask.getStartTime().toString());
//                jsonWriter.name("endTime").value(subTask.getEndTime().toString());
//                jsonWriter.name("parentEpic").value(subTask.getParentTask().getId().intValue());
//                jsonWriter.endObject();
//            }
//
//            @Override
//            public SubTask read(JsonReader jsonReader) throws IOException {
//                jsonReader.beginObject();
//                String name = null;
//                String description = null;
//                Status status = null;
//                Integer id = null;
//                Duration duration = null;
//                Instant startTime = null;
//                Integer parentEpic = null;
//
//                while (jsonReader.hasNext()) {
//                    String fieldName = jsonReader.nextName();
//
//                    switch (fieldName) {
//                        case "name":
//                            name = jsonReader.nextString();
//                            break;
//                        case "description":
//                            description = jsonReader.nextString();
//                            break;
//                        case "status":
//                            status = statusFromJson(jsonReader.nextString());
//                            break;
//                        case "id":
//                            id = getId(jsonReader);
//                            break;
//                        case "duration":
//                            duration = Duration.ofMinutes(jsonReader.nextInt());
//                            break;
//                        case "startTime":
//                            startTime = Instant.parse(jsonReader.nextString());
//                            break;
//                        case "parentEpic": parentEpic = jsonReader.nextInt();
//
//                            break;
//                        default:
//                            jsonReader.skipValue();
//                    }
//                }
//                jsonReader.endObject();
//
//                SubTask subTask = new SubTask(name, description, status, id, taskManager.findEpicTask(parentEpic) , duration, startTime);
//                return subTask;
//            }
//        }
//
//        private static Status statusFromJson(String status) {
//            Status statusFromJson = switch (status) {
//                case "NEW" -> Status.NEW;
//                case "DONE" -> Status.DONE;
//                case "IN_PROGRESS" -> Status.IN_PROGRESS;
//                default -> null;
//            };
//            return statusFromJson;
//        }
//
//        private static Integer getId(JsonReader jsonReader) throws IOException {
//            if (jsonReader.peek() == JsonToken.NULL) {
//                jsonReader.nextNull();
//                return null;
//            } else
//                return Integer.parseInt(String.valueOf(jsonReader.nextInt()));
//
//        }
//    }
}
