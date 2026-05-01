package view;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import util.Session;

public class UpdateProfileUI {

    public void show(Stage stage) {

        Label title = new Label("Update Profile");

        User user = UserController.getUserById(Session.userId);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());

        Button updateBtn = new Button("Update");
        Button backBtn = new Button("Back");

        Label message = new Label();

        // ================= UPDATE ACTION =================
        updateBtn.setOnAction(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                message.setText("All fields are required");
                return;
            }

            boolean ok = UserController.updateUser(
                    Session.userId,
                    name,
                    email,
                    password
            );

            if (ok) {
                message.setText("Profile updated successfully");
            } else {
                message.setText("Update failed");
            }
        });

        // ================= BACK =================
        backBtn.setOnAction(e -> {
            new UserDashboardUI().show(stage);
        });

        VBox root = new VBox(10,
                title,
                nameField,
                emailField,
                passwordField,
                updateBtn,
                backBtn,
                message
        );

        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 300));
        stage.setTitle("Update Profile");
        stage.show();
    }
}