package server;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.Task;
import service.FileBackedTasksManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class TaskHandler implements HttpHandler {
    protected FileBackedTasksManager fileBackedTasksManager;
    protected Gson gson;
    protected Optional<String> query;

    public TaskHandler(FileBackedTasksManager fileBackedTasksManager) {
        this.fileBackedTasksManager = fileBackedTasksManager;
        gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        query = Optional.ofNullable(exchange.getRequestURI().getQuery());
        switch (requestMethod) {
            case "GET":
                handleGetTasks(exchange);
                break;
            case "DELETE":
                handleDeleteTask(exchange);
                break;
            case "POST":
                handlePostTask(exchange);
            default:
                writeResponse(exchange, "Данный метод не поддерживается.", 405);
        }
    }

    private void handleGetTasks(HttpExchange exchange) {
        handleGet(exchange, fileBackedTasksManager::getAllTasks, fileBackedTasksManager::getTaskByID);
    }

    private void handleDeleteTask(HttpExchange exchange) {
        handleDelete(exchange, fileBackedTasksManager::removeAllTasks, fileBackedTasksManager::getTaskByID,
                fileBackedTasksManager::removeTaskById);
    }

    private void handlePostTask(HttpExchange exchange) {
        handlePost(exchange, fileBackedTasksManager::getAllTasks, fileBackedTasksManager::updateTask,
                fileBackedTasksManager::createTask, Task.class);
    }

    protected void writeResponse(HttpExchange exchange, String response, int code) {
        try {
            if (response.isBlank()) {
                exchange.sendResponseHeaders(code, 0);
            } else {
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(code, 0);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(bytes);
            }
            exchange.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Task> void handleGet(HttpExchange exchange, Supplier<ArrayList<T>> allTasksGetter,
                                              Function<Integer, T> taskByIdGetter) {
        if (query.isEmpty()) {
            ArrayList<T> allTasks = allTasksGetter.get();
            String allTasksInJson = gson.toJson(allTasks);
            writeResponse(exchange, allTasksInJson, 200);
        } else {
            String[] taskIdInString = query.get().split("=");
            int taskId = Integer.parseInt(taskIdInString[1]);
            writeResponse(exchange, gson.toJson(taskByIdGetter.apply(taskId)), 200);
        }
    }

    @FunctionalInterface
    protected interface AllTasksRemover {
        void allTasksRemove();
    }

    @FunctionalInterface
    protected interface TaskByIdRemover {
        void removeTask(int id);
    }

    protected <T extends Task> void handleDelete(HttpExchange exchange, AllTasksRemover allTasksRemover,
                                                 Function<Integer, T> taskByIdGetter,
                                                 TaskByIdRemover taskByIdRemover) {
        if (query.isEmpty()) {
            allTasksRemover.allTasksRemove();
            writeResponse(exchange, "Все задачи удалены.", 200);
        } else {
            String[] taskIdInString = query.get().split("=");
            int taskId = Integer.parseInt(taskIdInString[1]);
            T task = taskByIdGetter.apply(taskId);
            taskByIdRemover.removeTask(taskId);
            writeResponse(exchange, "задача " + task.getTitle() + " удалена", 200);
        }
    }

    protected <T extends Task> void handlePost(HttpExchange exchange, Supplier<ArrayList<T>> AllTasksGetter,
                                               Consumer<T> TaskUpdater, Consumer<T> TaskCreate, Class<T> tClass) {
        InputStream inputStream = exchange.getRequestBody();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                StandardCharsets.UTF_8))) {
            StringBuilder taskInString = new StringBuilder();
            while (bufferedReader.ready()) {
                taskInString.append(bufferedReader.readLine());
            }
            T taskForPost = gson.fromJson(taskInString.toString(), tClass);
            for (T task : AllTasksGetter.get()) {
                if (task.getId() == taskForPost.getId()) {
                    TaskUpdater.accept(taskForPost);
                    writeResponse(exchange, "задача " + taskForPost.getTitle() + " добавлена", 200);
                    return;
                }
            }
            TaskCreate.accept(taskForPost);
            writeResponse(exchange, "задача " + taskForPost.getTitle() + " добавлена", 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
