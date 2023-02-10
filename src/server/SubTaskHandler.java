package server;

import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import service.TaskManager;

import java.util.Optional;

public class SubTaskHandler extends TaskHandler {
    public SubTaskHandler(TaskManager httpTaskManager) {
        super(httpTaskManager);
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
                handleDeleteSubTask(exchange);
                break;
            case "POST":
                handlePostSubTask(exchange);
            default:
                writeResponse(exchange, "Данный метод не поддерживается.", 405);
        }
    }

    private void handleGetSubTasks(HttpExchange exchange) {
        handleGet(exchange, httpTaskManager::getAllSubTasks, httpTaskManager::getSubTaskById);
    }

    private void handleDeleteSubTask(HttpExchange exchange) {
        handleDelete(exchange, httpTaskManager::removeAllSubTasks, httpTaskManager::getSubTaskById,
                httpTaskManager::removeSubTaskById);
    }

    private void handlePostSubTask(HttpExchange exchange) {
        handlePost(exchange, httpTaskManager::getAllSubTasks, httpTaskManager::updateSubTask,
                httpTaskManager::addSubTask, SubTask.class);
    }
}