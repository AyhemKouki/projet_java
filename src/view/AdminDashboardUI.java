package view;

import controller.LibraryItemController;
import controller.UserController;
import model.Book;
import model.LibraryItem;
import model.Magazine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardUI {

    public void show(Stage stage) {

        // ── DATA ─────────────────────────────
        List<LibraryItem> items = LibraryItemController.getAllItems();

        int totalItems = items.size();

        int totalBooks = (int) items.stream()
                .filter(i -> i instanceof Book)
                .count();

        int totalMagazines = (int) items.stream()
                .filter(i -> i instanceof Magazine)
                .count();

        int totalUsers = UserController.listUsers().size();

        // ── TOP BAR ──────────────────────────
        Label brand = new Label("📚 Librarium Admin");

        brand.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2C2C2A;"
        );

        Button logoutBtn = new Button("Logout");

        logoutBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 13px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, logoutBtn);

        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        topBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #E5E3DA;"
        );

        // ── TITLE ────────────────────────────
        Label title = new Label("Dashboard");

        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2C2C2A;"
        );

        Label subtitle = new Label("Overview of library statistics");

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #888780;"
        );

        // ── STAT CARDS ───────────────────────
        VBox itemsCard = createStatCard(
                "Total Items",
                String.valueOf(totalItems)
        );

        VBox booksCard = createStatCard(
                "Books",
                String.valueOf(totalBooks)
        );

        VBox magazinesCard = createStatCard(
                "Magazines",
                String.valueOf(totalMagazines)
        );

        VBox usersCard = createStatCard(
                "Users",
                String.valueOf(totalUsers)
        );

        HBox statsRow = new HBox(
                20,
                itemsCard,
                booksCard,
                magazinesCard,
                usersCard
        );

        statsRow.setAlignment(Pos.CENTER);

        // ── PIE CHART ────────────────────────
        PieChart pieChart = new PieChart();

        pieChart.getData().add(
                new PieChart.Data("Books", totalBooks)
        );

        pieChart.getData().add(
                new PieChart.Data("Magazines", totalMagazines)
        );

        pieChart.setTitle("Library Content");

        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(260);

        // ── TOP 5 CATEGORIES BAR CHART ──────

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Category");
        yAxis.setLabel("Books");

        BarChart<String, Number> barChart =
                new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Top 5 Book Categories");

        // Count books by category
        Map<String, Long> categoryStats = items.stream()
                .filter(i -> i instanceof Book)
                .map(i -> (Book) i)
                .collect(Collectors.groupingBy(
                        Book::getCategory,
                        Collectors.counting()
                ));

        // Sort descending and keep top 5
        List<Map.Entry<String, Long>> topCategories =
                categoryStats.entrySet()
                        .stream()
                        .sorted((a, b) ->
                                Long.compare(
                                        b.getValue(),
                                        a.getValue()
                                )
                        )
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

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setPrefHeight(320);

        // ── ACTION BUTTONS ───────────────────
        Button manageBooks =
                createPrimaryButton("Manage Books");

        Button manageMagazines =
                createPrimaryButton("Manage Magazines");

        Button manageUsers =
                createPrimaryButton("Manage Users");

        VBox actions = new VBox(
                12,
                manageBooks,
                manageMagazines,
                manageUsers
        );

        // ── MAIN CARD ────────────────────────
        VBox mainCard = new VBox(
                25,
                title,
                subtitle,
                statsRow,
                new Separator(),
                pieChart,
                barChart,
                new Separator(),
                actions
        );

        mainCard.setPadding(new Insets(30));

        mainCard.setMaxWidth(950);

        mainCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
        );

        // Hover effect
        mainCard.setOnMouseEntered(e ->
                mainCard.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: 16;" +
                                "-fx-border-color: #E5E3DA;" +
                                "-fx-border-radius: 16;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 30, 0, 0, 6);"
                )
        );

        mainCard.setOnMouseExited(e ->
                mainCard.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: 16;" +
                                "-fx-border-color: #E5E3DA;" +
                                "-fx-border-radius: 16;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 24, 0, 0, 4);"
                )
        );

        // ── ROOT ─────────────────────────────
        ScrollPane scrollPane = new ScrollPane(mainCard);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: #F1EFE8;"
        );

        StackPane root = new StackPane(scrollPane);

        root.setPadding(new Insets(40));

        root.setStyle(
                "-fx-background-color: #F1EFE8;"
        );

        VBox layout = new VBox(topBar, root);

        // ── BUTTON ACTIONS ───────────────────
        manageBooks.setOnAction(
                e -> new BookManagementUI().show(stage)
        );

        manageMagazines.setOnAction(
                e -> new MagazineManagementUI().show(stage)
        );

        manageUsers.setOnAction(
                e -> new UserManagementUI().show(stage)
        );

        logoutBtn.setOnAction(
                e -> new LoginUI().start(stage)
        );

        // ── SCENE ────────────────────────────
        Scene scene = new Scene(layout, 1100, 850);

        stage.setScene(scene);

        stage.setTitle("Librarium — Admin Dashboard");

        stage.show();

        // ── APPLY COLORS AFTER SHOW ──────────
        applyChartColors(pieChart, barChart);
    }

    // ── STAT CARD ───────────────────────────
    private VBox createStatCard(
            String title,
            String value
    ) {

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #888780;"
        );

        Label valueLabel = new Label(value);

        valueLabel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2C2C2A;"
        );

        VBox box = new VBox(
                5,
                titleLabel,
                valueLabel
        );

        box.setPadding(new Insets(15));

        box.setPrefWidth(160);

        box.setAlignment(Pos.CENTER_LEFT);

        box.setStyle(
                "-fx-background-color: #F1EFE8;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E5E3DA;" +
                        "-fx-border-radius: 12;"
        );

        return box;
    }

    // ── BUTTON STYLE ────────────────────────
    private Button createPrimaryButton(String text) {

        Button btn = new Button(text);

        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color: #2C2C2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 12;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;"
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

    // ── CHART COLORS ───────────────────────
    private void applyChartColors(
            PieChart pieChart,
            BarChart<String, Number> barChart
    ) {

        // Pie chart colors
        for (PieChart.Data data : pieChart.getData()) {

            if (data.getName().equals("Books")) {

                data.getNode().setStyle(
                        "-fx-pie-color: #2C2C2A;"
                );

            } else {

                data.getNode().setStyle(
                        "-fx-pie-color: #888780;"
                );
            }
        }

        // Bar chart colors
        for (XYChart.Series<String, Number> s
                : barChart.getData()) {

            for (XYChart.Data<String, Number> data
                    : s.getData()) {

                data.getNode().setStyle(
                        "-fx-bar-fill: #2C2C2A;"
                );
            }
        }
    }
}