package me.hawkease;

import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class mapviewer {
    // The main map component from JXMapViewer
    private JXMapViewer mapViewer;
    // Current zoom level (1-15)
    private int currentZoom = 5;
    // Stores previous mouse position for drag operations
    private Point previousPoint;
    // Currently selected new waypoint (blue marker)
    private DefaultWaypoint currentWaypoint;
    // Collection of all waypoints to display
    private final Set<Waypoint> waypoints = new HashSet<>();
    // Painter for rendering waypoints and zones
    private WaypointPainter<Waypoint> waypointPainter;
    // Root JavaFX container for the map
    private BorderPane mapRoot;
    // Controller to communicate with parent application
    private final map_controller controller;
    // List of pre-existing locations to display
    private ArrayList<location_info> existingLocations;
    // Radius of each location zone in meters
    private static final double ZONE_RADIUS_METERS = 100;
    // Currently highlighted location (green marker)
    private location_info highlightedLocation;

    /**
     * Creates a new MapViewer connected to the provided controller
     *
     * @param controller The interface to communicate with the parent application
     */
    public mapviewer(map_controller controller) {
        this.controller = controller;
        initializeMap();
    }

    /**
     * Sets the list of existing locations to display on the map
     *
     * @param locations ArrayList of location_info objects to display
     */
    public void setExistingLocations(ArrayList<location_info> locations) {
        this.existingLocations = locations;
        updateExistingLocations();
    }

    /**
     * Updates the waypoints collection based on existing locations
     * Called when the locations list changes
     */
    private void updateExistingLocations() {
        // Use SwingUtilities.invokeLater to ensure thread safety with Swing components
        SwingUtilities.invokeLater(() -> {
            waypoints.clear();
            if (existingLocations != null) {
                for (location_info loc : existingLocations) {
                    if (loc != null) {
                        // Convert each location to a waypoint
                        waypoints.add(new DefaultWaypoint(
                                new GeoPosition(loc.getLatitude(), loc.getLongitude())
                        ));
                    }
                }
            }
            updateWaypoints();
        });
    }

    /**
     * Shows the map in the controller's page
     */
    public void show() {
        if (controller != null && controller.getPage() != null) {
            controller.getPage().setCenter(mapRoot);
        }
    }

    /**
     * Initializes the map UI components
     */
    private void initializeMap() {
        // Create the main layout
        mapRoot = new BorderPane();
        SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        // Create the Apply button
        Button submitButton = new Button("Apply");

        // Create a container for controls
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.getChildren().add(submitButton);
        controls.setStyle("-fx-alignment: center;");

        // Set up the button action
        submitButton.setOnAction(e -> {
            if (currentWaypoint != null) {
                // Send new waypoint position to controller
                GeoPosition pos = currentWaypoint.getPosition();
                controller.setLocation(pos.getLatitude(), pos.getLongitude());
                controller.getPage().setCenter(null);
            } else if (highlightedLocation != null) {
                // Send highlighted location to controller
                controller.setLocation(highlightedLocation.getLatitude(), highlightedLocation.getLongitude());
                controller.getPage().setCenter(null);
            }
        });

        // Arrange the components
        mapRoot.setTop(controls);
        mapRoot.setCenter(swingNode);
    }

    /**
     * Checks if a position is within the zone radius of any existing location
     *
     * @param position The GeoPosition to check
     * @return The location_info object if found within zone, null otherwise
     */
    private location_info findLocationInZone(GeoPosition position) {
        if (existingLocations != null) {
            for (location_info loc : existingLocations) {
                if (loc != null && calculateDistance(
                        position.getLatitude(), position.getLongitude(),
                        loc.getLatitude(), loc.getLongitude()) <= ZONE_RADIUS_METERS) {
                    return loc;
                }
            }
        }
        return null;
    }

    /**
     * Calculates the distance between two geographic points using the Haversine formula
     *
     * @param lat1 Latitude of first point
     * @param lon1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lon2 Longitude of second point
     * @return Distance between points in meters
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth's radius in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Creates and configures the Swing map component and embeds it in JavaFX
     *
     * @param swingNode The SwingNode to contain the map
     */
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            // Create and configure the map viewer
            mapViewer = new JXMapViewer();

            // Set up the OpenStreetMap tile provider
            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            tileFactory.setThreadPoolSize(8); // For better performance
            mapViewer.setTileFactory(tileFactory);

            // Set initial position and zoom
            mapViewer.setAddressLocation(new GeoPosition(23.735, 90.385)); // Bangladesh
            mapViewer.setZoom(currentZoom);

            // Create a custom waypoint painter that handles both waypoints and zones
            waypointPainter = new WaypointPainter<>() {
                @Override
                protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
                    // Create a copy of the graphics context
                    g = (Graphics2D) g.create();

                    // Adjust for map viewport position
                    Rectangle rect = map.getViewportBounds();
                    g.translate(-rect.x, -rect.y);

                    // Enable anti-aliasing for smoother shapes
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw zones first (so they appear under the waypoints)
                    if (existingLocations != null) {
                        for (location_info loc : existingLocations) {
                            if (loc == null) continue;

                            // Convert geo coordinates to pixel position
                            Point2D pt = map.getTileFactory().geoToPixel(
                                    new GeoPosition(loc.getLatitude(), loc.getLongitude()),
                                    map.getZoom());

                            // Convert radius to pixels at current zoom level
                            // Formula adjusts for Earth's curvature and zoom level
                            double metersPerPixel = 156543.03392 * Math.cos(Math.toRadians(loc.getLatitude()))
                                    / Math.pow(2, map.getZoom());
                            double radiusInPixels = ZONE_RADIUS_METERS / metersPerPixel;

                            // Use different colors for highlighted vs regular locations
                            if (highlightedLocation != null &&
                                    loc.getLatitude() == highlightedLocation.getLatitude() &&
                                    loc.getLongitude() == highlightedLocation.getLongitude()) {
                                g.setColor(Color.GREEN); // Solid green for highlighted
                            } else {
                                g.setColor(Color.YELLOW); // Solid yellow for regular
                            }

                            // Draw the filled zone
                            Ellipse2D zone = new Ellipse2D.Double(
                                    pt.getX() - radiusInPixels,
                                    pt.getY() - radiusInPixels,
                                    radiusInPixels * 2,
                                    radiusInPixels * 2
                            );
                            g.fill(zone);

                            // Add a black outline to make zones more visible
                            g.setColor(Color.BLACK);
                            g.draw(zone);
                        }
                    }

                    // Draw waypoints (markers for each location)
                    int size = 10; // Size of waypoint markers in pixels
                    for (Waypoint w : getWaypoints()) {
                        if (w == null) continue;

                        // Convert waypoint position to pixel coordinates
                        Point2D pt = map.getTileFactory().geoToPixel(
                                w.getPosition(), map.getZoom());

                        // Check if this waypoint represents a highlighted location
                        boolean isHighlighted = false;
                        if (highlightedLocation != null) {
                            GeoPosition pos = w.getPosition();
                            // Use small epsilon for floating point comparison
                            if (Math.abs(pos.getLatitude() - highlightedLocation.getLatitude()) < 0.0000001 &&
                                    Math.abs(pos.getLongitude() - highlightedLocation.getLongitude()) < 0.0000001) {
                                isHighlighted = true;
                            }
                        }

                        // Set color based on status
                        if (isHighlighted) {
                            g.setColor(Color.GREEN); // Green for highlighted location
                        } else {
                            g.setColor(Color.RED); // Red for regular locations
                        }

                        // Draw the waypoint marker
                        int x = (int) pt.getX() - size / 2;
                        int y = (int) pt.getY() - size / 2;
                        g.fillOval(x, y, size, size);

                        // Add black outline
                        g.setColor(Color.BLACK);
                        g.drawOval(x, y, size, size);
                    }

                    // Draw current selection (new waypoint) with blue
                    if (currentWaypoint != null) {
                        Point2D pt = map.getTileFactory().geoToPixel(
                                currentWaypoint.getPosition(), map.getZoom());
                        g.setColor(Color.BLUE);
                        int x = (int) pt.getX() - size / 2;
                        int y = (int) pt.getY() - size / 2;
                        g.fillOval(x, y, size, size);
                        g.setColor(Color.BLACK);
                        g.drawOval(x, y, size, size);
                    }

                    // Clean up the graphics context
                    g.dispose();
                }
            };

            // Set the painter as the overlay painter for the map
            mapViewer.setOverlayPainter(waypointPainter);

            // Add mouse listeners for interaction

            // Mouse press tracking for drag operations
            mapViewer.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    previousPoint = e.getPoint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // Handle double-click to select location
                    if (e.getClickCount() == 2) {
                        // Convert screen position to geographic coordinates
                        GeoPosition clicked = mapViewer.convertPointToGeoPosition(e.getPoint());
                        // Check if click is within an existing location's zone
                        location_info locationInZone = findLocationInZone(clicked);

                        if (locationInZone != null) {
                            // If clicked within a zone, highlight that location
                            highlightedLocation = locationInZone;
                            currentWaypoint = null; // Clear any current waypoint
                        } else {
                            // If clicked outside any zone, set a new waypoint and clear highlight
                            highlightedLocation = null;
                            setWaypoint(clicked);
                        }

                        // Refresh the display
                        updateWaypoints();
                    }
                }
            });

            // Map panning by dragging
            mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (previousPoint != null) {
                        // Calculate the movement amount
                        Point currentPoint = e.getPoint();
                        GeoPosition oldPos = mapViewer.convertPointToGeoPosition(previousPoint);
                        GeoPosition newPos = mapViewer.convertPointToGeoPosition(currentPoint);

                        // Calculate change in position
                        double lonChange = oldPos.getLongitude() - newPos.getLongitude();
                        double latChange = oldPos.getLatitude() - newPos.getLatitude();

                        // Move the map by the calculated amount
                        GeoPosition center = mapViewer.getCenterPosition();
                        GeoPosition newCenter = new GeoPosition(
                                center.getLatitude() + latChange,
                                center.getLongitude() + lonChange
                        );

                        mapViewer.setAddressLocation(newCenter);
                        previousPoint = currentPoint;
                    }
                }
            });

            // Zoom in/out with mouse wheel
            mapViewer.addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) {
                    // Zoom in (decrease zoom level number)
                    currentZoom = Math.max(1, currentZoom - 1);
                } else {
                    // Zoom out (increase zoom level number)
                    currentZoom = Math.min(15, currentZoom + 1);
                }
                mapViewer.setZoom(currentZoom);
            });

            // Set the map as the content of the SwingNode
            swingNode.setContent(mapViewer);
        });
    }

    /**
     * Sets a new waypoint at the specified position
     *
     * @param position The geographic position for the new waypoint
     */
    private void setWaypoint(GeoPosition position) {
        // Create new waypoint at position
        currentWaypoint = new DefaultWaypoint(position);

        // Reset waypoints collection
        waypoints.clear();
        waypoints.add(currentWaypoint);

        // Re-add existing locations as waypoints
        if (existingLocations != null) {
            for (location_info loc : existingLocations) {
                if (loc != null) {
                    waypoints.add(new DefaultWaypoint(
                            new GeoPosition(loc.getLatitude(), loc.getLongitude())
                    ));
                }
            }
        }

        // Update the display
        updateWaypoints();
    }

    /**
     * Updates the waypoint display on the map
     * Called whenever waypoints change
     */
    private void updateWaypoints() {
        SwingUtilities.invokeLater(() -> {
            waypointPainter.setWaypoints(waypoints);
            mapViewer.repaint();
        });
    }
}