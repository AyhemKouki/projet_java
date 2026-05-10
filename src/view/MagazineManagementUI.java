package view;

import controller.LibraryItemController;
import model.LibraryItem;
import model.Magazine;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MagazineManagementUI {

    // ================= THEME =================
    private static final String BG = "#0F0F10";
    private static final String CARD = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";
    private static final String BLUE = "#60A5FA";

    private TableView<Magazine> table = new TableView<>();
    private ObservableList<Magazine> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ================= TOP BAR =================
        Label brand = new Label("📚 Librarium Admin");
        brand.setStyle(brandStyle());

        Button backBtn = new Button("← Back");
        backBtn.setStyle(backStyle());

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backHover()));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backStyle()));
        backBtn.setOnAction(e -> new AdminDashboardUI().show(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, brand, spacer, backBtn);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color:" + BG + "; -fx-border-color:" + BORDER + ";");

        // ================= TITLE =================
        Label title = new Label("Magazine Management");
        title.setStyle(titleStyle());

        Label subtitle = new Label("Manage library magazines");
        subtitle.setStyle(subtitleStyle());

        // ================= TABLE =================
        setupColumns();
        loadMagazines();

        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle(tableStyle());

        // same as Book UI (no hacks)
        table.setRowFactory(tv -> {
            TableRow<Magazine> row = new TableRow<>();
            row.setStyle(
                    "-fx-background-color:#1E1E1F;" +
                            "-fx-text-fill:white;"
            );
            return row;
        });

        // ================= FORM =================
        TextField titleField = input("Title");
        TextField issueField = input("Issue Number");

        CheckBox available = new CheckBox("Available");
        available.setStyle("-fx-text-fill:#A1A1AA;");

        Label imageLabel = new Label("No image selected");
        imageLabel.setStyle("-fx-text-fill:#A1A1AA;");

        String[] imagePath = {""};

        Button imgBtn = smallBtn("Select Image");

        imgBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);

            if (file != null) {
                imagePath[0] = file.toURI().toString();
                imageLabel.setText(file.getName());
            }
        });

        Button addBtn = primaryBtn("Add");
        Button updateBtn = primaryBtn("Update");
        Button deleteBtn = dangerBtn("Delete");
        Button refreshBtn = smallBtn("Refresh");

        // ================= ACTIONS =================
        addBtn.setOnAction(e -> {
            try {
                Magazine m = new Magazine(
                        0,
                        titleField.getText(),
                        Integer.parseInt(issueField.getText()),
                        available.isSelected(),
                        imagePath[0]
                );

                LibraryItemController.addItem(m);
                loadMagazines();
                clear(titleField, issueField, available, imageLabel, imagePath);

            } catch (Exception ex) {
                showAlert("Invalid issue number!");
            }
        });

        updateBtn.setOnAction(e -> {
            Magazine selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                try {
                    selected.setTitle(titleField.getText());
                    selected.setIssueNumber(Integer.parseInt(issueField.getText()));
                    selected.setAvailable(available.isSelected());
                    selected.setImagePath(imagePath[0]);

                    LibraryItemController.updateItem(selected);
                    loadMagazines();

                } catch (Exception ex) {
                    showAlert("Invalid issue number!");
                }
            }
        });

        deleteBtn.setOnAction(e -> {
            Magazine selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                LibraryItemController.deleteItem(selected.getId());
                loadMagazines();
            }
        });

        refreshBtn.setOnAction(e -> loadMagazines());

        // ================= FORM =================
        VBox form = new VBox(12,
                new Label("Magazine Form"),
                titleField,
                issueField,
                available,
                imgBtn,
                imageLabel,
                new HBox(10, addBtn, updateBtn),
                deleteBtn,
                refreshBtn
        );

        form.setPadding(new Insets(20));
        form.setPrefWidth(300);
        form.setStyle(cardStyle());

        // ================= TABLE CARD =================
        VBox tableBox = new VBox(10,
                title,
                subtitle,
                table
        );

        tableBox.setPadding(new Insets(20));
        tableBox.setStyle(cardStyle());

        // ================= LAYOUT =================
        HBox content = new HBox(20, tableBox, form);
        content.setPadding(new Insets(20));

        VBox root = new VBox(topBar, content);
        root.setStyle("-fx-background-color:" + BG + ";");

        // ================= SELECT =================
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                titleField.setText(sel.getTitle());
                issueField.setText(String.valueOf(sel.getIssueNumber()));
                available.setSelected(sel.getAvailable());
                imageLabel.setText("Loaded");
                imagePath[0] = sel.getImagePath();
            }
        });

        Scene scene = new Scene(root, 1000, 650);
        stage.setScene(scene);
        stage.setTitle("Magazine Management");
        stage.show();
    }

    // ================= DATA =================
    private void loadMagazines() {
        data.clear();

        List<LibraryItem> items = LibraryItemController.getAllItems();

        for (LibraryItem i : items) {
            if (i instanceof Magazine m) {
                data.add(m);
            }
        }
    }

    // ================= TABLE COLUMNS =================
    private void setupColumns() {

        TableColumn<Magazine, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getId())
        );

        TableColumn<Magazine, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTitle())
        );

        TableColumn<Magazine, Integer> issueCol = new TableColumn<>("Issue");
        issueCol.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getIssueNumber())
        );

        TableColumn<Magazine, Boolean> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getAvailable())
        );

        table.getColumns().clear();
        table.getColumns().addAll(
                idCol,
                titleCol,
                issueCol,
                availableCol
        );
    }

    // ================= STYLE =================
    private String tableStyle() {
        return "-fx-background-color:#1E1E1F;" +
                "-fx-control-inner-background:#1E1E1F;" +
                "-fx-table-cell-border-color:#2B2B2D;" +
                "-fx-text-fill:white;" +
                "-fx-text-background-color:white;" +
                "-fx-base:#1E1E1F;" +
                "-fx-selection-bar:#27272A;" +
                "-fx-selection-bar-non-focused:#27272A;";
    }

    private String cardStyle() {
        return "-fx-background-color:" + CARD + ";" +
                "-fx-background-radius:18;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:18;";
    }

    private TextField input(String p) {
        TextField f = new TextField();
        f.setPromptText(p);
        f.setStyle("-fx-background-color:#171717;-fx-text-fill:white;-fx-background-radius:10;");
        return f;
    }

    private Button primaryBtn(String t) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:" + BLUE + ";-fx-text-fill:white;-fx-background-radius:10;");
        return b;
    }

    private Button smallBtn(String t) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:#27272A;-fx-text-fill:white;-fx-background-radius:8;");
        return b;
    }

    private Button dangerBtn(String t) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color:#EF4444;-fx-text-fill:white;-fx-background-radius:10;");
        return b;
    }

    private String backStyle() {
        return "-fx-background-color:transparent;-fx-text-fill:#60A5FA;-fx-font-weight:bold;";
    }

    private String backHover() {
        return "-fx-background-color:rgba(96,165,250,0.15);-fx-text-fill:#60A5FA;-fx-background-radius:8;";
    }

    private String brandStyle() {
        return "-fx-text-fill:white;-fx-font-size:18px;-fx-font-weight:bold;";
    }

    private String titleStyle() {
        return "-fx-text-fill:white;-fx-font-size:22px;-fx-font-weight:bold;";
    }

    private String subtitleStyle() {
        return "-fx-text-fill:#A1A1AA;-fx-font-size:13px;";
    }

    private void clear(TextField t, TextField i, CheckBox av,
                       Label img, String[] path) {
        t.clear();
        i.clear();
        av.setSelected(false);
        img.setText("No image selected");
        path[0] = "";
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}