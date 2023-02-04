package server;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.FileBackedTasksManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends TaskHandler {
    public EpicHandler(FileBackedTasksManager fileBackedTasksManager) {
        super(fileBackedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        query = Optional.ofNullable(exchange.getRequestURI().getQuery());
        switch (requestMethod) {
            case "GET":
                handleGetEpic(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
            case "POST":
                handlePost(exchange);
            default:
                writeResponse(exchange, "Данный метод не поддерживается.", 405);
        }
    }

    private void handleGetEpic(HttpExchange exchange) {
        if (query.isEmpty()) {
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.getAllEpics()), 200);
        } else {
            int epicId = Integer.parseInt(query.get().substring(3));
            try {
                Epic epic = fileBackedTasksManager.getEpicByID(epicId);
                writeResponse(exchange, gson.toJson(epic), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, "Эпика с таким id нет", 400);
            }
        }
    }

    private void handleDelete(HttpExchange exchange) {
        if (query.isEmpty()) {
            fileBackedTasksManager.removeAllEpics();
            writeResponse(exchange, "Все Эпики удалены", 200);
        } else {
            try {
                int epicID = Integer.parseInt(query.get().substring(3));
                Epic epic = fileBackedTasksManager.getEpicByID(epicID);
                fileBackedTasksManager.removeEpicById(epicID);
                writeResponse(exchange, String.format("Эпик \"%s\" удалён.", epic.getTitle()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, "Эпика с таким id нет", 400);
            }
        }
    }

    private void handlePost(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.
                UTF_8))) {
            StringBuilder taskInString = new StringBuilder();
            while (bufferedReader.ready()) {
                taskInString.append(bufferedReader.readLine());
            }
            Epic epicForPost = gson.fromJson(taskInString.toString(), Epic.class);
            for (Epic epic : fileBackedTasksManager.getAllEpics()) {
                if (epic.getId() == epicForPost.getId()) {
                    fileBackedTasksManager.updateEpic(epicForPost);
                    writeResponse(exchange, String.format("Эпик с id \"%s\" обновлён.", epicForPost.getId()), 200);
                    return;
                }
            }
            fileBackedTasksManager.createEpic(epicForPost);
            writeResponse(exchange, String.format("Эпик \"%s\" добавлен.", epicForPost.getTitle()), 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
