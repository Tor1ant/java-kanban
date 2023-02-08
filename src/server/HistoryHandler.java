package server;

import com.sun.net.httpserver.HttpExchange;
import service.HttpTaskManager;

public class HistoryHandler extends PrioritizedTasksHandler {
    public HistoryHandler(HttpTaskManager httpTaskManager) {
        super(httpTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            historyHandle(exchange);
        } else
            writeResponse(exchange, "Данный метод не поддерживается.", 405);
    }

    private void historyHandle(HttpExchange exchange) {
        if (exchange.getRequestURI().getQuery() == null) {
            String historyInJson = gson.toJson(httpTaskManager.getHistory());
            writeResponse(exchange, historyInJson, 200);
        } else
            writeResponse(exchange, "у данного запроса не может быть параметров", 400);
    }
}