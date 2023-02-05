package server;

import com.sun.net.httpserver.HttpExchange;

import model.SubTask;
import service.FileBackedTasksManager;

import java.util.ArrayList;

import java.util.Optional;

public class EpicSubTasksHandler extends TaskHandler {
    public EpicSubTasksHandler(FileBackedTasksManager fileBackedTasksManager) {
        super(fileBackedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        query = Optional.ofNullable(exchange.getRequestURI().getQuery());
        if (requestMethod.equals("GET")) {
            handleGetEpicSubTasks(exchange);
        }
        writeResponse(exchange, "������ ����� �� ��������������.", 405);
    }

    private void handleGetEpicSubTasks(HttpExchange exchange) {
        if (query.isEmpty()) {
            writeResponse(exchange, "������� �� ������ ���������.", 400);
        } else {
            String[] epicIdInString = query.get().split("=");
            int taskId = Integer.parseInt(epicIdInString[1]);
            ArrayList<SubTask> subTaskList = fileBackedTasksManager.getListOfEpicsSubTasks(taskId);
            writeResponse(exchange, gson.toJson(subTaskList), 200);
        }
    }
}
