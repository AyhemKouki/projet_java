package view;

import controller.BookController;
import controller.BorrowController;
import model.Book;
import util.Session;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class UserDashboardUI {

    private TableView<Book> table = new TableView<>();
    private TableView<Book> borrowedTable = new TableView<>();

    private ObservableList<Book> data = FXCollections.observableArrayList();
    private ObservableList<Book> borrowedData = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ── Top Bar ─────────────────────────────
        Label brand = new Label("📚 Librarium");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button refreshBtn = createTopButton("Refresh");
        Button profileBtn = createTopButton("Profile");
        Button logoutBtn = createTopButton("Logout");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(15, brand, spacer, refreshBtn, profileBtn, logoutBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Tables Setup ───────────────────────
        setupColumns(table);
        setupColumns(borrowedTable);

        styleTable(table);
        styleTable(borrowedTable);

        loadAvailableBooks();
        loadBorrowedBooks();

        // ── Left Card (Available Books) ────────
        Label title1 = createSectionTitle("Available Books");

        Button borrowBtn = createPrimaryButton("Borrow");

        VBox leftCard = new VBox(12, title1, table, borrowBtn);
        styleCard(leftCard);

        // ── Right Card (Borrowed Books) ───────
        Label title2 = createSectionTitle("My Borrowed Books");

        Button returnBtn = createPrimaryButton("Return");

        VBox rightCard = new VBox(12, title2, borrowedTable, returnBtn);
        styleCard(rightCard);

        HBox content = new HBox(20, leftCard, rightCard);
        content.setPadding(new Insets(20));

        // ── Actions ───────────────────────────
        borrowBtn.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (BorrowController.borrowBook(Session.userId, selected.getId())) {
                    loadAvailableBooks();
                    loadBorrowedBooks();
                }
            }
        });

        returnBtn.setOnAction(e -> {
            Book selected = borrowedTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int borrowId = BorrowController.getBorrowId(Session.userId, selected.getId());
                if (BorrowController.returnBook(borrowId, selected.getId())) {
                    loadAvailableBooks();
                    loadBorrowedBooks();
                }
            }
        });

        refreshBtn.setOnAction(e -> {
            loadAvailableBooks();
            loadBorrowedBooks();
        });

        profileBtn.setOnAction(e -> new UpdateProfileUI().show(stage));

        logoutBtn.setOnAction(e -> new LoginUI().start(stage));

        // ── Root ──────────────────────────────
        VBox root = new VBox(topBar, content);
        root.setStyle("-fx-background-color: #F1EFE8;");

        Scene scene = new Scene(root, 1000, 550);
        stage.setScene(scene);
        stage.setTitle("Librarium — Dashboard");
        stage.show();
    }

    // ── UI Helpers ───────────────────────────

    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");
        return label;
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-weight: bold;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #444441;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-weight: bold;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-weight: bold;"
        ));

        return btn;
    }

    private Button createTopButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 13px;"
        );
        return btn;
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

    private void styleTable(TableView<Book> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 8;"
        );
    }

    // ── Data ────────────────────────────────

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

    private void loadAvailableBooks() {
        data.clear();
        List<Book> books = BookController.getAvailableBooks();
        data.addAll(books);
        table.setItems(data);
    }

    private void loadBorrowedBooks() {
        borrowedData.clear();
        List<Book> books = BorrowController.getUserBorrowedBooks(Session.userId);
        borrowedData.addAll(books);
        borrowedTable.setItems(borrowedData);
    }
}