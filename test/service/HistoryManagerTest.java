package service;

import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HistoryManagerTest {
    Task task = new Task("Выйти на улицу", "в 20:00");
    FileBackedTasksManager tasksManager = new FileBackedTasksManager();

    @DisplayName("Проверка добавления таски в историю вызовов")
    @Test
    void addTaskInHistoryWhenItCall() {
        tasksManager.createTask(task);
        tasksManager.getTaskByID(1);
        Assertions.assertEquals(1, tasksManager.getHistory().size(), "таска не добавлена в историю");
        Assertions.assertNotNull(tasksManager.getHistory(), "История пустая и не инициализирована");
    }

    @DisplayName("Проверка удаления таски из истории вызовов при вызове этой же таски")
    @Test
    void removeTaskFromCallHistoryWhenItIsAlreadyInIt() {
        Task task2 = new Task("Изучить интеллектуальное право", "Раздел 'авторские права'");
        tasksManager.createTask(task);
        tasksManager.createTask(task2);
        tasksManager.getTaskByID(1);


    }

    @Test
    void getHistory() {
    }
}