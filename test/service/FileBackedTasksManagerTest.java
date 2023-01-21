package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class FileBackedTasksManagerTest {
    static TaskManager taskManager = new FileBackedTasksManager();

    @BeforeAll
    static void createTasksAndCallThem() {
        Task task1 = new Task("Погулять с собакой", "в 5 утра", Status.NEW);
        Task task2 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику", Status.NEW);
        Epic epicWithoutSubTasks = new Epic("Сделать ремонт в квартире", "Давно пора");
        Epic epicWithSubTasks = new Epic("Сходить в магазин", "Купить продукты");
        SubTask subtask1 = new SubTask("Купить молоко", "Зайти за молоком в пятерочку", Status.NEW, 4);
        SubTask subtask2 = new SubTask("Купить сахар", "это не обязательно", Status.NEW, 4);
        SubTask subTask3 = new SubTask("Купить вазелин", "Допустим", Status.NEW, 4);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epicWithoutSubTasks);
        taskManager.createEpic(epicWithSubTasks);
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subTask3);

        taskManager.getTaskByID(task1.getId());
        taskManager.getTaskByID(task1.getId());
        taskManager.getEpicByID(epicWithoutSubTasks.getId());
        taskManager.getTaskByID(task1.getId());
        taskManager.getSubTaskById(subtask1.getId());
        taskManager.getEpicByID(epicWithSubTasks.getId());
        taskManager.getTaskByID(task2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getSubTaskById(subtask2.getId());

    }

    @DisplayName("Проверка загрузки тасок из файла")
    @Test
    void loadFromFileTasksEpicsAndSubtasksTest() {
        TaskManager taskManagerAfterESC = FileBackedTasksManager.loadFromFile(Paths.get("SaveData.csv").toFile());
        Assertions.assertEquals(FileBackedTasksManager.class, taskManagerAfterESC.getClass(), "Таскменеджер " +
                "не загружен");
        int tasksSIze = taskManager.getAllTasks().size();
        int epicSIze = taskManager.getAllEpics().size();
        int subTasksSIze = taskManager.getAllSubTasks().size();
        Assertions.assertEquals(2, tasksSIze, "неверное количество тасок");
        Assertions.assertEquals(2, epicSIze, "неверное количество эпиков");
        Assertions.assertEquals(3, subTasksSIze, "неверное количество сабтасок");
    }
}
