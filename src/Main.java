import model.Epic;
import model.SubTask;
import model.Task;
import service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        int EpicTask1ID = manager.createEpic(new Epic("������ ��������", "����� ����"));
        int EpicTask2ID = manager.createEpic(new Epic("������� � �������", "���� ����� ��������"));

        //��������� ������� �����

        int SubTask1ID = manager.createSubTask(new SubTask("������ �����", "��� ������� �����", "NEW"), 1);
        //��������� ������� �����


        int SubTask2ID = manager.createSubTask(new SubTask("������ ����", "�������", "NEW"), 2);
        int SubTask3ID = manager.createSubTask(new SubTask("������ ������", "����� � �������", "DONE"), 2);

        System.out.println(manager.getSubTaskById(SubTask3ID));

    }
}
