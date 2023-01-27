package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    SubTask getSubTaskById(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    ArrayList<SubTask> getListOfEpicsSubTasks(int epicId);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}