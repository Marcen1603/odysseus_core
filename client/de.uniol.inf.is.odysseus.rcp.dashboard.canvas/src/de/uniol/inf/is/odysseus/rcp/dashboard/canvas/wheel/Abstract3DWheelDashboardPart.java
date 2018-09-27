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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractGLCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: Abstract3DWheelDashboardPart.java | Fri Apr 10 23:28:57 2015
 *          +0000 | ckuka $
 *
 */
public abstract class Abstract3DWheelDashboardPart extends AbstractGLCanvasDashboardPart {
    private class CircleCoordinate {
        final double x;
        final double y;
        final double z;

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
            this.x = circleX;
            this.y = circleY;
            this.z = 0;
        }
    }

    private static final double TWO_PI = 2.0 * Math.PI;
    private double radius = 2.0;
    /** Min Y value. */
    private double minY = 0.0;
    /** Max Y value. */
    private double maxY = 1.0;
    /** Min X value. */
    private double minX = 0.0;
    /** Max X value. */
    private double maxX = Abstract3DWheelDashboardPart.TWO_PI;
    /** Min Color value. */
    private double minZ = 0.0;
    /** Max Color value. */
    private double maxZ = 1.0;
    /** Auto adjust min and max value. */
    private boolean autoadjust = true;
    private int xPos = 0;
    private int yPos = 1;
    private int zPos = 2;
    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB foregroundColor = new RGB(255, 0, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaint() {
        if (this.isAutoadjust()) {
            this.adjust();
        }
        final RGB foreground = this.getForegroundColor();

        final RGB[] circleColor = foreground.getTriadic();
        this.drawTorus(1, this.getRadius(), 15, 15, circleColor[1]);
        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        final List<Coordinate> coordinates = new ArrayList<>(this.getObjects().size());
        IStreamObject<?> element = null;
        while (iter.hasNext()) {
            element = iter.next();
            final CircleCoordinate coordinate = this.getCoordinate(element);
            coordinates.add(new Coordinate(coordinate.x, coordinate.y, coordinate.z));
        }

        this.drawLine(coordinates.toArray(new Coordinate[coordinates.size()]), foreground);
        if (element != null) {
            this.doPaintZ(this.getZ(element));
        }

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
        this.setMinY((this.getMinY() + minY) / 2.0);
        this.setMaxY((this.getMaxY() + maxY) / 2.0);
        this.setMinX((this.getMinX() + minX) / 2.0);
        this.setMaxX((this.getMaxX() + maxX) / 2.0);
        this.setMinZ((this.getMinZ() + minZ) / 2.0);
        this.setMaxZ((this.getMaxZ() + maxZ) / 2.0);

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
            return new Double(Abstract3DWheelDashboardPart.TWO_PI);
        }
        if (value.doubleValue() < this.getMinX()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMinX()) / Math.abs(this.getMaxX() - this.getMinX())) * Abstract3DWheelDashboardPart.TWO_PI);
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
        return this.radius;
    }

    /**
     * @param radius
     */
    public void setRadius(final double radius) {
        this.radius = radius;
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
     * @return the foregroundColor
     */
    public RGB getForegroundColor() {
        return this.foregroundColor;
    }

    /**
     * @param foregroundColor
     *            the foregroundColor to set
     */
    public void setForegroundColor(final RGB foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

}
