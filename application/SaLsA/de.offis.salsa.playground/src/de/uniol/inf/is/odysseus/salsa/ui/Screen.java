package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;

import com.vividsolutions.jts.geom.Coordinate;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class Screen extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 253561357461155938L;
    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private final ScanMap map;

    public Screen() {
        this.map = new ScanMap();

        this.setContentPane(this.map);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.map.setLayout(new GridLayout(1, 1));
        this.map.setVisible(true);
        this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.map.setPreferredSize(new Dimension(Screen.WIDTH + this.map.getX(), Screen.HEIGHT
                + this.map.getX()));
        this.setTitle("Scan");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    public void onFeature(final List<Coordinate> segment) {
        this.map.onFeature(segment);
    }

}
