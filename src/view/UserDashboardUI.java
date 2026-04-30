package view;

import controller.BookController;
import controller.BorrowController;
import model.Book;
import util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class UserDashboardUI {

    private TableView<Book> table = new TableView<>();
    private ObservableList<Book> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ================= TITLE =================
        Label title = new Label("Available Books");

        // ================= TABLE =================
        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId())
        );

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle())
        );

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getAuthor())
        );

        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory())
        );

        TableColumn<Book, Boolean> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAvailable())
        );

        table.getColumns().addAll(idCol, titleCol, authorCol, categoryCol, availCol);

        loadBooks();

        // ================= BUTTONS =================
        Button borrowBtn = new Button("Borrow Selected");
        Button refreshBtn = new Button("Refresh");
        Button logoutBtn = new Button("Logout");

        // ================= BORROW =================
        borrowBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();

            if (selected != null && selected.getAvailable()) {

                boolean ok = BorrowController.borrowBook(
                        Session.userId,
                        selected.getId()
                );

                if (ok) {
                    loadBooks();
                    System.out.println("Book borrowed successfully");
                }

            } else {
                System.out.println("Book not available");
            }
        });

        // ================= REFRESH =================
        refreshBtn.setOnAction(e -> loadBooks());

        // ================= LOGOUT =================
        logoutBtn.setOnAction(e -> {
            new LoginUI().start(stage);
        });

        HBox actions = new HBox(10, borrowBtn, refreshBtn, logoutBtn);

        VBox root = new VBox(10, title, table, actions);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 800, 450));
        stage.setTitle("User Dashboard");
        stage.show();
    }

    // ================= LOAD BOOKS =================
    private void loadBooks() {
        data.clear();
        List<Book> books = BookController.ListBooks();
        data.addAll(books);
        table.setItems(data);
    }
}