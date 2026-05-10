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

    private static final String BG = "#0F0F10";
    private static final String CARD = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";
    private static final String TEXT = "#FFFFFF";
    private static final String MUTED = "#A1A1AA";
    private static final String BLUE = "#60A5FA";

    @Override
    public void start(Stage stage) {

        Label title = new Label("Welcome back");
        title.setStyle(textBig());

        Label subtitle = new Label("Sign in to Librarium");
        subtitle.setStyle(textMuted());

        Label emailLabel = new Label("EMAIL");
        emailLabel.setStyle(labelStyle());

        TextField emailField = new TextField();
        emailField.setStyle(inputStyle());
        emailField.setPromptText("you@example.com");

        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle(labelStyle());

        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(inputStyle());
        passwordField.setPromptText("••••••••");

        Label message = new Label();
        message.setStyle("-fx-text-fill: #EF4444;");
        message.setVisible(false);

        Button loginBtn = new Button("Sign in");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(primaryButton());

        addBtnEffects(loginBtn);

        Hyperlink register = new Hyperlink("Create account");
        register.setStyle(linkStyle());

        register.setOnAction(e -> new RegisterUI().show(stage));

        VBox card = new VBox(14,
                title,
                subtitle,
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                message,
                loginBtn,
                register
        );

        card.setPadding(new Insets(30));
        card.setMaxWidth(420);
        card.setStyle(cardStyle());

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: " + BG + ";");

        Scene scene = new Scene(root, 800, 550);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        entrance(card);

        loginBtn.setOnAction(e -> {

            boolean ok = UserController.login(
                    emailField.getText(),
                    passwordField.getText()
            );

            if (ok) {

                if ("admin".equals(Session.role)) {
                    new AdminDashboardUI().show(stage);
                } else {
                    new UserDashboardUI().show(stage);
                }

            } else {
                message.setText("Invalid credentials");
                message.setVisible(true);
                shake(card);
            }
        });
    }

    // ================= STYLE =================

    private String cardStyle() {
        return "-fx-background-color:" + CARD + ";" +
                "-fx-background-radius: 18;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius: 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 20, 0, 0, 6);";
    }

    private String inputStyle() {
        return "-fx-background-color:#171717;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#2B2B2D;" +
                "-fx-border-radius:12;" +
                "-fx-padding:10 14;";
    }

    private String labelStyle() {
        return "-fx-text-fill:#A1A1AA;-fx-font-size:11px;-fx-font-weight:bold;";
    }

    private String textBig() {
        return "-fx-text-fill:white;-fx-font-size:28px;-fx-font-weight:bold;";
    }

    private String textMuted() {
        return "-fx-text-fill:#A1A1AA;-fx-font-size:13px;";
    }

    private String primaryButton() {
        return "-fx-background-color:#60A5FA;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:12;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:12;";
    }

    private String linkStyle() {
        return "-fx-text-fill:#A1A1AA;";
    }

    private void addBtnEffects(Button b) {
        b.setOnMouseEntered(e -> { b.setScaleX(1.03); b.setScaleY(1.03); });
        b.setOnMouseExited(e -> { b.setScaleX(1); b.setScaleY(1); });
    }

    private void entrance(VBox card) {
        card.setOpacity(0);
        card.setTranslateY(20);

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), card);
        tt.setToY(0);
        card.setOpacity(1);
        tt.play();
    }

    private void shake(VBox card) {
        TranslateTransition t = new TranslateTransition(Duration.millis(60), card);
        t.setByX(8);
        t.setCycleCount(6);
        t.setAutoReverse(true);
        t.play();
    }
    public static void main(String[] args) {
        launch();
    }
}