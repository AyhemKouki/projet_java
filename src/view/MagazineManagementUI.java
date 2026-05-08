package view;

import controller.LibraryItemController;
import model.LibraryItem;
import model.Magazine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MagazineManagementUI {

    private TableView<Magazine> table = new TableView<>();
    private ObservableList<Magazine> data = FXCollections.observableArrayList();

    public void show(Stage stage) {

        // ── TITLE ─────────────────────────────
        Label title = new Label("Magazine Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // ── TABLE ─────────────────────────────
        setupColumns();
        loadMagazines();

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ── INPUTS ────────────────────────────
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField issueField = new TextField();
        issueField.setPromptText("Issue Number");

        TextField imagePathField = new TextField();
        imagePathField.setPromptText("Image Path");
        imagePathField.setEditable(false);

        Button browseImage = new Button("Browse");

        browseImage.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Magazine Image");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(
                            "Image Files",
                            "*.png",
                            "*.jpg",
                            "*.jpeg"
                    )
            );

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {

                // SAME FORMAT AS BOOKS
                imagePathField.setText(file.toURI().toString());
            }
        });

        CheckBox available = new CheckBox("Available");

        // ── BUTTONS ────────────────────────────
        Button add = new Button("Add");
        Button update = new Button("Update");
        Button delete = new Button("Delete");

        // ── IMAGE BOX ─────────────────────────
        HBox imageBox = new HBox(10, imagePathField, browseImage);

        // ── FORM ──────────────────────────────
        VBox form = new VBox(10,
                titleField,
                issueField,
                imageBox,
                available,
                new HBox(10, add, update, delete)
        );

        form.setPadding(new Insets(15));

        // ── LAYOUT ─────────────────────────────
        VBox left = new VBox(15, title, table);

        HBox root = new HBox(20, left, form);
        root.setPadding(new Insets(20));

        // ── SELECT ITEM ───────────────────────
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, m) -> {

            if (m != null) {

                titleField.setText(m.getTitle());
                issueField.setText(String.valueOf(m.getIssueNumber()));
                imagePathField.setText(m.getImagePath());
                available.setSelected(m.getAvailable());
            }
        });

        // ── ADD ───────────────────────────────
        add.setOnAction(e -> {

            try {

                Magazine m = new Magazine(
                        0,
                        titleField.getText(),
                        Integer.parseInt(issueField.getText()),
                        available.isSelected(),
                        imagePathField.getText()
                );

                LibraryItemController.addItem(m);

                loadMagazines();

                clearFields(
                        titleField,
                        issueField,
                        imagePathField,
                        available
                );

            } catch (Exception ex) {
                showAlert("Invalid issue number!");
            }
        });

        // ── UPDATE ────────────────────────────
        update.setOnAction(e -> {

            Magazine selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {

                try {

                    selected.setTitle(titleField.getText());
                    selected.setIssueNumber(Integer.parseInt(issueField.getText()));
                    selected.setImagePath(imagePathField.getText());
                    selected.setAvailable(available.isSelected());

                    LibraryItemController.updateItem(selected);

                    loadMagazines();

                } catch (Exception ex) {
                    showAlert("Invalid issue number!");
                }
            }
        });

        // ── DELETE ────────────────────────────
        delete.setOnAction(e -> {

            Magazine selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {

                LibraryItemController.deleteItem(selected.getId());
                loadMagazines();
            }
        });

        // ── SCENE ─────────────────────────────
        Scene scene = new Scene(root, 950, 500);
        stage.setScene(scene);
        stage.setTitle("Magazines");
        stage.show();
    }

    private void loadMagazines() {

        data.clear();

        List<LibraryItem> items = LibraryItemController.getAllItems();

        for (LibraryItem item : items) {

            if (item instanceof Magazine m) {
                data.add(m);
            }
        }

        table.setItems(data);
    }

    private void setupColumns() {

        TableColumn<Magazine, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId())
        );

        TableColumn<Magazine, String> title = new TableColumn<>("Title");
        title.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle())
        );

        TableColumn<Magazine, Integer> issue = new TableColumn<>("Issue");
        issue.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getIssueNumber())
        );

        TableColumn<Magazine, String> image = new TableColumn<>("Image Path");
        image.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getImagePath())
        );

        TableColumn<Magazine, Boolean> available = new TableColumn<>("Available");
        available.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAvailable())
        );

        table.getColumns().addAll(id, title, issue, image, available);
    }

    private void clearFields(
            TextField titleField,
            TextField issueField,
            TextField imagePathField,
            CheckBox available
    ) {

        titleField.clear();
        issueField.clear();
        imagePathField.clear();
        available.setSelected(false);
    }

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}