/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.wheel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Rectangle;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: AbstractWheelDashboardPart.java |
 *          AbstractWheelDashboardPart.java | AbstractWheelDashboardPart.java $
 *
 */
public abstract class AbstractWheelDashboardPart extends AbstractCanvasDashboardPart {
    private final static String X_POS = "xPos";
    private final static String Y_POS = "yPos";
    private final static String Z_POS = "zPos";
    private final static String MIN_X = "minX";
    private final static String MAX_X = "maxX";
    private final static String MIN_Y = "minY";
    private final static String MAX_Y = "maxY";
    private final static String MIN_Z = "minZ";
    private final static String MAX_Z = "maxZ";
    private final static String AUTOADJUST = "autoadjust";
    private final static String BACKGROUND_COLOR = "backgroundColor";
    private final static String BACKGROUND_ALPHA = "backgroundAlpha";
    private final static String COLOR = "color";
    private final static String FONT = "font";

    private class CircleCoordinate {
        final int x;
        final int y;

        /**
         * Class constructor.
         *
         */
        public CircleCoordinate(final Number x, final Number y, final double radius) {
            double circleX = Math.sin(x.doubleValue());
            double circleY = Math.cos(x.doubleValue());
            final double scale = (radius + ((2.0 / 3.0) * radius * y.doubleValue()));
            circleX *= scale;
            circleY *= scale;
            // Center to 2*radius
            this.x = (int) ((2 * radius) + circleX);
            this.y = (int) ((2 * radius) + circleY);
        }
    }

