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
    private TableView<Book> borrowedTable = new TableView<>();

    private ObservableList<Book> data = FXCollections.observableArrayList();
    private ObservableList<Book> borrowedData = FXCollections.observableArrayList();

    public void show(Stage stage) {

        Label title1 = new Label("Available Books");
        Label title2 = new Label("My Borrowed Books");

        setupColumns(table);
        setupColumns(borrowedTable);

        loadAvailableBooks();
        loadBorrowedBooks();

        // ================= BUTTONS =================
        Button borrowBtn = new Button("Borrow");
        Button returnBtn = new Button("Return");
        Button refreshBtn = new Button("Refresh");
        Button logoutBtn = new Button("Logout");

        // ================= BORROW =================
        borrowBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {

                boolean ok = BorrowController.borrowBook(
                        Session.userId,
                        selected.getId()
                );

                if (ok) {
                    loadAvailableBooks();
                    loadBorrowedBooks();
                    System.out.println("Book borrowed successfully");
                }
            }
        });

        // ================= RETURN =================
        returnBtn.setOnAction(e -> {
            Book selected = borrowedTable.getSelectionModel().getSelectedItem();

            if (selected != null) {

                int borrowId = BorrowController.getBorrowId(
                        Session.userId,
                        selected.getId()
                );

                boolean ok = BorrowController.returnBook(
                        borrowId,
                        selected.getId()
                );

                if (ok) {
                    loadAvailableBooks();
                    loadBorrowedBooks();
                    System.out.println("Book returned successfully");
                }
            }
        });

        // ================= REFRESH =================
        refreshBtn.setOnAction(e -> {
            loadAvailableBooks();
            loadBorrowedBooks();
        });

        // ================= LOGOUT =================
        logoutBtn.setOnAction(e -> {
            new LoginUI().start(stage);
        });

        // ================= LAYOUT =================
        VBox left = new VBox(10, title1, table, borrowBtn);
        VBox right = new VBox(10, title2, borrowedTable, returnBtn);

        HBox tables = new HBox(20, left, right);

        HBox topBar = new HBox(10, refreshBtn, logoutBtn);

        VBox root = new VBox(15, topBar, tables);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("User Dashboard");
        stage.show();
    }

    // ================= SETUP COLUMNS =================
    private void setupColumns(TableView<Book> table) {

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

        table.getColumns().addAll(idCol, titleCol, authorCol);
    }

    // ================= LOAD AVAILABLE =================
    private void loadAvailableBooks() {
        data.clear();
        List<Book> books = BookController.getAvailableBooks();
        data.addAll(books);
        table.setItems(data);
    }

    // ================= LOAD BORROWED =================
    private void loadBorrowedBooks() {
        borrowedData.clear();
        List<Book> books = BorrowController.getUserBorrowedBooks(Session.userId);
        borrowedData.addAll(books);
        borrowedTable.setItems(borrowedData);
    }
}