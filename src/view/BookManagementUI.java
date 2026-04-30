package view;

import controller.BookController;
import model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class BookManagementUI {

    private TableView<Book> table = new TableView<>();
    private ObservableList<Book> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

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

        // ================= INPUT FIELDS =================
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField availableField = new TextField();
        availableField.setPromptText("Available (1/0)");

        // ================= BUTTONS =================
        Button addBtn = new Button("Add Book");
        Button deleteBtn = new Button("Delete Selected");
        Button refreshBtn = new Button("Refresh");
        Button backBtn = new Button("Back");

        // ================= ADD BOOK =================
        addBtn.setOnAction(e -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String category = categoryField.getText();
                boolean available = availableField.getText().equals("1");

                BookController.addBook(title, author, category, available);

                loadBooks();

                titleField.clear();
                authorField.clear();
                categoryField.clear();
                availableField.clear();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ================= DELETE BOOK =================
        deleteBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                BookController.deleteBook(selected.getId());
                loadBooks();
            }
        });

        // ================= REFRESH =================
        refreshBtn.setOnAction(e -> {
            try {
                Thread.sleep(50);
                loadBooks();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // ================= BACK =================
        backBtn.setOnAction(e -> {
            new AdminDashboardUI().show(stage);
        });

        // ================= LAYOUT =================
        HBox form = new HBox(10, titleField, authorField, categoryField, availableField, addBtn);
        HBox actions = new HBox(10, deleteBtn, refreshBtn ,backBtn);

        VBox root = new VBox(10, table, form, actions);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 800, 450));
        stage.setTitle("Book Management System");
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