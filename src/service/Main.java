package service;

import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        int task = manager.createTask(new Task("Погулять с собакой", "в 5 утра", "NEW"));
        int task2 = manager.createTask(new Task("изучить ФЗ #115 о Банкротстве физических лиц", "К понедельнику", "NEW"));
        int epic = manager.createEpic(new Epic("Сделать ремонт в квартире", "Давно пора"));
        int epic2 = manager.createEpic(new Epic("Сходить в магазин", "Купить продукты"));
        int subtask1 = manager.createSubTask(new SubTask("Начать ремонт с кухни", "Сначала выровнять стены", "NEW", 3));
        int subtask2 = manager.createSubTask(new SubTask("Купить молоко", "Зайти за молоком в пятерочку", "NEW", 4));
        int subtask3 = manager.createSubTask(new SubTask("Купить сахар", "это не обязательно", "NEW", 4));

        System.out.println(manager.getAllTasks());

        int updatedSubTask1 = manager.updateSubTask(new SubTask("Купить тростниковый сахар", "Вместо обычного", "NEW", 4));
        int updatedSubTask2 = manager.updateSubTask(new SubTask("Начать ремонт с кухни", "Сначала выровнять стены", "IN_PROGRESS", 3));

        System.out.println("Распечатываем все таски после обновления" + "\n");
        System.out.println(manager.getAllTasks());
        manager.removeSubTaskById(updatedSubTask2);

        System.out.println("распечатываем все таски после удаления одной сабтаски" + "\n");
        System.out.println(manager.getAllTasks());

        int subtask4 = manager.createSubTask(new SubTask("Ремонт завершен", "Ура", "DONE", 3));
        System.out.println("Печатаем результаты после добавления новой сабтаски" + "\n");
        System.out.println(manager.getAllTasks());
    }
}
