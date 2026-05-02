package view;

import controller.BookController;
import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminDashboardUI {

    public void show(Stage stage) {

        // ── Data ─────────────────────────────
        int totalBooks = BookController.ListBooks().size();
        int totalUsers = UserController.listUsers().size();

        // ── Top Bar ──────────────────────────
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 13px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, logoutBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Title ────────────────────────────
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Label subtitle = new Label("Overview of system statistics");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

        // ── Stats Cards ──────────────────────
        VBox booksCard = createStatCard("Total Books", String.valueOf(totalBooks));
        VBox usersCard = createStatCard("Total Users", String.valueOf(totalUsers));

        HBox statsRow = new HBox(20, booksCard, usersCard);

        // ── Actions ──────────────────────────
        Button manageBooks = createPrimaryButton("Manage Books");
        Button manageUsers = createPrimaryButton("Manage Users");

        VBox actions = new VBox(12, manageBooks, manageUsers);

        // ── Card Container ───────────────────
        VBox mainCard = new VBox(20,
                title,
                subtitle,
                statsRow,
                new Separator(),
                actions
        );

        mainCard.setPadding(new Insets(30));
        mainCard.setMaxWidth(500);
        mainCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
        );

        // hover shadow
        mainCard.setOnMouseEntered(e -> mainCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 30, 0, 0, 6);"
        ));

        mainCard.setOnMouseExited(e -> mainCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
        ));

        // ── Root ─────────────────────────────
        StackPane root = new StackPane(mainCard);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #F1EFE8;");

        VBox layout = new VBox(topBar, root);

        // ── Actions ──────────────────────────
        manageBooks.setOnAction(e -> new BookManagementUI().show(stage));
        manageUsers.setOnAction(e -> new UserManagementUI().show(stage));
        logoutBtn.setOnAction(e -> new LoginUI().start(stage));

        // ── Scene ────────────────────────────
        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Librarium — Admin Dashboard");
        stage.show();
    }

    // ── UI Helpers ──────────────────────────

    private VBox createStatCard(String title, String value) {

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        VBox box = new VBox(5, titleLabel, valueLabel);
        box.setPadding(new Insets(15));
        box.setPrefWidth(200);

        box.setStyle(
                "-fx-background-color: #F1EFE8;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;"
        );

        return box;
    }

    private Button createPrimaryButton(String text) {

        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 12;" +
                        "-fx-font-weight: bold;"
        );

        // hover animation
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
}