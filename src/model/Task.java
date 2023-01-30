package model;

import service.Status;
import service.TaskType;

import java.time.LocalDateTime;
import java.util.Optional;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected Long duration;
    protected LocalDateTime startTime;

    public Task(String title, String description, Status status, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getStartTime() {
        Optional<LocalDateTime> start = Optional.ofNullable(startTime);
        if (start.isEmpty()) {
            return Optional.empty();
        } else return Optional.of(startTime);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Optional<LocalDateTime> getEndTime() {
        Optional<LocalDateTime> start = Optional.ofNullable(startTime);
        if (start.isEmpty()) {
            return Optional.empty();
        } else
            return Optional.of(startTime.plusMinutes(duration));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!title.equals(task.title)) return false;
        if (!description.equals(task.description)) return false;
        return status.equals(task.status);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    protected TaskType getTaskType() {
        if (this instanceof SubTask) {
            return TaskType.SUBTASK;
        } else if (this instanceof Epic) {
            return TaskType.EPIC;
        } else {
            return TaskType.TASK;
        }
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", this.getId(), getTaskType(), this.getTitle(), this.getStatus(),
                this.getDescription(), this.getDuration(), this.getStartTime());
    }
}