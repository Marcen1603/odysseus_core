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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorchart;

import java.util.Iterator;
import java.util.Random;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.CIELCH;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: ColorChartDashboardPart.java | Sat Apr 11 02:35:49 2015 +0000 |
 *          ckuka $
 *
 */
public class ColorChartDashboardPart extends AbstractCanvasDashboardPart {
    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB color = new RGB(0, 255, 0);

    /** Min Y value. */
    private double minY = 0.0;
    /** Max Y value. */
    private double maxY = 1.0;
    /** Min X value. */
    private double minX = 0.0;
    /** Max X value. */
    private double maxX = 1.0;
    /** Min Color value. */
    private double minZ = 0.0;
    /** Max Color value. */
    private double maxZ = 1.0;
    /** Auto adjust min and max value. */
    private boolean autoadjust = true;
    private int xPos = 0;
    private int yPos = 1;
    private int zPos = 2;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPaint() {
        if (this.isAutoadjust()) {
            this.adjust();
        }
        final RGB background = this.getBackgroundColor();
        this.setAlpha(0);
        this.fill(background);
        this.setAlpha(255);
        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        IStreamObject<?> element = null;
        while (iter.hasNext()) {
            element = iter.next();
            final Number value = this.normalizeZ(this.getZ(element));
            final int width = (int) ((this.getClipping().width / this.getMaxX()) + 0.5);
            final int height = (int) ((this.getClipping().height / this.getMaxY()) + 0.5);
            this.fillRectangle(this.getCoordinate(element), width, height, this.getColor(value));
        }
    }

    @SuppressWarnings("hiding")
    private void adjust() {
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
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

    @SuppressWarnings("static-method")
    private Tuple<?> getTuple(final IStreamObject<?> element) {
        return (Tuple<?>) element;
    }

    private Coordinate getCoordinate(final IStreamObject<?> element) {
        return new Coordinate(this.normalizeX(this.getX(element)).doubleValue(), this.normalizeY(this.getY(element)).doubleValue());
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
            return new Double(1.0);
        }
        if (value.doubleValue() < this.getMinX()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMinX()) / Math.abs(this.getMaxX() - this.getMinX())) * this.getClipping().width);
    }

    private Number normalizeY(final Number value) {
        if (value.doubleValue() > this.getMaxY()) {
            return new Double(1.0);
        }
        if (value.doubleValue() < this.getMinY()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMinY()) / Math.abs(this.getMaxY() - this.getMinY())) * this.getClipping().height);
    }

    protected Number normalizeZ(final Number value) {
        if (value.doubleValue() > this.getMaxZ()) {
            return new Double(1.0);
        }
        if (value.doubleValue() < this.getMinZ()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMinZ()) / Math.abs(this.getMaxZ() - this.getMinZ())) * 180.0);
    }

    private RGB getColor(final Number value) {
        final RGB rgb = this.getColor();
        final CIELCH colorspace = rgb.toCIELCH();
        return new CIELCH(colorspace.L, colorspace.C, colorspace.H + value.doubleValue()).toCIELab().toXYZ().toRGB();
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
     * @param color
     *            the color to set
     */
    public void setColor(final RGB color) {
        this.color = color;
    }

    /**
     * @return the arrowColor
     */
    public RGB getColor() {
        return this.color;
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

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final ColorChartDashboardPart chart = new ColorChartDashboardPart();
        chart.setMaxElements(1000);
        final Thread generator = new Thread() {
            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("boxing")
            @Override
            public void run() {
                final Random rnd = new Random();
                while (!this.isInterrupted()) {
                    @SuppressWarnings("rawtypes")
                    final Tuple tuple = new Tuple(3, false);
                    tuple.setAttribute(0, rnd.nextDouble() * 100);
                    tuple.setAttribute(1, rnd.nextDouble() * 100);
                    tuple.setAttribute(2, rnd.nextDouble() * 10);
                    chart.streamElementRecieved(null, tuple, 0);
                    try {
                        Thread.sleep(100);
                    }
                    catch (final InterruptedException e) {
                        // Empty block
                    }
                }
            }
        };
        chart.createPartControl(shell, null);
        generator.start();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        generator.interrupt();
        display.dispose();
    }

}
