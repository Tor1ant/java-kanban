package model;

import service.Status;
import service.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, Status status, int epicId, long duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return super.toString() + "SubTask{" +
                "epicId=" + epicId +
                ", status='" + status + '\'' +
                "} " + "\n";
    }

    @Override
    public String ToStringForSave() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", this.getId(), TaskType.SUBTASK, this.getTitle(), this.getStatus(),
                this.getDescription(), this.getDuration(), this.getStartTime(), this.getEpicId());
    }
}