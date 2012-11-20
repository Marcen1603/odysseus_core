package de.offis.gwtsvgeditor.client.svg;

import de.offis.gui.client.gwtgraphics.shape.Rectangle;

/**
 * This class is the SVG rectangle in a Multi Selection.
 * 
 * @author Alexander Funk
 *
 */
public class SvgMultiSelectArea extends Rectangle {

    private int startX;
    private int startY;

    public SvgMultiSelectArea(int startX, int startY) {
        super(startX, startY, 1, 1);
        this.startX = startX;
        this.startY = startY;
        setFillColor("lightblue");
        setFillOpacity(0.5);
        setStrokeWidth(1);

    }

    public void setEndPoint(int x, int y) {
        if (x < startX) {
            setX(x);
        } else {
            setX(startX);
        }

        if (y < startY) {
            setY(y);
        } else {
            setY(startY);
        }

        setWidth(Math.abs(startX - x));
        setHeight(Math.abs(startY - y));
    }

    /**
     * Liefert die abgedeckte Flaeche durch Angabe der Ecke oben links und unten rechts, jeweils x und y-Koordinate.
     * @return
     */
    public double[] getArea() {
        return new double[]{getX(), getY(), getX() + getWidth(), getY() + getHeight()};
    }
}
