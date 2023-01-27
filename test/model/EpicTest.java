package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.Status;

import java.time.LocalDateTime;

class EpicTest {
    Epic epic;
    FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    public void createFileBackedTasksManager() {
        fileBackedTasksManager = new FileBackedTasksManager("SaveData.csv");
    }

    @BeforeEach
    public void createNewEpic() {
        fileBackedTasksManager.createEpic(epic = new Epic("Сходить в кино", "не покупать попкорн"));
    }

    @Test
    public void shouldEpicStatusIsNewWhenSubTasksIdIsEmpty() {
        Assertions.assertEquals(Status.NEW.toString(), String.valueOf(epic.getStatus()));
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
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.NEW, 1, 60,
                LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.NEW, 1,
                60, LocalDateTime.now().plusMinutes(70));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.NEW.toString(), String.valueOf(epic.getStatus()));
    }

    @Test
    public void shouldEpicStatusIsDoneWhenAllSubTasksInSabTasksIdIsDone() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.DONE, 1, 60,
                LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.DONE, 1,
                60, LocalDateTime.now().plusMinutes(70));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.DONE.toString(), String.valueOf(epic.getStatus()));
    }

    @Test
    public void shouldEpicStatusIsInProgressWhenAllSubTasksInSabTasksIdIsNewAndDone() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.DONE, 1, 60,
                LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.NEW, 1,
                60, LocalDateTime.now().plusMinutes(70));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.IN_PROGRESS.toString(), String.valueOf(epic.getStatus()));
    }

    @Test
    public void shouldEpicStatusIsInProgressWhenAllSubTasksInSabTasksIdIsInProgress() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.IN_PROGRESS, 1,
                30, LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.IN_PROGRESS,
                1, 60, LocalDateTime.now().plusMinutes(31));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(Status.IN_PROGRESS.toString(), String.valueOf(epic.getStatus()));
    }

    @Test
    public void shouldEpicStartTimeAndEndTimeEqualsSubtaskStartTimeAndEndTime() {
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.IN_PROGRESS, 1,
                30, LocalDateTime.now());
        fileBackedTasksManager.addSubTask(subTask);
        Assertions.assertEquals(subTask.startTime, epic.startTime);
        Assertions.assertEquals(subTask.getEndTime(), epic.getEndTime());
    }

    @Test
    public void ShouldEpicStartTimeIsNullIfEpicDontHaveSubtasks() {
        Assertions.assertNull(epic.getStartTime());
    }

    @Test
    public void shouldAssertEqualsTrueIfStartTimeAndEndTimeEpicEqualsWithSubtasks() {
        epic.setTitle("Сходить в IMAX кино");
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.IN_PROGRESS, 1,
                30, LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.IN_PROGRESS,
                1, 60, LocalDateTime.now().plusMinutes(31));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(subTask.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(subTask2.getEndTime(), epic.getEndTime());
    }

    @Test
    public void shouldTheDurationOfEpicBe90() {
        epic.setTitle("Сходить в IMAX кино");
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.IN_PROGRESS, 1,
                30, LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.IN_PROGRESS,
                1, 60, LocalDateTime.now().plusMinutes(31));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        Assertions.assertEquals(subTask.getDuration() + subTask2.getDuration(), epic.getDuration());
    }

    @Test
    public void shouldEpicDurationIsNullWHenEpicDontHaveSubtasks() {
        epic.setTitle("Сходить в IMAX кино");
        SubTask subTask = new SubTask("Купить билеты", "На фильм", Status.IN_PROGRESS, 1,
                30, LocalDateTime.now());
        SubTask subTask2 = new SubTask("Купить сладкую вату", "без описания", Status.IN_PROGRESS,
                1, 60, LocalDateTime.now().plusMinutes(31));
        fileBackedTasksManager.addSubTask(subTask);
        fileBackedTasksManager.addSubTask(subTask2);
        fileBackedTasksManager.removeAllSubTasks();
        Assertions.assertNull(epic.getDuration());
    }
}