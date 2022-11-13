package service;

import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        int task = manager.createTask(new Task("�������� � �������", "� 5 ����", "NEW"));
        int task2 = manager.createTask(new Task("������� �� #115 � ����������� ���������� ���", "� ������������", "NEW"));
        int epic = manager.createEpic(new Epic("������� ������ � ��������", "����� ����"));
        int epic2 = manager.createEpic(new Epic("������� � �������", "������ ��������"));
        int subtask1 = manager.createSubTask(new SubTask("������ ������ � �����", "������� ��������� �����", "NEW", 3));
        int subtask2 = manager.createSubTask(new SubTask("������ ������", "����� �� ������� � ���������", "NEW", 4));
        int subtask3 = manager.createSubTask(new SubTask("������ �����", "��� �� �����������", "NEW", 4));

        System.out.println(manager.getAllTasks());

        int updatedSubTask1 = manager.updateSubTask(new SubTask("������ ������������ �����", "������ ��������", "NEW", 4));
        int updatedSubTask2 = manager.updateSubTask(new SubTask("������ ������ � �����", "������� ��������� �����", "IN_PROGRESS", 3));

        System.out.println("������������� ��� ����� ����� ����������" + "\n");
        System.out.println(manager.getAllTasks());
        manager.removeSubTaskById(updatedSubTask2);

        System.out.println("������������� ��� ����� ����� �������� ����� ��������" + "\n");
        System.out.println(manager.getAllTasks());

        int subtask4 = manager.createSubTask(new SubTask("������ ��������", "���", "DONE", 3));
        System.out.println("�������� ���������� ����� ���������� ����� ��������" + "\n");
        System.out.println(manager.getAllTasks());
    }
}
