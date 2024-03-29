package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private TaskNode head;
    private TaskNode tail;
    private final Map<Integer, TaskNode> idAndTaskNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        remove(task.getId());
        TaskNode newNode = linkLast(task);
        idAndTaskNodes.put(newNode.getCurrentTask().getId(), newNode);
    }

    @Override
    public void remove(int id) {
        removeNode(idAndTaskNodes.get(id));
        idAndTaskNodes.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private TaskNode linkLast(Task task) {
        TaskNode taskNode = new TaskNode(task);
        if (head != null && tail != null) {
            taskNode.prevTask = tail;
            tail.nextTask = taskNode;
        } else {
            head = taskNode;
        }
        tail = taskNode;
        return taskNode;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> browsingHistory = new ArrayList<>();
        TaskNode currentTaskNode = tail;
        browsingHistory.add(currentTaskNode.currentTask);
        while (currentTaskNode.prevTask != null) {
            currentTaskNode = currentTaskNode.prevTask;
            browsingHistory.add(currentTaskNode.currentTask);
        }
        return browsingHistory;
    }

    public void removeNode(TaskNode taskNode) {
        if (taskNode != null) {
            if (taskNode.prevTask == null && taskNode.nextTask == null) {
                head = null;
                tail = null;
            } else if (taskNode.prevTask == null) {
                head = taskNode.nextTask;
                head.prevTask = null;
            } else if (taskNode.nextTask == null) {
                tail = taskNode.prevTask;
                tail.nextTask = null;
            } else {
                taskNode.prevTask.nextTask = taskNode.nextTask;
                taskNode.nextTask.prevTask = taskNode.prevTask;
            }
        }
    }

    public Map<Integer, TaskNode> getIdAndTaskNodes() {
        return idAndTaskNodes;
    }

    private class TaskNode {
        private final Task currentTask;
        private TaskNode prevTask;
        private TaskNode nextTask;

        public TaskNode(Task currentTask) {
            this.currentTask = currentTask;
        }

        public Task getCurrentTask() {
            return currentTask;
        }
    }
}