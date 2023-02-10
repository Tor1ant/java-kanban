package service;

import model.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager <T> {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
    Map<Integer, T> getIdAndTaskNodes();
}