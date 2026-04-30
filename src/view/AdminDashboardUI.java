package view;

import controller.BookController;
import controller.UserController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardUI {

    public void show(Stage stage) {

        // ===== STATS =====
        int totalBooks = BookController.ListBooks().size();
        int totalUsers = UserController.listUsers().size();

        Label title = new Label("Admin Dashboard");
        Label booksStat = new Label("Total Books: " + totalBooks);
        Label usersStat = new Label("Total Users: " + totalUsers);

        // ===== BUTTONS =====
        Button manageBooks = new Button("Manage Books");
        Button manageUsers = new Button("Manage Users");
        Button logout = new Button("Logout");

        // NAVIGATION
        manageBooks.setOnAction(e -> {
            new BookManagementUI().show(stage);
        });

        manageUsers.setOnAction(e -> {
            UserManagementUI userManagementUI = new UserManagementUI();
            userManagementUI.show(stage);
        });

        logout.setOnAction(e -> {
            new LoginUI().start(stage);
        });

        VBox root = new VBox(15, title, booksStat, usersStat, manageBooks, manageUsers, logout);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}