import controller.AuthController;
import controller.LabelController;
import controller.TaskController;
import model.Label;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // Constant for the main application menu (after login)
    private static final String APP_MENU_OPTIONS =
            "\n--- Task Management Menu ---\n" +
                    "1. View all tasks\n" +
                    "2. View tasks by priority\n" +
                    "3. View tasks by label\n" +
                    "4. Log out\n";

    // Constant for the menu before login
    private static final String PRE_LOGIN_MENU =
            "\n--- Welcome to Maple ---\n" +
                    "1. Log In\n" +
                    "2. Sign Up\n" +
                    "3. Exit\n";

    public static void main(String[] args) throws IOException {

        //initiation and data loading

        // Initialize controllers
        AuthController authController = new AuthController();
        TaskController taskController = new TaskController();
        LabelController labelController = new LabelController();

        // Initialize Scanner for user input
        Scanner sc = new Scanner(System.in);

        try {
            authController.loadUsersFromCSV("data/users.csv");
            //labelController.loadLabelsFromCSV("data/labels.csv");
            taskController.loadTasksFromCSV("data/tasks.csv", authController.getUsers(), labelController.getLabels());
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load data files. Check file paths and file contents.");
            e.printStackTrace();
            sc.close();
            return;
        }

        //main application loop (Handles returning to pre-login menu)

        // Outer loop to keep the application running until the user chooses to exit
        while (true) {
            User currentUser = null;
            String preLoginChoice;

            // Pre-login loop: Runs until login/signup is successful OR user exits
            do {
                System.out.println(PRE_LOGIN_MENU);
                System.out.print("Enter your choice: ");
                preLoginChoice = sc.nextLine();

                currentUser = handlePreLoginChoice(preLoginChoice, authController, sc);

                // If the user chooses '3' (Exit), break the pre-login loop and the outer application loop.
                if (preLoginChoice.equals("3")) {
                    System.out.println("Exiting. Goodbye!");
                    sc.close();
                    return; // Exit the entire application
                }

                // Break if login/signup was successful (currentUser is not null)
            } while (currentUser == null);

            // Main application menu loop: Only runs if currentUser is not null
            runApplicationMenu(currentUser, taskController, labelController, authController, sc);
        }
    }

    private static User handlePreLoginChoice(String choice, AuthController authController, Scanner sc) throws IOException { // menu before logIn in
        switch (choice) {
            case "1" -> {
                return handleLogin(authController, sc);
            }
            case "2" -> {
                return handleSignUp(authController, sc);
            }
            case "3" -> {
                return null;
            }
            default -> {
                System.out.println("\nInvalid choice. Please try again.");
                return null;
            }
        }
    }

    private static User handleLogin(AuthController authController, Scanner sc) {
        System.out.println("\n--- Log In ---");
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        return authController.login(email, password);
    }

    private static User handleSignUp(AuthController authController, Scanner sc) throws IOException {
        System.out.println("\n--- Sign Up ---");
        System.out.print("Enter a username: ");
        String username = sc.nextLine();
        System.out.print("Enter an email: ");
        String email = sc.nextLine();
        System.out.print("Enter a password: ");
        String password = sc.nextLine();

        return authController.singUp(username, email, password);
    }

    /**
     * Runs the main task management menu after successful authentication.
     */
    private static void runApplicationMenu(
            User currentUser,
            TaskController taskController,
            LabelController labelController,
            AuthController authController,
            Scanner sc) {

        String appChoice;

        do {
            System.out.println(APP_MENU_OPTIONS);
            System.out.print("Enter your choice: ");
            appChoice = sc.nextLine();

            // Case "4" (Log out) is handled by the loop condition
            processUserChoice(appChoice, currentUser, taskController, labelController, sc);

        } while (!appChoice.equals("4"));

        // Once the loop breaks (appChoice == "4"), perform the logout action
        authController.logout(currentUser);
        System.out.println("\nReturning to the welcome screen...");
        // The method returns, and the outer while(true) loop in main() restarts.
    }

    private static void processUserChoice(
            String choice,
            User currentUser,
            TaskController taskController,
            LabelController labelController,
            Scanner sc) {

        switch (choice) {
            case "1" -> {
                System.out.println("\n--- All Tasks ---");
                taskController.getAllTasks(currentUser);
            }
            case "2" -> {
                System.out.print("\nSelect priority (low, medium, high): ");
                String priority = sc.nextLine();
                taskController.getTasksByPriority(currentUser, priority);
            }
            case "3" -> {
                System.out.println("\n--- Available Labels ---");
                ArrayList<Label> labels = labelController.getLabels();

                labels.forEach(label -> System.out.println("- " + label.getName()));

                System.out.print("Select label: ");
                String userLabel = sc.nextLine();
                taskController.getTasksByLabel(currentUser, userLabel);
            }
            case "4" -> {
                // User chose to log out. The do-while loop in runApplicationMenu will break.
            }
            default -> {
                System.out.println("\nInvalid choice. Try again.");
            }
        }
    }
}
