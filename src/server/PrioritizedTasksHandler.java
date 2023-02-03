package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import service.FileBackedTasksManager;

public class PrioritizedTasksHandler extends TaskHandler {
    Gson gson = new Gson();

    public PrioritizedTasksHandler(FileBackedTasksManager fileBackedTasksManager) {
        super(fileBackedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            PrioritizedTasksHandle(exchange);
        } else
            writeResponse(exchange, "Данный метод не поддерживается.", 405);
    }

    private void PrioritizedTasksHandle(HttpExchange exchange) {
        if (exchange.getRequestURI().getQuery() == null) {
            String prioritizedTasksInJson = gson.toJson(fileBackedTasksManager.getPrioritizedTasks());
            writeResponse(exchange, prioritizedTasksInJson, 200);
        } else {
            writeResponse(exchange, "у данного запроса не может быть параметров", 400);
        }
    }

    @Override
    protected void writeResponse(HttpExchange exchange, String response, int code) {
        if (code == 405) {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Method-Not-Allowed", "доступные методы: GET");
        } else if (code == 400) {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Bad-Request", "у данного запроса не может быть параметров");
        }
        super.writeResponse(exchange, response, code);
    }
}
