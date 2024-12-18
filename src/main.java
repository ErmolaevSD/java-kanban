public class main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = inMemoryTaskManager.addNewTask(new Task("Сделать домашнее задание", "Качествено", Status.NEW, null));
        Task task2 = inMemoryTaskManager.addNewTask(new Task("Приготовить кофе", "Качествено", Status.NEW, null));

        Epic epic1 = inMemoryTaskManager.addNewEpic(new Epic("Уборка квартиры", "Клининг", Status.NEW, null));
        Epic epic2 = inMemoryTaskManager.addNewEpic(new Epic("Починить машину", "капитал", Status.NEW, null));

        SubTask subTask1 = inMemoryTaskManager.addNewSubTask(new SubTask("Вымыть пол", "Средство", Status.DONE, null, epic1));
        SubTask subTask2 = inMemoryTaskManager.addNewSubTask(new SubTask("Вымыть стекло", "Средство", Status.NEW, null,epic1));
        SubTask subTask3 = inMemoryTaskManager.addNewSubTask(new SubTask("Ремонт двигателя", "Тотал", Status.IN_PROGRESS, null, epic2));


        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(subTask1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(subTask1);

        System.out.println(inMemoryTaskManager.getHistory());


//
//        System.out.println("Замена задачи");
//        Epic epic3 = new Epic("Сделать домашнее молоко", "Качественно", Status.DONE, 3);
//        inMemoryTaskManager.updateEpic(epic3);
//        System.out.println(inMemoryTaskManager.printListEpicTask());
//
//        System.out.println(inMemoryTaskManager.printSubtasks(epic1));
//
//        System.out.println("Статусы");
//        System.out.println(epic1);
//        System.out.println(epic2);
//
//        System.out.println();
//        System.out.println("Удаление Такс");
//        inMemoryTaskManager.deleteNameTask(2);
//        System.out.println(inMemoryTaskManager.printListTask());
//        inMemoryTaskManager.deleteEpicTask(epic2);
//        System.out.println(inMemoryTaskManager.printListEpicTask());
//
//        System.out.println("Удаление 'эпика'");
//        inMemoryTaskManager.deleteEpicTask(epic1);
//        System.out.println(inMemoryTaskManager.printListEpicTask());
//        System.out.println(inMemoryTaskManager.printListSubTask());

    }
}
