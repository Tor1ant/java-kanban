package server;

import com.sun.net.httpserver.HttpServer;
import service.FileBackedTasksManager;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    int PORT = 8080;
    private final HttpServer httpServer;
    FileBackedTasksManager fileBackedTasksManager;

    public HttpTaskServer() throws IOException {
        fileBackedTasksManager = Managers.getDefaultFileBacked();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks", new PrioritizedTasksHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks", new HistoryHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/history", new HistoryHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic",new EpicHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask", new SubTaskHandler(fileBackedTasksManager));
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

    public static void main(String[] args) {
        try {
            HttpTaskServer taskServer = new HttpTaskServer();
            taskServer.startServer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
