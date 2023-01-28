package model;

import service.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        status = Status.NEW;
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
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
}