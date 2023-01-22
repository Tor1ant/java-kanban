package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.Status;

class EpicTest {
    Epic epic;
    FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    public void createFileBackedTasksManager() {
        fileBackedTasksManager = new FileBackedTasksManager();
    }

    @BeforeEach
    public void createNewEpic() {
        fileBackedTasksManager.createEpic(epic = new Epic("Сходить в кино", "не покупать попкорн"));
    }

    @Test
    public void shouldEpicStatusIsNewWhenSubTasksIdIsEmpty() {
        Assertions.assertEquals(Status.NEW.toString(), String.valueOf(epic.getProgress()));
    }

    @Test
    public void shouldTrueWhenCallEquals() {
        Epic epic = new Epic("Сходить в кино", "не покупать попкорн");
        fileBackedTasksManager.createEpic(epic);
        boolean isEqualEpics = epic.equals(new Epic("Сходить в кино", "не покупать попкорн"));
        Assertions.assertTrue(isEqualEpics);
    }

    @Test
    public void shouldEpicStatusIsNewWhenAllSubTasksInSabTasksIdIsNew() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.NEW, 1);
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.NEW, 1);
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.NEW.toString(), String.valueOf(epic.getProgress()));
    }

    @Test
    public void shouldEpicStatusIsDoneWhenAllSubTasksInSabTasksIdIsDone() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.DONE, 1);
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.DONE, 1);
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.DONE.toString(), String.valueOf(epic.getProgress()));
    }

    @Test
    public void shouldEpicStatusIsInProgressWhenAllSubTasksInSabTasksIdIsNewAndDone() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.DONE, 1);
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.NEW, 1);
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.IN_PROGRESS.toString(), String.valueOf(epic.getProgress()));
    }

    @Test
    public void shouldEpicStatusIsInProgressWhenAllSubTasksInSabTasksIdIsInProgress() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.IN_PROGRESS, 1);
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.IN_PROGRESS, 1);
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.IN_PROGRESS.toString(), String.valueOf(epic.getProgress()));
    }
}