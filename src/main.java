public class main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = taskManager.addNewTask(new Task("Сделать домашнее задание", "Качествено", Status.NEW, null));
        Task task2 = taskManager.addNewTask(new Task("Приготовить кофе", "Качествено", Status.NEW, null));

        Epic epic1 = taskManager.addNewEpic(new Epic("Уборка квартиры", "Клининг", Status.NEW, null));
        Epic epic2 = taskManager.addNewEpic(new Epic("Починить машину", "капитал", Status.NEW, null));

        SubTask subTask1 = taskManager.addNewSubTask(new SubTask("Вымыть пол", "Средство", Status.DONE, null, epic1));
        SubTask subTask2 = taskManager.addNewSubTask(new SubTask("Вымыть стекло", "Средство", Status.NEW, null,epic1));
        SubTask subTask3 = taskManager.addNewSubTask(new SubTask("Ремонт двигателя", "Тотал", Status.IN_PROGRESS, null, epic2));

        System.out.println("Cписок задач");
        System.out.println(taskManager.printListTask());
        System.out.println();
        System.out.println("Cписок эпиков");
        System.out.println(taskManager.printListEpicTask());
        System.out.println();
        System.out.println("Cписок сабтасков");
        System.out.println(taskManager.printListSubTask());


        System.out.println("Замена задачи");
        Epic epic3 = new Epic("Сделать домашнее молоко", "Качественно", Status.DONE, 3);
        taskManager.updateEpic(epic3);
        System.out.println(taskManager.printListEpicTask());

        System.out.println(taskManager.printSubtasks(epic1));

        System.out.println("Статусы");
        System.out.println(epic1);
        System.out.println(epic2);

        System.out.println();
        System.out.println("Удаление Такс");
        taskManager.deleteNameTask(2);
        System.out.println(taskManager.printListTask());
        taskManager.deleteEpicTask(epic2);
        System.out.println(taskManager.printListEpicTask());

        System.out.println("Удаление 'эпика'");
        taskManager.deleteEpicTask(epic1);
        System.out.println(taskManager.printListEpicTask());
        System.out.println(taskManager.printListSubTask());

    }
}
