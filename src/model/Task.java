package model;

import service.Status;

import java.time.LocalDateTime;

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

    public void setStatus(Status progress) {
        this.status = progress;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
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

    @Override
    public String toString() {
        return "model.Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}' + "\n";
    }
}