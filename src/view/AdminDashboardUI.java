package view;

import controller.LibraryItemController;
import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Book;
import model.LibraryItem;
import model.Magazine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardUI {

    // ================= COLORS =================

    private static final String BG = "#0F0F10";
    private static final String SIDEBAR_BG = "#171717";
    private static final String CARD_BG = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";

    private static final String TEXT_PRIMARY = "#F5F5F5";
    private static final String TEXT_SECOND = "#A1A1AA";

    private static final String GREEN = "#A3E635";
    private static final String BLUE = "#60A5FA";
    private static final String ORANGE = "#FB923C";
    private static final String RED = "#F87171";

    public void show(Stage stage) {

        // ================= DATA =================

        List<LibraryItem> items =
                LibraryItemController.getAllItems();

        int totalItems = items.size();

        int books = (int) items.stream()
                .filter(i -> i instanceof Book)
                .count();

        int magazines = (int) items.stream()
                .filter(i -> i instanceof Magazine)
                .count();

        int users = UserController.listUsers().size();

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
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                """);

        VBox nav = new VBox(12);

        Button dashboardBtn =
                createSidebarButton("Dashboard", true);

        Button booksBtn =
                createSidebarButton("Books", false);

        Button magazinesBtn =
                createSidebarButton("Magazines", false);

        Button membersBtn =
                createSidebarButton("Members", false);

        Button reportsBtn =
                createSidebarButton("Reports", false);

        Button settingsBtn =
                createSidebarButton("Settings", false);

        nav.getChildren().addAll(
                dashboardBtn,
                booksBtn,
                magazinesBtn,
                membersBtn,
                reportsBtn,
                settingsBtn
        );

        Region sidebarSpacer = new Region();

        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox profileCard = new VBox(5);

        profileCard.setPadding(new Insets(15));
        profileCard.setStyle(cardStyle());

        Label adminLabel = new Label("Admin");

        adminLabel.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                """);

        Label roleLabel = new Label("Librarian");

        roleLabel.setStyle("""
                -fx-text-fill: #A1A1AA;
                -fx-font-size: 13px;
                """);

        profileCard.getChildren().addAll(
                adminLabel,
                roleLabel
        );

        sidebar.getChildren().addAll(
                logo,
                nav,
                sidebarSpacer,
                profileCard
        );

        // ================= TOP BAR =================

        Label title = new Label("Dashboard");

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 30px;
                -fx-font-weight: bold;
                """);

        TextField searchField = new TextField();

        searchField.setPromptText("Search books, members...");

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

        Button logoutBtn = new Button("Logout");

        logoutBtn.setStyle("""
                -fx-background-color: #27272A;
                -fx-text-fill: white;
                -fx-background-radius: 10;
                -fx-padding: 10 18 10 18;
                -fx-cursor: hand;
                -fx-font-weight: bold;
                """);

        HBox topBar = new HBox(
                15,
                title,
                topSpacer,
                searchField,
                logoutBtn
        );

        topBar.setAlignment(Pos.CENTER_LEFT);

        // ================= STATS =================

        HBox statsRow = new HBox(18);

        VBox totalCard =
                createStatCard(
                        "Total Items",
                        String.valueOf(totalItems),
                        GREEN
                );

        VBox booksCard =
                createStatCard(
                        "Books",
                        String.valueOf(books),
                        BLUE
                );

        VBox magazinesCard =
                createStatCard(
                        "Magazines",
                        String.valueOf(magazines),
                        ORANGE
                );

        VBox usersCard =
                createStatCard(
                        "Members",
                        String.valueOf(users),
                        RED
                );

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(booksCard, Priority.ALWAYS);
        HBox.setHgrow(magazinesCard, Priority.ALWAYS);
        HBox.setHgrow(usersCard, Priority.ALWAYS);

        statsRow.getChildren().addAll(
                totalCard,
                booksCard,
                magazinesCard,
                usersCard
        );

        // ================= BAR CHART =================

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Category");
        yAxis.setLabel("Books");

        xAxis.setTickLabelFill(Color.web(TEXT_SECOND));
        yAxis.setTickLabelFill(Color.web(TEXT_SECOND));

        BarChart<String, Number> barChart =
                new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Top 5 Book Categories");

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);

        barChart.setPrefHeight(320);

        barChart.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: white;
                """);
        xAxis.setStyle("""
        -fx-tick-label-font-size: 16px;
        """);

        // ================= CATEGORY LOGIC =================

        Map<String, Long> categoryStats =
                items.stream()
                        .filter(i -> i instanceof Book)
                        .map(i -> (Book) i)
                        .collect(Collectors.groupingBy(
                                Book::getCategory,
                                Collectors.counting()
                        ));

        List<Map.Entry<String, Long>> topCategories =
                categoryStats.entrySet()
                        .stream()
                        .sorted((a, b) ->
                                Long.compare(
                                        b.getValue(),
                                        a.getValue()
                                ))
                        .limit(5)
                        .toList();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        for (Map.Entry<String, Long> entry : topCategories) {

            series.getData().add(
                    new XYChart.Data<>(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        barChart.getData().add(series);

        // ================= PIE CHART =================

        PieChart pieChart = new PieChart();

        pieChart.getData().add(
                new PieChart.Data("Books", books)
        );

        pieChart.getData().add(
                new PieChart.Data("Magazines", magazines)
        );

        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);

        pieChart.setPrefHeight(300);

        pieChart.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: white;

                CHART_COLOR_1: #60A5FA;
                CHART_COLOR_2: #FB923C;
                """);

        // ================= CHART CARDS =================

        VBox chartCard = new VBox(15);

        Label chartTitle =
                new Label("Top Categories");

        chartTitle.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """);

        chartCard.getChildren().addAll(
                chartTitle,
                barChart
        );

        chartCard.setPadding(new Insets(22));

        chartCard.setStyle(cardStyle());

        HBox.setHgrow(chartCard, Priority.ALWAYS);

        VBox pieCard = new VBox(15);

        Label pieTitle =
                new Label("Collection Breakdown");

        pieTitle.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """);

        pieCard.getChildren().addAll(
                pieTitle,
                pieChart
        );

        pieCard.setPadding(new Insets(22));

        pieCard.setPrefWidth(350);

        pieCard.setStyle(cardStyle());

        HBox chartsSection =
                new HBox(18, chartCard, pieCard);

        // ================= QUICK ACTIONS =================

        VBox actionsCard = new VBox(14);

        Label quickTitle =
                new Label("Quick Actions");

        quickTitle.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """);

        Button manageBooksBtn =
                createActionButton("Manage Books");

        Button manageMagazinesBtn =
                createActionButton("Manage Magazines");

        Button manageUsersBtn =
                createActionButton("Manage Users");

        actionsCard.getChildren().addAll(
                quickTitle,
                manageBooksBtn,
                manageMagazinesBtn,
                manageUsersBtn
        );

        actionsCard.setPadding(new Insets(22));

        actionsCard.setStyle(cardStyle());

        // ================= MAIN CONTENT =================

        VBox content = new VBox(22);

        content.setPadding(new Insets(30));

        content.getChildren().addAll(
                topBar,
                statsRow,
                chartsSection,
                actionsCard
        );

        content.setStyle("""
                -fx-background-color: #0F0F10;
                """);

        // ================= ROOT =================

        BorderPane root = new BorderPane();

        root.setLeft(sidebar);

        root.setCenter(content);

        root.setStyle("""
                -fx-background-color: #0F0F10;
                """);

        // ================= SCENE =================

        Scene scene = new Scene(root, 1450, 900);

        stage.setScene(scene);

        stage.setTitle("Librarium Dashboard");

        stage.show();

        // ================= BAR COLORS =================

        for (XYChart.Series<String, Number> s :
                barChart.getData()) {

            for (XYChart.Data<String, Number> d :
                    s.getData()) {

                d.nodeProperty().addListener(
                        (obs, oldNode, node) -> {

                            if (node != null) {

                                node.setStyle("""
                                        -fx-bar-fill: #F59E0B;
                                        """);
                            }
                        });
            }
        }

        // ================= PIE COLORS =================

        pieChart.applyCss();

        for (PieChart.Data data : pieChart.getData()) {

            if (data.getName().equals("Books")) {

                data.getNode().setStyle(
                        "-fx-pie-color: #60A5FA;"
                );
            }

            else if (data.getName().equals("Magazines")) {

                data.getNode().setStyle(
                        "-fx-pie-color: #FB923C;"
                );
            }
        }

        // ================= ACTIONS =================

        manageBooksBtn.setOnAction(e ->
                new BookManagementUI().show(stage));

        manageMagazinesBtn.setOnAction(e ->
                new MagazineManagementUI().show(stage));

        manageUsersBtn.setOnAction(e ->
                new UserManagementUI().show(stage));

        logoutBtn.setOnAction(e ->
                new LoginUI().start(stage));
    }

    // =====================================================
    // STAT CARD
    // =====================================================

    private VBox createStatCard(
            String title,
            String value,
            String color
    ) {

        Label dot = new Label("●");

        dot.setStyle("""
                -fx-font-size: 18px;
                -fx-text-fill: """ + color + ";");

        Label valueLabel = new Label(value);

        valueLabel.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 34px;
                -fx-font-weight: bold;
                """);

        Label titleLabel = new Label(title);

        titleLabel.setStyle("""
                -fx-text-fill: #A1A1AA;
                -fx-font-size: 14px;
                """);

        VBox card = new VBox(
                12,
                dot,
                valueLabel,
                titleLabel
        );

        card.setPadding(new Insets(22));

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
    // ACTION BUTTON
    // =====================================================

    private Button createActionButton(String text) {

        Button btn = new Button(text);

        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle("""
                -fx-background-color: #27272A;
                -fx-text-fill: white;
                -fx-background-radius: 12;
                -fx-padding: 14;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                """);

        btn.setOnMouseEntered(e -> {

            btn.setScaleX(1.02);
            btn.setScaleY(1.02);
        });

        btn.setOnMouseExited(e -> {

            btn.setScaleX(1);
            btn.setScaleY(1);
        });

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