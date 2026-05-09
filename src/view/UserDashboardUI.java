package view;

import Execption.MaximumBooksLimitException;
import controller.BorrowController;
import controller.LibraryItemController;
import model.Book;
import model.LibraryItem;
import model.Magazine;
import util.Session;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class UserDashboardUI {

    private TilePane itemsGrid = new TilePane();

    public void show(Stage stage) {

        // ── TOP BAR ─────────────────────────────
        Label brand = new Label("📚 Librarium");

        brand.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2C2C2A;"
        );

        Button refreshBtn = createTopButton("Refresh");
        Button myItemsBtn = createTopButton("My Items");
        Button profileBtn = createTopButton("Profile");
        Button logoutBtn = createTopButton("Logout");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(
                15,
                brand,
                spacer,
                refreshBtn,
                myItemsBtn,
                profileBtn,
                logoutBtn
        );

        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        topBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #E5E3DA;"
        );

        // ── GRID ────────────────────────────────
        itemsGrid.setPadding(new Insets(20));
        itemsGrid.setHgap(15);
        itemsGrid.setVgap(15);
        itemsGrid.setPrefColumns(4);

        loadItemsGrid();

        Label title = createSectionTitle("Available Library Items");

        ScrollPane scroll = new ScrollPane(itemsGrid);
        scroll.setFitToWidth(true);

        VBox content = new VBox(12, title, scroll);

        content.setPadding(new Insets(20));

        styleCard(content);

        // ── ACTIONS ─────────────────────────────
        refreshBtn.setOnAction(e -> loadItemsGrid());

        myItemsBtn.setOnAction(e ->
                showBorrowedItems(stage)
        );

        profileBtn.setOnAction(e ->
                new UpdateProfileUI().show(stage)
        );

        logoutBtn.setOnAction(e ->
                new LoginUI().start(stage)
        );

        // ── ROOT ────────────────────────────────
        VBox root = new VBox(topBar, content);

        root.setStyle(
                "-fx-background-color: #F1EFE8;"
        );

        Scene scene = new Scene(root, 1000, 550);

        stage.setScene(scene);
        stage.setTitle("Librarium — Dashboard");
        stage.show();
    }

    // ── LOAD AVAILABLE ITEMS ──────────────────
    private void loadItemsGrid() {

        itemsGrid.getChildren().clear();

        List<LibraryItem> items =
                LibraryItemController.getAvailableItems();

        for (LibraryItem item : items) {

            itemsGrid.getChildren().add(
                    createItemCard(item)
            );
        }
    }

    // ── ITEM CARD ─────────────────────────────
    private VBox createItemCard(LibraryItem item) {

        ImageView image = new ImageView();

        image.setImage(
                loadImage(item.getImagePath())
        );

        image.setFitWidth(120);
        image.setFitHeight(160);
        image.setPreserveRatio(true);

        Label title = new Label(
                item.getTitle()
        );

        title.setStyle(
                "-fx-font-weight: bold;"
        );

        Label info = new Label();

        if (item instanceof Book book) {

            info.setText(
                    "Author: " + book.getAuthor()
            );

        } else if (item instanceof Magazine magazine) {

            info.setText(
                    "Issue #" + magazine.getIssueNumber()
            );
        }

        info.setStyle(
                "-fx-text-fill: #666;"
        );

        Button borrowBtn =
                createPrimaryButton("Borrow");

        borrowBtn.setOnAction(e -> {

            try {

                boolean success = BorrowController.borrowItem(
                        Session.userId,
                        item.getId()
                );

                if (success) {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Item borrowed successfully!");

                    alert.showAndWait();

                    loadItemsGrid();
                }

            } catch (MaximumBooksLimitException ex) {

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setTitle("Borrow Limit");
                alert.setHeaderText("Maximum Limit Reached");

                alert.setContentText(
                        ex.getMessage()
                );

                alert.showAndWait();
            }
        });

        VBox card = new VBox(
                8,
                image,
                title,
                info,
                borrowBtn
        );

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

    // ── BORROWED ITEMS ────────────────────────
    private void showBorrowedItems(Stage stage) {

        TilePane grid = new TilePane();

        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPrefColumns(4);

        List<LibraryItem> items =
                BorrowController.getUserBorrowedItems(
                        Session.userId
                );

        for (LibraryItem item : items) {

            ImageView image = new ImageView();

            image.setImage(
                    loadImage(item.getImagePath())
            );

            image.setFitWidth(120);
            image.setFitHeight(160);
            image.setPreserveRatio(true);

            Label title = new Label(
                    item.getTitle()
            );

            title.setStyle(
                    "-fx-font-weight: bold;"
            );

            Label info = new Label();

            if (item instanceof Book book) {

                info.setText(
                        "Author: " + book.getAuthor()
                );

            } else if (item instanceof Magazine magazine) {

                info.setText(
                        "Issue #" + magazine.getIssueNumber()
                );
            }

            info.setStyle(
                    "-fx-text-fill: #666;"
            );

            Button returnBtn =
                    createPrimaryButton("Return");

            returnBtn.setOnAction(e -> {

                int borrowId =
                        BorrowController.getBorrowId(
                                Session.userId,
                                item.getId()
                        );

                if (BorrowController.returnItem(
                        borrowId,
                        item.getId()
                )) {

                    showBorrowedItems(stage);
                }
            });

            VBox card = new VBox(
                    8,
                    image,
                    title,
                    info,
                    returnBtn
            );

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

        Button backBtn =
                createTopButton("← Back");

        backBtn.setOnAction(e ->
                show(stage)
        );

        VBox root = new VBox(
                10,
                backBtn,
                new ScrollPane(grid)
        );

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 900, 500);

        stage.setScene(scene);
    }

    // ── IMAGE LOADER ──────────────────────────
    private Image loadImage(String path) {

        try {

            if (path == null || path.isEmpty()) {

                return new Image(
                        "https://via.placeholder.com/120x160"
                );
            }

            // URL image
            if (path.startsWith("http")) {

                return new Image(
                        path,
                        120,
                        160,
                        true,
                        true
                );
            }

            // File URI image
            if (path.startsWith("file:")) {

                return new Image(
                        path,
                        120,
                        160,
                        true,
                        true
                );
            }

            // Resource image
            return new Image(
                    getClass().getResourceAsStream(path)
            );

        } catch (Exception e) {

            return new Image(
                    "https://via.placeholder.com/120x160"
            );
        }
    }

    // ── HELPERS ───────────────────────────────
    private Label createSectionTitle(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        return label;
    }

    private Button createPrimaryButton(String text) {

        Button btn = new Button(text);

        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color:#2C2C2A;" +
                        "-fx-text-fill:white;" +
                        "-fx-padding:10;"
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