    private static final double TWO_PI = 2.0 * Math.PI;
    /** Background alpha. */
    private int backgroundAlpha = 255;
    /** Min Y value. */
    private double minY = 0.0;
    /** Max Y value. */
    private double maxY = 1.0;
    /** Min X value. */
    private double minX = 0.0;
    /** Max X value. */
    private double maxX = AbstractWheelDashboardPart.TWO_PI;
    /** Min Color value. */
    private double minZ = 0.0;
    /** Max Color value. */
    private double maxZ = 1.0;
    /** Auto adjust min and max value. */
    private boolean autoadjust = true;
    /** The font name. */
    private String font = "Verdana";
    private int xPos = 0;
    private int yPos = 1;
    private int zPos = 2;
    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB color = new RGB(255, 0, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaint(final PaintEvent e) {
        if (this.isAutoadjust()) {
            this.adjust();
        }
        final RGB foreground = this.getForegroundColor();

        final RGB background = this.getBackgroundColor();
        this.setAlpha(getBackgroundAlpha());
        this.fill(background);
        final RGB[] circleColor = foreground.getTriadic();
        this.setAlpha(128);
        this.fillArc(this.getCenter(), (int) (2 * this.getRadius()), 0, 360, circleColor[0]);
        this.setAlpha(255);
        this.fillArc(this.getCenter(), (int) (this.getRadius()), 0, 360, circleColor[1]);
        final RGB innerBackground = this.getForegroundColor().getComplement();
        this.fillArc(this.getCenter(), (int) ((1.0 / 3.0) * this.getRadius()), 0, 360, innerBackground);

        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        final List<Coordinate> coordinates = new ArrayList<>(this.getObjects().size());
        IStreamObject<?> element = null;
        while (iter.hasNext()) {
            element = iter.next();
            final CircleCoordinate coordinate = this.getCoordinate(element);
            coordinates.add(new Coordinate(coordinate.x, coordinate.y));
        }
        this.drawLine(coordinates.toArray(new Coordinate[coordinates.size()]), foreground);
        if (element != null) {
            this.doPaintZ(this.getZ(element));
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        super.onLoad(saved);
        xPos = Integer.valueOf(saved.get(X_POS) != null ? saved.get(X_POS) : "0");
        yPos = Integer.valueOf(saved.get(Y_POS) != null ? saved.get(Y_POS) : "0");
        zPos = Integer.valueOf(saved.get(Z_POS) != null ? saved.get(Z_POS) : "0");
        minX = Double.valueOf(saved.get(MIN_X) != null ? saved.get(MIN_X) : "0");
        maxX = Double.valueOf(saved.get(MAX_X) != null ? saved.get(MAX_X) : "1");
        minY = Double.valueOf(saved.get(MIN_Y) != null ? saved.get(MIN_Y) : "0");
        maxY = Double.valueOf(saved.get(MAX_Y) != null ? saved.get(MAX_Y) : "1");
        minZ = Double.valueOf(saved.get(MIN_Z) != null ? saved.get(MIN_Z) : "0");
        maxZ = Double.valueOf(saved.get(MAX_Z) != null ? saved.get(MAX_Z) : "1");
        autoadjust = Boolean.valueOf(saved.get(AUTOADJUST) != null ? saved.get(AUTOADJUST) : "true");
        backgroundColor = RGB.valueOf(saved.get(BACKGROUND_COLOR) != null ? saved.get(BACKGROUND_COLOR) : "255,0,0");
        backgroundAlpha = Integer.valueOf(saved.get(BACKGROUND_ALPHA) != null ? saved.get(BACKGROUND_ALPHA) : "255");
        color = RGB.valueOf(saved.get(COLOR) != null ? saved.get(COLOR) : "0,0,0");
        font = String.valueOf(saved.get(FONT) != null ? saved.get(FONT) : "Verdana");
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = super.onSave();
        toSaveMap.put(X_POS, String.valueOf(xPos));
        toSaveMap.put(Y_POS, String.valueOf(yPos));
        toSaveMap.put(Z_POS, String.valueOf(zPos));
        toSaveMap.put(MIN_X, String.valueOf(minX));
        toSaveMap.put(MAX_X, String.valueOf(maxX));
        toSaveMap.put(MIN_Y, String.valueOf(minY));
        toSaveMap.put(MAX_Y, String.valueOf(maxY));
        toSaveMap.put(MIN_Z, String.valueOf(minZ));
        toSaveMap.put(MAX_Z, String.valueOf(maxZ));
        toSaveMap.put(AUTOADJUST, String.valueOf(autoadjust));
        toSaveMap.put(BACKGROUND_COLOR, String.valueOf(backgroundColor));
        toSaveMap.put(BACKGROUND_ALPHA, String.valueOf(backgroundAlpha));
        toSaveMap.put(COLOR, String.valueOf(color));
        toSaveMap.put(FONT, String.valueOf(font));

        return toSaveMap;
    }

    /**
     * @param z
     *            The z value
     */
    public abstract void doPaintZ(Number z);

    private void adjust() {
        double minY = Double.POSITIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        while (iter.hasNext()) {
            final IStreamObject<?> element = iter.next();
            final double x = this.getX(element).doubleValue();
            final double y = this.getY(element).doubleValue();
            final double z = this.getZ(element).doubleValue();
            if (y > maxY) {
                maxY = y;
            }
            if (y < minY) {
                minY = y;
            }

            if (x > maxX) {
                maxX = x;
            }
            if (x < minX) {
                minX = x;
            }

            if (z > maxZ) {
                maxZ = z;
            }
            if (z < minZ) {
                minZ = z;
            }
        }
        this.setMinY(minY);
        this.setMaxY(maxY);
        this.setMinX(minX);
        this.setMaxX(maxX);
        this.setMinZ(minZ);
        this.setMaxZ(maxZ);
    }

    //@SuppressWarnings("static-method")
    private Tuple<?> getTuple(final IStreamObject<?> element) {
        return (Tuple<?>) element;
    }

    private Number getX(final IStreamObject<?> element) {
        final Tuple<?> tuple = this.getTuple(element);
        if ((tuple == null) || (tuple.getAttributes().length <= this.getXPos())) {
            return new Double(0.0);
        }
        return (Number) tuple.getAttribute(this.getXPos());
    }

    private Number getY(final IStreamObject<?> element) {
        final Tuple<?> tuple = this.getTuple(element);
        if ((tuple == null) || (tuple.getAttributes().length <= this.getYPos())) {
            return new Double(0.0);
        }
        return (Number) tuple.getAttribute(this.getYPos());
    }

    private Number getZ(final IStreamObject<?> element) {
        final Tuple<?> tuple = this.getTuple(element);
        if ((tuple == null) || (tuple.getAttributes().length <= this.getZPos())) {
            return new Double(0.0);
        }
        return (Number) tuple.getAttribute(this.getZPos());
    }

    private Number normalizeX(final Number value) {
        if (value.doubleValue() > this.getMaxX()) {
            return new Double(AbstractWheelDashboardPart.TWO_PI);
        }
        if (value.doubleValue() < this.getMinX()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMinX()) / Math.abs(this.getMaxX() - this.getMinX())) * AbstractWheelDashboardPart.TWO_PI);
    }

