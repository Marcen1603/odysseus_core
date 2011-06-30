package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class ScanMap extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 8022249253673132751L;
    private final List<List<Coordinate>> segments = new CopyOnWriteArrayList<List<Coordinate>>();

    private static final int SCALE = 20;

    public ScanMap() {
    }

    public void onFeature(final List<Coordinate> segment) {
        this.segments.add(segment);
        this.repaint();
    }

    @Override
    public void paint(final Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 1000, 1000);
        graphics.setColor(Color.WHITE);
        graphics.drawLine(500, 0, 500, 1000);
        graphics.drawLine(0, 500, 1000, 500);
        if (this.segments != null) {
            synchronized (this.segments) {
                final Color color = new Color(254, 0, 0);
                for (final List<Coordinate> segment : this.segments) {
                    int[] xPoints = new int[segment.size()];
                    int[] yPoints = new int[segment.size()];
                    int index = 0;
                    final Polygon segmentPolygon = new Polygon();
                    for (final Coordinate coordinate : segment) {
                        xPoints[index] = 500 + (int) (coordinate.x / ScanMap.SCALE);
                        yPoints[index] = 500 + (int) (coordinate.y / ScanMap.SCALE);
//                        segmentPolygon.addPoint(500 + (int) (coordinate.x / ScanMap.SCALE),
//                                500 + (int) (coordinate.y / ScanMap.SCALE));
                        index++;
                    }
                    graphics.setColor(color);
                //    graphics.drawPolygon(segmentPolygon);
                    graphics.drawPolyline(xPoints, yPoints, segment.size());
                }
                this.segments.clear();
            }

        }
    }
}
