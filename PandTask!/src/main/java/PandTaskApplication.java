import controller.AuthController;
import controller.LabelController;
import controller.TaskController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The PandTaskApplication class starts and run the Task Management System.
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class PandTaskApplication extends Application {

    private AuthController authController;
    private TaskController taskController;
    private LabelController labelController;

    /**
     * Starts the application by creating an authController, taskController, and labelController instances
     * and loading the users data, labels data, and tasks data
     */
    @Override
    public void start(Stage stage) {
        try {
            authController = new AuthController();
            taskController = new TaskController();
            labelController = new LabelController();

            //Load data
            authController.loadUsersFromCSV("data/users.csv");
            labelController.setUsers(authController.getUsers());
            //labelController.loadLabelsFromCSV("data/labels.csv");
            taskController.loadTasksFromCSV("data/tasks.csv", authController.getUsers(), labelController.getLabels());

            // Get Log In scene
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("log-in.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            stage.setTitle("PandTask Log In");
            stage.setScene(scene);

            LogInController logInController = fxmlLoader.getController();
            logInController.setAuthController(authController);
            logInController.setTaskController(taskController);
            logInController.setLabelController(labelController);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start PandTaskApplication: " + e.getMessage());
        }
    }
}

