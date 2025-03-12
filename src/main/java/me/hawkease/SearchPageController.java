package me.hawkease;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchPageController implements Initializable {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TableView<buyerhomepageController.Stall> stallTable;

    @FXML
    private TableColumn<buyerhomepageController.Stall, String> nameColumn;

    @FXML
    private TableColumn<buyerhomepageController.Stall, String> ownerColumn;

    @FXML
    private TableColumn<buyerhomepageController.Stall, String> locationColumn;

    @FXML
    private TableColumn<buyerhomepageController.Stall, Double> ratingColumn;

    @FXML
    private TableColumn<buyerhomepageController.Stall, buyerhomepageController.Stall> actionColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Set up action column
        actionColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        actionColumn.setCellFactory(param -> new TableCell<buyerhomepageController.Stall, buyerhomepageController.Stall>() {
            private final Button viewButton = new Button("View Details");
            private final Button rateButton = new Button("Rate & Review");
            private final HBox buttonBox = new HBox(5, viewButton, rateButton);

            {
                viewButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-cursor: hand;");
                rateButton.setStyle("-fx-background-color: #FF7F50; -fx-text-fill: white; -fx-cursor: hand;");
                buttonBox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(buyerhomepageController.Stall stall, boolean empty) {
                super.updateItem(stall, empty);

                if (empty || stall == null) {
                    setGraphic(null);
                } else {
                    viewButton.setOnAction(event -> showStallDetails(stall));
                    rateButton.setOnAction(event -> showRatingDialog(stall));
                    setGraphic(buttonBox);
                }
            }
        });

        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            System.out.println("Category selected: " + selectedCategory);
            updateSearchResults(selectedCategory);
        });

        updateSearchResults(categoryComboBox.getValue());
    }

    private void updateSearchResults(String category) {
        List<buyerhomepageController.Stall> stalls = searchStalls(category);
        ObservableList<buyerhomepageController.Stall> stallData = FXCollections.observableArrayList(stalls);
        stallTable.setItems(stallData);

        if (stalls.isEmpty()) {
            stallTable.setPlaceholder(new Label("No stalls found in category '" + category + "'"));
        }
    }

    private List<buyerhomepageController.Stall> searchStalls(String category) {
        List<buyerhomepageController.Stall> results = new ArrayList<>();

        if (category.equals("All Categories") || category.equals("Food")) {
            buyerhomepageController.Stall foodStall = new buyerhomepageController.Stall("Fresh Foods Market", "John Smith", "Market Square", 4.7, 120);
            results.add(foodStall);

            foodStall = new buyerhomepageController.Stall("Foodpanda", "John Smith", "Market Square", 4.7, 120);
            results.add(foodStall);
        }

        if (category.equals("All Categories") || category.equals("Clothing")) {
            buyerhomepageController.Stall clothingStall = new buyerhomepageController.Stall("Fashion Corner", "Emily Johnson", "Main Street", 4.5, 85);
            results.add(clothingStall);

            clothingStall = new buyerhomepageController.Stall("Fashion show", "Emily Johnson", "Main Street", 4.5, 85);
            results.add(clothingStall);
        }

        if (category.equals("All Categories") || category.equals("Electronics")) {
            buyerhomepageController.Stall electronicsStall = new buyerhomepageController.Stall("Tech Zone", "Michael Brown", "Tech Plaza", 4.8, 150);
            results.add(electronicsStall);

            electronicsStall = new buyerhomepageController.Stall("Tech stall", "Michael Brown", "Tech Plaza", 4.8, 150);
            results.add(electronicsStall);
        }

        return results;
    }

    private void showRatingDialog(buyerhomepageController.Stall stall) {

        Stage ratingStage = new Stage();
        ratingStage.setTitle("Rate " + stall.getName());

        VBox ratingBox = new VBox(15);
        ratingBox.setPadding(new Insets(20));
        ratingBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Rate & Review " + stall.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox ratingSelectionBox = new HBox(10);
        ratingSelectionBox.setAlignment(Pos.CENTER);

        Label ratingLabel = new Label("Your Rating:");

        ComboBox<String> ratingCombo = new ComboBox<>();
        ratingCombo.setItems(FXCollections.observableArrayList(
                "5 - Excellent", "4 - Very Good", "3 - Good", "2 - Fair", "1 - Poor"
        ));
        ratingCombo.setValue("5 - Excellent");

        ratingSelectionBox.getChildren().addAll(ratingLabel, ratingCombo);

        Label commentLabel = new Label("Your Review:");

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Write your review here...");
        commentArea.setPrefRowCount(5);
        commentArea.setWrapText(true);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #FF7F50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cancelButton = new Button("Cancel");

        buttonBox.getChildren().addAll(submitButton, cancelButton);

        ratingBox.getChildren().addAll(
                titleLabel, ratingSelectionBox, commentLabel, commentArea, buttonBox
        );

        cancelButton.setOnAction(e -> ratingStage.close());

        submitButton.setOnAction(e -> {
            String ratingValue = ratingCombo.getValue();
            String comment = commentArea.getText().trim();

            if (comment.isEmpty()) {
                showError("Please write a review");
                return;
            }

            int rating = Integer.parseInt(ratingValue.substring(0, 1));

            submitRating(stall, rating, comment);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your review of " + stall.getName());
            alert.showAndWait();

            ratingStage.close();
        });

        Scene scene = new Scene(ratingBox, 400, 350);
        ratingStage.setScene(scene);
        ratingStage.show();
    }

    private void showStallDetails(buyerhomepageController.Stall stall) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stall Details");
        alert.setHeaderText(stall.getName());
        alert.setContentText("Viewing details for " + stall.getName());
        alert.showAndWait();
    }

    private void submitRating(buyerhomepageController.Stall stall, int rating, String comment) {
        System.out.println("Rating submitted for " + stall.getName() + ": " + rating + " stars");
        System.out.println("Comment: " + comment);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}