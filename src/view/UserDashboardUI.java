package view;

import Execption.MaximumBooksLimitException;
import controller.BorrowController;
import controller.LibraryItemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Book;
import model.LibraryItem;
import model.Magazine;
import util.Session;

import java.util.List;

public class UserDashboardUI {

    // ================= COLORS =================

    private static final String BG = "#0F0F10";
    private static final String SIDEBAR = "#171717";
    private static final String CARD = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";

    private static final String TEXT = "#F5F5F5";
    private static final String MUTED = "#A1A1AA";

    private static final String BLUE = "#60A5FA";

    private TilePane itemsGrid = new TilePane();

    public void show(Stage stage) {

        // ================= SIDEBAR =================

        VBox sidebar = new VBox(18);

        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(240);

        sidebar.setStyle("""
                -fx-background-color: #171717;
                -fx-border-color: #2B2B2D;
                -fx-border-width: 0 1 0 0;
                """);

        Label logo = new Label("📚 Librarium");

        logo.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                """);

        VBox nav = new VBox(12);

        Button dashboardBtn =
                createSidebarButton("Dashboard", true);

        Button myItemsBtn =
                createSidebarButton("My Borrowed Items", false);

        Button profileBtn =
                createSidebarButton("Profile", false);

        Button refreshBtn =
                createSidebarButton("Refresh", false);

        Button logoutBtn =
                createSidebarButton("Logout", false);

        nav.getChildren().addAll(
                dashboardBtn,
                myItemsBtn,
                profileBtn,
                refreshBtn,
                logoutBtn
        );

        Region spacer = new Region();

        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox profileCard = new VBox(5);

        profileCard.setPadding(new Insets(15));

        profileCard.setStyle(cardStyle());

        Label userLabel = new Label("Library User");

        userLabel.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                """);

        Label statusLabel = new Label("Active Member");

        statusLabel.setStyle("""
                -fx-text-fill: #A1A1AA;
                -fx-font-size: 13px;
                """);

        profileCard.getChildren().addAll(
                userLabel,
                statusLabel
        );

        sidebar.getChildren().addAll(
                logo,
                nav,
                spacer,
                profileCard
        );

        // ================= TOP BAR =================

        Label pageTitle = new Label(
                "Available Library Items"
        );

