public class main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();


        Task task1 = new Task("Сделать домашнее задание", "Качествено", Task.STATUS.NEW, null);
        taskManager.newAddTask(task1);
        Task task2 = taskManager.newAddTask(new Task("Приготовить кофе", "Качествено", Task.STATUS.NEW, null));

        Epic epic1 = taskManager.newAddEpic(new Epic("Уборка квартиры", "Клининг", Task.STATUS.NEW, null));
        Epic epic2 = taskManager.newAddEpic(new Epic("Починить машину", "капитал", Task.STATUS.NEW, null));

        SubTask subTask1 = taskManager.newAddSub(new SubTask("Вымыть пол", "Средство", Task.STATUS.DONE, null, epic1));
        SubTask subTask2 = taskManager.newAddSub(new SubTask("Вымыть стекло", "Средство", Task.STATUS.NEW, null,epic1));
        SubTask subTask3 = taskManager.newAddSub(new SubTask("Ремонт двигателя", "Тотал", Task.STATUS.IN_PROGRESS, null, epic2));

        System.out.println("Cписок задач");
        System.out.println(taskManager.printListTask());
        System.out.println();
        System.out.println("Cписок эпиков");
        System.out.println(taskManager.printListEpicTask());
        System.out.println();
        System.out.println("Cписок сабтасков");
        System.out.println(taskManager.printListSubTask());


//
//
//        System.out.println("Замена задачи");
//        Epic epic3 = new Epic("Сделать домашнее хуй", "Качественно", Task.STATUS.DONE, 3);
//        taskManager.updateEpic(epic3);
//        System.out.println(taskManager.printListEpicTask());
//
//        System.out.println(taskManager.printSubtasks(epic1));
//
//        System.out.println("Статусы");
//        System.out.println(epic1);
//        System.out.println(epic2);
//
//        System.out.println();
//        System.out.println("Удаление Такс");
//        taskManager.deleteNameTask(2);
//        System.out.println(taskManager.printListTask());
//        taskManager.deleteEpicTask(epic2);
//        System.out.println(taskManager.printListEpicTask());
////
//        System.out.println("Удаление 'эпика'");
//        taskManager.deleteEpicTask(epic1);
//        System.out.println(taskManager.printListEpicTask());
//        System.out.println(taskManager.printListSubTask());
//
//        System.out.println(taskManager.findTask(2));
//        taskManager.deleteAllEpicTask();
//        System.out.println(taskManager.printListEpicTask());
//        System.out.println(taskManager.printListSubTask());
//


    }
}
