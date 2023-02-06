package server;

import com.sun.net.httpserver.HttpServer;
import service.HttpTaskManager;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    int PORT = 8080;
    private final HttpServer httpServer;
    HttpTaskManager httpTaskManager;

    public HttpTaskServer() throws IOException {
        httpTaskManager = Managers.getDefaultHttpTaskManager();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(httpTaskManager));
        httpServer.createContext("/tasks", new PrioritizedTasksHandler(httpTaskManager));
        httpServer.createContext("/tasks/history", new HistoryHandler(httpTaskManager));
        httpServer.createContext("/tasks/epic",new EpicHandler(httpTaskManager));
        httpServer.createContext("/tasks/subtask", new SubTaskHandler(httpTaskManager));
        httpServer.createContext("/tasks/subtask/epic", new EpicSubTasksHandler(httpTaskManager));
    }

    public void startServer() {
        System.out.println("Сервер начал работу на " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        httpServer.start();
    }

    public void stopServer() {
        System.out.println("Сервер прекратил работу " + PORT);
        httpServer.stop(0);
    }
}
