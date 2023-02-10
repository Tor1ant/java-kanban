package server;

import com.sun.net.httpserver.HttpExchange;

import model.SubTask;
import service.TaskManager;

import java.util.ArrayList;

import java.util.Optional;

public class EpicSubTasksHandler extends TaskHandler {
    public EpicSubTasksHandler(TaskManager httpTaskManager) {
        super(httpTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        query = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (requestMethod.equals("GET")) {
            handleGetEpicSubTasks(exchange);
        }
        writeResponse(exchange, "Данный метод не поддерживается.", 405);
    }

    private void handleGetEpicSubTasks(HttpExchange exchange) {
        if (query.isEmpty()) {
            writeResponse(exchange, "Указаны не верные параметры.", 400);
        } else {
            String[] epicIdInString = query.get().split("=");
            int taskId = Integer.parseInt(epicIdInString[1]);
            ArrayList<SubTask> subTaskList = httpTaskManager.getListOfEpicsSubTasks(taskId);
            writeResponse(exchange, gson.toJson(subTaskList), 200);
        }
    }
}