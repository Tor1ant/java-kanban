package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

public class PrioritizedTasksHandler extends TaskHandler {

    public PrioritizedTasksHandler(TaskManager httpTaskManager) {
        super(httpTaskManager);
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
        String prioritizedTasksInJson = gson.toJson(httpTaskManager.getPrioritizedTasks());
        writeResponse(exchange, prioritizedTasksInJson, 200);
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