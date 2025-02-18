package me.hawkease;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class allocatestallrent {

    @FXML
    private Text massage;

    private static final int PORT = 8080;

    @FXML
    public void initialize() {
        startHttpServer();
    }

    @FXML
    private void openmap() {
        try {
            Desktop.getDesktop().browse(new URI("http://localhost/open_the_map.html"));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error opening map");
        }
    }

    private void startHttpServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            CoordinateHandler coordinateHandler = new CoordinateHandler(this); // Pass reference of outer class
            server.createContext("/coordinates", coordinateHandler);
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            System.out.println("Error starting HTTP server");
        }
    }

    // Method to update the text
    public void updateMassage(String text) {
        Platform.runLater(() -> {
            massage.setText("Location: " + text);
            massage.setVisible(true);
        });
    }

    class CoordinateHandler implements HttpHandler {
        private allocatestallrent parent; // Reference to outer class

        public CoordinateHandler(allocatestallrent parent) {
            this.parent = parent;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                System.out.println("Coordinates received: " + query);

                // Update UI using the method in the outer class
                parent.updateMassage(query);

                String response = "Received: " + query;
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }
        }
    }
}
