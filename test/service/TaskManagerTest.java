package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


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
        taskManager.getTaskByID(1).setDescription("Testing");
        List<Task> taskList = taskManager.getAllTasks();
        boolean isEqualsTasks = taskList.get(0).equals(task);

        Assertions.assertEquals(1, taskManager.getAllTasks().size(),"Неверное количество задач");
        Assertions.assertEquals("Testing", taskManager.getTaskByID(1).getDescription());
        Assertions.assertNotNull(taskManager.getAllTasks(), "Список задач пуст");
        Assertions.assertNotNull(taskManager.getTaskByID(1), "Задача не найдена");
        Assertions.assertEquals(task, taskManager.getTaskByID(1), "Задачи не совпадают");
        Assertions.assertTrue(isEqualsTasks);

    }


    @Test
    void shouldTaskManagerEpicsReturnSize1WhenCreateEpic() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
        //начать отсюда
    }

    @Test
    void shouldTaskManagerSubTasksReturnSize1WhenAddSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        Assertions.assertEquals(1, subTask.getEpicId());
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
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        Assertions.assertEquals("новый заголовок", taskManager.getEpicByID(1).getTitle());
    }

    @Test
    void ShouldSubtasksHaveUpdatedSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
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
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void ShouldReturn1WhenGetAllSubTasks() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
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
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        boolean isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertFalse(isEmpty);
        taskManager.removeAllEpics();
        isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    void shouldReturnTrueWhenRemoveAllSubTasks() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
        boolean isEmpty = taskManager.getAllSubTasks().isEmpty();
        Assertions.assertFalse(isEmpty);
        taskManager.removeAllSubTasks();
        isEmpty = taskManager.getAllSubTasks().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    void shouldReturnTaskTitleWhenGetTaskByID() {
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getTaskByID(1));
        Assertions.assertEquals("Выгулять собаку", taskManager.getTaskByID(1).getTitle());
    }

    @Test
    void shouldReturnEpicTitleWhenGetEpicByID() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        Assertions.assertNotNull(taskManager.getEpicByID(1));
        Assertions.assertEquals("Сходить в магазин", taskManager.getEpicByID(1).getTitle());
    }

    @Test
    void shouldReturnSubTaskTitleWhenGetSubTaskById() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
        Assertions.assertNotNull(taskManager.getSubTaskById(2));
        Assertions.assertEquals("Выкинуть мусор", taskManager.getSubTaskById(2).getTitle());

    }

    @Test
    void shouldReturnTrueWhenRemoveTaskById() {
        taskManager.createTask(task);
        Assertions.assertEquals(1, taskManager.getAllTasks().size());
        taskManager.removeTaskById(task.getId());
        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void shouldReturnTrueWhenRemoveEpicById() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
        taskManager.removeEpicById(epic.getId());
        Assertions.assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    void shouldReturnTrueWhenRemoveSubTaskById() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
        Assertions.assertEquals(1, taskManager.getAllSubTasks().size());
        taskManager.removeSubTaskById(subTask.getId());
        Assertions.assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    void shouldReturn1WhenGetListOfEpicsSubTasks() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertFalse(epic.getSubTasksId().isEmpty(), "У эпика нет  сабтасок");
        Assertions.assertEquals(1, taskManager.getListOfEpicsSubTasks(1).size());
    }

    @Test
    void shouldChangeEpicProgress() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getProgress().toString());
        taskManager.addSubTask(subTask);
        taskManager.getSubTaskById(2).setProgress(Status.DONE);
        taskManager.updateSubTask(taskManager.getSubTaskById(2));
        Assertions.assertEquals("DONE", epic.getProgress().toString());
    }

    @Test
    void getHistory() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        taskManager.addSubTask(subTask);
        taskManager.getEpicByID(1);
        taskManager.getTaskByID(2);
        taskManager.getSubTaskById(3);
        List<Task> taskList = taskManager.getHistory();
        Assertions.assertEquals(3, taskList.size(), "История пустая");
    }
}