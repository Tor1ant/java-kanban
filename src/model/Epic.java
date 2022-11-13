package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);

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

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void addSubTasksId(int subTaskId) {
        subTasksId.add(subTaskId);
    }

    public void removeSubTask(int subTaskId) {
        ArrayList<Integer> subTasksIdCopy = new ArrayList<>(subTasksId);

        for (Integer integer : subTasksIdCopy) {
            if (integer == subTaskId) {
                subTasksId.remove(integer);
            }
        }

    }

    /*    public void removeEpicById(int id) {
        ArrayList<SubTask> subTaskArrayList = getListOfEpicsSubTasks(id);
        epics.remove(id);
        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getEpicId() == id) {
                subTasks.remove(subTask.getId());
            }
        }

    }*/

    @Override
    public String toString() {
        return super.toString() + "Epic{" +
                "subTasksId=" + subTasksId +
                ", progress='" + progress + '\'' +
                "} " + "\n";
    }
}
