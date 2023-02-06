package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import server.KVTaskClient;

import java.util.HashMap;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;


    public HttpTaskManager(String URL) {
        super(URL);
        kvTaskClient = new KVTaskClient(URL + "/register");
        gson = new Gson();
    }

    @Override
    protected void save() {
        final String historyInString = historyToString((InMemoryHistoryManager) historyManager);
        kvTaskClient.put("tasks", gson.toJson(tasks.values()));
        kvTaskClient.put("epics", gson.toJson(epics.values()));
        kvTaskClient.put("subtasks", gson.toJson(subTasks.values()));
        kvTaskClient.put("history", historyInString);
    }

    public static HttpTaskManager loadFromServer(String url) {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        Gson gson = new Gson();
        KVTaskClient kvTaskClient = new KVTaskClient(url+"/register");
        String tasks = kvTaskClient.load("tasks");
        String epics = kvTaskClient.load("epics");
        String subTasks = kvTaskClient.load("subtasks");
        httpTaskManager.tasks = gson.fromJson(tasks, new TypeToken<HashMap<Integer, Task>>() {
        }.getType());
        httpTaskManager.epics = gson.fromJson(epics, new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());
        httpTaskManager.subTasks = gson.fromJson(subTasks, new TypeToken<HashMap<Integer, SubTask>>() {
        }.getType());
        return httpTaskManager;
    }
}
