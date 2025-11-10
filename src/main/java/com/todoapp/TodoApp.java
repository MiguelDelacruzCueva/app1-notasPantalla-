package main.java.com.todoapp;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;

public class TodoApp extends Application {

    private Stage primaryStage;
    private boolean isExpanded = false;
    private double xOffset = 0;
    private double yOffset = 0;

    private VBox compactView;
    private VBox expandedView;
    private StackPane mainContainer;

    private ObservableList<Task> tasks;
    private ListView<Task> taskListView;
    private Task selectedTask;
    private Label selectedTaskLabel;
    private Label timerLabel;
    private TimerController timerController;
    private UserPreferences userPreferences;

    private VBox timerControlPanel;
    private boolean timerVisible = false;

    private static final String[] TASK_COLORS = {
            "#4A90E2",
            "#50C878",
            "#F5A623",
            "#E94B3C",
            "#9B59B6",
            "#1ABC9C"
    };

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.tasks = FXCollections.observableArrayList();
        this.userPreferences = UserPreferences.load();

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        mainContainer = new StackPane();
        Scene scene = new Scene(mainContainer, 320, 100);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/com/todoapp/styles.css").toExternalForm());

        createCompactView();
        createExpandedView();

        mainContainer.getChildren().add(compactView);

        makeDraggable(mainContainer);

