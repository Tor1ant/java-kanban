package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskManagerTest {

    TaskManager taskManager = Managers.getDefault();
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    public void createTasks() {
        task = new Task("Выгулять собаку", "ПЛАТНО", Status.NEW);
        epic = new Epic("Сходить в магазин", "на это есть пол часа");
        subTask = new SubTask("Выкинуть мусор", "перед магазином", Status.NEW, 1);
    }


    @Test
    void shouldTaskManagerTasksReturnSize1WhenCreateTask() {
        taskManager.createTask(task);
        Assertions.assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void shouldTaskManagerEpicsReturnSize1WhenCreateEpic() {
        taskManager.createEpic(epic);
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void shouldTaskManagerSubTasksReturnSize1WhenAddSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    void ShouldTasksHaveUpdatedTask() {
        taskManager.createTask(task);
        task.setTitle("новый заголовок");
        taskManager.updateTask(task);
        Assertions.assertEquals("новый заголовок", taskManager.getTaskByID(1).getTitle());
    }

    @Test
    void ShouldEpicsHaveUpdatedEpic() {
        taskManager.createEpic(epic);
        epic.setTitle("новый заголовок");
        taskManager.updateEpic(epic);
        Assertions.assertEquals("новый заголовок", taskManager.getEpicByID(1).getTitle());
    }

    @Test
    void ShouldSubtasksHaveUpdatedSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        subTask.setTitle("новый заголовок");
        taskManager.updateSubTask(subTask);
        Assertions.assertEquals("новый заголовок", taskManager.getSubTaskById(2).getTitle());
    }

    @Test
    void ShouldReturn1WhenGetAllTasks() {
        taskManager.createTask(task);
        Assertions.assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void ShouldReturn1WhenGetAllEpics() {
        taskManager.createEpic(epic);
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void ShouldReturn1WhenGetAllSubTasks() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    void shouldReturnTrueWhenRemoveAllTasks() {
        taskManager.createTask(task);
        boolean isEmpty = taskManager.getAllTasks().isEmpty();
        Assertions.assertFalse(isEmpty);
        taskManager.removeAllTasks();
        isEmpty = taskManager.getAllTasks().isEmpty();
        Assertions.assertTrue(isEmpty);

    }

    @Test
    void shouldReturnTrueWhenRemoveAllEpics() {
        taskManager.createEpic(epic);
        boolean isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertFalse(isEmpty);
        taskManager.removeAllEpics();
        isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    void removeAllSubTasks() {
    }

    @Test
    void getTaskByID() {
    }

    @Test
    void getEpicByID() {
    }

    @Test
    void getSubTaskById() {
    }

    @Test
    void removeTaskById() {
    }

    @Test
    void removeEpicById() {
    }

    @Test
    void removeSubTaskById() {
    }

    @Test
    void getListOfEpicsSubTasks() {
    }

    @Test
    void changeEpicProgress() {
    }

    @Test
    void getHistory() {
    }
}