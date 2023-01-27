package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });
    private int id = 0;

    @Override
    public void createTask(Task task) {
        boolean isThereAnOverlap = doesNewTaskCrossesWithExistingTask(task);
        if (!isThereAnOverlap) {
            id++;
            task.setId(id);
            tasks.put(id, task);
            prioritizedTasks.add(task);
        } else {
            throw new ValidateException("Задача " + task.getTitle() + " пересекается по времени с существующей задачей");
        }
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
        boolean isThereAnOverlap = doesNewTaskCrossesWithExistingTask(subTask);
        if (!isThereAnOverlap) {
            id++;
            subTask.setId(id);
            epics.get(subTask.getEpicId()).addSubTasksId(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            changeEpicProgress(subTask.getEpicId());
            prioritizedTasks.add(subTask);
        } else {
            throw new ValidateException("Задача " + subTask.getTitle() + " пересекается по времени с существующей" +
                    " задачей");
        }
    }

    @Override
    public void updateTask(Task task) {
        boolean isThereAnOverlap = doesNewTaskCrossesWithExistingTask(task);
        if (!isThereAnOverlap) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new ValidateException("Задача " + task.getTitle() + " пересекается по времени с существующей задачей");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        boolean isThereAnOverlap = doesNewTaskCrossesWithExistingTask(subTask);
        if (!isThereAnOverlap) {
            prioritizedTasks.remove(subTasks.get(subTask.getId()));
            subTasks.put(subTask.getId(), subTask);
            prioritizedTasks.add(subTask);
            changeEpicProgress(subTask.getEpicId());
        } else {
            throw new ValidateException("Задача " + subTask.getTitle() + " пересекается по времени с существующей" +
                    " задачей");
        }
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
        prioritizedTasks.removeIf(tasks::containsValue);
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        prioritizedTasks.removeIf(subTasks::containsValue);
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.keySet().forEach(historyManager::remove);
        prioritizedTasks.removeIf(subTasks::containsValue);
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
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        for (Integer subtaskId : epics.get(id).getSubTasksId()) {
            prioritizedTasks.remove(subTasks.get(subtaskId));
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
        prioritizedTasks.remove(subTasks.get(id));
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

    private void changeEpicProgress(int epicID) {
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(epicID);
        int countOfProgressNew = 0;
        int countOfProgressDone = 0;
        if (epics.get(epicID).getSubTasksId().isEmpty()) {
            epics.get(epicID).setStatus(Status.NEW);
            changeEpicDuration(epicID, subTaskArrayList);
            return;
        }
        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getStatus().equals(Status.NEW)) {
                countOfProgressNew++;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                countOfProgressDone++;
            }
        }
        if (countOfProgressNew == subTaskArrayList.size()) {
            epics.get(epicID).setStatus(Status.NEW);
            changeEpicDuration(epicID, subTaskArrayList);
            return;
        }
        if (countOfProgressDone == subTaskArrayList.size()) {
            epics.get(epicID).setStatus(Status.DONE);
            changeEpicDuration(epicID, subTaskArrayList);
        } else {
            epics.get(epicID).setStatus(Status.IN_PROGRESS);
            changeEpicDuration(epicID, subTaskArrayList);
        }
    }

    private void changeEpicDuration(int epicID, List<SubTask> epicSubTasks) {
        long duration = 0;
        LocalDateTime MIN_TIME = LocalDateTime.MAX;
        LocalDateTime MAX_TIME = LocalDateTime.MIN;
        if (epicSubTasks.isEmpty()) {
            epics.get(epicID).setStartTime(null);
            epics.get(epicID).setEndTime(null);
            epics.get(epicID).setDuration(null);
            return;
        }
        for (SubTask epicSubTask : epicSubTasks) {
            duration += epicSubTask.getDuration();
            if (MIN_TIME.isAfter(epicSubTask.getStartTime()) || epicSubTask.getStartTime() == null) {
                MIN_TIME = epicSubTask.getStartTime();
            }
            if (MAX_TIME.isBefore(epicSubTask.getEndTime()) || epicSubTask.getEndTime() == null) {
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void setId(int id) {
        this.id = id;
    }

    private boolean doesNewTaskCrossesWithExistingTask(Task task) {
        if (getPrioritizedTasks().isEmpty()) {
            return false;
        }
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (task.getId() == prioritizedTask.getId()) {
                return false;
            }
            if (task.getStartTime() != null && task.getEndTime() != null && task.getStartTime() != null &&
                    prioritizedTask.getStartTime() != null) {
                return !task.getStartTime().isAfter(prioritizedTask.getEndTime()) && !task.getEndTime().isBefore(
                        prioritizedTask.getStartTime());
            }
        }
        return true;
    }
}