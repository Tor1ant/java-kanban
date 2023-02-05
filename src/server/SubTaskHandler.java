package server;

import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import service.FileBackedTasksManager;

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
                handleDeleteSubTask(exchange);
                break;
            case "POST":
            handlePostSubTask(exchange);
            default:
                writeResponse(exchange, "Данный метод не поддерживается.", 405);
        }
    }

    private void handleGetSubTasks(HttpExchange exchange) {
        handleGet(exchange, fileBackedTasksManager::getAllSubTasks, fileBackedTasksManager::getSubTaskById);
    }

    private void handleDeleteSubTask(HttpExchange exchange) {
        handleDelete(exchange, fileBackedTasksManager::removeAllSubTasks, fileBackedTasksManager::getSubTaskById,
                fileBackedTasksManager::removeSubTaskById);
    }

    private void handlePostSubTask(HttpExchange exchange) {
        handlePost(exchange, fileBackedTasksManager::getAllSubTasks, fileBackedTasksManager::updateSubTask,
                fileBackedTasksManager::addSubTask, SubTask.class);
    }
}
