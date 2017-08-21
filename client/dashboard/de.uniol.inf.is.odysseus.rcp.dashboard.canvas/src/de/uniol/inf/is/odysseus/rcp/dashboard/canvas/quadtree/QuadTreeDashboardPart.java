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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.quadtree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import org.eclipse.swt.events.PaintEvent;
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
 * @version $Id: QuadTreeDashboardPart.java | Thu Apr 16 17:51:24 2015 +0000 |
 *          ckuka $
 * @param <Node>
 *
 */
public class QuadTreeDashboardPart extends AbstractCanvasDashboardPart {
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

    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB color = new RGB(0, 255, 0);

    /** Background alpha. */
    private int backgroundAlpha = 255;
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
    public void doPaint(final PaintEvent e) {
        if (this.isAutoadjust()) {
            this.adjust();
        }
        final RGB background = this.getBackgroundColor();
        this.setAlpha(getBackgroundAlpha());
        this.fill(background);
        this.setAlpha(255);
        QuadTree<Double, Double> tree = new QuadTree<>();
        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        IStreamObject<?> element = null;
        while (iter.hasNext()) {
            element = iter.next();
            final Number x = this.normalizeX(this.getX(element));
            final Number y = this.normalizeY(this.getY(element));
            final Number value = this.normalizeZ(this.getZ(element));
            tree.insert(new Double(x.doubleValue()), new Double(y.doubleValue()), new Double(value.doubleValue()));
        }

        Queue<QuadTree<Double, Double>.Node> queue = new LinkedList<>();
        queue.offer(tree.root);
        CIELCH c = getColor().toCIELCH();
        while (!queue.isEmpty()) {
            QuadTree<Double, Double>.Node node = queue.poll();
            if (node != null) {
                Coordinate corner = new Coordinate(node.ex1.doubleValue(), node.ey1.doubleValue());
                this.fillRectangle(corner, (node.ex2.intValue() - node.ex1.intValue()), (node.ey2.intValue() - node.ey1.intValue()), new CIELCH(c.L, c.C, c.H + node.value.doubleValue()));
                if (node.NE != null) {
                    queue.offer(node.NE);
                }
                if (node.NW != null) {
                    queue.offer(node.NW);
                }
                if (node.SE != null) {
                    queue.offer(node.SE);
                }
                if (node.SW != null) {
                    queue.offer(node.SW);
                }
            }
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
        return toSaveMap;
    }

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

    private static class QuadTree<K extends Comparable<K>, T> {

        private class Node {
            K x;
            K y;
            T value;
            Node NW, NE, SE, SW;
            K ex1;
            K ey1;
            K ex2;
            K ey2;

            /**
             * Class constructor.
             *
             * @param x
             * @param y
             * @param value
             */
            public Node(final K x, final K y, final T value) {
                super();
                this.x = x;
                this.y = y;
                this.value = value;
                this.ex1 = x;
                this.ex2 = x;
                this.ey1 = y;

                this.ey2 = y;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return "Node [x=" + this.x + ", y=" + this.y + ", value=" + this.value + "]";
            }

        }

        Node root;

        /**
         * Class constructor.
         *
         */
        public QuadTree() {
        }

        public void insert(final K x, final K y, final T value) {
            this.root = this.insert(this.root, x, y, value);
        }

        private Node insert(final Node n, final K x, final K y, final T value) {
            if (n == null) {
                return new Node(x, y, value);
            }
            n.ex1 = n.ex1.compareTo(x) < 0 ? n.ex1 : x;
            n.ey1 = n.ey1.compareTo(y) < 0 ? n.ey1 : y;
            n.ex2 = x.compareTo(n.ex2) < 0 ? n.ex2 : x;
            n.ey2 = y.compareTo(n.ey2) < 0 ? n.ey2 : y;
            if ((x.compareTo(n.x) < 0) && (y.compareTo(n.y) < 0)) {
                n.SW = this.insert(n.SW, x, y, value);
            }
            else if ((x.compareTo(n.x) < 0) && (y.compareTo(n.y) >= 0)) {
                n.NW = this.insert(n.NW, x, y, value);
            }
            else if ((x.compareTo(n.x) >= 0) && (y.compareTo(n.y) < 0)) {
                n.SE = this.insert(n.SE, x, y, value);
            }
            else if ((x.compareTo(n.x) >= 0) && (y.compareTo(n.y) >= 0)) {
                n.NE = this.insert(n.NE, x, y, value);
            }
            return n;
        }

    }

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final QuadTreeDashboardPart chart = new QuadTreeDashboardPart();
        chart.setMaxElements(1000);
        final Thread generator = new Thread() {
            /**
             * {@inheritDoc}
             */
            //@SuppressWarnings("boxing")
            @Override
            public void run() {
                final Random rnd = new Random();
                while (!this.isInterrupted()) {
                    @SuppressWarnings("rawtypes")
                    final Tuple tuple = new Tuple(3, false);
                    tuple.setAttribute(0, rnd.nextDouble() * 100);
                    tuple.setAttribute(1, rnd.nextDouble() * 100);
                    tuple.setAttribute(2, rnd.nextDouble() * 10);
                    chart.streamElementReceived(null, tuple, 0);
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
