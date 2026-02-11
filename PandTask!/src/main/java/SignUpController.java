import controller.AuthController;
import controller.TaskController;
import controller.LabelController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;

public class SignUpController {
    // FXML Components
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Hyperlink logInLink;
    @FXML
    private Label messageLabel;

    AuthController authController;
    LabelController labelController;
    TaskController taskController;
    User user;

    /**
     * Sets the user on the applicatio
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
    }

    /**
     * Gets the credentials of the new user, check if the user already exists, and if not, sign ups the user.
     * Then switch to home page scene
     * @param event, the action event (ActionEvent)
     * @throws IOException, throws exceptions related to file I/O
     */
    @FXML
    private void handleSignUpButtonAction(ActionEvent event) throws IOException {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            messageLabel.setText("All fields are required.");
            System.err.println("Sign Up Failed: Missing fields.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            System.err.println(password + " and " + repeatPassword + " does not match");
            messageLabel.setText("Password and Repeated Password does not match");
            return;
        }

        user = authController.singUp(name, email, password);

        if (user == null) {
            System.err.println("Error: User already Exist");
            messageLabel.setText(email + " already Exist");
            return;
        }

        try {
            String fxmlPath = "home-page.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource(fxmlPath));
            Parent mainPage = fxmlLoader.load();
            Scene scene = new Scene(mainPage, 1000, 700);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("PandTask Home Page");
            stage.setScene(scene);
            HomePageController homePageController = fxmlLoader.getController();
            homePageController.setUser(user);
            homePageController.setAuthController(authController);
            homePageController.setTaskController(taskController);
            homePageController.setLabelController(labelController);
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load the main application scene from sign up scene.");
            e.printStackTrace();
            if (messageLabel != null) {
                messageLabel.setText("System Error: Could not load main dashboard.");
            }
        }
    }

    /**
     * Switches to LogIn scene whe the LogIn link is clicked
     * @param event, the action event (ActionEvent)
     */
    @FXML
    private void handleLogInLinkClick(ActionEvent event) {
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
            logInController.setUser(user);
            logInController.setAuthController(authController);
            logInController.setTaskController(taskController);
            logInController.setLabelController(labelController);
            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load Log In scene from Sign Up scene... " + e.getMessage());
            messageLabel.setText("-- Unable to set Scene --");
        }
    }
}