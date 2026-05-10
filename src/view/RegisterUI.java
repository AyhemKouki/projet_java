package view;

import controller.UserController;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterUI {

    private static final String BG = "#0F0F10";
    private static final String CARD = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";

    public void show(Stage stage) {

        Label title = new Label("Create account");
        title.setStyle(titleStyle());

        Label subtitle = new Label("Join Librarium");
        subtitle.setStyle(subtitleStyle());

        TextField name = new TextField();
        TextField email = new TextField();
        PasswordField pass = new PasswordField();

        name.setStyle(input());
        email.setStyle(input());
        pass.setStyle(input());

        name.setPromptText("Full name");
        email.setPromptText("Email");
        pass.setPromptText("Password");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill:#EF4444;");
        msg.setVisible(false);

        Button btn = new Button("Create account");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(primaryButton());

        addEffects(btn);

        Hyperlink login = new Hyperlink("Already have an account? Sign in");
        login.setStyle(link());

        login.setOnAction(e -> new LoginUI().start(stage));

        VBox card = new VBox(14,
                title,
                subtitle,
                name,
                email,
                pass,
                msg,
                btn,
                login
        );

        card.setPadding(new Insets(30));
        card.setMaxWidth(420);
        card.setStyle(cardStyle());

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color:" + BG + ";");

        Scene scene = new Scene(root, 800, 550);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();

        entrance(card);

        btn.setOnAction(e -> {

            if (name.getText().isEmpty() ||
                    email.getText().isEmpty() ||
                    pass.getText().isEmpty()) {

                msg.setText("Fill all fields");
                msg.setVisible(true);
                return;
            }

            boolean ok = UserController.addUser(
                    name.getText(),
                    email.getText(),
                    pass.getText(),
                    "user"
            );

            if (ok) {
                new LoginUI().start(stage);
            } else {
                msg.setText("Error creating account");
                msg.setVisible(true);

                ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
                st.setFromX(1);
                st.setToX(1.02);
                st.setCycleCount(2);
                st.setAutoReverse(true);
                st.play();
            }
        });
    }

    // ================= STYLE =================

    private String cardStyle() {
        return "-fx-background-color:" + CARD + ";" +
                "-fx-background-radius:18;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 20, 0, 0, 6);";
    }

    private String input() {
        return "-fx-background-color:#171717;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#2B2B2D;" +
                "-fx-border-radius:12;" +
                "-fx-padding:10 14;";
    }

    private String primaryButton() {
        return "-fx-background-color:#60A5FA;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:12;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:12;";
    }

    private String titleStyle() {
        return "-fx-text-fill:white;-fx-font-size:26px;-fx-font-weight:bold;";
    }

    private String subtitleStyle() {
        return "-fx-text-fill:#A1A1AA;-fx-font-size:13px;";
    }

    private String link() {
        return "-fx-text-fill:#A1A1AA;";
    }

    private void addEffects(Button b) {
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
}