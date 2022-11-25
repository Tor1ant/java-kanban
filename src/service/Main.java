package service;

import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Погулять с собакой", "в 5 утра", "NEW");
        Task task2 = new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику", "NEW");
        Epic epic = new Epic("Сделать ремонт в квартире", "Давно пора");
        Epic epic2 = new Epic("Сходить в магазин", "Купить продукты");
        SubTask subtask1 = new SubTask("Начать ремонт с кухни", "Сначала выровнять стены", "NEW", 3);
        SubTask subtask2 = new SubTask("Купить молоко", "Зайти за молоком в пятерочку", "NEW", 4);
        SubTask subtask3 = new SubTask("Купить сахар", "это не обязательно", "NEW", 4);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic);
        manager.createEpic(epic2);

        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        manager.addSubTask(subtask3);

        manager.getEpicByID(3);
        manager.getEpicByID(4);
        manager.getTaskByID(1);
        // Печатаем все таски после создания

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        System.out.println("печатаем историю просмотров");
        System.out.println(manager.getHistory());

        // обновляем таски, эпики и сабтаски

        subtask3.setTitle("Купить тростниковый сахар");
        subtask3.setDescription("Вместо обычного");
        subtask3.setProgress("NEW");
        subtask3.setEpicId(4);
        manager.updateSubTask(subtask3);
        subtask1.setProgress("IN_PROGRESS");
        manager.updateSubTask(subtask1);
        task1.setProgress("DONE");
        task2.setProgress("IN_PROGRESS");
        manager.updateTask(task1);
        manager.updateTask(task2);

        System.out.println("Распечатываем все таски после обновления" + "\n");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        manager.removeSubTaskById(subtask1.getId());

        System.out.println("распечатываем все таски после удаления одной сабтаски" + "\n");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        SubTask subTask5 = (new SubTask("Ремонт завершен", "Ура", "DONE", 3));
        manager.addSubTask(subTask5);
        System.out.println("Печатаем результаты после добавления новой сабтаски" + "\n");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        System.out.println("меняем эпик и выводим результат на экран" + "\n");
        epic.setTitle("Ремонт нужно сделать повторно");
        manager.updateEpic(epic);
        manager.removeSubTaskById(subTask5.getId());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());


    }
}
