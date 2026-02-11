import controller.AuthController;
import controller.LabelController;
import controller.TaskController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Task;
import model.User;
import controller.QuickTaskController;
import model.QuickTask;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The HomePageController class represents the controller for the home page scene of the PandTaskApplication
 * @author Paula Com Morales wkz778
 * @author Shahad K Ali
 * @author Raymond Huelitl
 * @author Francisco Espinoza
 */
public class HomePageController {

    private User user;
    private AuthController authController = new AuthController();
    private LabelController labelController = new LabelController();
    private TaskController taskController = new TaskController();
    private QuickTaskController quickTaskController = new QuickTaskController();

    private LocalDate currentStartOfWeek;

    // FXML Components
    @FXML private GridPane headerGrid;
    @FXML private GridPane calendarGrid;

    @FXML private Label monthLabel;
    @FXML private Button addTaskButton;
    @FXML private Button settingsButton;
    @FXML private VBox quickTasksVBox;
    @FXML private TextField quickTaskInput;
    @FXML private VBox tasksContainer;
    @FXML private VBox labelsContainer;

    // Day Headers
    @FXML private Label lblSun;
    @FXML private Label lblMon;
    @FXML private Label lblTue;
    @FXML private Label lblWed;
    @FXML private Label lblThu;
    @FXML private Label lblFri;
    @FXML private Label lblSat;


