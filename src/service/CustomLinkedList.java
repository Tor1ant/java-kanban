package service;

import model.Task;
import java.util.ArrayList;

public class CustomLinkedList<E extends Task> {
    private TaskNode head;
    private TaskNode tail;

    public TaskNode linkLast(E e) {
        final TaskNode oldTail = tail;
        final TaskNode newNode = new TaskNode(null, e, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.prevTask = newNode;
        }
        return newNode;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> browsingHistory = new ArrayList<>();
        TaskNode currentTaskNode = tail;
        browsingHistory.add(currentTaskNode.currentTask);
        while (currentTaskNode.nextTask != null) {
            currentTaskNode = currentTaskNode.nextTask;
            browsingHistory.add(currentTaskNode.currentTask);
        }
        return browsingHistory;
    }

    public void removeNode(TaskNode taskNode) {
        if (taskNode != null) {
            if (taskNode.nextTask == null) {
                taskNode.prevTask.setNextTask(null);
                taskNode.setPrevTask(null);

            } else if (taskNode.prevTask == null) {
                taskNode.setNextTask(null);

            } else {
                taskNode.prevTask.setNextTask(taskNode.nextTask);
                taskNode.nextTask.setPrevTask(taskNode.prevTask);
            }
        }
    }

    public class TaskNode {
        private Task currentTask;
        private TaskNode prevTask;
        private TaskNode nextTask;

        public TaskNode(TaskNode prevTask, Task currentTask, TaskNode nextTask) {
            this.currentTask = currentTask;
            this.prevTask = prevTask;
            this.nextTask = nextTask;
        }

        public void setCurrentTask(Task currentTask) {
            this.currentTask = currentTask;
        }

        public void setNextTask(TaskNode nextTask) {
            this.nextTask = nextTask;
        }

        public void setPrevTask(TaskNode prevTask) {
            this.prevTask = prevTask;
        }

        public Task getCurrentE() {
            return currentTask;
        }
    }
}