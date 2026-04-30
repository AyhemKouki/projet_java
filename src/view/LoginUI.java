package view;

import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Session;

public class LoginUI extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("Login");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");

        Label message = new Label();

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            String role = null;
            boolean login = UserController.login(email, password);

            if (login) {
                if (Session.role.equals("admin")) {
                    AdminDashboardUI adminDashboardUI = new AdminDashboardUI();
                    adminDashboardUI.show(stage);
                }else if (Session.role.equals("user")) {
                    UserDashboardUI userDashboardUI = new UserDashboardUI();
                    userDashboardUI.show(stage);
                }

            } else {
                message.setText("Invalid credentials");
            }
        });

        VBox root = new VBox(10, title, emailField, passwordField, loginBtn, message);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 700, 450);

        stage.setTitle("Library Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}