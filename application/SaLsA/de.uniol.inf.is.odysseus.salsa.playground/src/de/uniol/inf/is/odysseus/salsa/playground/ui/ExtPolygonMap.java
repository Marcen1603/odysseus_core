package de.uniol.inf.is.odysseus.salsa.playground.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ExtPolygonMap extends JPanel implements KeyListener {

    /**
     * 
     */
    private static final long serialVersionUID = 8022249253673132751L;
    private final List<RelationalTuple<? extends IMetaAttribute>> segments = new CopyOnWriteArrayList<RelationalTuple<? extends IMetaAttribute>>();
    private int zoom = 20;
    private Double angle = 0.0;
    private Double[] offset = new Double[] {
            0.0, 0.0
    };

    public ExtPolygonMap() {
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public void onFeature(final RelationalTuple<? extends IMetaAttribute> segment) {
        this.segments.add(segment);
        if (this.segments.size() > 10) {
            this.repaint();
        }
    }
    
    @Override
    public void paint(final Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 1000, 1000);
        graphics.setColor(Color.WHITE);
        graphics.drawLine(500, 0, 500, 1000);
        graphics.drawLine(0, 500, 1000, 500);

        double angle = Math.toRadians(this.angle);
        for (final RelationalTuple<? extends IMetaAttribute> tuple : this.segments) {
        	Geometry segment = (Geometry)tuple.getAttribute(0);
        	String text = (String)tuple.getAttribute(1);
        	
            final Coordinate[] coordinates = segment.getCoordinates();
            int[] xPoints = new int[coordinates.length];
            int[] yPoints = new int[coordinates.length];
            int index = 0;
            
            for (final Coordinate coordinate : coordinates) {
            	double x = ((coordinate.x - this.offset[0]) * Math.cos(angle) - (coordinate.y - this.offset[1])
                        * Math.sin(angle));
            	double y = ((coordinate.x - this.offset[0]) * Math.sin(angle) + (coordinate.y - this.offset[1])
                        * Math.cos(angle));
                xPoints[index] = 500 + (int) (x / zoom);
                yPoints[index] = 500 - (int) (y / zoom);
                index++;
            }
            graphics.setColor(Color.WHITE);
            if(text.equals("Human")){
            	graphics.setColor(Color.RED);	
            }
            if(text.equals("Scooter")){
            	graphics.setColor(Color.GREEN);	
            }
            
            graphics.drawPolyline(xPoints, yPoints, coordinates.length);
            graphics.drawString(text, xPoints[0], yPoints[0]);
            graphics.drawString("Off: " + this.offset[0] + "/" + this.offset[1], 5, 35 + 20);
            graphics.drawString("Angle: " + this.angle, 5, 35 + 30);
            // final Coordinate[] enverlopeCoordinates = segment.getEnvelope().getCoordinates();
            // xPoints = new int[enverlopeCoordinates.length];
            // yPoints = new int[enverlopeCoordinates.length];
            // index = 0;
            // for (final Coordinate coordinate : enverlopeCoordinates) {
            // xPoints[index] = 500 + (int) (coordinate.x / PolygonMap.SCALE);
            // yPoints[index] = 500 + (int) (coordinate.y / PolygonMap.SCALE);
            // index++;
            // }
            // graphics.setColor(Color.RED);
            // graphics.drawPolyline(xPoints, yPoints, enverlopeCoordinates.length);

        }
        this.segments.clear();

    }

    @Override
    public void keyPressed(KeyEvent event) {
        double angle = Math.toRadians(this.angle);
        double radian = Math.toRadians(180);
        switch (event.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if (event.isControlDown()) {
                    this.offset[0] += (10.0 * Math.cos(radian - angle));
                    this.offset[1] += (10.0 * Math.sin(radian - angle));
                }
                else {
                    this.offset[0] += Math.cos(radian - angle);
                    this.offset[1] += Math.sin(radian - angle);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (event.isControlDown()) {
                    this.offset[0] -= (10.0 * Math.cos(radian - angle));
                    this.offset[1] -= (10.0 * Math.sin(radian - angle));
                }
                else {
                    this.offset[0] -= Math.cos(radian - angle);
                    this.offset[1] -= Math.sin(radian - angle);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (event.isControlDown()) {
                    this.offset[0] += (10.0 * Math.sin(angle));
                    this.offset[1] += (10.0 * Math.cos(angle));
                }
                else {
                    this.offset[0] += Math.sin(angle);
                    this.offset[1] += Math.cos(angle);
                }
                break;
            case KeyEvent.VK_UP:
                if (event.isControlDown()) {
                    this.offset[0] -= (10.0 * Math.sin(angle));
                    this.offset[1] -= (10.0 * Math.cos(angle));
                }
                else {
                    this.offset[0] -= Math.sin(angle);
                    this.offset[1] -= Math.cos(angle);
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if (event.isControlDown()) {
                    if (zoom > 1) {
                        zoom--;
                    }
                }
                else {
                    this.angle += 0.5f;
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if (event.isControlDown()) {
                    zoom++;
                }
                else {
                    this.angle -= 0.5f;
                }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}
