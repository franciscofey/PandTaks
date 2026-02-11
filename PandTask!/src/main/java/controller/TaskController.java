package controller;

import model.Label;
import model.Task;
import model.User;

import java.io.*;
import java.util.ArrayList;

/**
 * The TaskController class represents the controller for the user's tasks in the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class TaskController {

    private ArrayList<Task> tasks;
    private int nextTaskId = 1;

    public TaskController() {
        this.tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Load tasks from tasks.csv file
     * @param filePath, the name of the file (String)
     * @param users, the users registered (ArrayList)
     * @param labels, the labels (ArrayList)
     * @throws IOException, throws exceptions related to file I/O
     */
    public void loadTasksFromCSV(String filePath, ArrayList<User> users, ArrayList<Label> labels) throws IOException {
        tasks.clear();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Tasks file not found. Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("id,userId,title,startHour,endHour,repeat,description,eventDate,priority\n");
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {

                    int id = Integer.parseInt(data[0].trim());
                    int userId = Integer.parseInt(data[1].trim());
                    String title = data[2].trim();
                    String startHour = data[3].trim();
                    String endHour = data[4].trim();
                    String repeat = data[5].trim();
                    String description = data[6].trim();
                    String eventDate = data[7].trim();
                    String priority = data[8].trim();

                    Task task = new Task(id, userId, title, startHour, endHour, repeat, description, eventDate, priority);
                    tasks.add(task);

                    // Link the task to a user
                    for (User user : users) {
                        if (user.getId() == userId) {
                            user.getTasks().add(task);
                            break;
                        }
                    }

                    if (id >= nextTaskId) {
                        nextTaskId = id + 1;
                    }
                }
            }
            System.out.println("Loaded " + tasks.size() + " tasks from CSV");
        }
    }

    /**
     * Creates a task for the current user
     * @param user, the current user (User)
     * @param title, the task's name (String)
     * @param startHour, the task's start hour (String)
     * @param endHour, the task's end hour (String)
     * @param repeat, the task's repetition type (String)
     * @param description, the task's description (String)
     * @param eventDate, the task's date (String)
     * @param priority, the task's priority (String)
     * @return the nest task (Task)
     */
    public Task createTask(User user, String title, String startHour, String endHour,
                           String repeat, String description, String eventDate, String priority) {

        Task newTask = new Task(
                nextTaskId++,
                user.getId(),
                title,
                startHour,
                endHour,
                repeat,
                description,
                eventDate,
                priority
        );

        tasks.add(newTask);
        user.getTasks().add(newTask);

        try {
            saveTasksToCSV("data/tasks.csv");
        } catch (IOException e) {
            System.err.println("Error saving tasks to CSV: " + e.getMessage());
        }

        System.out.println("Task created: " + title + " for " + user.getUsername());
        return newTask;
    }

    /**
     * Deletes the user's selected task
     * @param user, the current user (User)
     * @param task, the task the user wants to delete (Task)
     */
    public void deleteTask(User user, Task task) {
        tasks.remove(task);
        user.getTasks().remove(task);

        try {
            saveTasksToCSV("data/tasks.csv");
        } catch (IOException e) {
            System.err.println("Error saving tasks to CSV: " + e.getMessage());
        }

        System.out.println("Task deleted: " + task.getTitle());
    }

    /**
     * Saves the tasks on the tasks file
     * @param filePath, the tasks file (String)
     * @throws IOException, throws exceptions related to file I/O
     */
    private void saveTasksToCSV(String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("id,userId,title,startHour,endHour,repeat,description,eventDate,priority\n");

            for (Task task : tasks) {
                bw.write(String.format(
                        "%d,%d,%s,%s,%s,%s,%s,%s,%s\n",
                        task.getId(),
                        task.getUserId(),
                        task.getTitle(),
                        task.getStartHour(),
                        task.getEndHour(),
                        task.getRepeat(),
                        task.getDescription(),
                        task.getEventDate(),
                        task.getPriority()
                ));
            }
        }
    }

    /**
     * Method to debug that prints all tasks
     * @param user, the current user (User)
     */
    public void getAllTasks(User user) {
        System.out.println("\n=== All Tasks for " + user.getUsername() + " ===");
        if (user.getTasks().isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (Task task : user.getTasks()) {
                System.out.println(task);
            }
        }
    }

    /**
     * Filter tasks by priority
     * @param user, the current user (User)
     * @param priority, the task's priority (String)
     */
    public void getTasksByPriority(User user, String priority) {
        System.out.println("\n=== " + priority + " Priority Tasks ===");
        boolean found = false;

        for (Task task : user.getTasks()) {
            if (task.getPriority().equalsIgnoreCase(priority)) {
                System.out.println(task);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found with priority: " + priority);
        }
    }

    /**
     * Filter user tasks by name
     * @param user, the current user (User)
     * @param labelName, the label's name (String)
     */
    public void getTasksByLabel(User user, String labelName) {
        System.out.println("\n=== Tasks with label: " + labelName + " ===");
        boolean found = false;

        for (Task task : user.getTasks()) {
            for (Label label : task.getLabels()) {
                if (label.getName().equalsIgnoreCase(labelName)) {
                    System.out.println(task);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("No tasks found with label: " + labelName);
        }
    }
}