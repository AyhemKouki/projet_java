package view;

import controller.UserController;
import model.User;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class UserManagementUI {

    private TableView<User> table = new TableView<>();
    private ObservableList<User> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ── Top Bar ──────────────────────────
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Table Setup ──────────────────────
        setupColumns();
        styleTable();
        loadUsers();

        Label title = new Label("User Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        // ── Input Style ──────────────────────
        String inputStyle =
                "-fx-background-color: #F1EFE8;" +
                        "-fx-border-color: #D3D1C7;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 14;";

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setStyle(inputStyle);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(inputStyle);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(inputStyle);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "user");
        roleBox.setValue("user");
        roleBox.setStyle(inputStyle);

        // ── Buttons ──────────────────────────
        Button addBtn = createPrimaryButton("Add User");
        Button deleteBtn = createPrimaryButton("Delete");
        Button refreshBtn = createTopButton("Refresh");

        // ── Form Card ────────────────────────
        VBox form = new VBox(10,
                new Label("Add New User"),
                nameField,
                emailField,
                passwordField,
                roleBox,
                addBtn
        );
        styleCard(form);

        // ── Table Card ───────────────────────
        VBox tableCard = new VBox(10, title, table);

        HBox actions = new HBox(10, deleteBtn, refreshBtn);
        tableCard.getChildren().add(actions);

        styleCard(tableCard);

        // ── Layout ───────────────────────────
        HBox content = new HBox(20, tableCard, form);
        content.setPadding(new Insets(20));

        VBox root = new VBox(topBar, content);
        root.setStyle("-fx-background-color: #F1EFE8;");

        // ── Actions ──────────────────────────
        addBtn.setOnAction(e -> {
            try {
                boolean success = UserController.addUser(
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        roleBox.getValue()
                );

                if (success) {
                    loadUsers();
                    nameField.clear();
                    emailField.clear();
                    passwordField.clear();
                    roleBox.setValue("user");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                UserController.deleteUser(selected.getId());
                loadUsers();
            }
        });

        refreshBtn.setOnAction(e -> loadUsers());

        backBtn.setOnAction(e -> new AdminDashboardUI().show(stage));

        // ── Scene ────────────────────────────
        Scene scene = new Scene(root, 1000, 550);
        stage.setScene(scene);
        stage.setTitle("Librarium — User Management");
        stage.show();
    }

    // ── Helpers ─────────────────────────────

    private void setupColumns() {

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId())
        );

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getName())
        );

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail())
        );

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getRole())
        );

        table.getColumns().addAll(idCol, nameCol, emailCol, roleCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void styleTable() {
        table.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 8;"
        );
    }

    private void styleCard(VBox box) {
        box.setPadding(new Insets(15));
        box.setPrefWidth(450);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10,0,0,2);"
        );
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10;" +
                        "-fx-font-weight: bold;"
        );

        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.03);
            btn.setScaleY(1.03);
        });

        btn.setOnMouseExited(e -> {
            btn.setScaleX(1);
            btn.setScaleY(1);
        });

        return btn;
    }

    private Button createTopButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;"
        );
        return btn;
    }

    private void loadUsers() {
        data.clear();
        List<User> users = UserController.listUsers();
        data.addAll(users);
        table.setItems(data);
    }
}