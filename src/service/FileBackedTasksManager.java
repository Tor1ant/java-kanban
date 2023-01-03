package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final Path pathToSaveData;

    public FileBackedTasksManager() {
        this.pathToSaveData = Paths.get("src/sources/SaveData");
    }

    public void save() {

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyTasks = super.getHistory();
        List<Integer> tasksId = new ArrayList<>();
        for (Task historyTask : historyTasks) {
            tasksId.add(historyTask.getId());
        }
        save();
        return null;
    }

    String toString(Task task) {
        StringBuilder taskToCSV = new StringBuilder();
        if (task instanceof SubTask) {
            taskToCSV.append(task.getId()).append(",").append(TaskType.SUBTASK).append(",").append(task.getTitle()).
                    append(",").append(task.getProgress()).append(",").append(task.getDescription()).append(",").
                    append(((SubTask) task).getEpicId());

        } else if (task instanceof Epic) {
            taskToCSV.append(task.getId()).append(",").append(TaskType.EPIC).append(",").append(task.getTitle()).
                    append(",").append(task.getProgress()).append(",").append(task.getDescription());
        } else if (task != null) {
            taskToCSV.append(task.getId()).append(",").append(TaskType.TASK).append(",").append(task.getTitle()).
                    append(",").append(task.getProgress()).append(",").append(task.getDescription());
        }
        return taskToCSV.toString();

    }
}
