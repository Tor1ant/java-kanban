package server;

import com.sun.net.httpserver.HttpServer;
import service.FileBackedTasksManager;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args) {
        HttpServer httpServer;

        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBacked();
            httpServer.createContext("/tasks/task", new TaskHandler(fileBackedTasksManager));
            httpServer.createContext("/tasks",new PrioritizedTasksHandler(fileBackedTasksManager));
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
