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

        // ── Data ─────────────────────────────
        int totalBooks = BookController.ListBooks().size();

        // ── Top Bar (same style as dashboard) ─
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button backBtn = new Button("Back");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 13px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Title ─────────────────────────────
        Label title = new Label("Book Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Label subtitle = new Label("Add, update and manage library books");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

        // ── Table ─────────────────────────────
        setupColumns();
        loadBooks();

        table.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;"
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ── Inputs ────────────────────────────
        TextField titleField = createField("Title");
        TextField authorField = createField("Author");
        TextField categoryField = createField("Category");

        CheckBox availableCheck = new CheckBox("Available");

        // ── Image ─────────────────────────────
        Label imageLabel = new Label("No image selected");
        imageLabel.setStyle("-fx-text-fill: #888780;");

        Button selectImageBtn = createButton("Select Image", "#9b59b6");

        final String[] imagePath = {""};

        selectImageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Book Image");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                imagePath[0] = file.toURI().toString();
                imageLabel.setText(file.getName());
            }
        });

        // ── Buttons ───────────────────────────
        Button addBtn = createButton("Add", "#2C2C2A");
        Button updateBtn = createButton("Update", "#2C2C2A");
        Button deleteBtn = createButton("Delete", "#e74c3c");
        Button refreshBtn = createButton("Refresh", "#2C2C2A");

        // ── Form Card ─────────────────────────
        VBox form = new VBox(12,
                new Label("Manage Book"),
                titleField,
                authorField,
                categoryField,
                availableCheck,
                selectImageBtn,
                imageLabel,
                new HBox(10, addBtn, updateBtn)
        );

        form.setPadding(new Insets(20));
        form.setPrefWidth(280);
        form.setStyle(cardStyle());

        // ── Table Card ────────────────────────
        VBox tableBox = new VBox(10,
                title,
                subtitle,
                table,
                new HBox(10, deleteBtn, refreshBtn)
        );

        tableBox.setPadding(new Insets(20));
        tableBox.setStyle(cardStyle());

        // ── Layout ────────────────────────────
        HBox rootLayout = new HBox(20, tableBox, form);
        rootLayout.setPadding(new Insets(20));

        StackPane root = new StackPane(rootLayout);
        root.setStyle("-fx-background-color: #F1EFE8;");

        VBox layout = new VBox(topBar, root);

        // ── Selection (auto-fill) ─────────────
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                titleField.setText(selected.getTitle());
                authorField.setText(selected.getAuthor());
                categoryField.setText(selected.getCategory());
                availableCheck.setSelected(selected.getAvailable());

                imagePath[0] = selected.getImagePath();
                imageLabel.setText("Image loaded");
            }
        });

        // ── Actions ───────────────────────────

        addBtn.setOnAction(e -> {
            BookController.addBook(
                    titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    availableCheck.isSelected(),
                    imagePath[0]
            );

            loadBooks();
            clear(titleField, authorField, categoryField, availableCheck, imageLabel, imagePath);
        });

        updateBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                BookController.updateBook(
                        selected.getId(),
                        titleField.getText(),
                        authorField.getText(),
                        categoryField.getText(),
                        availableCheck.isSelected(),
                        imagePath[0]
                );

                loadBooks();
                clear(titleField, authorField, categoryField, availableCheck, imageLabel, imagePath);
            }
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
        Scene scene = new Scene(layout, 900, 550);
        stage.setScene(scene);
        stage.setTitle("Book Management");
        stage.show();
    }

    // ── Helpers ─────────────────────────────

    private TextField createField(String placeholder) {
        TextField f = new TextField();
        f.setPromptText(placeholder);
        f.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-padding: 8;"
        );
        return f;
    }

    private Button createButton(String text, String color) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10 15;" +
                        "-fx-font-weight: bold;"
        );

        b.setOnMouseEntered(e -> {
            b.setScaleX(1.03);
            b.setScaleY(1.03);
        });

        b.setOnMouseExited(e -> {
            b.setScaleX(1);
            b.setScaleY(1);
        });

        return b;
    }

    private String cardStyle() {
        return """
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-border-radius: 16;
            -fx-border-color: #E5E3DA;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 20, 0, 0, 4);
        """;
    }

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
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAvailable())
        );

        table.getColumns().addAll(idCol, titleCol, authorCol, categoryCol, availCol);
    }

    private void loadBooks() {
        data.clear();
        List<Book> books = BookController.ListBooks();
        data.addAll(books);
        table.setItems(data);
    }

    private void clear(TextField t, TextField a, TextField c,
                       CheckBox av, Label img, String[] path) {

        t.clear();
        a.clear();
        c.clear();
        av.setSelected(false);
        img.setText("No image selected");
        path[0] = "";
    }
}