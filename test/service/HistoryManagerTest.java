package service;

import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class HistoryManagerTest {
    Task task = new Task("Выйти на улицу", "в 20:00");
    Task task2 = new Task("Изучить интеллектуальное право", "Раздел 'авторские права'");
    Task task3 = new Task("Выучить Английский язык", "до 2025 года");
    FileBackedTasksManager tasksManager = new FileBackedTasksManager();

    @DisplayName("Проверка добавления таски в историю вызовов")
    @Test
    void addTaskInHistoryWhenItCall() {
        tasksManager.createTask(task);
        tasksManager.getTaskByID(1);
        Assertions.assertEquals(1, tasksManager.getHistory().size(), "таска не добавлена в историю");
        Assertions.assertNotNull(tasksManager.getHistory(), "История пустая и не инициализирована");
    }

    @DisplayName("Проверка добавления одной и той же таски в историю")
    @Test
    void shouldHistorySize2WhenAddTheSameTaskToTheHistory() {
        tasksManager.createTask(task);
        tasksManager.createTask(task2);
        tasksManager.getTaskByID(1);
        tasksManager.getTaskByID(2);
        tasksManager.getTaskByID(1);
        List<Task> taskList = tasksManager.getHistory();
        Assertions.assertEquals(2, taskList.size(), "Размер истории не совпадает с нужным значением");
    }

    @DisplayName("Проверка удаления таски из истории вызовов при вызове этой же таски")
    @Test
    void removeTaskFromCallHistoryWhenItIsAlreadyInIt() {
        tasksManager.createTask(task);
        tasksManager.createTask(task2);
        tasksManager.getTaskByID(1);
        tasksManager.getTaskByID(2);
        tasksManager.getTaskByID(1);
        List<Task> taskList = tasksManager.getHistory();
        Task testTask = taskList.get(0);
        Assertions.assertEquals(task.toString(), testTask.toString());
    }

    @DisplayName("Проверка удаления таски из середины истории вызовов")
    @Test
    void shouldReturnTheStoryWithoutTheTaskThatWasInTheMiddle() {
        tasksManager.createTask(task);
        tasksManager.createTask(task2);
        tasksManager.createTask(task3);
        tasksManager.getTaskByID(1);
        tasksManager.getTaskByID(2);
        tasksManager.getTaskByID(3);
        tasksManager.removeTaskById(2);
        boolean taskInTheList = tasksManager.getHistory().contains(task2);
        Assertions.assertFalse(taskInTheList, "таска содержится в истории вызовов");
    }

    @DisplayName("Проверка получения истории вызовов тасок")
    @Test()
    void shouldReturnTasksListWhenGetHistoryCalled() {
        tasksManager.createTask(task);
        tasksManager.createTask(task2);
        tasksManager.getTaskByID(1);
        tasksManager.getTaskByID(2);
        List<Task> taskList = tasksManager.getHistory();
        Assertions.assertEquals(2, taskList.size(), "Размер истории не совпадает с нужным значением");
    }

    @DisplayName("1. Проверка получения пустой истории вызовов тасок." +
            "2. проверка невозможности удаления таски из пустой истории вызовов")
    @Test
    void shouldThrowsNullPointerEXPWhenGetHistoryCalled() {
        tasksManager.createTask(task);
        tasksManager.createTask(task2);
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, tasksManager::getHistory,
                "история вызовов не пустая");
        Assertions.assertNull(exception.getMessage());
    }
}
