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
        //здесь должен быть кастомный ремувинг, который может удалять таску по id
        // для этого нужно реализовать кастомный линкедлист
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
    private int size = 0;
    HashMap<Integer, TaskNode> idAndTaskNodes = new HashMap<>();

/*    CustomLinkedList() {
        tail = new TaskNode<>(null, head, null);
        head = new TaskNode<>(null, null, tail);
    }*/

    public void linkLast(E e) {

/*        TaskNode<E> prev = secondTaskNode;
        prev.setCurrentTask(e);
        secondTaskNode = new TaskNode<>(null, prev, null);
        prev.setNextTask(secondTaskNode);
        Task task = (Task) e;
        idAndTaskNodes.put(task.getId(), secondTaskNode);*/
        final TaskNode<E> oldTail = tail;
        final TaskNode<E> newNode = new TaskNode<>(null, e, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.prevTask = newNode;
            size++;
        }
        Task task = (Task) e;
        idAndTaskNodes.put(task.getId(), newNode);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        for (TaskNode taskNode : idAndTaskNodes.values()) {
            taskArrayList.add(taskNode.currentE);
        }
        return taskArrayList;
    }

    public void removeNode(TaskNode taskNode) {
        if (taskNode != null) {
            if (taskNode.nextTask == null) {
                idAndTaskNodes.remove(taskNode.currentE.getId());
                taskNode.prevTask.setNextTask(null);
                taskNode.setPrevTask(null);
                size--;

            } else if (taskNode.prevTask == null) {
                idAndTaskNodes.remove(taskNode.currentE.getId());
                taskNode.setNextTask(null);
             //   taskNode.nextTask.setPrevTask(null);
                size--;

            } else {
                if (taskNode.prevTask != null && taskNode.nextTask != null) {
                    idAndTaskNodes.remove(taskNode.currentE.getId());
                    taskNode.prevTask.setNextTask(taskNode.nextTask);
                    taskNode.nextTask.setPrevTask(taskNode.prevTask);
                    taskNode.currentE = null;
                    size--;
                }
            }
        }
    }


    protected class TaskNode<E extends Task> {
        private E currentE;
        private TaskNode<E> prevTask;
        private TaskNode<E> nextTask;

        public TaskNode(TaskNode<E> prevTask, E currentE, TaskNode<E> nextTask) {
            this.currentE = currentE;
            this.prevTask = prevTask;
            this.nextTask = nextTask;
        }

        public void setCurrentTask(E currentE) {
            this.currentE = currentE;
        }

        public void setNextTask(TaskNode<E> nextTask) {
            this.nextTask = nextTask;
        }

        public void setPrevTask(TaskNode<E> prevTask) {
            this.prevTask = prevTask;
        }

        public void setCurrentE(E currentE) {
            this.currentE = currentE;
        }

        public E getCurrentE() {
            return currentE;
        }
    }
}
