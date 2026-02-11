import controller.AuthController;
import controller.TaskController;
import controller.LabelController;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.User;
import java.util.prefs.Preferences;

/**
 * The LogInController class represents the controller for the login scene of the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class LogInController {
    //FXML Components
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberMeCheck;
    @FXML
    private Button LoginButton;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private Label messageLabel;

    AuthController authController;
    TaskController taskController;
    LabelController labelController;
    User user;
    private String savedEmail;
    private String savedPassword;
    String email;
    String password;
    boolean rememberMe;

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
    }

    /**
     * Getters and Setters
     */

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getSavedPassword() {
        return savedPassword;
    }

    public void setSavedPassword(String savedPassword) {
        this.savedPassword = savedPassword;
    }

    public String getSavedEmail() {
        return savedEmail;
    }

    public void setSavedEmail(String savedEmail) {
        this.savedEmail = savedEmail;
    }

    /**
     * Initializes the LogInController. Retrieves user's email and password when the remember me checkbox is clicked
     */
    @FXML
    public void initialize() {
        // Get Preferences node
        Preferences prefs = Preferences.userNodeForPackage(LogInController.class);

        // Retrieve data (default to empty string if nothing is found)
        String storedEmail = prefs.get("email", "");
        String storedPassword = prefs.get("password", "");

        // If an email was found, populate fields and check the box
        if (!storedEmail.isEmpty()) {
            emailField.setText(storedEmail);
            passwordField.setText(storedPassword);
            rememberMeCheck.setSelected(true);
        }
    }

    /**
     * Checks if the user's email and password are valid, handles the remember me checkbox, and switches to the Home Page scene
     * @param event, the action event (ActionEvent)
     * @throws IOException, throws exceptions related to file I/O
     */
    public void handleLogInAction(ActionEvent event) throws IOException {
        email = emailField.getText();
        password = passwordField.getText();
        rememberMe = rememberMeCheck.isSelected();

        System.out.println(" --- Log In Attempt---");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("RememberMe: " + rememberMe);

        if (email.isEmpty() || password.isEmpty()) {
            System.err.println("Error: Email or Password are empty.");
            messageLabel.setText("Email or Password are empty");
            return;
        }

        user = authController.login(email, password);

        if (user != null) {
            System.out.println(" -- Login Successfully --");
            Preferences prefs = Preferences.userNodeForPackage(LogInController.class);

            if (rememberMeCheck.isSelected()) {
                // Save to disk
                prefs.put("email", email);
                prefs.put("password", password);
                System.out.println("Credentials saved to Preferences.");
            } else {
                // User unchecked the box, so clear stored data from disk
                prefs.remove("email");
                prefs.remove("password");
                System.out.println("Credentials cleared from Preferences.");
            }

            messageLabel.setText("-- Message Label: Log In Successfully --");
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
                System.err.println("Failed to load the main page scene from log in scene.");
                e.printStackTrace();
                if (messageLabel != null) {
                    messageLabel.setText("System Error: Could not load main dashboard.");
                }
            }
        } else {
            System.err.println("Incorrect Email or Password");
            messageLabel.setText("Incorrect Email or Password");
        }
    }

    /**
     * Switches to Sign Up scene whe the Sign Up link is clicked
     * @param event, the action event (ActionEvent)
     */
    @FXML
    public void handleSwitchToSignUp(ActionEvent event) {
        System.out.println("Switching scene to Sign Up view...");
        try {
            String fxmlPath = "sign-up.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource(fxmlPath));
            Parent mainPage = fxmlLoader.load();
            Scene scene = new Scene(mainPage, 1000, 700);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("PandTask Sign Up Page");
            stage.setScene(scene);
            SignUpController signUpController = fxmlLoader.getController();
            signUpController.setUser(user);
            signUpController.setAuthController(authController);
            signUpController.setTaskController(taskController);
            signUpController.setLabelController(labelController);
            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load Sign Up scene from log in scene... " + e.getMessage());
            messageLabel.setText("-- Unable to set Scene --");
        }
    }

    /**
     * Helper method to handle the remember me checkbox
     */
    @FXML
    public void handleRememberMe() {
        if(rememberMeCheck.isSelected()){
            System.out.println("Username and Password successfully saved and will be remembered.");
            setSavedEmail(emailField.getText());
            setSavedPassword(passwordField.getText());
            setRememberMe(true);
        } else{setSavedEmail(null);
            setSavedPassword(null);
            setRememberMe(false);
            System.err.println("The data will no longer be remembered..");
        }
    }
}