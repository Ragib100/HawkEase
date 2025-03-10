package me.hawkease;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;

import javax.swing.*;

public class view_map {
    private JXMapViewer mapViewer;
    private int current_zoom = 5;
    private BorderPane maproot;
    private final map_controller controller;

    public view_map(map_controller controller) {
        this.controller = controller;
        initialize_map();
    }

    private void initialize_map() {
        maproot = new BorderPane();
        SwingNode swingnode = new SwingNode();
        create_and_set_swing_node(swingnode);
        maproot.setCenter(swingnode);
    }

    private void create_and_set_swing_node(final SwingNode swingnode) {
        SwingUtilities.invokeLater(() -> {
           mapViewer = new JXMapViewer();
           TileFactoryInfo info = new OSMTileFactoryInfo();
           DefaultTileFactory tileFactory = new DefaultTileFactory(info);
           tileFactory.setThreadPoolSize(8);
           mapViewer.setTileFactory(tileFactory);

           mapViewer.setAddressLocation(new GeoPosition(23.735,90.385));
           mapViewer.setZoom(current_zoom);


        });
    }
}
