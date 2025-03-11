package me.hawkease;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchStallsController implements Initializable {

    @FXML
    private VBox searchContainer;

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
    private TableColumn<buyerhomepageController.Stall, String> productsColumn;

    @FXML
    private TableColumn<buyerhomepageController.Stall, buyerhomepageController.Stall> actionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize category ComboBox
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "All Categories", "Clothing", "Food", "Electronics", "Home", "Beauty"
        ));
        categoryComboBox.setValue("All Categories");

        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Custom column for products
        productsColumn.setCellValueFactory(cellData -> {
            List<buyerhomepageController.Product> products = cellData.getValue().getProducts();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < products.size(); i++) {
                buyerhomepageController.Product product = products.get(i);
                sb.append(product.getName())
                        .append(" ($").append(String.format("%.2f", product.getPrice())).append(")");

                if (i < products.size() - 1) {
                    sb.append(", ");
                }
            }
            return new SimpleStringProperty(sb.toString());
        });

        // Action column for buttons
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

        // Handle category selection changes
        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            System.out.println("Category selected: " + selectedCategory);
            updateSearchResults(selectedCategory);
        });

        // Populate initial data
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

    // Method to show stall details
    private void showStallDetails(buyerhomepageController.Stall stall) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stall Details");
        alert.setHeaderText(stall.getName());
        alert.setContentText("Viewing details for " + stall.getName());
        alert.showAndWait();
    }

    // Method to show rating dialog
    private void showRatingDialog(buyerhomepageController.Stall stall) {
        // Create a new stage for the rating dialog
        Stage ratingStage = new Stage();
        ratingStage.setTitle("Rate " + stall.getName());

        VBox ratingBox = new VBox(15);
        ratingBox.setPadding(new Insets(20));
        ratingBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Rate & Review " + stall.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Rating selection
        HBox ratingSelectionBox = new HBox(10);
        ratingSelectionBox.setAlignment(Pos.CENTER);

        Label ratingLabel = new Label("Your Rating:");

        ComboBox<String> ratingCombo = new ComboBox<>();
        ratingCombo.setItems(FXCollections.observableArrayList(
                "5 - Excellent", "4 - Very Good", "3 - Good", "2 - Fair", "1 - Poor"
        ));
        ratingCombo.setValue("5 - Excellent");

        ratingSelectionBox.getChildren().addAll(ratingLabel, ratingCombo);

        // Comment area
        Label commentLabel = new Label("Your Review:");

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Write your review here...");
        commentArea.setPrefRowCount(5);
        commentArea.setWrapText(true);

        // Submit and cancel buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #FF7F50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cancelButton = new Button("Cancel");

        buttonBox.getChildren().addAll(submitButton, cancelButton);

        // Add everything to the rating box
        ratingBox.getChildren().addAll(
                titleLabel, ratingSelectionBox, commentLabel, commentArea, buttonBox
        );

        // Set up button actions
        cancelButton.setOnAction(e -> ratingStage.close());

        submitButton.setOnAction(e -> {
            String ratingValue = ratingCombo.getValue();
            String comment = commentArea.getText().trim();

            if (comment.isEmpty()) {
                showError("Please write a review");
                return;
            }

            // Extract numerical rating from selection
            int rating = Integer.parseInt(ratingValue.substring(0, 1));

            // Submit rating
            submitRating(stall, rating, comment);

            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your review of " + stall.getName());
            alert.showAndWait();

            ratingStage.close();
        });

        javafx.scene.Scene scene = new javafx.scene.Scene(ratingBox, 400, 350);
        ratingStage.setScene(scene);
        ratingStage.show();
    }

    // Helper method to show error messages
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to submit a rating (placeholder implementation)
    private void submitRating(buyerhomepageController.Stall stall, int rating, String comment) {
        // In a real application, this would save to a database
        System.out.println("Rating submitted for " + stall.getName() + ": " + rating + " stars");
        System.out.println("Comment: " + comment);
    }

    // Search method to filter stalls by category
    private List<buyerhomepageController.Stall> searchStalls(String category) {
        // In a real application, this would query a database
        // For this example, we'll return dummy data
        List<buyerhomepageController.Stall> results = new ArrayList<>();
        buyerhomepageController controller = new buyerhomepageController();

        // Dummy stalls with products
        if (category.equals("All Categories") || category.equals("Food")) {
            buyerhomepageController.Stall foodStall = new buyerhomepageController.Stall("Fresh Foods Market", "John Smith", "Market Square", 4.7, 120);
            foodStall.addProduct(controller.new Product("Organic Apples", 2.99));
            foodStall.addProduct(controller.new Product("Fresh Vegetables", 3.49));
            results.add(foodStall);

            foodStall = new buyerhomepageController.Stall("Foodpanda", "John Smith", "Market Square", 4.7, 120);
            foodStall.addProduct(controller.new Product("Organic Apples", 2.99));
            foodStall.addProduct(controller.new Product("Fresh Vegetables", 3.49));
            results.add(foodStall);
        }

        if (category.equals("All Categories") || category.equals("Clothing")) {
            buyerhomepageController.Stall clothingStall = new buyerhomepageController.Stall("Fashion Corner", "Emily Johnson", "Main Street", 4.5, 85);
            clothingStall.addProduct(controller.new Product("Summer Dresses", 29.99));
            clothingStall.addProduct(controller.new Product("Cotton T-shirts", 15.99));
            results.add(clothingStall);

            clothingStall = new buyerhomepageController.Stall("Fashion show", "Emily Johnson", "Main Street", 4.5, 85);
            clothingStall.addProduct(controller.new Product("Summer Dresses", 29.99));
            clothingStall.addProduct(controller.new Product("Cotton T-shirts", 15.99));
            results.add(clothingStall);
        }

        if (category.equals("All Categories") || category.equals("Electronics")) {
            buyerhomepageController.Stall electronicsStall = new buyerhomepageController.Stall("Tech Zone", "Michael Brown", "Tech Plaza", 4.8, 150);
            electronicsStall.addProduct(controller.new Product("Phone Accessories", 12.99));
            electronicsStall.addProduct(controller.new Product("Smart Gadgets", 49.99));
            results.add(electronicsStall);

            electronicsStall = new buyerhomepageController.Stall("Tech stall", "Michael Brown", "Tech Plaza", 4.8, 150);
            electronicsStall.addProduct(controller.new Product("Phone Accessories", 12.99));
            electronicsStall.addProduct(controller.new Product("Smart Gadgets", 49.99));
            results.add(electronicsStall);
        }

        return results;
    }
}