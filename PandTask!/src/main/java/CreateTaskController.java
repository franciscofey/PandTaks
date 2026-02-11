import controller.AuthController;
import controller.LabelController;
import controller.TaskController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Label;
import model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The CreateTaskController class represents the controller for the create-task scene of the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class CreateTaskController {

    // FXML Components
    @FXML
    private TextField taskNameField;
    @FXML
    private DatePicker eventDate;
    @FXML
    private ComboBox<String> priorityCombo;
    @FXML
    private ComboBox<String> fromTimeCombo;
    @FXML
    private ComboBox<String> toTimeCombo;
    @FXML
    private ComboBox<String> repeatsCombo;
    @FXML
    private ComboBox<String> labelNameCombo;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button settingsButton;

    private AuthController authController;

    private User user;
    private TaskController taskController;
    private LabelController labelController;

    /**
     * Sets the user on the application
     * @param user, the current user (User)
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the authController on the application
     * @param authController, the authController (AuthController)
     */
    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    /**
     * Sets the taskController on the application
     * @param taskController, the taskController (TaskController)
     */
    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    /**
     * Sets the labelController on the application
     * @param labelController, the labelController (LabelController)
     */
    public void setLabelController(LabelController labelController) {
        this.labelController = labelController;
        if (labelController != null && labelNameCombo != null) {
            labelNameCombo.getItems().clear();
            for (Label label : labelController.getLabels()) {
                labelNameCombo.getItems().add(label.getName());
            }
        }
    }

    /**
     * Initializes the CreateTaskController, populates the priority combo, the start times combo, the end times combo,
     * the repeats combo, and sets the date to the current date.
     */
    @FXML
    public void initialize() {
        priorityCombo.getItems().addAll("Low", "Medium", "High");
        priorityCombo.setValue("Medium");

        String[] times = {"1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM",
                "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12AM"};
        fromTimeCombo.getItems().addAll(times);
        toTimeCombo.getItems().addAll(times);
        fromTimeCombo.setValue("10AM");
        toTimeCombo.setValue("11AM");

        repeatsCombo.getItems().addAll("None", "Daily", "Weekly", "Monthly", "Yearly");
        repeatsCombo.setValue("None");

        eventDate.setValue(LocalDate.now());
    }

    /**
     * Saves the task when clicking the save button
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
            String title = taskNameField.getText().trim();
            String description = descriptionField.getText().trim();
            String priority = priorityCombo.getValue();
            String startHour = fromTimeCombo.getValue();
            String endHour = toTimeCombo.getValue();
            String repeat = repeatsCombo.getValue();
            LocalDate date = eventDate.getValue();

            if (title.isEmpty()) {
                showAlert("Error", "Task title cannot be empty!");
                return;
            }

            if (date == null) {
                showAlert("Error", "Please select a date!");
                return;
            }

            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            try {
                taskController.createTask(user, title, startHour, endHour, repeat, description, dateStr, priority);
                System.out.println("Task created successfully for user: " + user.getUsername());

                navigateToHomePage(actionEvent);
            } catch (Exception e) {
                showAlert("Error", "Failed to create task: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * Calls the navigateToHomePage and switches scenes
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    public void onCloseButtonClick(ActionEvent actionEvent) {
        navigateToHomePage(actionEvent);
    }

    /**
     * Switch to settings scene
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    public void onSettingsButtonClicked(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("settings-page.fxml"));
            Parent settingsPage = fxmlLoader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(settingsPage, 1000, 700);
            stage.setTitle("PandTask - Settings");
            stage.setScene(scene);
            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setTaskController(taskController);
            settingsController.setLabelController(labelController);
            settingsController.setUser(user);
            settingsController.setAuthController(authController);
            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load Settings scene from Home Page scene... " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Switches to home page scene
     * @param actionEvent, the action event (ActionEvent)
     */
    private void navigateToHomePage(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("home-page.fxml"));
            Parent homePage = fxmlLoader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(homePage, 1000, 700);
            stage.setTitle("PandTask Home Page");
            stage.setScene(scene);

            HomePageController homePageController = fxmlLoader.getController();
            homePageController.setUser(user);
            homePageController.setTaskController(taskController);
            homePageController.setLabelController(labelController);
            homePageController.setAuthController(authController);

            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load Home Page scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper function to show an alert if there is an error during the task creation
     * @param title, the name of the task (String)
     * @param message, the alert message (String)
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}