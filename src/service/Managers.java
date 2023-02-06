package service;

public final class Managers {

    static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return FileBackedTasksManager.loadFromFile("SaveData.csv");
    }

   public static HttpTaskManager getDefaultHttpTaskManager() {
        return HttpTaskManager.loadFromServer("http://localhost:8078");
    }
}