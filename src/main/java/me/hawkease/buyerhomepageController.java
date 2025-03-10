package me.hawkease;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class buyerhomepageController {

    @FXML
    private StackPane page;

    @FXML
    void log_out(MouseEvent event) {
        fxml_loader fxmlLoader = new fxml_loader(event);
        try {
            fxmlLoader.load_fxml("log_in.fxml");
        } catch (IOException e) {
            System.out.println("Error loading log_in.fxml");
        }
    }

    @FXML
    void open_profile(MouseEvent event) {
        // Keep original implementation empty
    }

    @FXML
    void search(MouseEvent event) {
        // Clear current content
        page.getChildren().clear();

        // Create search container
        VBox searchContainer = new VBox(15);
        searchContainer.setPadding(new Insets(20));
        searchContainer.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Search Products");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Search bar with category selection
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        // Category ComboBox
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "All Categories", "Clothing", "Food", "Electronics", "Home", "Beauty"
        ));
        categoryComboBox.setValue("All Categories");
        categoryComboBox.setPrefHeight(40);
        categoryComboBox.setStyle("-fx-font-size: 14px;");

        // Add all elements to search box
        searchBox.getChildren().add(categoryComboBox);

        // Results container
        VBox resultsContainer = new VBox(10);
        resultsContainer.setPadding(new Insets(10, 0, 0, 0));

        Label resultsLabel = new Label("Search Results");
        resultsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create TableView for stalls
        TableView<Stall> stallTable = new TableView<>();
        stallTable.setPrefHeight(400);
        stallTable.setPrefWidth(800);

        // Define columns
        TableColumn<Stall, String> nameColumn = new TableColumn<>("Stall Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<Stall, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        ownerColumn.setPrefWidth(120);

        TableColumn<Stall, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.setPrefWidth(120);

        TableColumn<Stall, Double> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setPrefWidth(80);

        // Custom column for products
        TableColumn<Stall, String> productsColumn = new TableColumn<>("Products");
        productsColumn.setPrefWidth(200);
        productsColumn.setCellValueFactory(cellData -> {
            List<Product> products = cellData.getValue().getProducts();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                sb.append(product.getName())
                        .append(" ($").append(String.format("%.2f", product.getPrice())).append(")");

                if (i < products.size() - 1) {
                    sb.append(", ");
                }
            }
            return new SimpleStringProperty(sb.toString());
        });

        // Action column for buttons
        TableColumn<Stall, Stall> actionColumn = new TableColumn<>("Actions");
        actionColumn.setPrefWidth(200);
        actionColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        actionColumn.setCellFactory(param -> new TableCell<Stall, Stall>() {
            private final Button viewButton = new Button("View Details");
            private final Button rateButton = new Button("Rate & Review");
            private final HBox buttonBox = new HBox(5, viewButton, rateButton);

            {
                viewButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-cursor: hand;");
                rateButton.setStyle("-fx-background-color: #FF7F50; -fx-text-fill: white; -fx-cursor: hand;");
                buttonBox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Stall stall, boolean empty) {
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

        // Add columns to table
        stallTable.getColumns().addAll(nameColumn, ownerColumn, locationColumn, ratingColumn, productsColumn, actionColumn);

        // Add table to results container
        resultsContainer.getChildren().addAll(resultsLabel, stallTable);

        // Handle category selection changes
        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();

            // Log selection to console
            System.out.println("Category selected: " + selectedCategory);

            // Perform search and update table
            List<Stall> stalls = searchStalls(selectedCategory);
            ObservableList<Stall> stallData = FXCollections.observableArrayList(stalls);
            stallTable.setItems(stallData);

            // Show message if no results
            if (stalls.isEmpty()) {
                stallTable.setPlaceholder(new Label("No stalls found in category '" + selectedCategory + "'"));
            }
        });

        // Add to search container
        searchContainer.getChildren().addAll(titleLabel, searchBox, resultsContainer);

        // Add to page
        page.getChildren().add(searchContainer);

        // Trigger initial search with default category
        String selectedCategory = categoryComboBox.getValue();
        List<Stall> stalls = searchStalls(selectedCategory);
        ObservableList<Stall> stallData = FXCollections.observableArrayList(stalls);
        stallTable.setItems(stallData);

        // Show message if no results
        if (stalls.isEmpty()) {
            stallTable.setPlaceholder(new Label("No stalls found in category '" + selectedCategory + "'"));
        }
    }

    // Helper method to show error messages
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Updated search method to include only category filtering
    private List<Stall> searchStalls(String category) {
        // In a real application, this would query a database
        // For this example, we'll return dummy data
        List<Stall> results = new ArrayList<>();

        // Dummy stalls with products
        if (category.equals("All Categories") || category.equals("Food")) {
            Stall foodStall = new Stall("Fresh Foods Market", "John Smith", "Market Square", 4.7, 120);
            foodStall.addProduct(new Product("Organic Apples", 2.99));
            foodStall.addProduct(new Product("Fresh Vegetables", 3.49));
            results.add(foodStall);

            foodStall = new Stall("Foodpanda", "John Smith", "Market Square", 4.7, 120);
            foodStall.addProduct(new Product("Organic Apples", 2.99));
            foodStall.addProduct(new Product("Fresh Vegetables", 3.49));
            results.add(foodStall);
        }

        if (category.equals("All Categories") || category.equals("Clothing")) {
            Stall clothingStall = new Stall("Fashion Corner", "Emily Johnson", "Main Street", 4.5, 85);
            clothingStall.addProduct(new Product("Summer Dresses", 29.99));
            clothingStall.addProduct(new Product("Cotton T-shirts", 15.99));
            results.add(clothingStall);

            clothingStall = new Stall("Fashion show", "Emily Johnson", "Main Street", 4.5, 85);
            clothingStall.addProduct(new Product("Summer Dresses", 29.99));
            clothingStall.addProduct(new Product("Cotton T-shirts", 15.99));
            results.add(clothingStall);
        }

        if (category.equals("All Categories") || category.equals("Electronics")) {
            Stall electronicsStall = new Stall("Tech Zone", "Michael Brown", "Tech Plaza", 4.8, 150);
            electronicsStall.addProduct(new Product("Phone Accessories", 12.99));
            electronicsStall.addProduct(new Product("Smart Gadgets", 49.99));
            results.add(electronicsStall);

            electronicsStall = new Stall("Tech stall", "Michael Brown", "Tech Plaza", 4.8, 150);
            electronicsStall.addProduct(new Product("Phone Accessories", 12.99));
            electronicsStall.addProduct(new Product("Smart Gadgets", 49.99));
            results.add(electronicsStall);
        }

        return results;
    }

    // Method to show rating dialog
    private void showRatingDialog(Stall stall) {
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

            // Submit rating (in a real app, this would save to database)
            submitRating(stall, rating, comment);

            // Show confirmation
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

    // Method to show stall details
    private void showStallDetails(Stall stall) {
        // Create a placeholder implementation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stall Details");
        alert.setHeaderText(stall.getName());
        alert.setContentText("Viewing details for " + stall.getName());
        alert.showAndWait();
    }

    // Method to submit a rating (placeholder implementation)
    private void submitRating(Stall stall, int rating, String comment) {
        // In a real application, this would save to a database
        System.out.println("Rating submitted for " + stall.getName() + ": " + rating + " stars");
        System.out.println("Comment: " + comment);
    }

    // Model classes (would normally be in separate files)

    // Stall class
    public static class Stall {
        private String name;
        private String owner;
        private String location;
        private double rating;
        private int reviewCount;
        private List<Product> products = new ArrayList<>();

        public Stall(String name, String owner, String location, double rating, int reviewCount) {
            this.name = name;
            this.owner = owner;
            this.location = location;
            this.rating = rating;
            this.reviewCount = reviewCount;
        }

        public String getName() {
            return name;
        }

        public String getOwner() {
            return owner;
        }

        public String getLocation() {
            return location;
        }

        public double getRating() {
            return rating;
        }

        public int getReviewCount() {
            return reviewCount;
        }

        public void addProduct(Product product) {
            products.add(product);
        }

        public List<Product> getProducts() {
            return products;
        }
    }

    // Product class
    public class Product {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
        public String getName(){
            return name;
        }
        public double getPrice(){
        return price;
        }
    }
}
