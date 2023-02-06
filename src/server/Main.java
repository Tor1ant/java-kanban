package server;

import com.google.gson.Gson;
import model.Task;
import service.Status;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        new HttpTaskServer().startServer();
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/register");
        Task task = new Task("Новая таска для сохранения", "описание таски", Status.IN_PROGRESS,
                60, LocalDateTime.now());
        Task task2 = new Task("Еще одна таска для проверки", "описание таски", Status.NEW,
                60, LocalDateTime.now());
        Task task3 = new Task("Последняя таска для проверки", "описание таски", Status.NEW,
                60, LocalDateTime.now());
        String jsonObject = gson.toJson(task);
        String jsonObject2 = gson.toJson(task2);
        String jsonObject3 = gson.toJson(task3);
        kvTaskClient.put("265", jsonObject);
        kvTaskClient.put("1998", jsonObject2);
        kvTaskClient.put("2023", jsonObject3);
        System.out.println(kvTaskClient.load("265"));
        System.out.println(kvTaskClient.load("1998"));
        System.out.println(kvTaskClient.load("2023"));
        Task task4 = new Task("НОВАЯ ЗАДАЧА", "описание таски", Status.NEW,
                60, LocalDateTime.now());
        String jsonObject4 = gson.toJson(task4);
        kvTaskClient.put("265", jsonObject4);
        System.out.println(kvTaskClient.load("265"));
    }
}
