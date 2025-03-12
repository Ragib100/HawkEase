package me.hawkease;

import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class mapviewer {
    private JXMapViewer mapViewer;
    private int currentZoom = 5;
    private Point previousPoint;
    private DefaultWaypoint currentWaypoint;
    private final Set<Waypoint> waypoints = new HashSet<>();
    private final Map<Waypoint, location_info> waypointToLocationMap = new HashMap<>();
    private WaypointPainter<Waypoint> waypointPainter;
    private BorderPane mapRoot;
    private final map_controller controller;
    private HashMap<location_info, Boolean> existingLocations;
    private static final double ZONE_RADIUS_METERS = 100;
    private location_info highlightedLocation;

    public mapviewer(map_controller controller) {
        this.controller = controller;
        initializeMap();
    }

    public void setExistingLocations(HashMap<location_info, Boolean> existingLocations) {
        this.existingLocations = existingLocations;
        updateExistingLocations();
    }

    private void updateExistingLocations() {
        SwingUtilities.invokeLater(() -> {
            waypoints.clear();
            waypointToLocationMap.clear();

            if (existingLocations != null) {
                for (Map.Entry<location_info, Boolean> entry : existingLocations.entrySet()) {
                    location_info loc = entry.getKey();
                    if (loc != null) {
                        DefaultWaypoint waypoint = new DefaultWaypoint(
                                new GeoPosition(loc.getLatitude(), loc.getLongitude())
                        );
                        waypoints.add(waypoint);

                        waypointToLocationMap.put(waypoint, loc);
                    }
                }
            }
            updateWaypoints();
        });
    }

    public void show() {
        if (controller != null && controller.getPage() != null) {
            controller.getPage().setCenter(mapRoot);
        }
    }

    private void initializeMap() {
        // Create the main layout
        mapRoot = new BorderPane();
        SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-alignment: center;");

        mapRoot.setTop(controls);
        mapRoot.setCenter(swingNode);
    }

    private location_info findLocationInZone(GeoPosition position) {
        if (existingLocations != null) {
            for (location_info loc : existingLocations.keySet()) {
                if (loc != null && calculateDistance(
                        position.getLatitude(), position.getLongitude(),
                        loc.getLatitude(), loc.getLongitude()) <= ZONE_RADIUS_METERS) {
                    return loc;
                }
            }
        }
        return null;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
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

            mapViewer.setAddressLocation(new GeoPosition(23.735, 90.385)); // Bangladesh
            mapViewer.setZoom(currentZoom);

            waypointPainter = new WaypointPainter<>() {
                @Override
                protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
                    g = (Graphics2D) g.create();

                    Rectangle rect = map.getViewportBounds();
                    g.translate(-rect.x, -rect.y);

                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (existingLocations != null) {
                        for (Map.Entry<location_info, Boolean> entry : existingLocations.entrySet()) {
                            location_info loc = entry.getKey();
                            if (loc == null) continue;

                            Point2D pt = map.getTileFactory().geoToPixel(
                                    new GeoPosition(loc.getLatitude(), loc.getLongitude()),
                                    map.getZoom());

                            double metersPerPixel = 156543.03392 * Math.cos(Math.toRadians(loc.getLatitude()))
                                    / Math.pow(2, map.getZoom());
                            double radiusInPixels = ZONE_RADIUS_METERS / metersPerPixel;

                            if (highlightedLocation != null &&
                                    loc.getLatitude() == highlightedLocation.getLatitude() &&
                                    loc.getLongitude() == highlightedLocation.getLongitude()) {
                                g.setColor(Color.GREEN);
                            } else {
                                g.setColor(Color.YELLOW);
                            }

                            Ellipse2D zone = new Ellipse2D.Double(
                                    pt.getX() - radiusInPixels,
                                    pt.getY() - radiusInPixels,
                                    radiusInPixels * 2,
                                    radiusInPixels * 2
                            );
                            g.fill(zone);

                            g.setColor(Color.BLACK);
                            g.draw(zone);
                        }
                    }

                    int size = 10;
                    for (Waypoint w : getWaypoints()) {
                        if (w == null) continue;

                        Point2D pt = map.getTileFactory().geoToPixel(
                                w.getPosition(), map.getZoom());

                        if (w == currentWaypoint) {
                            g.setColor(Color.BLUE);
                        }
                        else if (highlightedLocation != null &&
                                isMatchingPosition(w.getPosition(), highlightedLocation.getLatitude(), highlightedLocation.getLongitude())) {
                            g.setColor(Color.GREEN);
                        }
                        else if (waypointToLocationMap.containsKey(w)) {
                            location_info loc = waypointToLocationMap.get(w);
                            Boolean colorFlag = existingLocations.get(loc);
                            g.setColor(Boolean.TRUE.equals(colorFlag) ? Color.RED : Color.YELLOW);
                        }
                        else {
                            g.setColor(Color.RED);
                        }

                        int x = (int) pt.getX() - size / 2;
                        int y = (int) pt.getY() - size / 2;
                        g.fillOval(x, y, size, size);

                        g.setColor(Color.BLACK);
                        g.drawOval(x, y, size, size);
                    }

                    g.dispose();
                }

                private boolean isMatchingPosition(GeoPosition pos, double lat, double lon) {
                    return Math.abs(pos.getLatitude() - lat) < 0.0000001 &&
                            Math.abs(pos.getLongitude() - lon) < 0.0000001;
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
                        location_info locationInZone = findLocationInZone(clicked);

                        if (locationInZone != null) {
                            highlightedLocation = locationInZone;
                            currentWaypoint = null;

                            controller.setLocation(locationInZone.getLatitude(), locationInZone.getLongitude());
                        } else {
                            highlightedLocation = null;
                            setWaypoint(clicked);

                            controller.setLocation(clicked.getLatitude(), clicked.getLongitude());
                        }

                        updateWaypoints();
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
        waypointToLocationMap.clear();

        waypoints.add(currentWaypoint);

        if (existingLocations != null) {
            for (Map.Entry<location_info, Boolean> entry : existingLocations.entrySet()) {
                location_info loc = entry.getKey();
                if (loc != null) {
                    DefaultWaypoint wp = new DefaultWaypoint(
                            new GeoPosition(loc.getLatitude(), loc.getLongitude())
                    );
                    waypoints.add(wp);
                    waypointToLocationMap.put(wp, loc);
                }
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