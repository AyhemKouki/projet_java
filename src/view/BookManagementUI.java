package view;

import controller.BookController;
import model.Book;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class BookManagementUI {

    private TableView<Book> table = new TableView<>();
    private ObservableList<Book> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ── Top Bar ──────────────────────────
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button backBtn = new Button("← Back");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtn);
        topBar.setPadding(new Insets(10));

        // ── Table ────────────────────────────
        setupColumns();
        loadBooks();

        // ── Inputs ───────────────────────────
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField availableField = new TextField();
        availableField.setPromptText("Available (1/0)");

        // ── Image Picker ─────────────────────
        Label imageLabel = new Label("No image selected");
        Button selectImageBtn = new Button("Select Image");

        final String[] imagePath = {""};

        selectImageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Book Image");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                imagePath[0] = file.toURI().toString(); // important
                imageLabel.setText(file.getName());
            }
        });

        // ── Buttons ──────────────────────────
        Button addBtn = new Button("Add Book");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        // ── Layout ───────────────────────────
        VBox form = new VBox(10,
                new Label("Add Book"),
                titleField,
                authorField,
                categoryField,
                availableField,
                selectImageBtn,
                imageLabel,
                addBtn
        );

        VBox tableBox = new VBox(10, table, new HBox(10, deleteBtn, refreshBtn));

        HBox rootLayout = new HBox(20, tableBox, form);
        rootLayout.setPadding(new Insets(20));

        VBox root = new VBox(topBar, rootLayout);

        // ── Actions ──────────────────────────
        addBtn.setOnAction(e -> {

            BookController.addBook(
                    titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    availableField.getText().equals("1"),
                    imagePath[0]
            );

            loadBooks();

            // reset fields
            titleField.clear();
            authorField.clear();
            categoryField.clear();
            availableField.clear();
            imagePath[0] = "";
            imageLabel.setText("No image selected");
        });

        deleteBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                BookController.deleteBook(selected.getId());
                loadBooks();
            }
        });

        refreshBtn.setOnAction(e -> loadBooks());

        backBtn.setOnAction(e -> new AdminDashboardUI().show(stage));

        // ── Scene ────────────────────────────
        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);
        stage.setTitle("Book Management");
        stage.show();
    }

    // ── Table Columns ───────────────────────
    private void setupColumns() {

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
                new javafx.beans.property.SimpleObjectProperty(c.getValue().getAvailable())
        );

        table.getColumns().addAll(idCol, titleCol, authorCol, categoryCol, availCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ── Load Data ───────────────────────────
    private void loadBooks() {
        data.clear();
        List<Book> books = BookController.ListBooks();
        data.addAll(books);
        table.setItems(data);
    }
}