        primaryStage.setScene(scene);
        primaryStage.setOpacity(userPreferences.getWindowOpacity());
        primaryStage.show();
    }

    private void createCompactView() {
        compactView = new VBox(8);
        compactView.setPadding(new Insets(12));
        compactView.setAlignment(Pos.CENTER_LEFT);
        compactView.getStyleClass().add("compact-view");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label greetingLabel = new Label("Hola, " + userPreferences.getNickname());
        greetingLabel.getStyleClass().add("greeting-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button expandBtn = new Button("⚙");
        expandBtn.getStyleClass().add("icon-button");
        expandBtn.setOnAction(e -> toggleView());

        header.getChildren().addAll(greetingLabel, spacer, expandBtn);

        selectedTaskLabel = new Label("Sin tarea seleccionada");
        selectedTaskLabel.getStyleClass().add("selected-task-label");
        selectedTaskLabel.setWrapText(true);

        timerLabel = new Label("00:00:00");
        timerLabel.getStyleClass().add("timer-label");
        timerController = new TimerController(timerLabel);

        compactView.getChildren().addAll(header, selectedTaskLabel, timerLabel);
    }

    private void createExpandedView() {
        expandedView = new VBox(10);
        expandedView.setPadding(new Insets(15));
        expandedView.getStyleClass().add("expanded-view");

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Mis Tareas");
        titleLabel.getStyleClass().add("title-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button collapseBtn = new Button("−");
        collapseBtn.getStyleClass().add("icon-button");
        collapseBtn.setOnAction(e -> toggleView());

        topBar.getChildren().addAll(titleLabel, spacer, collapseBtn);

        HBox addTaskBox = new HBox(8);
        addTaskBox.setAlignment(Pos.CENTER_LEFT);

        TextField taskInput = new TextField();
        taskInput.setPromptText("Nueva tarea...");
        taskInput.getStyleClass().add("task-input");
        HBox.setHgrow(taskInput, Priority.ALWAYS);

        ComboBox<String> colorPicker = new ComboBox<>();
        colorPicker.getItems().addAll("Azul", "Verde", "Naranja", "Rojo", "Morado", "Turquesa");
        colorPicker.setValue("Azul");
        colorPicker.getStyleClass().add("color-picker");

        Button addBtn = new Button("+");
        addBtn.getStyleClass().add("add-button");
        addBtn.setOnAction(e -> {
            String taskText = taskInput.getText().trim();
            if (!taskText.isEmpty()) {
                int colorIndex = colorPicker.getSelectionModel().getSelectedIndex();
                Task task = new Task(taskText, TASK_COLORS[colorIndex]);
                tasks.add(task);
                taskInput.clear();
            }
        });

        taskInput.setOnAction(e -> addBtn.fire());

        addTaskBox.getChildren().addAll(taskInput, colorPicker, addBtn);

        taskListView = new ListView<>(tasks);
        taskListView.getStyleClass().add("task-list");
        taskListView.setCellFactory(lv -> new TaskCell());
        taskListView.setPrefHeight(300);
        VBox.setVgrow(taskListView, Priority.ALWAYS);

        taskListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Task task = taskListView.getSelectionModel().getSelectedItem();
                if (task != null) {
                    selectedTask = task;
                    updateSelectedTaskDisplay();
                }
            }
        });

        HBox controlsBox = new HBox(8);
        controlsBox.setAlignment(Pos.CENTER);

        Button timerBtn = new Button("⏱ Timer");
        timerBtn.getStyleClass().add("control-button");
        timerBtn.setOnAction(e -> toggleTimerPanel());

        Button settingsBtn = new Button("⚙ Ajustes");
        settingsBtn.getStyleClass().add("control-button");
        settingsBtn.setOnAction(e -> showSettings());

        controlsBox.getChildren().addAll(timerBtn, settingsBtn);

        createTimerPanel();

        expandedView.getChildren().addAll(topBar, addTaskBox, taskListView, controlsBox);
    }

    private void createTimerPanel() {
        timerControlPanel = new VBox(10);
        timerControlPanel.setPadding(new Insets(12));
        timerControlPanel.getStyleClass().add("timer-panel");
        timerControlPanel.setVisible(false);
        timerControlPanel.setManaged(false);

        Label timerTitle = new Label("Controlador de Tiempo");
        timerTitle.getStyleClass().add("timer-title");

        HBox stopwatchBox = new HBox(8);
        stopwatchBox.setAlignment(Pos.CENTER);

        Button startStopwatchBtn = new Button("Iniciar Cronómetro");
        startStopwatchBtn.getStyleClass().add("timer-button");
        startStopwatchBtn.setOnAction(e -> {
            timerController.startStopwatch();
            updateTimerButtonStates(startStopwatchBtn);
        });

        Button pauseBtn = new Button("Pausar");
        pauseBtn.getStyleClass().add("timer-button");
        pauseBtn.setOnAction(e -> {
            if (timerController.isRunning()) {
                timerController.pause();
            } else {
                timerController.resume();
            }
        });

        Button stopBtn = new Button("Detener");
        stopBtn.getStyleClass().add("timer-button");
        stopBtn.setOnAction(e -> timerController.stop());

        stopwatchBox.getChildren().addAll(startStopwatchBtn, pauseBtn, stopBtn);

        Label countdownLabel = new Label("Temporizador de Cuenta Regresiva");
        countdownLabel.getStyleClass().add("timer-subtitle");

        HBox countdownBox = new HBox(8);
        countdownBox.setAlignment(Pos.CENTER);

        TextField minutesInput = new TextField();
        minutesInput.setPromptText("Minutos");
        minutesInput.setPrefWidth(80);
        minutesInput.getStyleClass().add("minutes-input");

        Button startCountdownBtn = new Button("Iniciar");
        startCountdownBtn.getStyleClass().add("timer-button");
        startCountdownBtn.setOnAction(e -> {
            try {
                int minutes = Integer.parseInt(minutesInput.getText());
                if (minutes > 0) {
                    timerController.startCountdown(minutes);
                    timerController.setOnTimerComplete(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("¡Tiempo terminado!");
                        alert.setHeaderText(null);
                        alert.setContentText("El tiempo ha finalizado.");
                        alert.show();
                    });
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Por favor ingresa un número válido de minutos.");
                alert.showAndWait();
            }
        });

        countdownBox.getChildren().addAll(minutesInput, startCountdownBtn);

        timerControlPanel.getChildren().addAll(
                timerTitle,
                stopwatchBox,
                new Separator(),
                countdownLabel,
                countdownBox
        );
    }

    private void toggleTimerPanel() {
        timerVisible = !timerVisible;
        timerControlPanel.setVisible(timerVisible);
        timerControlPanel.setManaged(timerVisible);

        if (timerVisible) {
            if (!expandedView.getChildren().contains(timerControlPanel)) {
                expandedView.getChildren().add(timerControlPanel);
            }
            primaryStage.setHeight(primaryStage.getHeight() + 150);
        } else {
            primaryStage.setHeight(primaryStage.getHeight() - 150);
        }
    }

    private void updateTimerButtonStates(Button activeBtn) {
    }

    private void showSettings() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Ajustes");
        dialog.setHeaderText("Personaliza tu aplicación");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label nicknameLabel = new Label("Apodo:");
        TextField nicknameField = new TextField(userPreferences.getNickname());

        Label opacityLabel = new Label("Transparencia:");
        Slider opacitySlider = new Slider(0.3, 1.0, userPreferences.getWindowOpacity());
        opacitySlider.setShowTickLabels(true);
        opacitySlider.setShowTickMarks(true);
        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            primaryStage.setOpacity(newVal.doubleValue());
        });

        content.getChildren().addAll(nicknameLabel, nicknameField, opacityLabel, opacitySlider);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userPreferences.setNickname(nicknameField.getText());
                userPreferences.setWindowOpacity(opacitySlider.getValue());
                userPreferences.save();
                createCompactView();
                if (!isExpanded) {
                    mainContainer.getChildren().clear();
                    mainContainer.getChildren().add(compactView);
                }
            }
        });
    }

    private void updateSelectedTaskDisplay() {
        if (selectedTask != null) {
            selectedTaskLabel.setText(selectedTask.getTitle());
            selectedTaskLabel.setStyle("-fx-text-fill: " + selectedTask.getColor() + ";");
        }
    }

    private void toggleView() {
        isExpanded = !isExpanded;
        mainContainer.getChildren().clear();

        if (isExpanded) {
            mainContainer.getChildren().add(expandedView);
            primaryStage.setWidth(400);
            primaryStage.setHeight(500);
        } else {
            mainContainer.getChildren().add(compactView);
            primaryStage.setWidth(320);
            primaryStage.setHeight(100);
            timerVisible = false;
            timerControlPanel.setVisible(false);
            timerControlPanel.setManaged(false);
        }
    }

    private void makeDraggable(Pane pane) {
        pane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        pane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    private class TaskCell extends ListCell<Task> {
        private HBox content;
        private CheckBox checkBox;
        private Label titleLabel;
        private Button deleteBtn;

        public TaskCell() {
            content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(8));

            checkBox = new CheckBox();
            checkBox.setOnAction(e -> {
                Task task = getItem();
                if (task != null) {
                    task.setCompleted(checkBox.isSelected());
                    updateTaskDisplay();
                }
            });

            titleLabel = new Label();
            titleLabel.setWrapText(true);
            HBox.setHgrow(titleLabel, Priority.ALWAYS);

            deleteBtn = new Button("×");
            deleteBtn.getStyleClass().add("delete-button");
            deleteBtn.setOnAction(e -> {
                Task task = getItem();
                if (task != null) {
                    tasks.remove(task);
                    if (selectedTask == task) {
                        selectedTask = null;
                        selectedTaskLabel.setText("Sin tarea seleccionada");
                        selectedTaskLabel.setStyle("");
                    }
                }
            });

            content.getChildren().addAll(checkBox, titleLabel, deleteBtn);
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
            } else {
                checkBox.setSelected(task.isCompleted());
                titleLabel.setText(task.getTitle());
                updateTaskDisplay();
                setGraphic(content);
            }
        }

        private void updateTaskDisplay() {
            Task task = getItem();
            if (task != null) {
                titleLabel.setStyle("-fx-text-fill: " + task.getColor() + ";" +
                        (task.isCompleted() ? "-fx-strikethrough: true; -fx-opacity: 0.6;" : ""));
                content.setStyle("-fx-background-color: " + task.getColor() + "20; " +
                        "-fx-background-radius: 8;");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

