import controller.AuthController;
import controller.LabelController;
import controller.TaskController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.HashMap;

/**
 * The SettingsController class represents the controller for the Settings scene of the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class SettingsController {
    // FXML Components
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label nameLabel;
    @FXML
    Button logOutButton;
    
    User user;
    AuthController authController = new AuthController();
    TaskController taskController = new TaskController();
    LabelController labelController = new LabelController();

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
    }

    /**
     * Sets the user on the application and sets their username on the name label
     * @param user, the current user (User)
     */
    public void setUser(User user) {
        this.user = user;
        nameLabel.setText(user.getUsername());
    }

    /**
     * Switches to the log in scene when pressing the Log Out button
     * @param event, the action event (ActionEvent)
     */
    @FXML
    protected void onLogOutButtonClicked(ActionEvent event){
        System.out.println("Switching to log in page\n");
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("log-in.fxml"));
            Parent logInPage = fxmlLoader.load();
            Scene scene = new Scene(logInPage, 1000, 700);
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("PandTask Log In Page");
            stage.setScene(scene);

            LogInController logInController = fxmlLoader.getController();
            logInController.setAuthController(authController);
            logInController.setTaskController(taskController);
            logInController.setLabelController(labelController);
            logInController.setUser(user);
        } catch (Exception e) {
            System.err.println("Failed to load the Log In page scene from Settings scene.");
            e.printStackTrace();
        }
    }

    /**
     * Switches to the home page when pressing the 'x' button
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    protected void onCloseButtonClicked(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("home-page.fxml"));
            Parent homePage = fxmlLoader.load();
            Scene scene = new Scene(homePage, 1000, 700);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("PandTask Home Page");
            stage.setScene(scene);
            HomePageController homePageController = fxmlLoader.getController();
            homePageController.setAuthController(authController);
            homePageController.setLabelController(labelController);
            homePageController.setTaskController(taskController);
            homePageController.setUser(user);
            stage.show();
            System.out.println("Switched from Settings Scene to Home Page Scene successfully");
        } catch (Exception e) {
            System.err.println("Failed to load the Log In page scene from Settings scene.");
            e.printStackTrace(); throw new RuntimeException(e);
        }

    }

    /**
     * Changes the user's username, password, or both. Switches to log in page once the submit button is clicked
     * @param event,  the action event (ActionEvent)
     * @throws IOException, throws exceptions related to file I/O
     */
     @FXML
    public void changeUserInfo(ActionEvent event) throws IOException {

        User user;
        String newUsername = usernameField.getText().trim();
        String newPassword = passwordField.getText().trim();

        if(newUsername.isEmpty() && newPassword.isEmpty()){
            System.err.println("username and password can't be empty\n");
            messageLabel.setText("username and password can't be empty");
            return;
        } else if(newUsername.isEmpty()){
            authController.setPassword(newPassword);
            user = new User(authController.getId(), authController.getUsername(), authController.getEmail(), authController.getPassword());
            authController.getUsers().add(user);
            authController.appendUserToCsv(user);
            System.out.println("Username Successfully Changed\n");
        } else if(newPassword.isEmpty()){
            authController.setUsername(newUsername);
            user = new User(authController.getId(), authController.getUsername(), authController.getEmail(), authController.getPassword());
            authController.getUsers().add(user);
            authController.appendUserToCsv(user);
            System.out.println("Password Successfully Changed\n");
        } else {
            authController.setUsername(newUsername);
            authController.setPassword(newPassword);
            user = new User(authController.getId(), authController.getUsername(), authController.getEmail(), authController.getPassword());
            authController.getUsers().add(user);
            authController.appendUserToCsv(user);
            System.out.println("Both Username and Password Successfully Changed\n");
        }
        System.out.println("Switching scene to Log In view...");
        try {
            String fxmlPath = "log-in.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource(fxmlPath));
            Parent mainPage = fxmlLoader.load();
            Scene scene = new Scene(mainPage, 1000, 700);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("PandTask Calender Log - In Page");
            stage.setScene(scene);
            LogInController logInController = fxmlLoader.getController();
            logInController.setAuthController(authController);
            logInController.setTaskController(taskController);
            logInController.setLabelController(labelController);
            stage.show();
        }
        catch(Exception e) {
            System.err.println("Unable to load a new scene... " + e.getMessage());
            messageLabel.setText("-- Unable to set Scene --");
        }
    }
}
