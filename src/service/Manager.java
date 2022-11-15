package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;

    public int createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);

        return task.getId();
    }

    public int createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);

        return epic.getId();
    }

    public int addSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        epics.get(subTask.getEpicId()).addSubTasksId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        changeEpicProgress(subTask.getEpicId());

        return subTask.getId();
    }

    public int updateTask(Task task) {
        if (tasks.containsValue(task)) {
            tasks.put(task.getId(), task);
        }
        return task.getId();
    }

    public int updateEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            epics.put(epic.getId(), epic);
        }
        return epic.getId();
    }

    public SubTask updateSubTask(SubTask subTask) {
        if (subTasks.containsValue(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            changeEpicProgress(subTask.getEpicId());
        }
        return subTask;

    }

    public ArrayList<Task> getAllTasks() {

        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {

        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {

        return new ArrayList<>(subTasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasksId();
            changeEpicProgress(epic.getId());
        }
    }

    public Task getTaskByID(int id) {

        return tasks.get(id);
    }

    public Epic getEpicByID(int id) {

        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {

        return subTasks.get(id);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        epics.remove(id);
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                subTasks.remove(subTask.getId());
            }
        }
    }

    public void removeSubTaskById(int id) {
        int epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).removeSubTask(id);
        subTasks.remove(id);
        changeEpicProgress(epicId);
    }

    public ArrayList<SubTask> getListOfEpicsSubTasks(int epicId) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer subTaskId : epic.getSubTasksId()) {
            subTaskArrayList.add(subTasks.get(subTaskId));
        }
        return subTaskArrayList;
    }

    private void changeEpicProgress(int epicID) {
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(epicID);
        String progressNew = "NEW";
        String progressInProgress = "IN_PROGRESS";
        String progressDone = "DONE";
        int countOfProgressNew = 0;
        int countOfProgressDone = 0;

        if (epics.get(epicID).getSubTasksId().isEmpty()) {
            epics.get(epicID).setProgress(progressNew);
            return;
        }

        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getProgress().equals(progressNew)) {
                countOfProgressNew++;
            } else if (subTask.getProgress().equals(progressDone)) {
                countOfProgressDone++;
            }
        }

        if (countOfProgressNew == subTaskArrayList.size()) {
            epics.get(epicID).setProgress(progressNew);
            return;
        }

        if (countOfProgressDone == subTaskArrayList.size()) {
            epics.get(epicID).setProgress(progressDone);
        } else {
            epics.get(epicID).setProgress(progressInProgress);
        }
    }
}