    private Number normalizeY(final Number value) {
        if (value.doubleValue() > this.getMaxY()) {
            return new Double(1);
        }
        if (value.doubleValue() < this.getMinY()) {
            return new Double(-1);
        }
        return new Double((((value.doubleValue() - this.getMinY()) / Math.abs(this.getMaxY() - this.getMinY())) - 0.5) * 2.0);
    }

    protected Number normalizeZ(final Number value) {
        if (value.doubleValue() > this.getMaxZ()) {
            return new Double(1.0);
        }
        if (value.doubleValue() < this.getMinZ()) {
            return new Double(0.0);
        }
        return new Double((value.doubleValue() - this.getMinZ()) / Math.abs(this.getMaxZ() - this.getMinZ()));
    }

    private CircleCoordinate getCoordinate(final IStreamObject<?> element) {
        return new CircleCoordinate(this.normalizeX(this.getX(element)), this.normalizeY(this.getY(element)), this.getRadius());
    }

    public Coordinate getCenter() {
        return new Coordinate((int) (2 * this.getRadius()), (int) (2 * this.getRadius()));
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        final Rectangle bounds = this.getClipping();
        return Math.min(bounds.width, bounds.height) / 4.0;
    }

    /**
     * @return the minY
     */
    public double getMinY() {
        return this.minY;
    }

    /**
     * @param minY
     *            the minY to set
     */
    public void setMinY(final double minY) {
        this.minY = minY;
    }

    /**
     * @return the maxY
     */
    public double getMaxY() {
        return this.maxY;
    }

    /**
     * @param maxY
     *            the maxY to set
     */
    public void setMaxY(final double maxY) {
        this.maxY = maxY;
    }

    /**
     * @return the minX
     */
    public double getMinX() {
        return this.minX;
    }

    /**
     * @param minX
     *            the minX to set
     */
    public void setMinX(final double minX) {
        this.minX = minX;
    }

    /**
     * @return the maxX
     */
    public double getMaxX() {
        return this.maxX;
    }

    /**
     * @param maxX
     *            the maxX to set
     */
    public void setMaxX(final double maxX) {
        this.maxX = maxX;
    }

    /**
     * @return the minColor
     */
    public double getMinZ() {
        return this.minZ;
    }

    /**
     * @param minZ
     *            the minZ to set
     */
    public void setMinZ(final double minZ) {
        this.minZ = minZ;
    }

    /**
     * @return the maxZ
     */
    public double getMaxZ() {
        return this.maxZ;
    }

    /**
     * @param maxZ
     *            the maxZ to set
     */
    public void setMaxZ(final double maxZ) {
        this.maxZ = maxZ;
    }

    /**
     * @return the auto adjust
     */
    public boolean isAutoadjust() {
        return this.autoadjust;
    }

    /**
     * @param autoadjust
     *            the auto adjust to set
     */
    public void setAutoadjust(final boolean autoadjust) {
        this.autoadjust = autoadjust;
    }

    /**
     * @return the xPos
     */
    public int getXPos() {
        return this.xPos;
    }

    /**
     * @param xPos
     *            the xPos to set
     */
    public void setXPos(final int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the yPos
     */
    public int getYPos() {
        return this.yPos;
    }

    /**
     * @param yPos
     *            the yPos to set
     */
    public void setYPos(final int yPos) {
        this.yPos = yPos;
    }

    /**
     * @return the zPos
     */
    public int getZPos() {
        return this.zPos;
    }

    /**
     * @param zPos
     *            the zPos to set
     */
    public void setZPos(final int zPos) {
        this.zPos = zPos;
    }

    /**
     * @return the backgroundColor
     */
    public RGB getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * @param backgroundColor
     *            the backgroundColor to set
     */
    public void setBackgroundColor(final RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the backgroundAlpha
     */
    public int getBackgroundAlpha() {
        return backgroundAlpha;
    }

    /**
     * @param backgroundAlpha
     *            the backgroundAlpha to set
     */
    public void setBackgroundAlpha(int backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
    }

    /**
     * @return the foregroundColor
     */
    public RGB getForegroundColor() {
        return this.color;
    }

    /**
     * @param foregroundColor
     *            the foregroundColor to set
     */
    public void setForegroundColor(final RGB foregroundColor) {
        this.color = foregroundColor;
    }

    /**
     * @param font
     *            the font to set
     */
    public void setFont(final String font) {
        this.font = font;
    }

    /**
     * @return the font
     */
    public String getFont() {
        return this.font;
    }
}
