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
    private JXMapViewer mapViewer;
    private int currentZoom = 5;
    private Point previousPoint;
    private DefaultWaypoint currentWaypoint;
    private final Set<Waypoint> waypoints = new HashSet<>();
    private WaypointPainter<Waypoint> waypointPainter;
    private BorderPane mapRoot;
    private SwingNode swingNode;
    private allocatestallrent controller;
    private ArrayList<LocationInfo> existingLocations;
    private static final double ZONE_RADIUS_METERS = 100;

    public mapviewer(allocatestallrent controller) {
        this.controller = controller;
        initializeMap();
    }

    public void setExistingLocations(ArrayList<LocationInfo> locations) {
        this.existingLocations = locations;
        updateExistingLocations();
    }

    private void updateExistingLocations() {
        SwingUtilities.invokeLater(() -> {
            waypoints.clear();
            if (existingLocations != null) {
                for (LocationInfo loc : existingLocations) {
                    waypoints.add(new DefaultWaypoint(
                            new GeoPosition(loc.getLatitude(), loc.getLongitude())
                    ));
                }
            }
            updateWaypoints();
        });
    }

    public void show() {
        controller.getPage().setCenter(mapRoot);
    }

    private void initializeMap() {
        mapRoot = new BorderPane();
        swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        Button submitButton = new Button("Use this Location");

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.getChildren().add(submitButton);
        controls.setStyle("-fx-alignment: center;");

        submitButton.setOnAction(e -> {
            if (currentWaypoint != null) {
                GeoPosition pos = currentWaypoint.getPosition();
                controller.setLocation(pos.getLatitude(), pos.getLongitude());
                controller.getPage().setCenter(null);
            }
        });

        mapRoot.setTop(controls);
        mapRoot.setCenter(swingNode);
    }

    private boolean isWithinAnyZone(GeoPosition position) {
        if (existingLocations != null) {
            for (LocationInfo loc : existingLocations) {
                if (calculateDistance(
                        position.getLatitude(), position.getLongitude(),
                        loc.getLatitude(), loc.getLongitude()) <= ZONE_RADIUS_METERS) {
                    return true;
                }
            }
        }
        return false;
    }

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

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            mapViewer = new JXMapViewer();

            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            tileFactory.setThreadPoolSize(8);
            mapViewer.setTileFactory(tileFactory);

            mapViewer.setAddressLocation(new GeoPosition(23.735, 90.385));
            mapViewer.setZoom(currentZoom);

            waypointPainter = new WaypointPainter<Waypoint>() {
                @Override
                protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
                    g = (Graphics2D) g.create();
                    Rectangle rect = map.getViewportBounds();
                    g.translate(-rect.x, -rect.y);

                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw zones first
                    if (existingLocations != null) {
                        g.setColor(new Color(255, 255, 0, 50)); // Semi-transparent yellow
                        for (LocationInfo loc : existingLocations) {
                            Point2D pt = map.getTileFactory().geoToPixel(
                                    new GeoPosition(loc.getLatitude(), loc.getLongitude()),
                                    map.getZoom());

                            // Convert 250m to pixels at current zoom level
                            double metersPerPixel = 156543.03392 * Math.cos(Math.toRadians(loc.getLatitude()))
                                    / Math.pow(2, map.getZoom());
                            double radiusInPixels = ZONE_RADIUS_METERS / metersPerPixel;

                            g.fill(new Ellipse2D.Double(
                                    pt.getX() - radiusInPixels,
                                    pt.getY() - radiusInPixels,
                                    radiusInPixels * 2,
                                    radiusInPixels * 2
                            ));
                        }
                    }

                    // Draw waypoints
                    g.setColor(Color.RED);
                    int size = 10;
                    for (Waypoint w : getWaypoints()) {
                        Point2D pt = map.getTileFactory().geoToPixel(
                                w.getPosition(), map.getZoom());
                        int x = (int) pt.getX() - size / 2;
                        int y = (int) pt.getY() - size / 2;
                        g.fillOval(x, y, size, size);
                        g.setColor(Color.BLACK);
                        g.drawOval(x, y, size, size);
                        g.setColor(Color.RED);
                    }

                    // Draw current selection with different color if exists
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

                    g.dispose();
                }
            };

            mapViewer.setOverlayPainter(waypointPainter);

            mapViewer.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    previousPoint = e.getPoint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        GeoPosition clicked = mapViewer.convertPointToGeoPosition(e.getPoint());
                        if (!isWithinAnyZone(clicked)) {
                            setWaypoint(clicked);
                        }
                    }
                }
            });

            mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (previousPoint != null) {
                        Point currentPoint = e.getPoint();
                        GeoPosition oldPos = mapViewer.convertPointToGeoPosition(previousPoint);
                        GeoPosition newPos = mapViewer.convertPointToGeoPosition(currentPoint);

                        double lonChange = oldPos.getLongitude() - newPos.getLongitude();
                        double latChange = oldPos.getLatitude() - newPos.getLatitude();

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

            mapViewer.addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) {
                    currentZoom = Math.max(1, currentZoom - 1);
                } else {
                    currentZoom = Math.min(15, currentZoom + 1);
                }
                mapViewer.setZoom(currentZoom);
            });

            swingNode.setContent(mapViewer);
        });
    }

    private void setWaypoint(GeoPosition position) {
        currentWaypoint = new DefaultWaypoint(position);
        waypoints.clear();
        waypoints.add(currentWaypoint);
        if (existingLocations != null) {
            for (LocationInfo loc : existingLocations) {
                waypoints.add(new DefaultWaypoint(
                        new GeoPosition(loc.getLatitude(), loc.getLongitude())
                ));
            }
        }
        updateWaypoints();
    }

    private void updateWaypoints() {
        SwingUtilities.invokeLater(() -> {
            waypointPainter.setWaypoints(waypoints);
            mapViewer.repaint();
        });
    }
}