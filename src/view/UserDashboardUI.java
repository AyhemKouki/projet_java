package view;

import controller.BookController;
import controller.BorrowController;
import model.Book;
import util.Session;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class UserDashboardUI {

    private TilePane booksGrid = new TilePane();

    public void show(Stage stage) {

        // ── Top Bar ─────────────────────────────
        Label brand = new Label("📚 Librarium");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C2C2A;");

        Button refreshBtn = createTopButton("Refresh");
        Button myBooksBtn = createTopButton("My Books");
        Button profileBtn = createTopButton("Profile");
        Button logoutBtn = createTopButton("Logout");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(15, brand, spacer, refreshBtn, myBooksBtn, profileBtn, logoutBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E3DA;");

        // ── Grid Config ─────────────────────────
        booksGrid.setPadding(new Insets(20));
        booksGrid.setHgap(15);
        booksGrid.setVgap(15);
        booksGrid.setPrefColumns(4);

        loadBooksGrid();

        // ── Content ─────────────────────────────
        Label title = createSectionTitle("Available Books");

        ScrollPane scroll = new ScrollPane(booksGrid);
        scroll.setFitToWidth(true);

        VBox content = new VBox(12, title, scroll);
        content.setPadding(new Insets(20));
        styleCard(content);

        // ── Actions ─────────────────────────────
        refreshBtn.setOnAction(e -> loadBooksGrid());

        myBooksBtn.setOnAction(e -> showBorrowedBooks(stage));

        profileBtn.setOnAction(e -> new UpdateProfileUI().show(stage));

        logoutBtn.setOnAction(e -> new LoginUI().start(stage));

        // ── Root ────────────────────────────────
        VBox root = new VBox(topBar, content);
        root.setStyle("-fx-background-color: #F1EFE8;");

        Scene scene = new Scene(root, 1000, 550);
        stage.setScene(scene);
        stage.setTitle("Librarium — Dashboard");
        stage.show();
    }

    // ── Load Books ────────────────────────────
    private void loadBooksGrid() {
        booksGrid.getChildren().clear();

        List<Book> books = BookController.getAvailableBooks();

        for (Book book : books) {
            booksGrid.getChildren().add(createBookCard(book));
        }
    }

    // ── Book Card ─────────────────────────────
    private VBox createBookCard(Book book) {

        ImageView image = new ImageView();

        try {
            image.setImage(new Image(book.getImagePath(), 120, 160, true, true));
        } catch (Exception e) {
            image.setImage(new Image("https://via.placeholder.com/120x160"));
        }

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-weight: bold;");

        Label author = new Label(book.getAuthor());
        author.setStyle("-fx-text-fill: #666;");

        Button borrowBtn = createPrimaryButton("Borrow");

        borrowBtn.setOnAction(e -> {
            if (BorrowController.borrowBook(Session.userId, book.getId())) {
                loadBooksGrid();
            }
        });

        VBox card = new VBox(8, image, title, author, borrowBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,2);"
        );

        return card;
    }

    // ── Borrowed Books Page ───────────────────
    private void showBorrowedBooks(Stage stage) {

        TilePane grid = new TilePane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPrefColumns(4);

        List<Book> books = BorrowController.getUserBorrowedBooks(Session.userId);

        for (Book book : books) {

            ImageView image = new ImageView();

            try {
                image.setImage(new Image(book.getImagePath(), 120, 160, true, true));
            } catch (Exception e) {
                image.setImage(new Image("https://via.placeholder.com/120x160"));
            }

            Label title = new Label(book.getTitle());
            title.setStyle("-fx-font-weight: bold;");

            Label author = new Label(book.getAuthor());
            author.setStyle("-fx-text-fill: #666;");

            Button returnBtn = createPrimaryButton("Return");

            returnBtn.setOnAction(e -> {
                int borrowId = BorrowController.getBorrowId(Session.userId, book.getId());
                if (BorrowController.returnBook(borrowId, book.getId())) {
                    showBorrowedBooks(stage);
                }
            });

            VBox card = new VBox(8, image, title, author, returnBtn);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(10));

            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: #E5E3DA;" +
                            "-fx-border-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,2);"
            );

            grid.getChildren().add(card);
        }

        Button backBtn = createTopButton("← Back");

        backBtn.setOnAction(e -> show(stage));

        VBox root = new VBox(10, backBtn, new ScrollPane(grid));
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);
    }

    // ── UI Helpers ───────────────────────────
    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
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

    private void styleCard(VBox box) {
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;"
        );
    }
}