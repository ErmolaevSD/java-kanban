package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        HistoryManager historyManager =Managers.getDefaultHistory();

        Task task = new Task("Первая задача", "-", Status.NEW, 1);
        Task task1 = new Task("Вторая задача", "-", Status.NEW, 2);
        Epic epic = new Epic("Первый эпик", "-", Status.NEW, 3);
        SubTask subTask = new SubTask("Первый сабтаск", "-", Status.NEW, 5, epic);

        File file = new File("data.csv");


        Managers.loadFrom(file);
    }
}
