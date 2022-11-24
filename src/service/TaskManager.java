package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public interface TaskManager <Type> {


    public int createTask(Task task);


    public int createEpic(Epic epic);

    public int addSubTask(SubTask subTask);

    public int updateTask(Task task);

    public int updateEpic(Epic epic);

    public SubTask updateSubTask(SubTask subTask);

    public ArrayList<Task> getAllTasks();

    public ArrayList<Epic> getAllEpics();

    public ArrayList<SubTask> getAllSubTasks();

    public void removeAllTasks();

    public void removeAllEpics();

    public void removeAllSubTasks();

    public Task getTaskByID(int id);

    public Epic getEpicByID(int id);

    public SubTask getSubTaskById(int id);

    public void removeTaskById(int id);

    public void removeEpicById(int id);

    public void removeSubTaskById(int id);

    public ArrayList<SubTask> getListOfEpicsSubTasks(int epicId);

    public void changeEpicProgress(int epicID);


}
