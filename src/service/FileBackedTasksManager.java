package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    private void save() {
        final String historyInString = historyToString((InMemoryHistoryManager) historyManager);
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write("id,type,name,status,description,duration,startTime,epicId");
            fileWriter.write("\n");
            for (Task task : tasks.values()) {
                fileWriter.write(task.toString());
                fileWriter.write("\n");
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(epic.toString());
                fileWriter.write("\n");
            }
            for (SubTask subTask : subTasks.values()) {
                fileWriter.write(subTask.toString());
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyInString);
        } catch (IOException e) {
            throw new ManagerSaveException("Данные не сохранены", e);
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

    private Task stringToTask(String value) {
        String[] tasksInString = value.split(",");
        int id = Integer.parseInt(tasksInString[0]);
        String title = tasksInString[2];
        String description = tasksInString[4];
        Status status = Status.valueOf(tasksInString[3]);
        Long duration;
        try {
            duration = Long.parseLong(tasksInString[5]);
        } catch (NumberFormatException e) {
            duration = 0L;
        }
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(tasksInString[6]);
        } catch (Exception e) {
            startTime = null;
        }
        int epicId = tasksInString.length == 8 ? Integer.parseInt(tasksInString[7]) : 0;

        switch (TaskType.valueOf(tasksInString[1])) {
            case SUBTASK: {
                SubTask subTask;
                subTask = new SubTask(title, description, status, epicId, duration, startTime);
                subTask.setId(id);
                return subTask;
            }
            case EPIC: {
                Epic epic;
                epic = new Epic(title, description);
                epic.setId(id);
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                epic.setEndTime(startTime == null ? null : startTime.plusMinutes(duration));
                return epic;
            }
            case TASK: {
                Task task;
                task = new Task(title, description, status, duration, startTime);
                task.setId(id);
                return task;
            }
            default:
                throw new NullPointerException("Задача не считана, файл сохранения повреждён или не найден.");
        }
    }

    private static String historyToString(InMemoryHistoryManager manager) {
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

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        String[] tasksId = value.split(",");
        for (String s : tasksId) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    public static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
        File file = new File(fileBackedTasksManager.path);
        String taskInString;
        int MAX_TASK_ID = Integer.MIN_VALUE;
        List<Task> taskList = new ArrayList<>();
        List<Integer> tasksId = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                taskInString = bufferedReader.readLine();
                if (taskInString.isEmpty()) {
                    taskInString = bufferedReader.readLine();
                    if (taskInString == null) {
                        break;
                    }
                    tasksId = FileBackedTasksManager.historyFromString(taskInString);
                } else {
                    taskList.add(fileBackedTasksManager.stringToTask(taskInString));
                }
            }
            for (Task task : taskList) {
                if (task instanceof SubTask) {
                    fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                    fileBackedTasksManager.epics.get(((SubTask) task).getEpicId()).addSubTasksId(task.getId());
                } else if (task instanceof Epic) {
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                } else
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                if (tasksId.contains(task.getId())) {
                    fileBackedTasksManager.historyManager.add(task);
                }
            }
            for (Task task : taskList) {
                if (task.getId() > MAX_TASK_ID) {
                    MAX_TASK_ID = task.getId();
                }
            }
            if (file.length() != 0) {
                fileBackedTasksManager.setId(MAX_TASK_ID + 1);
            } else fileBackedTasksManager.setId(0);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла с сохранёнными данными");
        }
        fileBackedTasksManager.prioritizedTasks.addAll(fileBackedTasksManager.tasks.values());
        fileBackedTasksManager.prioritizedTasks.addAll(fileBackedTasksManager.subTasks.values());
        return fileBackedTasksManager;
    }
}