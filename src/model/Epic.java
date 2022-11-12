package model;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);

    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void addSubTasksId(int subTaskId) {
        subTasksId.add(subTaskId);
    }

    @Override
    public String toString() {
        return super.toString() + "Epic{" +
                "subTasksId=" + subTasksId +
                ", progress='" + progress + '\'' +
                "} " + "\n";
    }
}
