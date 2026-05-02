package view;

import controller.UserController;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import util.Session;

public class UpdateProfileUI {

    public void show(Stage stage) {

        // ── Brand ─────────────────────────────
        Label brand = new Label("📚 Librarium");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button backBtnTop = new Button("← Back");
        backBtnTop.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 13px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtnTop);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Title ─────────────────────────────
        Label title = new Label("Update Profile");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Label subtitle = new Label("Edit your personal information");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

        // ── Load user ─────────────────────────
        User user = UserController.getUserById(Session.userId);

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

        // ── Fields ────────────────────────────
        Label nameLabel = new Label("NAME");
        nameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888780;");

        TextField nameField = new TextField(user.getName());
        nameField.setStyle(normalStyle);

        Label emailLabel = new Label("EMAIL");
        emailLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888780;");

        TextField emailField = new TextField(user.getEmail());
        emailField.setStyle(normalStyle);

        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888780;");

        PasswordField passwordField = new PasswordField();
        passwordField.setText(user.getPassword());
        passwordField.setStyle(normalStyle);

        TextField visiblePassword = new TextField();
        visiblePassword.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePassword.setManaged(false);
        visiblePassword.setVisible(false);
        visiblePassword.setStyle(normalStyle);

        CheckBox showPassword = new CheckBox("Show");

        showPassword.setOnAction(e -> {
            if (showPassword.isSelected()) {
                passwordField.setVisible(false);
                passwordField.setManaged(false);

                visiblePassword.setVisible(true);
                visiblePassword.setManaged(true);
            } else {
                passwordField.setVisible(true);
                passwordField.setManaged(true);

                visiblePassword.setVisible(false);
                visiblePassword.setManaged(false);
            }
        });

        // focus effects
        nameField.focusedProperty().addListener((obs, o, n) -> nameField.setStyle(n ? focusStyle : normalStyle));
        emailField.focusedProperty().addListener((obs, o, n) -> emailField.setStyle(n ? focusStyle : normalStyle));
        passwordField.focusedProperty().addListener((obs, o, n) -> passwordField.setStyle(n ? focusStyle : normalStyle));
        visiblePassword.focusedProperty().addListener((obs, o, n) -> visiblePassword.setStyle(n ? focusStyle : normalStyle));

        VBox nameGroup = new VBox(6, nameLabel, nameField);
        VBox emailGroup = new VBox(6, emailLabel, emailField);
        HBox passwordBox = new HBox(10, passwordField, visiblePassword, showPassword);
        VBox passwordGroup = new VBox(6, passwordLabel, passwordBox);

        // ── Message ───────────────────────────
        Label message = new Label();
        message.setStyle("-fx-text-fill: #E24B4A;");
        message.setVisible(false);
        message.setManaged(false);

        // ── Button ────────────────────────────
        Button updateBtn = new Button("Save Changes");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 12;" +
                        "-fx-font-weight: bold;"
        );

        // hover effect
        updateBtn.setOnMouseEntered(e -> {
            updateBtn.setScaleX(1.03);
            updateBtn.setScaleY(1.03);
        });
        updateBtn.setOnMouseExited(e -> {
            updateBtn.setScaleX(1);
            updateBtn.setScaleY(1);
        });

        // ── Card ──────────────────────────────
        VBox card = new VBox(16,
                title,
                subtitle,
                nameGroup,
                emailGroup,
                passwordGroup,
                message,
                updateBtn
        );

        card.setPadding(new Insets(30));
        card.setMaxWidth(400);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
        );

        // hover shadow
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 30, 0, 0, 6);"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
        ));

        // ── Actions ───────────────────────────
        updateBtn.setOnAction(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                message.setText("All fields are required");
                message.setVisible(true);
                message.setManaged(true);
                return;
            }

            boolean ok = UserController.updateUser(Session.userId, name, email, password);

            if (ok) {
                message.setStyle("-fx-text-fill: #2E7D32;");
                message.setText("Profile updated successfully");
                message.setVisible(true);
                message.setManaged(true);
            } else {
                message.setStyle("-fx-text-fill: #E24B4A;");
                message.setText("Update failed");
                message.setVisible(true);
                message.setManaged(true);

                // shake animation
                TranslateTransition shake = new TranslateTransition(Duration.millis(80), card);
                shake.setFromX(0);
                shake.setByX(10);
                shake.setCycleCount(4);
                shake.setAutoReverse(true);
                shake.play();
            }
        });

        backBtnTop.setOnAction(e -> new UserDashboardUI().show(stage));

        // ── Root ──────────────────────────────
        StackPane root = new StackPane(card);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #F1EFE8;");

        VBox main = new VBox(topBar, root);

        Scene scene = new Scene(main, 600, 500);
        stage.setTitle("Librarium — Update Profile");
        stage.setScene(scene);
        stage.show();
    }
}