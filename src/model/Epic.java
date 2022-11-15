package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksId = new ArrayList<>();
    private String oldTitle;

    public Epic(String title, String description) {
        super(title, description);

    }

    @Override
    public void setTitle(String title) {
        oldTitle = super.getTitle();
        this.title = title;

    }

    public String getOldTitle() {
        return oldTitle;
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void addSubTasksId(int subTaskId) {
        subTasksId.add(subTaskId);
    }

    public void removeSubTask(Integer subTaskId) {
        subTasksId.remove(subTaskId);
    }

    public void clearSubTasksId() {
        subTasksId.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subTasksId.equals(epic.subTasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksId);
    }

    @Override
    public String toString() {
        return super.toString() + "Epic{" +
                "subTasksId=" + subTasksId +
                ", progress='" + progress + '\'' +
                "} " + "\n";
    }
}
