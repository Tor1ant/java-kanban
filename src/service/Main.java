package service;

import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("�������� � �������", "� 5 ����", "NEW");
        Task task2 = new Task("������� �� #115 � ����������� ���������� ���", "� ������������", "NEW");
        Epic epic = new Epic("������� ������ � ��������", "����� ����");
        Epic epic2 = new Epic("������� � �������", "������ ��������");
        SubTask subtask1 = new SubTask("������ ������ � �����", "������� ��������� �����", "NEW", 3);
        SubTask subtask2 = new SubTask("������ ������", "����� �� ������� � ���������", "NEW", 4);
        SubTask subtask3 = new SubTask("������ �����", "��� �� �����������", "NEW", 4);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic);
        manager.createEpic(epic2);
        int subTaskId1 = manager.addSubTask(subtask1);
        int subTaskId2 = manager.addSubTask(subtask2);
        int subTaskId3 = manager.addSubTask(subtask3);

        // �������� ��� ����� ����� ��������

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        // ��������� �����, ����� � ��������

        subtask3.setTitle("������ ������������ �����");
        subtask3.setDescription("������ ��������");
        subtask3.setProgress("NEW");
        subtask3.setEpicId(4);
        manager.updateSubTask(subtask3);
        subtask1.setProgress("IN_PROGRESS");
        manager.updateSubTask(subtask1);
        task1.setProgress("DONE");
        task2.setProgress("IN_PROGRESS");
        manager.updateTask(task1);
        manager.updateTask(task2);

        System.out.println("������������� ��� ����� ����� ����������" + "\n");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        manager.removeSubTaskById(subTaskId1);

        System.out.println("������������� ��� ����� ����� �������� ����� ��������" + "\n");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        manager.addSubTask(new SubTask("������ ��������", "���", "DONE", 3));
        System.out.println("�������� ���������� ����� ���������� ����� ��������" + "\n");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
    }
}
