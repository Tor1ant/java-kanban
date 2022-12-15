package service;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> browsingHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        remove(task.getId());
        browsingHistory.linkLast(task);
    }

    @Override
    public void remove(int id) {
        browsingHistory.removeNode(browsingHistory.idAndTaskNodes.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory.getTasks();
    }
}

class CustomLinkedList<E extends Task> {
    private TaskNode<E> head;
    private TaskNode<E> tail;
    Map<Integer, TaskNode> idAndTaskNodes = new LinkedHashMap<>();

    public void linkLast(E e) {
        final TaskNode<E> oldTail = tail;
        final TaskNode<E> newNode = new TaskNode<>(null, e, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.prevTask = newNode;
        }
        idAndTaskNodes.put(e.getId(), newNode);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        for (TaskNode taskNode : idAndTaskNodes.values()) {
            taskArrayList.add(taskNode.currentN);
        }
        return taskArrayList;
    }

    public void removeNode(TaskNode taskNode) {
        if (taskNode != null) {
            if (taskNode.nextTask == null) {
                idAndTaskNodes.remove(taskNode.currentN.getId());
                taskNode.prevTask.setNextTask(null);
                taskNode.setPrevTask(null);

            } else if (taskNode.prevTask == null) {
                idAndTaskNodes.remove(taskNode.currentN.getId());
                taskNode.setNextTask(null);

            } else {
                idAndTaskNodes.remove(taskNode.currentN.getId());
                taskNode.prevTask.setNextTask(taskNode.nextTask);
                taskNode.nextTask.setPrevTask(taskNode.prevTask);
                taskNode.currentN = null;
            }
        }
    }

    protected class TaskNode<N extends Task> {
        private N currentN;
        private TaskNode<N> prevTask;
        private TaskNode<N> nextTask;

        public TaskNode(TaskNode<N> prevTask, N currentN, TaskNode<N> nextTask) {
            this.currentN = currentN;
            this.prevTask = prevTask;
            this.nextTask = nextTask;
        }

        public void setCurrentTask(N currentN) {
            this.currentN = currentN;
        }

        public void setNextTask(TaskNode<N> nextTask) {
            this.nextTask = nextTask;
        }

        public void setPrevTask(TaskNode<N> prevTask) {
            this.prevTask = prevTask;
        }

        public void setCurrentE(N currentN) {
            this.currentN = currentN;
        }

        public N getCurrentE() {
            return currentN;
        }
    }
}
