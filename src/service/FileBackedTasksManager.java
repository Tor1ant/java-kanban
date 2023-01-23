package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path pathToSaveData;

    public FileBackedTasksManager() {
        this.pathToSaveData = Paths.get("SaveData.csv");
    }

    private void save() {
        final String historyInString = historyToString((InMemoryHistoryManager) historyManager);
        try (FileWriter fileWriter = new FileWriter(String.valueOf(pathToSaveData))) {
            fileWriter.write("id,type,name,status,description,epic(forSubTask),duration,startTime,endTime(forEpic)");
            fileWriter.write("\n");
            for (Task task : tasks.values()) {
                fileWriter.write(taskToString(task));
                fileWriter.write("\n");
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(taskToString(epic));
                fileWriter.write("\n");
            }
            for (SubTask subTask : subTasks.values()) {
                fileWriter.write(taskToString(subTask));
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyInString);
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
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = super.getEpicByID(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    private String taskToString(Task task) {
        StringBuilder taskToCSV = new StringBuilder();
        if (task instanceof SubTask) {
            taskToCSV.append(task.getId()).append(",").append(TaskType.SUBTASK).append(",").append(task.getTitle()).
                    append(",").append(task.getProgress()).append(",").append(task.getDescription()).append(",").
                    append(((SubTask) task).getEpicId()).append(",").append(task.getDuration()).append(",").append(task.
                            getStartTime());
        } else if (task instanceof Epic) {
            taskToCSV.append(task.getId()).append(",").append(TaskType.EPIC).append(",").append(task.getTitle()).
                    append(",").append(task.getProgress()).append(",").append(task.getDescription()).append(",").
                    append(task.getDuration()).append(",").append(task.getStartTime()).append(",").append(task.
                            getEndTime());
        } else if (task != null) {
            taskToCSV.append(task.getId()).append(",").append(TaskType.TASK).append(",").append(task.getTitle()).
                    append(",").append(task.getProgress()).append(",").append(task.getDescription()).append(",").
                    append(task.getDuration()).append(",").append(task.getStartTime());
        }
        return taskToCSV.toString();
    }

    private Task stringToTask(String value) {
        if (value != null) {
            String[] tasksInString = value.split(",");
            switch (TaskType.valueOf(tasksInString[1])) {
                case SUBTASK: {
                    int id = Integer.parseInt(tasksInString[0]);
                    SubTask subTask;
                    String title = tasksInString[2];
                    Status status = Status.valueOf(tasksInString[3]);
                    String description = tasksInString[4];
                    int epicId = Integer.parseInt(tasksInString[5]);
                    long duration = Long.parseLong(tasksInString[6]);
                    LocalDateTime startTime = LocalDateTime.parse(tasksInString[7]);
                    subTask = new SubTask(title, description, status, epicId, duration, startTime);
                    subTask.setId(id);

                    return subTask;
                }
                case EPIC: {
                    Epic epic;
                    int id = Integer.parseInt(tasksInString[0]);
                    String title = tasksInString[2];
                    String description = tasksInString[4];
                    long duration = Long.parseLong(tasksInString[5]);
                    LocalDateTime startTime = LocalDateTime.parse(tasksInString[6]);
                    LocalDateTime endTime = LocalDateTime.parse(tasksInString[7]);
                    epic = new Epic(title, description);
                    epic.setId(id);
                    epic.setDuration(duration);
                    epic.setStartTime(startTime);
                    epic.setEndTime(endTime);

                    return epic;
                }
                case TASK: {
                    Task task;
                    int id = Integer.parseInt(tasksInString[0]);
                    String title = tasksInString[2];
                    String description = tasksInString[4];
                    Status status = Status.valueOf(tasksInString[3]);
                    long duration = Long.parseLong(tasksInString[5]);
                    LocalDateTime startTime = LocalDateTime.parse(tasksInString[6]);
                    task = new Task(title, description, status, duration, startTime);
                    task.setId(id);

                    return task;
                }
            }
        }
        return null;
    }

    static private String historyToString(InMemoryHistoryManager manager) {
        ArrayList<Integer> tasksId = new ArrayList<>(manager.getIdAndTaskNodes().keySet());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tasksId.size(); i++) {
            if (i != tasksId.size() - 1) {
                stringBuilder.append(tasksId.get(i)).append(",");
            } else {
                stringBuilder.append(tasksId.get((i)));
            }
        }
        return stringBuilder.toString();
    }

    static private List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        String[] tasksId = value.split(",");
        for (String s : tasksId) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    static public FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        String taskInString;
        int MAX_TASK_ID = Integer.MIN_VALUE;
        List<Task> taskList = new ArrayList<>();
        List<Integer> tasksId = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                taskInString = bufferedReader.readLine();
                if (taskInString.equals("")) {
                    taskInString = bufferedReader.readLine();
                    if (taskInString != null) {
                        tasksId = FileBackedTasksManager.historyFromString(taskInString);
                        break;
                    }
                } else if (!taskInString.startsWith("id")) {
                    taskList.add(fileBackedTasksManager.stringToTask(taskInString));
                }
            }
            for (Task task : taskList) {
                if (task instanceof SubTask) {
                    fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                    fileBackedTasksManager.epics.get(((SubTask) task).getEpicId()).addSubTasksId(task.getId());
                } else if (task instanceof Epic) {
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                } else if (task != null) {
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                }
                if (task != null && tasksId.contains(task.getId())) {
                    fileBackedTasksManager.historyManager.add(task);
                }
            }
            for (Task task : taskList) {
                if (task.getId() > MAX_TASK_ID) {
                    MAX_TASK_ID = task.getId();
                }
            }
            fileBackedTasksManager.setId(MAX_TASK_ID + 1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("файл с данными не найден");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
    }
}
