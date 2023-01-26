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
        } else if (o1.getStartTime().isEqual(o2.getStartTime())) {
            return o1.getTitle().compareTo(o2.getTitle());
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });
    private int id = 0;

    @Override
    public void createTask(Task task) {
        Task validationTask = validation(task);
        if (validationTask == null) {
            id++;
            task.setId(id);
            tasks.put(id, task);
            prioritizedTasks.add(task);
        } else {
            throw new ValidateException("Задача " + task.getTitle() + " пересекается по времени с задачей " +
                    validationTask.getTitle());
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
        Task validationTask = validation(subTask);
        if (validationTask == null) {
            id++;
            subTask.setId(id);
            epics.get(subTask.getEpicId()).addSubTasksId(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            changeEpicProgress(subTask.getEpicId());
            prioritizedTasks.add(subTask);
        } else {
            throw new ValidateException("Задача " + subTask.getTitle() + " пересекается по времени с задачей " +
                    validationTask.getTitle());
        }
    }

    @Override
    public void updateTask(Task task) {
        Task validationTask = validation(task);
        if (validationTask == null) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new ValidateException("Задача " + task.getTitle() + " пересекается по времени с задачей " +
                    validationTask.getTitle());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Task validationTask = validation(subTask);
        if (validationTask == null) {
            prioritizedTasks.remove(subTasks.get(subTask.getId()));
            subTasks.put(subTask.getId(), subTask);
            prioritizedTasks.add(subTask);
            changeEpicProgress(subTask.getEpicId());
        } else {
            throw new ValidateException("Задача " + subTask.getTitle() + " пересекается по времени с задачей " +
                    validationTask.getTitle());
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

    @Override
    public void changeEpicProgress(int epicID) {
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(epicID);
        int countOfProgressNew = 0;
        int countOfProgressDone = 0;
        if (epics.get(epicID).getSubTasksId().isEmpty()) {
            epics.get(epicID).setProgress(Status.NEW);
            changeEpicDuration(epicID, subTaskArrayList);
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
        if (epicSubTasks.isEmpty()) {
            epics.get(epicID).setStartTime(null);
            epics.get(epicID).setEndTime(null);
            epics.get(epicID).setDuration(null);
            return;
        }
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    protected void setId(int id) {
        this.id = id;
    }

    private Task validation(Task task) {
        boolean startTimeIsNull = prioritizedTasks.stream().anyMatch(task1 -> task1.getStartTime() != null &&
                task.getStartTime() != null);
        boolean startTimeOrEndTimeIsEqualWitchTaskFromTreeSet = false;

        for (Task prioritizedTask : prioritizedTasks) {
            if (task.getId() == prioritizedTask.getId()) {
                return null;
            }
            if (task.getStartTime() != null && task.getEndTime() != null) {
                if (task.getEndTime().isBefore(prioritizedTask.getStartTime())) {
                    startTimeOrEndTimeIsEqualWitchTaskFromTreeSet = true;
                } else if (task.getStartTime().isAfter(prioritizedTask.getEndTime())) {
                    startTimeOrEndTimeIsEqualWitchTaskFromTreeSet = true;
                } else if (task.getStartTime().isEqual(prioritizedTask.getStartTime())) {
                    return prioritizedTask;
                } else if (task.getEndTime().isEqual(prioritizedTask.getEndTime())) {
                    return prioritizedTask;
                } else {
                    return prioritizedTask;
                }
            } else {
                startTimeOrEndTimeIsEqualWitchTaskFromTreeSet = true;
            }
        }
        if (startTimeIsNull && startTimeOrEndTimeIsEqualWitchTaskFromTreeSet) {
            return null;
        } else
            return null;
    }
}