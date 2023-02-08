package server;

import model.Epic;
import model.SubTask;
import model.Task;

import org.junit.jupiter.api.*;
import service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {
    HttpTaskManager httpTaskManager;
    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    @BeforeEach
    public void startServerAndCreateTasks() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer(httpTaskManager = Managers.getDefaultHttpTaskManager());
        httpTaskServer.startServer();

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
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task2);
        httpTaskManager.createEpic(epicWithoutSubTasks);
        httpTaskManager.createEpic(epicWithSubTasks);
        httpTaskManager.addSubTask(subtask1);
        httpTaskManager.addSubTask(subtask2);
        httpTaskManager.addSubTask(subTask3);
        httpTaskManager.getTaskByID(task1.getId());
        httpTaskManager.getTaskByID(task1.getId());
        httpTaskManager.getEpicByID(epicWithoutSubTasks.getId());
        httpTaskManager.getTaskByID(task1.getId());
        httpTaskManager.getSubTaskById(subtask1.getId());
        httpTaskManager.getEpicByID(epicWithSubTasks.getId());
        httpTaskManager.getTaskByID(task2.getId());
        httpTaskManager.getSubTaskById(subTask3.getId());
        httpTaskManager.getSubTaskById(subtask2.getId());
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
        httpTaskServer.stopServer();
    }

    @DisplayName("Проверка загрузки тасок c сервера после сохранения")
    @Test
    void loadFromFileTasksEpicsAndSubtasksTest() {
        TaskManager taskManagerAfterESC = HttpTaskManager.loadFromServer("http://localhost:8078");
        Assertions.assertEquals(HttpTaskManager.class, taskManagerAfterESC.getClass(), "Таскменеджер " +
                "не загружен");
        int tasksSIze = taskManagerAfterESC.getAllTasks().size();
        int epicSIze = taskManagerAfterESC.getAllEpics().size();
        int subTasksSIze = taskManagerAfterESC.getAllSubTasks().size();
        Assertions.assertEquals(2, tasksSIze, "неверное количество тасок");
        Assertions.assertEquals(2, epicSIze, "неверное количество эпиков");
        Assertions.assertEquals(3, subTasksSIze, "неверное количество сабтасок");
    }

    @DisplayName("Проверка загрузки эпика без сабтасок с сервера после сохранения")
    @Test
    void loadFromFileEpicWithoutSubTasksTest() {
        httpTaskManager.removeAllTasks();
        httpTaskManager.removeEpicById(4);
        TaskManager taskManagerAfterESC = HttpTaskManager.loadFromServer("http://localhost:8078");
        List<Task> taskList = taskManagerAfterESC.getHistory();
        Assertions.assertEquals(1, taskList.size(), "История содержит больше одного элемента");
    }

    @DisplayName("Проверка загрузки и вызова пустой истории c сервера после ее сохранения")
    @Test
    void loadFromFileEmptyHistory() {
        httpTaskManager.removeAllTasks();
        httpTaskManager.removeAllEpics();
        httpTaskManager.removeAllSubTasks();
        Task task = new Task("Встретить новый год", "...", Status.NEW, 60, LocalDateTime.now());
        httpTaskManager.createTask(task);
        TaskManager taskManagerAfterESC = HttpTaskManager.loadFromServer("http://localhost:8078");
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                taskManagerAfterESC::getHistory);
        Assertions.assertNull(nullPointerException.getMessage(), "История не пустая");
    }
}
