package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class buyerhomepageController {

    @FXML
    private StackPane page;

    @FXML
    void home(MouseEvent event) {
        try {
            fxml_loader fxmlLoader = new fxml_loader(event);
            fxmlLoader.load_fxml("buyerhome.fxml");
        }
        catch (IOException e){
            System.out.println("Error in loading fxml: " + e.getMessage());
        }
    }

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading fxml: ");
        }
    }

    @FXML
    void search(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search_stalls.fxml"));
            Node newContent = fxmlLoader.load();
            page.getChildren().clear();
            page.getChildren().add(newContent);
        }
        catch (IOException e) {
            System.out.println("Error in loading search_stalls.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
