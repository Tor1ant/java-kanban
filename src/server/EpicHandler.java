package server;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.HttpTaskManager;

import java.util.Optional;

public class EpicHandler extends TaskHandler {
    public EpicHandler(HttpTaskManager httpTaskManagerer) {
        super(httpTaskManagerer);
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
        handleGet(exchange, httpTaskManager::getAllEpics, httpTaskManager::getEpicByID);
    }

    private void handleDeleteEpic(HttpExchange exchange) {
        handleDelete(exchange, httpTaskManager::removeAllEpics, httpTaskManager::getEpicByID,
                httpTaskManager::removeEpicById);
    }

    private void handlePostEpic(HttpExchange exchange) {
        handlePost(exchange, httpTaskManager::getAllEpics, httpTaskManager::updateEpic,
                httpTaskManager::createEpic, Epic.class);
    }
}