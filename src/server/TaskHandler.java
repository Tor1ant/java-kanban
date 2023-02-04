package server;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.FileBackedTasksManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

class TaskHandler implements HttpHandler {
    protected FileBackedTasksManager fileBackedTasksManager;
    protected Gson gson;
    protected Optional<String> query;

    public TaskHandler(FileBackedTasksManager fileBackedTasksManager) {
        this.fileBackedTasksManager = fileBackedTasksManager;
        gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        query = Optional.ofNullable(exchange.getRequestURI().getQuery());
        switch (requestMethod) {
            case "GET":
                handleGetTasks(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            case "POST":
                handlePost(exchange);
            default:
                writeResponse(exchange, "Данный метод не поддерживается.", 405);
        }
    }

    private void handleGetTasks(HttpExchange exchange) {
        if (query.isEmpty()) {
            ArrayList<Task> allTasks = fileBackedTasksManager.getAllTasks();
            String allTasksInJson = gson.toJson(allTasks);
            writeResponse(exchange, allTasksInJson, 200);
        } else {
            String[] taskIdInString = query.get().split("=");
            int taskId = Integer.parseInt(taskIdInString[1]);
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.getTaskByID(taskId)), 200);
        }
    }

    private void handleDelete(HttpExchange exchange) {
        if (query.isEmpty()) {
            fileBackedTasksManager.removeAllTasks();
            writeResponse(exchange, "Все задачи удалены.", 200);
        } else {
            String[] taskIdInString = query.get().split("=");
            int taskId = Integer.parseInt(taskIdInString[1]);
            Task task = fileBackedTasksManager.getTaskByID(taskId);
            fileBackedTasksManager.removeTaskById(taskId);
            writeResponse(exchange, "задача " + task.getTitle() + " удалена", 200);
        }
    }

    private void handlePost(HttpExchange exchange){
        InputStream inputStream = exchange.getRequestBody();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder taskInString = new StringBuilder();
            while (bufferedReader.ready()) {
                taskInString.append(bufferedReader.readLine());
            }
            Task taskForPost = gson.fromJson(taskInString.toString(), Task.class);
            for (Task task : fileBackedTasksManager.getAllTasks()) {
                if (task.getId() == taskForPost.getId()) {
                    fileBackedTasksManager.updateTask(taskForPost);
                    writeResponse(exchange, "задача " + taskForPost.getTitle() + " добавлена", 200);
                    return;
                }
            }
            fileBackedTasksManager.createTask(taskForPost);
            writeResponse(exchange, "задача " + taskForPost.getTitle() + " добавлена", 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeResponse(HttpExchange exchange, String response, int code) {
        try {
            if (response.isBlank()) {
                exchange.sendResponseHeaders(code, 0);
            } else {
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(code, 0);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(bytes);
            }
            exchange.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
