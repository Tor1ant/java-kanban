package model;
public class SubTask extends Task {
    int epicId;

    public SubTask(String title, String description, String progress) {
        super(title, description, progress);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return super.toString() + "SubTask{" +
                "epicId=" + epicId +
                ", progress='" + progress + '\'' +
                "} " + "\n";
    }
}
