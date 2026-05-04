package view;

import controller.UserController;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Session;

public class LoginUI extends Application {

    @Override
    public void start(Stage stage) {

        // ── Brand ─────────────────────────────
        Label brandIcon = new Label("📚");
        brandIcon.setStyle("-fx-background-color: #2C2C2A; -fx-background-radius: 8; -fx-padding: 6 8;");

        Label brandName = new Label("Librarium");
        brandName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        HBox brand = new HBox(10, brandIcon, brandName);
        brand.setAlignment(Pos.CENTER_LEFT);

        // ── Title ─────────────────────────────
        Label title = new Label("Welcome back");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Label subtitle = new Label("Sign in to access your library account");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

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

        // bind text
        visiblePassword.textProperty().bindBidirectional(passwordField.textProperty());

        CheckBox showPassword = new CheckBox("Show");

        // toggle password visibility
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

        // ── Styles for inputs ─────────────────
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

        emailField.setStyle(normalStyle);
        passwordField.setStyle(normalStyle);
        visiblePassword.setStyle(normalStyle);

        // focus effect
        emailField.focusedProperty().addListener((obs, oldV, newV) ->
                emailField.setStyle(newV ? focusStyle : normalStyle));

        passwordField.focusedProperty().addListener((obs, oldV, newV) ->
                passwordField.setStyle(newV ? focusStyle : normalStyle));

        visiblePassword.focusedProperty().addListener((obs, oldV, newV) ->
                visiblePassword.setStyle(newV ? focusStyle : normalStyle));

        VBox emailGroup = new VBox(6, emailLabel, emailField);
        HBox passwordBox = new HBox(10, passwordField, visiblePassword, showPassword);
        VBox passwordGroup = new VBox(6, passwordLabel, passwordBox);

        // ── Message ───────────────────────────
        Label message = new Label();
        message.setStyle("-fx-text-fill: #E24B4A;");
        message.setVisible(false);
        message.setManaged(false);

        // ── Button ────────────────────────────
        Button loginBtn = new Button("Sign in");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2C2C2A, #3A3A38);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12;"
        );

        // hover animation
        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setScaleX(1.03);
            loginBtn.setScaleY(1.03);
        });

        loginBtn.setOnMouseExited(e -> {
            loginBtn.setScaleX(1);
            loginBtn.setScaleY(1);
        });

        // ── Footer ────────────────────────────
        Hyperlink forgot = new Hyperlink("Forgot password?");
        Hyperlink register = new Hyperlink("Create account");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox footer = new HBox(forgot, spacer, register);

        // ── Card ──────────────────────────────
        VBox card = new VBox(16,
                brand,
                title, subtitle,
                emailGroup,
                passwordGroup,
                message,
                loginBtn,
                new Separator(),
                footer
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

        // ── Root ─────────────────────────────
        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #F1EFE8;");
        root.setPadding(new Insets(40));

        // ── Login logic ──────────────────────
        loginBtn.setOnAction(e -> {
            boolean login = UserController.login(
                    emailField.getText(),
                    passwordField.getText()
            );

            if (login) {
                if (Session.role.equals("admin")) {
                    new AdminDashboardUI().show(stage);
                } else {
                    new UserDashboardUI().show(stage);
                }
            } else {
                message.setText("Invalid email or password");
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

        register.setOnAction(e -> {
            new RegisterUI().show(stage);
        });

        Scene scene = new Scene(root, 700, 520);
        stage.setTitle("Librarium — Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}