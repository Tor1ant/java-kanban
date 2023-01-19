package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager();

        Task task1 = new Task("Погулять с собакой", "в 5 утра", Status.NEW);
        Task task2 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику", Status.NEW);
        Epic epicWithoutSubTasks = new Epic("Сделать ремонт в квартире", "Давно пора");
        Epic epicWithSubTasks = new Epic("Сходить в магазин", "Купить продукты");
        SubTask subtask1 = new SubTask("Купить молоко", "Зайти за молоком в пятерочку", Status.NEW, 4);
        SubTask subtask2 = new SubTask("Купить сахар", "это не обязательно", Status.NEW, 4);
        SubTask subTask3 = new SubTask("Купить вазелин", "Допустим", Status.NEW, 4);

        //создаем задачи, эпики и подзадачи
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epicWithoutSubTasks);
        taskManager.createEpic(epicWithSubTasks);
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subTask3);

        //Запрашиваем созданные задачи несколько раз в разном порядке
        taskManager.getTaskByID(task1.getId());
        taskManager.getTaskByID(task1.getId());
        taskManager.getEpicByID(epicWithoutSubTasks.getId());
        taskManager.getTaskByID(task1.getId());
        taskManager.getSubTaskById(subtask1.getId());
        taskManager.getEpicByID(epicWithSubTasks.getId());
        taskManager.getTaskByID(task2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getSubTaskById(subtask2.getId());

        // Печатаем историю запросов
        System.out.println("Печатаем историю запросов" + "\n");
        System.out.println(taskManager.getHistory());

        //делаем еще один вызов таски с id1 и эпика с id 4 и печатаем историю вызовов
        System.out.println("делаем еще один вызов таски с id1 и эпика с id 4 и печатаем историю вызовов" + "\n");
        taskManager.getTaskByID(task1.getId());
        taskManager.getEpicByID(epicWithSubTasks.getId());
        subtask1.setProgress(Status.DONE);
        taskManager.updateSubTask(subtask1);
        System.out.println(taskManager.getHistory());


        //удаляем сабтаску с id 5 и печатаем историю
        System.out.println("удаляем сабтаску с id 5 и печатаем историю" + "\n");
        taskManager.removeSubTaskById(subtask1.getId());
        System.out.println(taskManager.getHistory());

        //удаляем таску с id1 и печатаем историю
        System.out.println("удаляем таску с id1 и печатаем историю" + "\n");
        taskManager.removeTaskById(task1.getId());
        System.out.println(taskManager.getHistory());

        //удаляем эпик с тремя подзадачами
        System.out.println("удаляем эпик с тремя подзадачами" + "\n");
        taskManager.removeEpicById(epicWithSubTasks.getId());

        //Печатаем историю запросов в последний раз
        System.out.println("Печатаем историю запросов в последний раз" + "\n");
        taskManager.getEpicByID(epicWithoutSubTasks.getId());
        System.out.println(taskManager.getHistory());

        //выключаем программу
        TaskManager taskManagerAfterESC = FileBackedTasksManager.loadFromFile(Paths.get("SaveData.csv").toFile());

        //после запуска программы печатаем историю запросов
        System.out.println("после запуска программы печатаем историю запросов");
        System.out.println(taskManagerAfterESC.getHistory());
        Task task15 = new Task("Погулять с собакой", "в 5 утра", Status.NEW);
        Task task25 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику", Status.NEW);
        taskManagerAfterESC.createTask(task15);
        taskManagerAfterESC.createTask(task25);
        taskManagerAfterESC.getTaskByID(task15.getId());
        taskManagerAfterESC.getTaskByID(task25.getId());
        taskManagerAfterESC.getTaskByID(task15.getId());
        System.out.println(taskManagerAfterESC.getHistory());

    }
}
