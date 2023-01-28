package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTasksManagerTest {
    TaskManager taskManager = new FileBackedTasksManager();

    @BeforeEach
    void createTasksEpicsSubtasksAndCallThem() {
        Task task1 = new Task("Погулять с собакой", "в 5 утра", Status.NEW, 60,
                LocalDateTime.now());
        Task task2 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику",
                Status.NEW, 60, LocalDateTime.now().minusMinutes(300));
        Epic epicWithoutSubTasks = new Epic("Сделать ремонт в квартире", "Давно пора");
        Epic epicWithSubTasks = new Epic("Сходить в магазин", "Купить продукты");
        SubTask subtask1 = new SubTask("Купить молоко", "Зайти за молоком в пятерочку", Status.NEW,
                4, 20, LocalDateTime.now().plusMinutes(500));
        SubTask subtask2 = new SubTask("Купить сахар", "это не обязательно", Status.NEW, 4,
                60, LocalDateTime.now().plusMinutes(350));
        SubTask subTask3 = new SubTask("Купить вазелин", "Допустим", Status.NEW, 4,
                5, LocalDateTime.now().plusMinutes(450));
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

    @DisplayName("Проверка загрузки тасок из файла после сохранения")
    @Test
    void loadFromFileTasksEpicsAndSubtasksTest() {
        TaskManager taskManagerAfterESC = FileBackedTasksManager.loadFromFile();
        Assertions.assertEquals(FileBackedTasksManager.class, taskManagerAfterESC.getClass(), "Таскменеджер " +
                "не загружен");
        int tasksSIze = taskManager.getAllTasks().size();
        int epicSIze = taskManager.getAllEpics().size();
        int subTasksSIze = taskManager.getAllSubTasks().size();
        Assertions.assertEquals(2, tasksSIze, "неверное количество тасок");
        Assertions.assertEquals(2, epicSIze, "неверное количество эпиков");
        Assertions.assertEquals(3, subTasksSIze, "неверное количество сабтасок");
    }

    @DisplayName("Проверка загрузки эпика без сабтасок из файла после сохранения")
    @Test
    void loadFromFileEpicWithoutSubTasksTest() {
        taskManager.removeAllTasks();
        taskManager.removeEpicById(4);
        TaskManager taskManagerAfterESC = FileBackedTasksManager.loadFromFile();
        List<Task> taskList = taskManagerAfterESC.getHistory();
        Assertions.assertEquals(1, taskList.size(), "История содержит больше одного элемента");
    }

    @DisplayName("Проверка загрузки и вызова пустой истории после ее сохранения")
    @Test
    void loadFromFileEmptyHistory() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubTasks();
        Task task = new Task("Встретить новый год", "...", Status.NEW, 60, LocalDateTime.now());
        taskManager.createTask(task);
        TaskManager taskManagerAfterESC = FileBackedTasksManager.loadFromFile();
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                taskManagerAfterESC::getHistory);
        Assertions.assertNull(nullPointerException.getMessage(), "История не пустая");
    }
}