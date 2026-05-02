package view;

import controller.BookController;
import model.Book;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class BookManagementUI {

    private TableView<Book> table = new TableView<>();
    private ObservableList<Book> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ── Top Bar ──────────────────────────
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Table Setup ──────────────────────
        setupColumns();
        styleTable();

        loadBooks();

        // ── Section Title ────────────────────
        Label title = new Label("Book Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        // ── Input Styles ─────────────────────
        String inputStyle =
                "-fx-background-color: #F1EFE8;" +
                        "-fx-border-color: #D3D1C7;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 14;";

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle(inputStyle);

        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        authorField.setStyle(inputStyle);

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        categoryField.setStyle(inputStyle);

        TextField availableField = new TextField();
        availableField.setPromptText("Available (1/0)");
        availableField.setStyle(inputStyle);

        // ── Buttons ──────────────────────────
        Button addBtn = createPrimaryButton("Add Book");
        Button deleteBtn = createPrimaryButton("Delete");
        Button refreshBtn = createTopButton("Refresh");

        // ── Form Layout ──────────────────────
        VBox form = new VBox(10,
                new Label("Add New Book"),
                titleField,
                authorField,
                categoryField,
                availableField,
                addBtn
        );

        styleCard(form);

        // ── Table Card ───────────────────────
        VBox tableCard = new VBox(10, title, table);

        HBox actions = new HBox(10, deleteBtn, refreshBtn);
        tableCard.getChildren().add(actions);

        styleCard(tableCard);

        // ── Layout ───────────────────────────
        HBox content = new HBox(20, tableCard, form);
        content.setPadding(new Insets(20));

        VBox root = new VBox(topBar, content);
        root.setStyle("-fx-background-color: #F1EFE8;");

        // ── Actions ──────────────────────────
        addBtn.setOnAction(e -> {
            try {
                BookController.addBook(
                        titleField.getText(),
                        authorField.getText(),
                        categoryField.getText(),
                        availableField.getText().equals("1")
                );

                loadBooks();

                titleField.clear();
                authorField.clear();
                categoryField.clear();
                availableField.clear();

            } catch (Exception ex) {
                ex.printStackTrace();
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
        Scene scene = new Scene(root, 1000, 550);
        stage.setScene(scene);
        stage.setTitle("Librarium — Book Management");
        stage.show();
    }

    // ── Helpers ─────────────────────────────

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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void styleTable() {
        table.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 8;"
        );
    }

    private void styleCard(VBox box) {
        box.setPadding(new Insets(15));
        box.setPrefWidth(450);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10,0,0,2);"
        );
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10;" +
                        "-fx-font-weight: bold;"
        );

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

    private Button createTopButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;"
        );
        return btn;
    }

    private void loadBooks() {
        data.clear();
        List<Book> books = BookController.ListBooks();
        data.addAll(books);
        table.setItems(data);
    }
}