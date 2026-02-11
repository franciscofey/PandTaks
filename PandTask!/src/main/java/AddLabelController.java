import controller.AuthController;
import controller.LabelController;
import controller.TaskController;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.User;

/**
 * Controller for Add Label popup in PandTask.
 * Allows user to create a new label with a chosen color.
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class AddLabelController {
    User user;
    AuthController authController;
    TaskController taskController;
    LabelController labelController;

    // FXML Components
    @FXML
    private TextField labelNameField;
    @FXML
    private Button doneButton;
    @FXML
    private FlowPane colorPickerPane;
    @FXML
    private Button closeButton;

    //private LabelController labelController;
    private Color selectedColor;


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
     * Initializes the AddLabelController by coloring the circles (color choices for the label)
     */
    @FXML
    public void initialize() {
        setupColorButtons();
    }

    /** Create interactive color circles */
    private void setupColorButtons() {
        Color[] colors = {
                Color.RED, Color.ORANGE, Color.GOLD, Color.MEDIUMSEAGREEN,
                Color.ROYALBLUE, Color.MEDIUMPURPLE, Color.DEEPPINK, Color.BLACK
        };

        for (Color c : colors) {
            Circle circle = new Circle(15, c);
            circle.setOnMouseClicked(e -> {
                selectedColor = c;
                highlightSelected(circle);
            });
            colorPickerPane.getChildren().add(circle);
        }
    }

    /** Highlight selected color */
    private void highlightSelected(Circle selected) {
        for (var node : colorPickerPane.getChildren()) {
            if (node instanceof Circle circle) {
                circle.setStroke(null);
            }
        }
        selected.setStroke(Color.GRAY);
        selected.setStrokeWidth(3);
    }

    /**
     * Handle Done button click
     * @param event, the action event (ActionEvent)
     */
    @FXML
    private void onDoneClicked(ActionEvent event) {
        String labelName = labelNameField.getText();

        if (labelName == null || labelName.trim().isEmpty()) {
            showAlert("Please enter a label name.");
            return;
        }

        if (selectedColor == null) {
            showAlert("Please choose a color.");
            return;
        }

        String colorHex = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));

        try {
            labelController.addLabel(user, labelName, colorHex);
            showAlert("Label added successfully!");
            labelNameField.clear();
            selectedColor = null;
            for (var node : colorPickerPane.getChildren()) {
                if (node instanceof Circle c) c.setStroke(null);
            }
        } catch (Exception e) {
            showAlert("Error adding label: " + e.getMessage());
        }


        // switch to home page
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("home-page.fxml"));
            Parent homePage = fxmlLoader.load();
            Scene scene = new Scene(homePage, 1000, 700);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("PandTask Home Page");
            stage.setScene(scene);
            HomePageController homePageController = fxmlLoader.getController();
            homePageController.setAuthController(authController);
            homePageController.setLabelController(labelController);
            homePageController.setTaskController(taskController);
            homePageController.setUser(user);
            stage.show();
            System.out.println("Switched from Add Label Scene to Home Page Scene successfully");
        } catch (Exception e) {
            System.err.println("Failed to load the Log In page scene from Settings scene.");
            e.printStackTrace();
        }
    }

    /**
     * Close the Add label scene and switch to home page
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    private void onCloseButtonClicked(ActionEvent actionEvent){
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
            System.out.println("Switched from Add Label Scene to Home Page Scene successfully");
        } catch (Exception e) {
            System.err.println("Failed to load the Log In page scene from Add Label scene.");
            e.printStackTrace(); throw new RuntimeException(e);
        }

    }

    /**
     * Helper function to show an alert if there is an error during the label addition
     * @param msg, the alert message (String)
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Label");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}