package service;

import model.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (browsingHistory.size() >= 10) {
            browsingHistory.removeFirst();
        }

        browsingHistory.addLast(task);

    }

    @Override
    public void remove(int id) {
        browsingHistory.removeIf(task -> task.getId() == id);
    }

    @Override
    public List<Task> getHistory() {

        return browsingHistory;
    }
}
