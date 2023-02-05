package server;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.FileBackedTasksManager;

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
                handleDeleteEpic(exchange);
            case "POST":
                handlePostEpic(exchange);
            default:
                writeResponse(exchange, "Данный метод не поддерживается.", 405);
        }
    }

    private void handleGetEpic(HttpExchange exchange) {
        handleGet(exchange, fileBackedTasksManager::getAllEpics, fileBackedTasksManager::getEpicByID);
    }

    private void handleDeleteEpic(HttpExchange exchange) {
        handleDelete(exchange, fileBackedTasksManager::removeAllEpics, fileBackedTasksManager::getEpicByID,
                fileBackedTasksManager::removeEpicById);
    }

    private void handlePostEpic(HttpExchange exchange) {
        handlePost(exchange,fileBackedTasksManager::getAllEpics,fileBackedTasksManager::updateEpic,
                fileBackedTasksManager::createEpic, Epic.class);
    }
}
