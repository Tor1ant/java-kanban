package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> browsingHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        task.setViewed(true);
        browsingHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history;
        if (browsingHistory.size() > 10) {
            do {
                browsingHistory.remove(0);
            } while (browsingHistory.size() != 10);

        }
        history = browsingHistory;
        return history;
    }
}
