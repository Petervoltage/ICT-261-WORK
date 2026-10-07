package com.example.hellofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class HelloJavaFX extends Application {

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private final Label feedbackLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Manager - 202508483");

        // --- Form Controls ---
        Label nameLabel = new Label("Customer name");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");

        Label provinceLabel = new Label("Province");
        ComboBox<String> provinceComboBox = new ComboBox<>();
        provinceComboBox.setPromptText("Choose a province");
        provinceComboBox.getItems().addAll(
                "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western"
        );

        Button saveButton = new Button("Save Customer");
        Button deleteButton = new Button("Delete Customer");

        HBox buttonBox = new HBox(10, saveButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        feedbackLabel.setTextFill(Color.DARKGREEN);

        // --- TableView Setup ---
        TableView<Customer> tableView = new TableView<>();
        tableView.setItems(customerList);
        tableView.setPlaceholder(new Label("No customers added yet."));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(220);

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        provinceCol.setPrefWidth(180);

        tableView.getColumns().addAll(nameCol, provinceCol);

        // --- Save Action & Validation ---
        Runnable saveAction = () -> {
            String name = nameField.getText().trim();
            String province = provinceComboBox.getValue();

            if (name.isEmpty()) {
                showError("Validation Error", "Customer name cannot be empty.");
                nameField.requestFocus();
                return;
            }

            if (province == null || province.isEmpty()) {
                showError("Validation Error", "Please select a province.");
                provinceComboBox.requestFocus();
                return;
            }

            Customer newCustomer = new Customer(name, province);
            customerList.add(newCustomer);

            feedbackLabel.setTextFill(Color.DARKGREEN);
            feedbackLabel.setText("Customer '" + name + "' saved successfully.");

            nameField.clear();
            provinceComboBox.getSelectionModel().clearSelection();
            nameField.requestFocus();
        };

        saveButton.setOnAction(e -> saveAction.run());

        // --- Delete Action & Confirmation Alert ---
        deleteButton.setOnAction(e -> {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                showError("Selection Error", "Please select a customer from the table to delete.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Customer");
            confirmAlert.setContentText("Are you sure you want to delete " + selectedCustomer.getName() + "?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                customerList.remove(selectedCustomer);
                feedbackLabel.setTextFill(Color.DARKBLUE);
                feedbackLabel.setText("Customer '" + selectedCustomer.getName() + "' deleted.");
            } else {
                feedbackLabel.setTextFill(Color.DARKGRAY);
                feedbackLabel.setText("Deletion cancelled.");
            }
        });

        // --- Keyboard Support ---
        nameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                provinceComboBox.requestFocus();
            }
        });

        provinceComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveAction.run();
            }
        });

        // --- Layout Layout ---
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getChildren().addAll(
                nameLabel,
                nameField,
                provinceLabel,
                provinceComboBox,
                buttonBox,
                tableView,
                feedbackLabel
        );

        Scene scene = new Scene(root, 450, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        feedbackLabel.setTextFill(Color.RED);
        feedbackLabel.setText(message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}