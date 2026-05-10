package view;

import controller.UserController;
import model.User;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class UserManagementUI {

    // ================= THEME =================
    private static final String BG = "#0F0F10";
    private static final String CARD = "#1E1E1F";
    private static final String BORDER = "#2B2B2D";
    private static final String BLUE = "#60A5FA";

    private TableView<User> table = new TableView<>();
    private ObservableList<User> data = FXCollections.observableArrayList();

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
        Label title = new Label("User Management");
        title.setStyle(titleStyle());

        Label subtitle = new Label("Manage system users");
        subtitle.setStyle(subtitleStyle());

        // ================= TABLE =================
        setupColumns();
        loadUsers();

        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle(tableStyle());

        table.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setStyle("-fx-background-color:#1E1E1F;-fx-text-fill:white;");
            return row;
        });

        // ================= FORM =================
        TextField nameField = input("Name");
        TextField emailField = input("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(inputStyle());

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "user");
        roleBox.setValue("user");
        roleBox.setStyle(inputStyle());

        Button addBtn = primaryBtn("Add User");
        Button deleteBtn = dangerBtn("Delete");
        Button refreshBtn = smallBtn("Refresh");

        // ================= ACTIONS =================
        addBtn.setOnAction(e -> {
            try {
                boolean ok = UserController.addUser(
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        roleBox.getValue()
                );

                if (ok) {
                    loadUsers();
                    clear(nameField, emailField, passwordField, roleBox);
                }

            } catch (Exception ex) {
                showAlert("Error adding user");
            }
        });

        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                UserController.deleteUser(selected.getId());
                loadUsers();
            }
        });

        refreshBtn.setOnAction(e -> loadUsers());

        // ================= FORM =================
        VBox form = new VBox(12,
                new Label("User Form"),
                nameField,
                emailField,
                passwordField,
                roleBox,
                addBtn,
                deleteBtn,
                refreshBtn
        );

        form.setPadding(new Insets(20));
        form.setPrefWidth(300);
        form.setStyle(cardStyle());

        // ================= TABLE CARD =================
        VBox tableBox = new VBox(10, title, subtitle, table);
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
                nameField.setText(sel.getName());
                emailField.setText(sel.getEmail());
                roleBox.setValue(sel.getRole());
            }
        });

        // ================= SCENE =================
        Scene scene = new Scene(root, 1000, 650);
        stage.setScene(scene);
        stage.setTitle("User Management");
        stage.show();
    }

    // ================= DATA =================
    private void loadUsers() {
        data.clear();
        List<User> users = UserController.listUsers();
        data.addAll(users);
    }

    // ================= TABLE =================
    private void setupColumns() {

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getId())
        );

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName())
        );

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEmail())
        );

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getRole())
        );

        table.getColumns().clear();
        table.getColumns().addAll(idCol, nameCol, emailCol, roleCol);
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

    private String inputStyle() {
        return "-fx-background-color:#171717;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:10;";
    }

    private TextField input(String p) {
        TextField f = new TextField();
        f.setPromptText(p);
        f.setStyle(inputStyle());
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

    private void clear(TextField n, TextField e, PasswordField p, ComboBox<String> r) {
        n.clear();
        e.clear();
        p.clear();
        r.setValue("user");
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}