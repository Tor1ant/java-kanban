package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.Managers;
import service.Status;
import service.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServerTest {
    Gson gson;
    TaskManager httpTaskManager;
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client;

    @BeforeEach
    public void startServer() throws IOException {
        gson = new Gson();
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer(httpTaskManager = Managers.getDefault());
        httpTaskServer.startServer();
        client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(3000)).build();

        Task task1 = new Task("Погулять с собакой", "в 5 утра", Status.NEW, 60,
                LocalDateTime.now());
        Task task2 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику",
                Status.NEW, 60, LocalDateTime.now().minusMinutes(300));
        Epic epicWithoutSubTasks = new Epic("Сделать ремонт в квартире", "Давно пора");
        Epic epicWithSubTasks = new Epic("Сходить в магазин", "Купить продукты");
        SubTask subtask1 = new SubTask("Купить молоко", "Зайти за молоком в пятерочку", Status.NEW,
                4, 20, LocalDateTime.now().plusMinutes(500));
        SubTask subtask2 = new SubTask("Купить сахар", "это не обязательно", Status.NEW, 4,
                60, LocalDateTime.now().plusMinutes(350));
        SubTask subTask3 = new SubTask("Купить вазелин", "Допустим", Status.NEW, 4,
                5, LocalDateTime.now().plusMinutes(450));
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task2);
        httpTaskManager.createEpic(epicWithoutSubTasks);
        httpTaskManager.createEpic(epicWithSubTasks);
        httpTaskManager.addSubTask(subtask1);
        httpTaskManager.addSubTask(subtask2);
        httpTaskManager.addSubTask(subTask3);
        httpTaskManager.getTaskByID(task1.getId());
        httpTaskManager.getTaskByID(task1.getId());
        httpTaskManager.getEpicByID(epicWithoutSubTasks.getId());
        httpTaskManager.getTaskByID(task1.getId());
        httpTaskManager.getSubTaskById(subtask1.getId());
        httpTaskManager.getEpicByID(epicWithSubTasks.getId());
        httpTaskManager.getTaskByID(task2.getId());
        httpTaskManager.getSubTaskById(subTask3.getId());
        httpTaskManager.getSubTaskById(subtask2.getId());
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
        httpTaskServer.stopServer();

    }

    @Test
    public void shouldTrueWhenGetAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listTaskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksFromJson = gson.fromJson(response.body(), listTaskType);

        Assertions.assertEquals(httpTaskManager.getAllTasks().size(), tasksFromJson.size());
    }

    @Test
    public void shouldTrueWhenGetTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);
        Assertions.assertEquals("Погулять с собакой", task.getTitle(), "Задачи не совпадают");
    }

    @Test
    public void shouldTrueWHenPostTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        Task newTask = new Task("Задача для проверки", "Testing", Status.NEW, 60, LocalDateTime.
                now().plusMinutes(700));
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).timeout(Duration.ofMillis(3000)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=8");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response2.body(), Task.class);
        Assertions.assertEquals("Задача для проверки", task.getTitle(), "Задачи не совпадают");
    }

    @Test
    public void ShouldSendCode400WhenDeleteTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        Task newTask = new Task("Задача для проверки", "Testing", Status.NEW, 60, LocalDateTime.
                now().plusMinutes(700));
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=8");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8080/tasks/task/?id=8");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        int code = response3.statusCode();
        Assertions.assertEquals(400, code, "Задача не удалена");
    }

    @Test
    public void shouldTrueWhenGetAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listTaskType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epicsFromJson = gson.fromJson(response.body(), listTaskType);
        Assertions.assertEquals(httpTaskManager.getAllEpics().size(), epicsFromJson.size());

    }

    @Test
    public void shouldTrueWhenGetEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);
        Assertions.assertEquals("Сделать ремонт в квартире", epic.getTitle(), "Задачи не совпадают");
    }

    @Test
    public void shouldTrueWHenPostEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        Epic newEpic = new Epic("Задача для проверки", "Testing");
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).timeout(Duration.ofMillis(3000)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=8");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response2.body(), Epic.class);
        Assertions.assertEquals("Задача для проверки", epic.getTitle(), "Задачи не совпадают");
    }

    @Test
    public void ShouldSendCode400WhenDeleteEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        Epic epic = new Epic("Задача для проверки", "Testing");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=8");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8080/tasks/epic/?id=8");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        int code = response3.statusCode();
        Assertions.assertEquals(400, code, "Задача не удалена");
    }

    @Test
    public void shouldTrueWhenGetAllSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listTaskType = new TypeToken<List<Epic>>() {
        }.getType();
        List<SubTask> subTasksFromJson = gson.fromJson(response.body(), listTaskType);
        Assertions.assertEquals(httpTaskManager.getAllSubTasks().size(), subTasksFromJson.size());
    }

    @Test
    public void shouldTrueWhenGetSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        Assertions.assertEquals("Купить молоко", subTask.getTitle(), "Задачи не совпадают");
    }

    @Test
    public void shouldTrueWHenPostSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        SubTask subTask = new SubTask("Задача для проверки", "Testing", Status.IN_PROGRESS, 3,
                10, LocalDateTime.now().plusMinutes(400));
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).timeout(Duration.ofMillis(3000)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?id=8");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = gson.fromJson(response2.body(), SubTask.class);
        Assertions.assertEquals("Задача для проверки", subTask1.getTitle(), "Задачи не совпадают");
    }

    @Test
    public void ShouldSendCode400WhenDeleteSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        SubTask subTask = new SubTask("Задача для проверки", "Testing", Status.IN_PROGRESS, 3,
                10, LocalDateTime.now().plusMinutes(400));
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).timeout(Duration.ofMillis(3000)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?id=8");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8080/tasks/subtask/?id=8");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        int code = response3.statusCode();
        Assertions.assertEquals(400, code, "Задача не удалена");
    }

    @Test
    public void shouldTrueWhenGetEpicSubTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> tasksFromJson = gson.fromJson(response.body(), listTaskType);
        Assertions.assertEquals(httpTaskManager.getListOfEpicsSubTasks(4).size(), tasksFromJson.size());
    }

    @Test
    public void shouldTrueWhenGetHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<Task> tasksFromJson = gson.fromJson(response.body(), listTaskType);
        Assertions.assertEquals(httpTaskManager.getHistory().size(), tasksFromJson.size());
    }

    @Test
    public void shouldTrueWhenGetPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
       Type listTaskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> tasksFromJson = gson.fromJson(response.body(), listTaskType);
        Assertions.assertEquals(httpTaskManager.getPrioritizedTasks().size(), tasksFromJson.size());
    }
}