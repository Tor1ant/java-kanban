package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class TaskManagerTest {
    FileBackedTasksManager taskManager = new FileBackedTasksManager("SaveData.csv");
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    public void createTasks() {
        task = new Task("Выгулять собаку", "ПЛАТНО", Status.NEW, 60, LocalDateTime.now());
        epic = new Epic("Сходить в магазин", "на это есть пол часа");
        subTask = new SubTask("Выкинуть мусор", "перед магазином", Status.NEW, 1, 120,
                LocalDateTime.now().plusMinutes(90));
    }

    @Test
    void shouldTaskManagerTasksReturnSize1WhenCreateTask() {
        taskManager.createTask(task);
        taskManager.getTaskByID(1).setDescription("Testing");
        List<Task> taskList = taskManager.getAllTasks();
        boolean isEqualsTasks = taskList.get(0).equals(task);
        Assertions.assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач");
        Assertions.assertEquals("Testing", taskManager.getTaskByID(1).getDescription());
        Assertions.assertNotNull(taskManager.getTaskByID(1), "Задача не найдена");
        Assertions.assertEquals(task, taskManager.getTaskByID(1), "Задачи не совпадают");
        Assertions.assertTrue(isEqualsTasks);
    }

    @Test
    void shouldTaskManagerEpicsReturnSize1WhenCreateEpic() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getStatus().toString());
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
        Assertions.assertFalse(taskManager.getAllEpics().isEmpty());
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getEpicByID(2));
        assertNull(nullPointerException.getMessage(), "эпик с не правильным номером");
    }

    @Test
    void shouldTaskManagerSubTasksReturnSize1WhenAddSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        subTask.setEpicId(1);
        Assertions.assertEquals("NEW", epic.getStatus().toString());
        Assertions.assertEquals(1, subTask.getEpicId());
        Assertions.assertEquals(1, taskManager.getAllSubTasks().size());
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getSubTaskById(3));
        assertNull(nullPointerException.getMessage(), "сабтаска" +
                " с не правильным номером");
    }

    @Test
    void shouldRemoveTaskWithoutStartTimeInPrioritizedTasks() {
        Task task2 = new Task("Тестовая задача", "ПЛАТНО", Status.NEW, 60, LocalDateTime.now());
        Task task3 = new Task("Тестовая задача Без времени", "ПЛАТНО", Status.NEW, 60,
                null);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        Task task4 = new Task("Тестовая задача Без времени АПДЕЙТНУТАЯ", "ПЛАТНО", Status.NEW, 60,
                null);
        task4.setId(task3.getId());
        taskManager.updateTask(task4);
        Assertions.assertEquals(2, taskManager.getPrioritizedTasks().size());

    }

    @Test
    void ShouldThrowNullPointerEXPWhenGetTaskWithWrongId() {
        taskManager.createTask(task);
        taskManager.getTaskByID(1).setDescription("Testing");
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getTaskByID(2));
        assertNull(nullPointerException.getMessage(), "таска с не правильным номером");

    }

    @Test
    void ShouldEpicsHaveUpdatedEpic() {
        taskManager.createEpic(epic);
        Epic epic1 = new Epic("новый заголовок", "обновление эпика");
        epic1.setId(1);
        taskManager.updateEpic(epic1);
        Assertions.assertEquals("NEW", epic.getStatus().toString());
        Assertions.assertEquals("новый заголовок", taskManager.getEpicByID(1).getTitle());
    }

    @Test
    void ShouldThrowNullPointerEXPWhenGetEpicWithWrongId() {
        taskManager.createEpic(epic);
        taskManager.getEpicByID(1).setDescription("Testing");
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getEpicByID(2), "номер совпадает с ожидаемым");
        assertNull(nullPointerException.getMessage());
    }


    @Test
    void ShouldSubtasksHaveUpdatedSubTask() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
        SubTask subTask1 = new SubTask("новый заголовок", "...", Status.NEW, 1, 60,
                LocalDateTime.now());
        subTask1.setId(2);
        taskManager.updateSubTask(subTask1);
        Assertions.assertEquals("новый заголовок", taskManager.getSubTaskById(2).getTitle());
    }

    @Test
    void ShouldThrowNullPointerEXPWhenGetSubtaskWithWrongId() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.getSubTaskById(2).setDescription("Testing");
        NullPointerException nullPointerException = Assertions.assertThrows(NullPointerException.class,
                () -> taskManager.getSubTaskById(1), "номер совпадает с ожидаемым");
        assertNull(nullPointerException.getMessage());
    }


    @Test
    void ShouldReturn1WhenGetAllTasks() {
        taskManager.createTask(task);
        Assertions.assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void ShouldReturnTrueWhenGetAllTasks() {
        List<Task> taskList = taskManager.getAllTasks();
        Assertions.assertTrue(taskList.isEmpty(), "список задач не пуст");
    }

    @Test
    void ShouldReturn1WhenGetAllEpics() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getStatus().toString());
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void ShouldReturnTrueWhenGetAllEpics() {
        ArrayList<Epic> taskList = taskManager.getAllEpics();
        Assertions.assertTrue(taskList.isEmpty(), "список эпиков не пуст");
    }

    @Test
    void ShouldReturn1WhenGetAllSubTasks() {
        taskManager.createEpic(epic);
        taskManager.addSubTask(subTask);
        Assertions.assertEquals(1, subTask.getEpicId());
        Assertions.assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    void ShouldReturnTrueWhenGetAllSubtasks() {
        ArrayList<SubTask> taskList = taskManager.getAllSubTasks();
        Assertions.assertTrue(taskList.isEmpty(), "список сабтасок не пуст");
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
    void shouldReturnTrueWhenRemoveAllTasksWhenTasksEmpty() {
        boolean isEmpty = taskManager.getAllTasks().isEmpty();
        Assertions.assertTrue(isEmpty);
        taskManager.removeAllTasks();
        isEmpty = taskManager.getAllTasks().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    void shouldReturnTrueWhenRemoveAllEpics() {
        taskManager.createEpic(epic);
        Assertions.assertEquals("NEW", epic.getStatus().toString());
        boolean isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertFalse(isEmpty);
        taskManager.removeAllEpics();
        isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    void shouldReturnTrueWhenRemoveAllEpicsWhenEpicsEmpty() {
        boolean isEmpty = taskManager.getAllEpics().isEmpty();
        Assertions.assertTrue(isEmpty);
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
    void shouldReturnTrueWhenRemoveAllSubTasksWhenSubtasksEmpty() {
        boolean isEmpty = taskManager.getAllSubTasks().isEmpty();
        Assertions.assertTrue(isEmpty);
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
        Assertions.assertEquals("NEW", epic.getStatus().toString());
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
        Assertions.assertEquals("NEW", epic.getStatus().toString());
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
        Assertions.assertEquals("NEW", epic.getStatus().toString());
        taskManager.addSubTask(subTask);
        taskManager.getSubTaskById(2).setStatus(Status.DONE);
        taskManager.updateSubTask(taskManager.getSubTaskById(2));
        Assertions.assertEquals("DONE", epic.getStatus().toString());
    }

    @Test
    void shouldReturnTaskListSize3WhenCallGetHistory() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        taskManager.addSubTask(subTask);
        taskManager.getEpicByID(1);
        taskManager.getTaskByID(2);
        taskManager.getSubTaskById(3);
        List<Task> taskList = taskManager.getHistory();
        Assertions.assertEquals(3, taskList.size(), "История пустая");
    }

    @Test
    void ShouldTasksEqualsWhenHaveUpdatedTask() {
        taskManager.createTask(task);
        Task task1 = new Task("новый заголовок", "обновленная задачи", Status.DONE, task.getDuration(),
                task.getStartTime().get());
        task1.setId(1);
        taskManager.updateTask(task1);
        Assertions.assertEquals("новый заголовок", taskManager.getTaskByID(1).getTitle());
    }

    @Test
    void shouldReturnEqualsWhenGetPrioritizedTasks() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        taskManager.addSubTask(subTask);
        taskManager.createTask(new Task("ТЕСТ", "TEST", Status.NEW, 30, LocalDateTime.now().
                plusMinutes(250)));
        taskManager.addSubTask(new SubTask("SUBTASKTEST", "TESTING", Status.NEW, subTask.getEpicId(),
                60, LocalDateTime.now().plusMinutes(300)));
        taskManager.createEpic(new Epic("TESTEPIC", "..."));
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile("SaveData.csv");
        List<Task> prioritizedTasksAfterESC = fileBackedTasksManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(prioritizedTasks.toArray(), prioritizedTasksAfterESC.toArray());
    }

    @Test
    void shouldTrueWhenGetPrioritizedTasksIsEmpty() {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        taskManager.addSubTask(subTask);
        taskManager.createTask(new Task("ТЕСТ", "TEST", Status.NEW, 30, LocalDateTime.now().
                plusMinutes(250)));
        taskManager.addSubTask(new SubTask("SUBTASKTEST", "TESTING", Status.NEW, subTask.getEpicId(),
                60, LocalDateTime.now().plusMinutes(300)));
        taskManager.createEpic(new Epic("TESTEPIC", "..."));
        taskManager.removeSubTaskById(3);
        taskManager.removeSubTaskById(5);
        taskManager.removeEpicById(1);
        taskManager.removeTaskById(2);
        taskManager.removeTaskById(4);
        taskManager.removeEpicById(6);
        Assertions.assertTrue(prioritizedTasks.isEmpty());
    }

    @Test
    void shouldThrowValidateExceptionWhenAddTaskInTimeAnotherTask() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        taskManager.addSubTask(subTask);
        Task newTask = new Task("ТЕСТ", "TEST", Status.NEW, 120, task.getStartTime().get());
        ValidateException validateException = Assertions.assertThrows(ValidateException.class, () -> taskManager.
                createTask(newTask));
        Assertions.assertEquals(ValidateException.class, validateException.getClass());
    }
}