public class SubTask extends Task {

    private final Epic parentsTask;

    public SubTask(String nameTask, String descriptionTask, STATUS status, Integer id, Epic parentsTask) {
        super(nameTask, descriptionTask, status, id);
        this.parentsTask = parentsTask;
    }

    public Epic getParentsTask() {
        return parentsTask;
    }

}
