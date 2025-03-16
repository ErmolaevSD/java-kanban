package httpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(8080, "localhost", taskManager);
    Gson gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new BaseHttpHandler.InstantTypeAdapter())
            .registerTypeAdapter(Duration.class, new BaseHttpHandler.DurationTypeAdapter())
            .serializeNulls()
            .create();

    HttpClient httpClient = HttpClient.newHttpClient();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteAllTask();
        taskManager.deleteAllEpicTask();
        taskManager.deleteAllSubTask();
        taskServer.startTaskServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopTaskServer(1);
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task1 = new Task("Test Task1", "Test Task1", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        String jsonTask1 = gsonBuilder.toJson(task1);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonTask1)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> taskFromManager = taskManager.getListTask();

        assertNotNull(taskFromManager, "Задачи не возвращаются");
        assertEquals(1, taskFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Task1", taskFromManager.getFirst().getName(), "Некорректное имя задачи");

        Task updateTask1 = new Task("Test Task1", "Update", Status.NEW, 0, Duration.ofMinutes(1), Instant.now().plusSeconds(120));
        String jsonTask2 = gsonBuilder.toJson(updateTask1);
        request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonTask2)).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        Task task2 = new Task("Test Task2", "Task2", Status.NEW, null, Duration.ofMinutes(5), Instant.now().plusSeconds(10));
        String jsonTask3 = gsonBuilder.toJson(task2);
        request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonTask3)).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

    }

    @Test
    public void testFindTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Test Task1", "Test Task1", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewTask(task1);
        List<Task> tasksList = taskManager.getListTask();
        String jsonTask1 = gsonBuilder.toJson(tasksList);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String tasksGet = response.body();

        assertEquals(jsonTask1, tasksGet);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testFindTaskByID() throws IOException, InterruptedException {
        Task task1 = new Task("Test Task1", "Test Task1", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewTask(task1);

        URI uriGetId = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String actuallyTask = gsonBuilder.toJson(taskManager.getListTask().getFirst());
        String taskGetId = response.body();

        assertEquals(actuallyTask, taskGetId);

        URI incorrectURI = URI.create("http://localhost:8080/tasks/dfgdf");
        request = HttpRequest.newBuilder().uri(incorrectURI).GET().build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task1 = new Task("Test Task1", "Test Task1", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewTask(task1);

        URI uriGetId = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        boolean taskIsEmpty = taskManager.getListTask().isEmpty();

        assertTrue(taskIsEmpty);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testFindEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        List<Epic> epicList = taskManager.getListEpicTask();
        String jsonEpic = gsonBuilder.toJson(epicList);

        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String tasksGet = response.body();

        assertEquals(jsonEpic, tasksGet);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testFindEpicByID() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);

        URI uriGetId = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String actuallyEpic = gsonBuilder.toJson(taskManager.getListEpicTask().getFirst());
        String taskGetId = response.body();

        assertEquals(actuallyEpic, taskGetId);

        URI incorrectURI = URI.create("http://localhost:8080/epics/sdfsd");
        request = HttpRequest.newBuilder().uri(incorrectURI).GET().build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        SubTask subTasks = new SubTask("Test SubTask1", "Test SubTask1",Status.NEW,null, epic.getId(), Duration.ofMinutes(1),Instant.now());
        taskManager.addNewSubTask(subTasks);

        boolean EpicsIdNotEmpty = taskManager.getListEpicTask().isEmpty();
        boolean SubTaskNotEmpty = taskManager.getListSubTask().isEmpty();

        assertFalse(EpicsIdNotEmpty);
        assertFalse(SubTaskNotEmpty);

        URI uriGetId = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        boolean taskIsEmpty = taskManager.getListEpicTask().isEmpty();
        boolean subtasksIsEmpty = taskManager.getListSubTask().isEmpty();

        assertTrue(taskIsEmpty);
        assertTrue(subtasksIsEmpty);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testFindSubTasksByEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(new SubTask("Test Subtask1", "Test SubTask1", Status.NEW, null, 0, Duration.ofMinutes(1), Instant.now()));
        taskManager.addNewSubTask(new SubTask("Test Subtask2", "Test SubTask2", Status.DONE, null, 0, Duration.ofMinutes(1), Instant.now().plusSeconds(120)));
        List<Integer> epicSubTask = epic.getSubTasksID();
        List<Integer> integers = List.of(1, 2);

        assertEquals(epicSubTask, integers);

        URI uriGetId = URI.create("http://localhost:8080/epics/0/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String actuallyEpic = gsonBuilder.toJson(taskManager.getSubTasks(epic.getSubTasksID()));
        String subTasksByEpic = response.body();

        assertEquals(actuallyEpic, subTasksByEpic);

        URI uriErrorGetId = URI.create("http://localhost:8080/epics/55/subtasks");
        request = HttpRequest.newBuilder().uri(uriErrorGetId).GET().build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        String jsonEpic = gsonBuilder.toJson(epic);
        URI uri = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonEpic)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = taskManager.getListEpicTask();
        assertNotNull(epicsFromManager, "Задачи не возвращаются");

        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Epic", epicsFromManager.getFirst().getName(), "Некорректное имя задачи");

        Epic updateEpic = new Epic("Test Epic", "Update", Status.NEW, 0, Duration.ofMinutes(1), Instant.now().plusSeconds(120));
        String jsonEpicUpdate = gsonBuilder.toJson(updateEpic);
        request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonEpicUpdate)).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String updateDescription = taskManager.findEpicTask(0).getDescription();

        assertEquals("Update", updateDescription);
    }

    @Test
    public void testFindSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        SubTask subTasks = new SubTask("Test SubTask1", "Test SubTask1",Status.NEW,null, epic.getId(), Duration.ofMinutes(1),Instant.now());
        taskManager.addNewSubTask(subTasks);
        List<SubTask> subTaskLists = taskManager.getListSubTask();
        String jsonSubTasks = gsonBuilder.toJson(subTaskLists);
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String subTasksGet = response.body();

        assertEquals(jsonSubTasks, subTasksGet);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testFindSubTaskByID() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        SubTask subTasks = new SubTask("Test SubTask1", "Test SubTask1",Status.NEW,null, epic.getId(), Duration.ofMinutes(1),Instant.now());
        taskManager.addNewSubTask(subTasks);


        URI uriGetId = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String actuallyEpic = gsonBuilder.toJson(taskManager.getListSubTask().getFirst());
        String taskGetId = response.body();

        assertEquals(actuallyEpic, taskGetId);

        URI incorrectURI = URI.create("http://localhost:8080/subtasks/10");
        request = HttpRequest.newBuilder().uri(incorrectURI).GET().build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        SubTask subTasks = new SubTask("Test SubTask1", "Test SubTask1",Status.NEW,null, epic.getId(), Duration.ofMinutes(1),Instant.now());
        taskManager.addNewSubTask(subTasks);
        int subtaskByEpic = taskManager.getSubTasks(taskManager.getListEpicTask().getFirst().getSubTasksID()).size();

        assertEquals(1, subtaskByEpic);

        URI uriGetId = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        boolean subtasksIsEmpty = taskManager.getListSubTask().isEmpty();
        int subtaskByEpicNew = taskManager.getSubTasks(taskManager.getListEpicTask().getFirst().getSubTasksID()).size();

        List<Integer> subtasks = epic.getSubTasksID();

        assertEquals("[]", subtasks.toString());
        assertTrue(subtasksIsEmpty);
        assertEquals(0, subtaskByEpicNew);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewEpic(epic);
        List<Integer> notSubTasks = new ArrayList<>();

        assertEquals(notSubTasks, epic.getSubTasksID());

        SubTask subTask = new SubTask("Test SubTask1", "Test SubTask1",Status.NEW,null, epic.getId(), Duration.ofMinutes(1),Instant.now());
        String jsonSubTask = gsonBuilder.toJson(subTask);
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonSubTask)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        notSubTasks.add(1);
        assertEquals(1, taskManager.getListSubTask().size());
        assertEquals(201, response.statusCode());
        assertEquals(notSubTasks, epic.getSubTasksID());

        SubTask updateSubTask = new SubTask("Test SubTask1", "Update",Status.NEW,1, epic.getId(), Duration.ofMinutes(1),Instant.now().plusSeconds(500));
        String jsonEpicUpdate = gsonBuilder.toJson(updateSubTask);
        request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonEpicUpdate)).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        String updateDescription = taskManager.findSubTask(1).getDescription();

        assertEquals("Update", updateDescription);

        SubTask subTaskInsertion = new SubTask("Test SubTask1", "Insertion",Status.NEW,null, epic.getId(), Duration.ofMinutes(1),Instant.now().plusSeconds(490));
        String jsonTask3 = gsonBuilder.toJson(subTaskInsertion);
        request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(jsonTask3)).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Test Task1", "Test Task1", Status.NEW, null, Duration.ofMinutes(1), Instant.now());
        taskManager.addNewTask(task1);
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now().plusSeconds(120));
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("Test SubTask1", "Insertion", Status.NEW, null, epic.getId(), Duration.ofMinutes(1), Instant.now().plusSeconds(490));
        taskManager.addNewSubTask(subTask);

        assertEquals(0, taskManager.getHistoryManager().getHistory().size());

        taskManager.findTask(task1.getId());
        taskManager.findEpicTask(epic.getId());
        taskManager.findSubTask(subTask.getId());
        taskManager.findTask(task1.getId());

        assertEquals(3, taskManager.getHistoryManager().getHistory().size());

        URI uriGetId = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String historyByManager = gsonBuilder.toJson(taskManager.getHistoryManager().getHistory());
        String historyByResponse = response.body();

        assertEquals(historyByManager, historyByResponse);
    }

    @Test
    public void testPriority() throws IOException, InterruptedException {
        Task task1 = new Task("Test Task1", "Вторая", Status.NEW, null, Duration.ofMinutes(1), Instant.now().plusSeconds(500));
        taskManager.addNewTask(task1);
        Task task2 = new Task("Test Task2", "Первая", Status.NEW, null, Duration.ofMinutes(1), Instant.now().plusSeconds(120));
        taskManager.addNewTask(task2);
        Epic epic = new Epic("Test Epic", "Test Epic", Status.NEW, null, Duration.ofMinutes(1), Instant.now().plusSeconds(100000000));
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("Test SubTask1", "Третья", Status.NEW, null, epic.getId(), Duration.ofMinutes(1), Instant.now().plusSeconds(1000));
        taskManager.addNewSubTask(subTask);
        URI uriGetId = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uriGetId).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String historyByManager = gsonBuilder.toJson(taskManager.getTaskPriotity());
        String historyByResponse = response.body();

        assertEquals(historyByManager, historyByResponse);
    }
}