    /**
     * Sets the user on the application. Loads the user's labels and quick tasks
     * @param user, the current user (User)
     */
    public void setUser(User user) {
        this.user = user;
        Platform.runLater(() -> {
            if (user != null) {
                System.out.println("User set, loading tasks for: " + user.getUsername());
                try {
                    labelController.loadLabelsFromUser(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(calendarGrid != null){
                    loadUserTasks();
                }
                loadSavedQuickTasks();
                loadLabels();
            }
        });
    }

    /**
     * Sets the authController on the application
     * @param authController, the authController (AuthController)
     */
    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    /**
     * Sets the labelController on the application
     * @param labelController, the labelController (LabelController)
     */
    public void setLabelController(LabelController labelController) {
        this.labelController = labelController;
    }

    /**
     * Sets the taskController on the application
     * @param taskController, the taskController (TaskController)
     */
    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
        Platform.runLater(() -> {
            if (user != null && calendarGrid != null) {
                loadUserTasks();
            }
        });
    }

    /**
     * Initializes the HomePageController. It gets today's date and shows the current week
     * @throws IOException, throws exceptions related to file I/O
     */
    @FXML
    public void initialize() throws IOException {
        System.out.println("HomePageController initialized");

        LocalDate today = LocalDate.now();
        currentStartOfWeek = today.minusDays(today.getDayOfWeek().getValue() % 7);

        // Sync calendar body columns
        for (int i = 0; i < calendarGrid.getColumnConstraints().size(); i++) {
            ColumnConstraints col = calendarGrid.getColumnConstraints().get(i);
            col.setHgrow(Priority.NEVER);
            col.setPrefWidth(120);
        }

        // Sync header grid columns
        for (int i = 0; i < headerGrid.getColumnConstraints().size(); i++) {
            ColumnConstraints col = headerGrid.getColumnConstraints().get(i);
            col.setHgrow(Priority.NEVER);
            col.setPrefWidth(120);
        }

        updateCalendarView();

        if (user != null) {
            Platform.runLater(() ->{
                loadUserTasks();
                loadSavedQuickTasks();
            });
        }
    }

    /**
     * Loads the quick tasks.
     */
    private void loadSavedQuickTasks(){
        if(tasksContainer == null || user == null) return;

        tasksContainer.getChildren().clear();
        List<QuickTask> tasks = quickTaskController.loadQuickTasks(user);

        for (QuickTask task : tasks) {
            createCheckBoxForTask(task);
        }
    }

    /**
     * Helper method to create a checkbox for each quick task
     * @param task, the quick task (QuickTask)
     */
    private void createCheckBoxForTask(QuickTask task){
        CheckBox checkBox = new CheckBox(task.getTitle());
        checkBox.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");
        checkBox.setWrapText(true);

        checkBox.setOnAction(event -> {
            if(checkBox.isSelected()){
                quickTaskController.markTaskAsCompleted(task.getId());
                tasksContainer.getChildren().remove(checkBox);
            }
        });
        tasksContainer.getChildren().add(checkBox);
    }

    /**
     * Adds the quick task when the user presses the '+' button
     */
    @FXML
    protected void onAddQuickTaskClicked(){
        String taskText = quickTaskInput.getText();
        if (taskText != null && !taskText.trim().isEmpty()) {
            QuickTask newTask = quickTaskController.addQuickTask(user, taskText);
            createCheckBoxForTask(newTask);
            quickTaskInput.clear();
        }
    }

    /**
     * Displays the week before the current week when pressing the '<' button
     */
    @FXML
    public void onPrevWeekClicked() {
        currentStartOfWeek = currentStartOfWeek.minusWeeks(1);
        updateCalendarView();
    }

    /**
     * Displays the week after the current week when pressing the '>' button
     */
    @FXML
    public void onNextWeekClicked() {
        currentStartOfWeek = currentStartOfWeek.plusWeeks(1);
        updateCalendarView();
    }

    /**
     * Helper function to update the calendar days, month and tasks.
     */
    private void updateCalendarView() {
        updateDayHeaders();
        updateMonthLabel();
        loadUserTasks();
    }

    /**
     * Sets the month to the current month. It checks the middle of the week to decide the month
     */
    private void updateMonthLabel() {
        LocalDate representativeDate = currentStartOfWeek.plusDays(3);
        String text = representativeDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        monthLabel.setText(text);
    }

    /**
     * Sets the day headers to the current week days.
     */
    private void updateDayHeaders() {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE d");

        lblSun.setText(currentStartOfWeek.format(dayFormatter));
        lblMon.setText(currentStartOfWeek.plusDays(1).format(dayFormatter));
        lblTue.setText(currentStartOfWeek.plusDays(2).format(dayFormatter));
        lblWed.setText(currentStartOfWeek.plusDays(3).format(dayFormatter));
        lblThu.setText(currentStartOfWeek.plusDays(4).format(dayFormatter));
        lblFri.setText(currentStartOfWeek.plusDays(5).format(dayFormatter));
        lblSat.setText(currentStartOfWeek.plusDays(6).format(dayFormatter));
    }

    /**
     * Loads the tasks of the user and displays it on the calendar view.
     */
    public void loadUserTasks() {
        if (user == null || taskController == null || calendarGrid == null) return;

        // Clean old labels
        ArrayList<Node> toRemove = new ArrayList<>();
        for (Node node : calendarGrid.getChildren()) {
            // Skip labels that mark the hours
            if (node instanceof Label && ((Label) node).getText().matches("\\d+(AM|PM)")) continue;

            // Remove task labels (colored background)
            if (node instanceof Label) {
                Label l = (Label) node;
                if (l.getStyle() != null && l.getStyle().contains("-fx-background-color")) {
                    toRemove.add(node);
                }
            }
        }
        calendarGrid.getChildren().removeAll(toRemove);

        // Gets the user's tasks
        ArrayList<Task> userTasks = user.getTasks();
        if (userTasks.isEmpty()) return;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        // Displays the user's tasks
        for (Task task : userTasks) {
            try {
                String eventDateStr = task.getEventDate();
                if (eventDateStr == null || eventDateStr.isEmpty()) continue;

                LocalDate taskStartDate = LocalDate.parse(eventDateStr.trim(), dateFormatter);
                String repeatType = task.getRepeat() != null ? task.getRepeat() : "None";

                for(int i = 0; i < 7; i++){
                    LocalDate viewDate = currentStartOfWeek.plusDays(i);

                    if(viewDate.isBefore(taskStartDate)){
                        continue;
                    }

                    // Determines if the tasks repeats or not
                    boolean shouldShow = switch (repeatType.toLowerCase()) {
                        case "daily" -> true;
                        case "weekly" -> viewDate.getDayOfWeek() == taskStartDate.getDayOfWeek();
                        case "monthly" -> viewDate.getDayOfMonth() == taskStartDate.getDayOfMonth();
                        case "yearly" -> viewDate.getDayOfYear() == taskStartDate.getDayOfYear();
                        default -> viewDate.isEqual(taskStartDate);
                    };

                    // Puts the tasks on the calendar
                    if (shouldShow){
                        addTaskToGrid(task, i+1);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error displaying task: " + task.getTitle() + " - " + e.getMessage());
            }
        }
    }

    /**
     * Helper method to add the user's tasks to the calendar view. It spans the grids according to the task hours
     * @param task, the user's task (Task)
     * @param colIndex, the index of the column where the task is going to be (int)
     */
    private void addTaskToGrid(Task task, int colIndex){
        int startRow = getRowFromTime(task.getStartHour());
        int endRow = getRowFromTime(task.getEndHour());

        // Fix for midnight/late tasks
        if (endRow < startRow) endRow = 24;

        if (startRow < 0 || endRow < 0) return;

        // Label from javafx
        Label taskLabel = createTaskLabel(task);

        GridPane.setRowIndex(taskLabel, startRow);
        GridPane.setColumnIndex(taskLabel, colIndex);

        int span = endRow - startRow;
        if (span < 1) span = 1;

        GridPane.setRowSpan(taskLabel, span);
        GridPane.setMargin(taskLabel, new Insets(1));

        calendarGrid.getChildren().add(taskLabel);
    }

    /**
     * Creates the task label to place it to the calendar view
     * @param task, the user's task (Task)
     * @return the label created (Label)
     */
    private Label createTaskLabel(Task task) {
        // Java FX Label
        Label taskLabel = new Label(task.getTitle());

        String bgColor = "#4a6bc3"; // Default
        if ("High".equalsIgnoreCase(task.getPriority())) bgColor = "#dd3b35";
        else if ("Medium".equalsIgnoreCase(task.getPriority())) bgColor = "#fbbc42";
        else if ("Low".equalsIgnoreCase(task.getPriority())) bgColor = "#35c840";

        taskLabel.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: white; " +
                "-fx-padding: 4; -fx-background-radius: 3; -fx-font-size: 11; -fx-cursor: hand;");

        taskLabel.setWrapText(true);
        taskLabel.setMaxWidth(Double.MAX_VALUE);
        taskLabel.setMaxHeight(Double.MAX_VALUE);
        taskLabel.setAlignment(Pos.TOP_LEFT);

        if (task.getTitle().length() > 20) {
            taskLabel.setText(task.getTitle().substring(0, 20) + "…");
        }

        taskLabel.setOnMouseClicked(event -> showTaskDetails(task));
        return taskLabel;
    }

    /**
     * Gets the row from the time the task starts or ends
     * @param time, the starting or ending time (String)
     * @return the hour when the task starts or ends (int)
     */
    private int getRowFromTime(String time) {
        if (time == null) return -1;
        time = time.toUpperCase().trim().replace(".", "").replace(" ", "");

        try {
            DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("h[:mm]a");
            LocalTime localTime = LocalTime.parse(time, formatter12);

            int hour = localTime.getHour();
            if (hour == 0) return 23;
            return hour - 1;

        } catch (DateTimeParseException e1) {
            try {
                DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern("H:mm");
                LocalTime localTime = LocalTime.parse(time, formatter24);

                int hour = localTime.getHour();
                if (hour == 0) return 23;
                return hour - 1;

            } catch (DateTimeParseException e2) {
                return -1;
            }
        }
    }

    /**
     * Helper method to show the task details
     * @param task, the user's task (Task)
     */
    private void showTaskDetails(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText(task.getTitle());

        String content = "Time: " + task.getStartHour() + " - " + task.getEndHour() + "\n" +
                "Date: " + task.getEventDate() + "\n" +
                "Priority: " + task.getPriority() + "\n" +
                "Repeat: " + task.getRepeat() + "\n" +
                "Description: " + (task.getDescription() != null ? task.getDescription() : "None");

        alert.setContentText(content);

        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButton, closeButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == deleteButton) {
                taskController.deleteTask(user, task);
                loadUserTasks();
            }
        });
    }

    /**
     * Switches to the create task scene when pressing the '+' button
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    public void onAddTaskButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("create-task.fxml"));
            Parent createTaskPage = fxmlLoader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(createTaskPage, 1000, 700);
            stage.setTitle("PandTask - Create Task");
            stage.setScene(scene);
            CreateTaskController createTaskController = fxmlLoader.getController();
            createTaskController.setUser(user);
            createTaskController.setTaskController(taskController);
            createTaskController.setLabelController(labelController);
            createTaskController.setAuthController(authController);
            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load CreateTask scene from Home Page scene... " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Switches to settings scene when the Settings button is clicked.
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
            settingsController.setAuthController(authController);
            settingsController.setTaskController(taskController);
            settingsController.setLabelController(labelController);
            settingsController.setUser(user);
            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load Settings scene from Home Page scene... " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Switches to add-label scene when the '+' button is clicked.
     * @param actionEvent, the action event (ActionEvent)
     */
    @FXML
    public void onAddLabelClicked(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(PandTaskApplication.class.getResource("add-label.fxml"));
            Parent addLabelPage = fxmlLoader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(addLabelPage, 1000, 700);
            stage.setTitle("PandTask Add Label Page");
            stage.setScene(scene);

            AddLabelController addLabelController = fxmlLoader.getController();
            addLabelController.setLabelController(this.labelController);
            addLabelController.setAuthController(authController);
            addLabelController.setTaskController(taskController);
            addLabelController.setUser(user);
            stage.show();
        } catch (Exception e) {
            System.err.println("Unable to load Add Label scene from Home Page scene... " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to load the labels on the upper left side if the home page
     */
    private void loadLabels(){
        if (labelsContainer == null) {
            System.out.println("Error: labelsContainer is null");
            return;
        }

        labelsContainer.getChildren().clear();
        ArrayList<model.Label> labels = labelController.getLabels();

        System.out.println("loading labels in homepage");

        for(model.Label label : labels){
            javafx.scene.layout.HBox row = new javafx.scene.layout.HBox();
            row.setSpacing(10.0);
            row.setAlignment(Pos.CENTER_LEFT);

            javafx.scene.shape.Rectangle colorBox = new  javafx.scene.shape.Rectangle();
            colorBox.setWidth(15);
            colorBox.setHeight(15);
            //Set colors
            try{
                String colorHex = label.getColor();
                if(colorHex == null || colorHex.isEmpty()) colorHex = "CCCCCC";

                colorBox.setFill(javafx.scene.paint.Color.web(colorHex));
                colorBox.setStroke(Color.BLACK);
                colorBox.setStrokeWidth(0.5);
            }catch (Exception e){
                System.err.println("Unable to load color box from label: " + e.getMessage());
                colorBox.setFill(javafx.scene.paint.Color.GRAY);
            }

            javafx.scene.control.Label labelText = new javafx.scene.control.Label(label.getName());
            labelText.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");

            row.getChildren().add(colorBox);
            row.getChildren().add(labelText);
            labelsContainer.getChildren().add(row);
        }
    }
}