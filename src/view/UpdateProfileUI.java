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
import model.User;
import util.Session;

public class UpdateProfileUI {

    // ================= THEME =================
    private static final String BG = "#0F0F10";
    private static final String CARD = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";
    private static final String TEXT = "#FFFFFF";
    private static final String MUTED = "#A1A1AA";
    private static final String BLUE = "#60A5FA";

    public void show(Stage stage) {

        // ================= USER =================
        User user = UserController.getUserById(Session.userId);

        // ================= BACK BUTTON =================
        Button backBtn = new Button("← Back");
        backBtn.setStyle(backStyle());

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backHoverStyle()));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backStyle()));

        backBtn.setOnAction(e ->
                new UserDashboardUI().show(stage)
        );

        // ================= TITLE =================
        Label title = new Label("Update Profile");
        title.setStyle(titleStyle());

        Label subtitle = new Label("Edit your personal information");
        subtitle.setStyle(subStyle());

        // ================= FIELDS =================
        TextField nameField = new TextField(user.getName());
        TextField emailField = new TextField(user.getEmail());
        PasswordField passwordField = new PasswordField();

        nameField.setStyle(inputStyle());
        emailField.setStyle(inputStyle());
        passwordField.setStyle(inputStyle());

        nameField.setPromptText("Full name");
        emailField.setPromptText("Email");
        passwordField.setPromptText("New password");

        Label msg = new Label();
        msg.setVisible(false);
        msg.setStyle("-fx-text-fill:#EF4444;");

        // ================= SAVE BUTTON =================
        Button saveBtn = new Button("Save Changes");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle(primaryButton());

        addButtonEffects(saveBtn);

        // ================= CARD =================
        VBox card = new VBox(14,
                backBtn,
                title,
                subtitle,
                nameField,
                emailField,
                passwordField,
                msg,
                saveBtn
        );

        card.setPadding(new Insets(30));
        card.setMaxWidth(450);
        card.setStyle(cardStyle());

        // ================= ROOT =================
        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: " + BG + ";");

        Scene scene = new Scene(root, 800, 550);
        stage.setScene(scene);
        stage.setTitle("Update Profile");
        stage.show();

        entrance(card);

        // ================= UPDATE LOGIC =================
        saveBtn.setOnAction(e -> {

            if (nameField.getText().isEmpty()
                    || emailField.getText().isEmpty()
                    || passwordField.getText().isEmpty()) {

                msg.setText("All fields are required");
                msg.setVisible(true);
                return;
            }

            boolean ok = UserController.updateUser(
                    Session.userId,
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText()
            );

            if (ok) {

                msg.setStyle("-fx-text-fill:#22C55E;");
                msg.setText("Profile updated successfully");
                msg.setVisible(true);

                ScaleTransition st =
                        new ScaleTransition(Duration.millis(120), card);

                st.setFromX(1);
                st.setToX(1.02);
                st.setCycleCount(2);
                st.setAutoReverse(true);
                st.play();

            } else {

                msg.setStyle("-fx-text-fill:#EF4444;");
                msg.setText("Update failed");
                msg.setVisible(true);

                shake(card);
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

    private String inputStyle() {
        return "-fx-background-color:#171717;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#2B2B2D;" +
                "-fx-border-radius:12;" +
                "-fx-padding:10 14;";
    }

    private String primaryButton() {
        return "-fx-background-color:" + BLUE + ";" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:12;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:12;" +
                "-fx-cursor: hand;";
    }

    private String titleStyle() {
        return "-fx-text-fill:white;-fx-font-size:26px;-fx-font-weight:bold;";
    }

    private String subStyle() {
        return "-fx-text-fill:#A1A1AA;-fx-font-size:13px;";
    }

    // ================= BACK BUTTON =================

    private String backStyle() {
        return "-fx-background-color: transparent;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;";
    }

    private String backHoverStyle() {
        return "-fx-background-color: rgba(96,165,250,0.12);" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;";
    }

    // ================= EFFECTS =================

    private void addButtonEffects(Button b) {
        b.setOnMouseEntered(e -> {
            b.setScaleX(1.03);
            b.setScaleY(1.03);
        });
        b.setOnMouseExited(e -> {
            b.setScaleX(1);
            b.setScaleY(1);
        });
    }

    private void entrance(VBox card) {
        card.setOpacity(0);
        card.setTranslateY(20);

        TranslateTransition tt =
                new TranslateTransition(Duration.millis(400), card);

        tt.setToY(0);
        card.setOpacity(1);
        tt.play();
    }

    private void shake(VBox card) {
        TranslateTransition t =
                new TranslateTransition(Duration.millis(60), card);

        t.setByX(8);
        t.setCycleCount(6);
        t.setAutoReverse(true);
        t.play();
    }
}