package view;

import controller.UserController;
import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class UserManagementUI {

    private TableView<User> table = new TableView<>();
    private ObservableList<User> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ================= TABLE =================
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

        loadUsers();

        // ================= INPUTS =================
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "user");
        roleBox.setValue("user");

        // ================= BUTTONS =================
        Button addBtn = new Button("Add User");
        Button deleteBtn = new Button("Delete Selected");
        Button refreshBtn = new Button("Refresh");
        Button backBtn = new Button("Back");

        // ================= ADD USER =================
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String role = roleBox.getValue();

                boolean success = UserController.addUser(name, email, password, role);

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

        // ================= DELETE USER =================
        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                UserController.deleteUser(selected.getId());
                loadUsers();
            }
        });

        // ================= REFRESH =================
        refreshBtn.setOnAction(e -> loadUsers());

        // ================= BACK =================
        backBtn.setOnAction(e -> {
            new AdminDashboardUI().show(stage);
        });

        // ================= LAYOUT =================
        HBox form = new HBox(10, nameField, emailField, passwordField, roleBox, addBtn);
        HBox actions = new HBox(10, deleteBtn, refreshBtn, backBtn);

        VBox root = new VBox(10, table, form, actions);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 800, 450));
        stage.setTitle("User Management");
        stage.show();
    }

    // ================= LOAD USERS =================
    private void loadUsers() {
        data.clear();
        List<User> users = UserController.listUsers();
        data.addAll(users);
        table.setItems(data);
    }
}