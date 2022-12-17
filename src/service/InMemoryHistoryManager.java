package service;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> browsingHistory = new CustomLinkedList<>();
    Map<Integer, CustomLinkedList.TaskNode> idAndTaskNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        remove(task.getId());
        CustomLinkedList.TaskNode newNode = browsingHistory.linkLast(task);
        idAndTaskNodes.put(newNode.getCurrentE().getId(), newNode);
    }

    @Override
    public void remove(int id) {
        browsingHistory.removeNode(idAndTaskNodes.get(id));
        idAndTaskNodes.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory.getTasks();
    }
}