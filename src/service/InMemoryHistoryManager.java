package service;

import model.Task;

import java.util.ArrayList;
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
        //здесь должен быть кастомный ремувинг, который может удалять таску по id
        // для этого нужно реализовать кастомный линкедлист
        browsingHistory.removeIf(task -> task.getId() == id);
    }

    @Override
    public List<Task> getHistory() {
 List<Task> browsingHistoryForReturn = new ArrayList<>(browsingHistory);
        return browsingHistoryForReturn;
    }
}
