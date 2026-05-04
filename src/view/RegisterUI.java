package view;

import controller.UserController;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterUI {

    public void show(Stage stage) {

        // ── Title ─────────────────────────────
        Label title = new Label("Create account");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Label subtitle = new Label("Join Librarium and start managing your books");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

        // ── Name ──────────────────────────────
        Label nameLabel = new Label("FULL NAME");
        nameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888780;");

        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");

        // ── Email ─────────────────────────────
        Label emailLabel = new Label("EMAIL");
        emailLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888780;");

        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");

        // ── Password ──────────────────────────
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888780;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");

        TextField visiblePassword = new TextField();
        visiblePassword.setManaged(false);
        visiblePassword.setVisible(false);
        visiblePassword.textProperty().bindBidirectional(passwordField.textProperty());

        CheckBox showPassword = new CheckBox("Show");

        showPassword.setOnAction(e -> {
            boolean show = showPassword.isSelected();
            passwordField.setVisible(!show);
            passwordField.setManaged(!show);
            visiblePassword.setVisible(show);
            visiblePassword.setManaged(show);
        });

        HBox passwordBox = new HBox(10, passwordField, visiblePassword, showPassword);

        // ── Input styles ──────────────────────
        String normalStyle =
                "-fx-background-color: #F1EFE8;" +
                        "-fx-border-color: #D3D1C7;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 14;";

        String focusStyle =
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: #2C2C2A;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 14;";

        TextField[] fields = {nameField, emailField, passwordField, visiblePassword};

        for (TextField f : fields) {
            f.setStyle(normalStyle);
            f.focusedProperty().addListener((obs, oldV, newV) ->
                    f.setStyle(newV ? focusStyle : normalStyle));
        }

        VBox nameGroup = new VBox(6, nameLabel, nameField);
        VBox emailGroup = new VBox(6, emailLabel, emailField);
        VBox passwordGroup = new VBox(6, passwordLabel, passwordBox);

        // ── Message ───────────────────────────
        Label message = new Label();
        message.setStyle("-fx-text-fill: #E24B4A;");
        message.setVisible(false);

        // ── Button ────────────────────────────
        Button registerBtn = new Button("Create account");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2C2C2A, #3A3A38);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12;"
        );

        // hover animation
        registerBtn.setOnMouseEntered(e -> {
            registerBtn.setScaleX(1.03);
            registerBtn.setScaleY(1.03);
        });

        registerBtn.setOnMouseExited(e -> {
            registerBtn.setScaleX(1);
            registerBtn.setScaleY(1);
        });

        // ── Footer ────────────────────────────
        Hyperlink backToLogin = new Hyperlink("Already have an account? Sign in");

        backToLogin.setOnAction(e -> {
            try {
                new LoginUI().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ── Card ──────────────────────────────
        VBox card = new VBox(16,
                title, subtitle,
                nameGroup,
                emailGroup,
                passwordGroup,
                message,
                registerBtn,
                new Separator(),
                backToLogin
        );

        card.setPadding(new Insets(30));
        card.setMaxWidth(400);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
        );

        // ── Root ─────────────────────────────
        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #F1EFE8;");
        root.setPadding(new Insets(40));

        // ── Register Logic ────────────────────
        registerBtn.setOnAction(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                message.setText("Please fill all fields");
                message.setVisible(true);
                return;
            }

            boolean success = UserController.addUser(name, email, password, "user");

            if (success) {
                try {
                    new LoginUI().start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                message.setText("Failed to create account");
                message.setVisible(true);

                // small animation
                ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
                st.setFromX(1);
                st.setToX(1.02);
                st.setCycleCount(2);
                st.setAutoReverse(true);
                st.play();
            }
        });

        Scene scene = new Scene(root, 700, 520);
        stage.setScene(scene);
    }
}