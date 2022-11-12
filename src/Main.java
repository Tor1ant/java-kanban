import model.Epic;
import model.SubTask;
import model.Task;
import service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        int EpicTask1ID = manager.createEpic(new Epic("Уборка квартиры", "Давно пора"));
        int EpicTask2ID = manager.createEpic(new Epic("Сходить в магазин", "Мама будет довольна"));

        //подзадача первого эпика

        int SubTask1ID = manager.createSubTask(new SubTask("Убрать кухню", "Это сложнее всего", "NEW"), 1);
        //подзадачи второго эпика


        int SubTask2ID = manager.createSubTask(new SubTask("Купить хлеб", "хороший", "NEW"), 2);
        int SubTask3ID = manager.createSubTask(new SubTask("Купить молоко", "Домик в деревне", "DONE"), 2);

        System.out.println(manager.getSubTaskById(SubTask3ID));

    }
}
