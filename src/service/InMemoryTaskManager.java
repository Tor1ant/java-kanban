package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;

    @Override
    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        changeEpicProgress(epic.getId());
    }

    @Override
    public void addSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        epics.get(subTask.getEpicId()).addSubTasksId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        changeEpicProgress(subTask.getEpicId());
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        changeEpicProgress(subTask.getEpicId());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.keySet().forEach(historyManager::remove);
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasksId();
            changeEpicProgress(epic.getId());
        }
    }

    @Override
    public Task getTaskByID(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicByID(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        for (Integer subtaskId : epics.get(id).getSubTasksId()) {
            subTasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        int epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).removeSubTask(id);
        subTasks.remove(id);
        changeEpicProgress(epicId);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<SubTask> getListOfEpicsSubTasks(int epicId) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer subTaskId : epic.getSubTasksId()) {
            subTaskArrayList.add(subTasks.get(subTaskId));
        }
        return subTaskArrayList;
    }

    @Override
    public void changeEpicProgress(int epicID) {
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(epicID);
        int countOfProgressNew = 0;
        int countOfProgressDone = 0;
        if (epics.get(epicID).getSubTasksId().isEmpty()) {
            epics.get(epicID).setProgress(Status.NEW);
            return;
        }
        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getProgress().equals(Status.NEW)) {
                countOfProgressNew++;
            } else if (subTask.getProgress().equals(Status.DONE)) {
                countOfProgressDone++;
            }
        }
        if (countOfProgressNew == subTaskArrayList.size()) {
            epics.get(epicID).setProgress(Status.NEW);
            changeEpicDuration(epicID, subTaskArrayList);
            return;
        }
        if (countOfProgressDone == subTaskArrayList.size()) {
            epics.get(epicID).setProgress(Status.DONE);
            changeEpicDuration(epicID, subTaskArrayList);
        } else {
            epics.get(epicID).setProgress(Status.IN_PROGRESS);
            changeEpicDuration(epicID, subTaskArrayList);
        }
    }

    private void changeEpicDuration(int epicID, List<SubTask> epicSubTasks) {
        long duration = 0;
        LocalDateTime MIN_TIME = LocalDateTime.MAX;
        LocalDateTime MAX_TIME = LocalDateTime.MIN;
        for (SubTask epicSubTask : epicSubTasks) {
            duration += epicSubTask.getDuration();
            if (MIN_TIME.isAfter(epicSubTask.getStartTime())) {
                MIN_TIME = epicSubTask.getStartTime();
            }
            if (MAX_TIME.isBefore(epicSubTask.getEndTime())) {
                MAX_TIME = epicSubTask.getEndTime();
            }
        }
        epics.get(epicID).setStartTime(MIN_TIME);
        epics.get(epicID).setEndTime(MAX_TIME);
        epics.get(epicID).setDuration(duration);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void setId(int id) {
        this.id = id;
    }
}

