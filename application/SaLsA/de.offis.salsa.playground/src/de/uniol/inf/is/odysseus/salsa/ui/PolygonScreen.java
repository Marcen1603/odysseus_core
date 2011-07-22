package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class PolygonScreen extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 253561357461155938L;
    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private final PolygonMap map;

    public PolygonScreen() {
        this.map = new PolygonMap();

        this.setContentPane(this.map);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.map.setLayout(new GridLayout(1, 1));
        this.map.setVisible(true);
        this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.map.setPreferredSize(new Dimension(PolygonScreen.WIDTH + this.map.getX(), PolygonScreen.HEIGHT
                + this.map.getX()));
        this.setTitle("Scan");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void onFeature(final Geometry segment) {
        this.map.onFeature(segment);
    }

}
