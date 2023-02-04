package server;

import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import service.FileBackedTasksManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTaskHandler extends TaskHandler {
    public SubTaskHandler(FileBackedTasksManager fileBackedTasksManager) {
        super(fileBackedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        query = Optional.ofNullable(exchange.getRequestURI().getQuery());
        switch (requestMethod) {
            case "GET":
                handleGetSubTasks(exchange);
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

    private void handleGetSubTasks(HttpExchange exchange) {
        if (query.isEmpty()) {
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.getAllSubTasks()), 200);
        } else {
            int subtaskId = Integer.parseInt(query.get().substring(3));
            try {
                SubTask subtask = fileBackedTasksManager.getSubTaskById(subtaskId);
                writeResponse(exchange, gson.toJson(subtask), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, "подзадачи с таким id нет", 400);
            }
        }
    }

    private void handleDelete(HttpExchange exchange) {
        if (query.isEmpty()) {
            fileBackedTasksManager.removeAllSubTasks();
            writeResponse(exchange, "Все подзадачи удалены", 200);
        } else {
            try {
                int subTaskID = Integer.parseInt(query.get().substring(3));
                SubTask subTask = fileBackedTasksManager.getSubTaskById(subTaskID);
                fileBackedTasksManager.removeSubTaskById(subTaskID);
                writeResponse(exchange, String.format("Подзадача \"%s\" удалена.", subTask.getTitle()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, "Подзадачи с таким id нет", 400);
            }
        }
    }

    private void handlePost(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.
                UTF_8))) {
            StringBuilder subTaskInString = new StringBuilder();
            while (bufferedReader.ready()) {
                subTaskInString.append(bufferedReader.readLine());
            }
            SubTask subTaskForPost = gson.fromJson(subTaskInString.toString(), SubTask.class);
            for (SubTask subTask : fileBackedTasksManager.getAllSubTasks()) {
                if (subTask.getId() == subTaskForPost.getId()) {
                    fileBackedTasksManager.updateSubTask(subTaskForPost);
                    writeResponse(exchange, String.format("Подзадача с id \"%s\" обновлена.", subTaskForPost.getId()),
                            200);
                    return;
                }
            }
            fileBackedTasksManager.addSubTask(subTaskForPost);
            writeResponse(exchange, String.format("Подзадача \"%s\" добавлена.", subTaskForPost.getTitle()), 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
