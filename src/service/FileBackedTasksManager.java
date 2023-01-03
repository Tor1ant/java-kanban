package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final Path pathToSaveData;

    public FileBackedTasksManager() {
        this.pathToSaveData = Paths.get("src/sources/SaveData.csv");
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(String.valueOf(pathToSaveData))) {
            for (Task task : tasks.values()) {
                fileWriter.write(taskToString(task));
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(taskToString(epic));
            }
            for (SubTask subTask : subTasks.values()) {
                fileWriter.write(taskToString(subTask));
            }
            fileWriter.write("\n");

        } catch (IOException e) {
            throw new ManagerSaveException("Данные не сохранены");


        }
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

    String taskToString(Task task) {
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

    Task stringToTask(String value) {
        if (value != null) {
            String[] tasksInString = value.split(",");
            switch (tasksInString[1]) {
                case "SUBTASK": {
                    int id = Integer.parseInt(tasksInString[0]);
                    SubTask subTask;
                    String title = tasksInString[2];
                    Status status = Status.valueOf(tasksInString[3]);
                    String description = tasksInString[4];
                    int epicId = Integer.parseInt(tasksInString[5]);
                    subTask = new SubTask(title, description, status, epicId);
                    subTask.setId(id);
                    return subTask;

                }
                case "EPIC": {
                    Epic epic;
                    int id = Integer.parseInt(tasksInString[0]);
                    String title = tasksInString[2];
                    String description = tasksInString[4];
                    epic = new Epic(title, description);
                    epic.setId(id);
                    break;
                }
                case "TASK": {
                    Task task;
                    int id = Integer.parseInt(tasksInString[0]);
                    String title = tasksInString[2];
                    String description = tasksInString[4];
                    Status status = Status.valueOf(tasksInString[3]);
                    task = new Task(title, description, status);
                    task.setId(id);
                    return task;
                }
            }

        }

        return null;
    }
}