        pageTitle.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 30px;
                -fx-font-weight: bold;
                """);

        TextField searchField = new TextField();

        searchField.setPromptText(
                "Search books..."
        );

        searchField.setPrefWidth(280);

        searchField.setStyle("""
                -fx-background-color: #1E1E1F;
                -fx-text-fill: white;
                -fx-prompt-text-fill: #71717A;
                -fx-background-radius: 12;
                -fx-border-color: #2B2B2D;
                -fx-border-radius: 12;
                -fx-padding: 10 14 10 14;
                """);

        Region topSpacer = new Region();

        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(
                15,
                pageTitle,
                topSpacer,
                searchField
        );

        topBar.setAlignment(Pos.CENTER_LEFT);

        // ================= GRID =================

        itemsGrid.setHgap(20);
        itemsGrid.setVgap(20);

        itemsGrid.setPrefColumns(4);

        loadItemsGrid();

        ScrollPane scrollPane =
                new ScrollPane(itemsGrid);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle("""
                -fx-background: transparent;
                -fx-background-color: transparent;
                """);

        VBox content = new VBox(
                22,
                topBar,
                scrollPane
        );

        content.setPadding(new Insets(30));

        content.setStyle("""
                -fx-background-color: #0F0F10;
                """);

        // ================= ACTIONS =================

        refreshBtn.setOnAction(e ->
                loadItemsGrid()
        );

        myItemsBtn.setOnAction(e ->
                showBorrowedItems(stage)
        );

        profileBtn.setOnAction(e ->
                new UpdateProfileUI().show(stage)
        );

        logoutBtn.setOnAction(e ->
                new LoginUI().start(stage)
        );

        // ================= ROOT =================

        BorderPane root = new BorderPane();

        root.setLeft(sidebar);

        root.setCenter(content);

        root.setStyle("""
                -fx-background-color: #0F0F10;
                """);

        Scene scene =
                new Scene(root, 1450, 900);

        stage.setScene(scene);

        stage.setTitle("Librarium Dashboard");

        stage.show();
    }

    // =====================================================
    // LOAD ITEMS
    // =====================================================

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

    // =====================================================
    // ITEM CARD
    // =====================================================

    private VBox createItemCard(LibraryItem item) {

        ImageView image = new ImageView();

        image.setImage(
                loadImage(item.getImagePath())
        );

        image.setFitWidth(160);
        image.setFitHeight(220);

        image.setPreserveRatio(true);

        Label title =
                new Label(item.getTitle());

        title.setWrapText(true);

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                """);

        Label info = new Label();

        if (item instanceof Book book) {

            info.setText(
                    "Author: " + book.getAuthor()
            );

        } else if (item instanceof Magazine magazine) {

            info.setText(
                    "Issue #" +
                            magazine.getIssueNumber()
            );
        }

        info.setWrapText(true);

        info.setStyle("""
                -fx-text-fill: #A1A1AA;
                -fx-font-size: 13px;
                """);

        Button borrowBtn =
                createPrimaryButton("Borrow");

        borrowBtn.setOnAction(e -> {

            try {

                boolean success =
                        BorrowController.borrowItem(
                                Session.userId,
                                item.getId()
                        );

                if (success) {

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.INFORMATION
                            );

                    alert.setTitle("Success");

                    alert.setHeaderText(null);

                    alert.setContentText(
                            "Item borrowed successfully!"
                    );

                    alert.showAndWait();

                    loadItemsGrid();
                }

            } catch (MaximumBooksLimitException ex) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.WARNING
                        );

                alert.setTitle("Borrow Limit");

                alert.setHeaderText(
                        "Maximum Limit Reached"
                );

                alert.setContentText(
                        ex.getMessage()
                );

                alert.showAndWait();
            }
        });

        VBox card = new VBox(
                14,
                image,
                title,
                info,
                borrowBtn
        );

        card.setAlignment(Pos.TOP_CENTER);

        card.setPadding(new Insets(18));

        card.setPrefWidth(250);

        card.setStyle(cardStyle());

        card.setOnMouseEntered(e -> {

            card.setScaleX(1.02);
            card.setScaleY(1.02);
        });

        card.setOnMouseExited(e -> {

            card.setScaleX(1);
            card.setScaleY(1);
        });

        return card;
    }

    // =====================================================
    // BORROWED ITEMS PAGE
    // =====================================================

    private void showBorrowedItems(Stage stage) {

        TilePane grid = new TilePane();

        grid.setPadding(new Insets(20));

        grid.setHgap(20);
        grid.setVgap(20);

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

            image.setFitWidth(160);
            image.setFitHeight(220);

            image.setPreserveRatio(true);

            Label title =
                    new Label(item.getTitle());

            title.setWrapText(true);

            title.setStyle("""
                    -fx-text-fill: white;
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                    """);

            Label info = new Label();

            if (item instanceof Book book) {

                info.setText(
                        "Author: " + book.getAuthor()
                );

            } else if (item instanceof Magazine magazine) {

                info.setText(
                        "Issue #" +
                                magazine.getIssueNumber()
                );
            }

            info.setStyle("""
                    -fx-text-fill: #A1A1AA;
                    -fx-font-size: 13px;
                    """);

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
                    14,
                    image,
                    title,
                    info,
                    returnBtn
            );

            card.setAlignment(Pos.TOP_CENTER);

            card.setPadding(new Insets(18));

            card.setPrefWidth(250);

            card.setStyle(cardStyle());

            grid.getChildren().add(card);
        }

        Button backBtn =
                createPrimaryButton("← Back");

        backBtn.setOnAction(e ->
                show(stage)
        );

        Label title = new Label(
                "My Borrowed Items"
        );

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 28px;
                -fx-font-weight: bold;
                """);

        VBox content = new VBox(
                20,
                backBtn,
                title,
                new ScrollPane(grid)
        );

        content.setPadding(new Insets(30));

        content.setStyle("""
                -fx-background-color: #0F0F10;
                """);

        Scene scene =
                new Scene(content, 1400, 900);

        stage.setScene(scene);
    }

    // =====================================================
    // IMAGE LOADER
    // =====================================================

    private Image loadImage(String path) {

        try {

            if (path == null || path.isEmpty()) {

                return new Image(
                        "https://via.placeholder.com/160x220"
                );
            }

            if (path.startsWith("http")) {

                return new Image(
                        path,
                        160,
                        220,
                        true,
                        true
                );
            }

            if (path.startsWith("file:")) {

                return new Image(
                        path,
                        160,
                        220,
                        true,
                        true
                );
            }

            return new Image(
                    getClass().getResourceAsStream(path)
            );

        } catch (Exception e) {

            return new Image(
                    "https://via.placeholder.com/160x220"
            );
        }
    }

    // =====================================================
    // PRIMARY BUTTON
    // =====================================================

    private Button createPrimaryButton(String text) {

        Button btn = new Button(text);

        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle("""
                -fx-background-color: #60A5FA;
                -fx-text-fill: white;
                -fx-background-radius: 12;
                -fx-padding: 12;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                """);

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

    // =====================================================
    // SIDEBAR BUTTON
    // =====================================================

    private Button createSidebarButton(
            String text,
            boolean active
    ) {

        Button btn = new Button(text);

        btn.setAlignment(Pos.CENTER_LEFT);

        btn.setMaxWidth(Double.MAX_VALUE);

        if (active) {

            btn.setStyle("""
                    -fx-background-color: #27272A;
                    -fx-text-fill: white;
                    -fx-background-radius: 10;
                    -fx-padding: 12 16 12 16;
                    -fx-font-size: 14px;
                    -fx-cursor: hand;
                    """);
        }

        else {

            btn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-text-fill: #A1A1AA;
                    -fx-background-radius: 10;
                    -fx-padding: 12 16 12 16;
                    -fx-font-size: 14px;
                    -fx-cursor: hand;
                    """);

            btn.setOnMouseEntered(e ->

                    btn.setStyle("""
                            -fx-background-color: #232326;
                            -fx-text-fill: white;
                            -fx-background-radius: 10;
                            -fx-padding: 12 16 12 16;
                            -fx-font-size: 14px;
                            -fx-cursor: hand;
                            """)
            );

            btn.setOnMouseExited(e ->

                    btn.setStyle("""
                            -fx-background-color: transparent;
                            -fx-text-fill: #A1A1AA;
                            -fx-background-radius: 10;
                            -fx-padding: 12 16 12 16;
                            -fx-font-size: 14px;
                            -fx-cursor: hand;
                            """)
            );
        }

        return btn;
    }

    // =====================================================
    // CARD STYLE
    // =====================================================

    private String cardStyle() {

        return """
                -fx-background-color: #1E1E1F;
                -fx-background-radius: 18;
                -fx-border-color: #2B2B2D;
                -fx-border-radius: 18;
                -fx-effect: dropshadow(
                    gaussian,
                    rgba(0,0,0,0.35),
                    20,
                    0,
                    0,
                    6
                );
                """;
    }
}