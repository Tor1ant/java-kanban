package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public interface TaskManager {

    int createTask(Task task);

    int createEpic(Epic epic);

    int addSubTask(SubTask subTask);

    int updateTask(Task task);

    int updateEpic(Epic epic);

    SubTask updateSubTask(SubTask subTask);

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

    void changeEpicProgress(int epicID);

    List<Task> getHistory();

}
