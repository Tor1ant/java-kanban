package service;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.*;

public class Manager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int id;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }


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

    public int createSubTask(SubTask subTask, int epicId) {
        id++;
        subTask.setId(id);
        subTask.setEpicId(epicId);
        epics.get(epicId).addSubTasksId(epicId);
        updateSubTask(subTask);
        return subTask.getId();
    }

    public int updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        changeEpicProgress(epic.getId());

        return epic.getId();
    }

    public int updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        changeEpicProgress(subTask.getEpicId());

        return subTask.getId();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subTasks.values());

        return allTasks;
    }

    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        id = 0;
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
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(id);
        epics.remove(id);
        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getEpicId() == id) {
                subTasks.remove(subTask.getId());
            }
        }

    }

    public void removeSubTaskById(int id) {
        int epicId = subTasks.get(id).getEpicId();
        subTasks.remove(id);
        changeEpicProgress(epicId);
    }

    public ArrayList<SubTask> getListOfEpicsSubTasks(int epicId) {
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epicId) {
                subTaskArrayList.add(subTask);
            }
        }
        return subTaskArrayList;
    }

    private void changeEpicProgress(int epicID) {
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(epicID);
        String progress1 = "NEW";
        String progress2 = "IN_PROGRESS";
        String progress3 = "DONE";
        int countOfProgressNew = 0;
        int countOfProgressDone = 0;
        if (epics.get(epicID).getSubTasksId().isEmpty()) {
            epics.get(epicID).setProgress(progress1);
        }

        for (SubTask subTask :
                subTaskArrayList) {
            if (subTask.getProgress().equals("NEW")) {
                countOfProgressNew++;
            } else if (subTask.getProgress().equals("DONE")) {
                countOfProgressDone++;
            }
        }

        if (countOfProgressNew == subTaskArrayList.size()) {
            epics.get(epicID).setProgress(progress1);
            return;
        }

        if (countOfProgressDone == subTaskArrayList.size()) {
            epics.get(epicID).setProgress(progress3);
        } else {
            epics.get(epicID).setProgress(progress2);
        }
    }
}
