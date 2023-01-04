package service;

import model.Epic;
import model.SubTask;
import model.Task;
import java.io.*;
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
        final String historyInString = historyToString((InMemoryHistoryManager) historyManager);
        try (FileWriter fileWriter = new FileWriter(String.valueOf(pathToSaveData))) {
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

                    return epic;
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

    static String historyToString(InMemoryHistoryManager manager) {
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

    static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        String[] tasksId = value.split(",");
        for (String s : tasksId) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        String taskInString;
        List<Task> taskList = new ArrayList<>();
        List<Integer> tasksId = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                taskInString = bufferedReader.readLine();
                if (taskInString.equals("")) {
                    taskInString = bufferedReader.readLine();
                    tasksId = FileBackedTasksManager.historyFromString(taskInString);
                    break;
                }
                taskList.add(fileBackedTasksManager.stringToTask(taskInString));
            }
            for (Task task : taskList) {
                if (task instanceof SubTask) {
                    //   fileBackedTasksManager.addSubTask((SubTask) task);
                    fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                } else if (task instanceof Epic) {
                    //  fileBackedTasksManager.createEpic((Epic) task);
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                } else if (task != null) {
                    // fileBackedTasksManager.createTask(task);
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                }
                if (task != null && tasksId.contains(task.getId())) {
                    fileBackedTasksManager.historyManager.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("файл с данными не найден");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
    }

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager();

        Task task1 = new Task("Погулять с собакой", "в 5 утра", Status.NEW);
        Task task2 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику", Status.NEW);
        Epic epicWithoutSubTasks = new Epic("Сделать ремонт в квартире", "Давно пора");
        Epic epicWithSubTasks = new Epic("Сходить в магазин", "Купить продукты");
        SubTask subtask1 = new SubTask("Купить молоко", "Зайти за молоком в пятерочку", Status.NEW, 4);
        SubTask subtask2 = new SubTask("Купить сахар", "это не обязательно", Status.NEW, 4);
        SubTask subTask3 = new SubTask("Купить вазелин", "Допустим", Status.NEW, 4);

        //создаем задачи, эпики и подзадачи
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epicWithoutSubTasks);
        taskManager.createEpic(epicWithSubTasks);
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subTask3);

        //Запрашиваем созданные задачи несколько раз в разном порядке
        taskManager.getTaskByID(task1.getId());
        taskManager.getEpicByID(epicWithoutSubTasks.getId());
        taskManager.getSubTaskById(subtask1.getId());
        taskManager.getEpicByID(epicWithSubTasks.getId());
        taskManager.getTaskByID(task2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getSubTaskById(subtask2.getId());

        // Печатаем историю запросов
        System.out.println("Печатаем историю запросов" + "\n");
        System.out.println(taskManager.getHistory());

        //делаем еще один вызов таски с id1 и эпика с id 4 и печатаем историю вызовов
        System.out.println("делаем еще один вызов таски с id1 и эпика с id 4 и печатаем историю вызовов" + "\n");
        taskManager.getTaskByID(task1.getId());
        taskManager.getEpicByID(epicWithSubTasks.getId());
        subtask1.setProgress(Status.DONE);
        taskManager.updateSubTask(subtask1);
        System.out.println(taskManager.getHistory());


        //удаляем сабтаску с id 5 и печатаем историю
        System.out.println("удаляем сабтаску с id 5 и печатаем историю" + "\n");
        taskManager.removeSubTaskById(subtask1.getId());
        System.out.println(taskManager.getHistory());

        //удаляем таску с id1 и печатаем историю
        System.out.println("удаляем таску с id1 и печатаем историю" + "\n");
        taskManager.removeTaskById(task1.getId());
        System.out.println(taskManager.getHistory());

        //удаляем эпик с тремя подзадачами
        System.out.println("удаляем эпик с тремя подзадачами" + "\n");
        taskManager.removeEpicById(epicWithSubTasks.getId());

        //Печатаем историю запросов в последний раз
        System.out.println("Печатаем историю запросов в последний раз" + "\n");
        taskManager.getEpicByID(epicWithoutSubTasks.getId());
        System.out.println(taskManager.getHistory());

        //выключаем программу
        TaskManager taskManagerAfterESC = FileBackedTasksManager.loadFromFile(Paths.get("src/sources/SaveData.csv").toFile());

        //после запуска программы печатаем историю запросов
        System.out.println("после запуска программы печатаем историю запросов");
        System.out.println(taskManagerAfterESC.getHistory());
    }
}
