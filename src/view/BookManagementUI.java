package view;

import controller.LibraryItemController;
import model.Book;
import model.LibraryItem;

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

        // ── TOP BAR ─────────────────────────────
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555; -fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── TITLE ───────────────────────────────
        Label title = new Label("Book Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Label subtitle = new Label("Manage library books");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888780;");

        // ── TABLE ───────────────────────────────
        setupColumns();
        loadBooks();

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ── INPUTS ──────────────────────────────
        TextField titleField = createField("Title");
        TextField authorField = createField("Author");
        TextField categoryField = createField("Category");

        CheckBox availableCheck = new CheckBox("Available");

        // ── IMAGE ───────────────────────────────
        Label imageLabel = new Label("No image selected");

        Button selectImageBtn = createButton("Select Image", "#9b59b6");

        final String[] imagePath = {""};

        selectImageBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);

            if (file != null) {
                imagePath[0] = file.toURI().toString();
                imageLabel.setText(file.getName());
            }
        });

        // ── BUTTONS ─────────────────────────────
        Button addBtn = createButton("Add", "#2C2C2A");
        Button updateBtn = createButton("Update", "#2C2C2A");
        Button deleteBtn = createButton("Delete", "#e74c3c");
        Button refreshBtn = createButton("Refresh", "#2C2C2A");

        // ── FORM ────────────────────────────────
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

        // ── TABLE BOX ───────────────────────────
        VBox tableBox = new VBox(10,
                title,
                subtitle,
                table,
                new HBox(10, deleteBtn, refreshBtn)
        );

        tableBox.setPadding(new Insets(20));
        tableBox.setStyle(cardStyle());

        HBox rootLayout = new HBox(20, tableBox, form);
        rootLayout.setPadding(new Insets(20));

        VBox layout = new VBox(topBar, new StackPane(rootLayout));

        // ── TABLE SELECTION ─────────────────────
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                titleField.setText(selected.getTitle());
                authorField.setText(selected.getAuthor());
                categoryField.setText(selected.getCategory());
                availableCheck.setSelected(selected.getAvailable());
                imageLabel.setText("Image loaded");
                imagePath[0] = selected.getImagePath();
            }
        });

        // ── ACTIONS ─────────────────────────────

        addBtn.setOnAction(e -> {
            Book book = new Book(
                    0,
                    titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    availableCheck.isSelected(),
                    imagePath[0]
            );

            LibraryItemController.addItem(book);

            loadBooks();
            clear(titleField, authorField, categoryField, availableCheck, imageLabel, imagePath);
        });

        updateBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                selected.setTitle(titleField.getText());
                selected.setAuthor(authorField.getText());
                selected.setCategory(categoryField.getText());
                selected.setAvailable(availableCheck.isSelected());
                selected.setImagePath(imagePath[0]);

                LibraryItemController.updateItem(selected);

                loadBooks();
            }
        });

        deleteBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                LibraryItemController.deleteItem(selected.getId());
                loadBooks();
            }
        });

        refreshBtn.setOnAction(e -> loadBooks());
        backBtn.setOnAction(e -> new AdminDashboardUI().show(stage));

        // ── SCENE ───────────────────────────────
        Scene scene = new Scene(layout, 900, 550);
        stage.setScene(scene);
        stage.setTitle("Book Management");
        stage.show();
    }

    // ── LOAD DATA ────────────────────────────
    private void loadBooks() {

        data.clear();

        List<LibraryItem> items = LibraryItemController.getAllItems();

        for (LibraryItem item : items) {
            if (item instanceof Book book) {
                data.add(book);
            }
        }

        table.setItems(data);
    }

    // ── TABLE ────────────────────────────────
    private void setupColumns() {

        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAuthor()));

        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory()));

        TableColumn<Book, Boolean> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAvailable()));

        table.getColumns().addAll(idCol, titleCol, authorCol, categoryCol, availCol);
    }

    // ── HELPERS ──────────────────────────────
    private TextField createField(String placeholder) {
        TextField f = new TextField();
        f.setPromptText(placeholder);
        return f;
    }

    private Button createButton(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white;");
        return b;
    }

    private String cardStyle() {
        return "-fx-background-color:white; -fx-background-radius:16; -fx-border-color:#E5E3DA;";
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