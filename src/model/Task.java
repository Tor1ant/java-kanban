package model;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected String progress; // -> ƒанна€ переменна€ имеет только 3 состо€ни€: NEW, IN_PROGRESS, DONE

    public Task(String title, String description, String progress) {
        this.title = title;
        this.description = description;
        this.progress = progress;
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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!title.equals(task.title)) return false;
        if (!description.equals(task.description)) return false;
        return progress.equals(task.progress);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + progress.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progress='" + progress + '\'' +
                '}' + "\n";
    }
}
