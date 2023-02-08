package service;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import server.KVTaskClient;

import java.lang.reflect.Type;
import java.util.List;

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
        KVTaskClient kvTaskClient = new KVTaskClient(url + "/register");
        String tasks = kvTaskClient.load("tasks");
        String epics = kvTaskClient.load("epics");
        String subTasks = kvTaskClient.load("subtasks");
        String history = kvTaskClient.load("history");
        List<Integer> historyTasksId = FileBackedTasksManager.historyFromString(history);
        Type listTaskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksFromJson = gson.fromJson(tasks, listTaskType);
        for (Task task : tasksFromJson) {
            httpTaskManager.tasks.put(task.getId(), task);
            if (historyTasksId.contains(task.getId())) {
                httpTaskManager.historyManager.add(task);
            }
        }
        Type listEpicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epicsFromJson = gson.fromJson(epics, listEpicType);
        for (Epic epic : epicsFromJson) {
            httpTaskManager.epics.put(epic.getId(), epic);
            if (historyTasksId.contains(epic.getId())) {
                httpTaskManager.historyManager.add(epic);
            }
        }
        Type listSubTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> subTasksFromJson = gson.fromJson(subTasks, listSubTaskType);
        for (SubTask subTask : subTasksFromJson) {
            httpTaskManager.subTasks.put(subTask.getId(), subTask);
            if (historyTasksId.contains(subTask.getId())) {
                httpTaskManager.historyManager.add(subTask);
            }
        }
        return httpTaskManager;
    }
}