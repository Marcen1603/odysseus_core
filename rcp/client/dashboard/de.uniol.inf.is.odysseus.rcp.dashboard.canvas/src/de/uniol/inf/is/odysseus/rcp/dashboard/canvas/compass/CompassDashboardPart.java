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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.compass;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: CompassDashboardPart.java | CompassDashboardPart.java |
 *          CompassDashboardPart.java $
 *
 */
public class CompassDashboardPart extends AbstractCanvasDashboardPart {
    private final static String POS = "pos";
    private final static String MIN = "min";
    private final static String MAX = "max";
    private final static String AUTOADJUST = "autoadjust";
    private final static String BACKGROUND_COLOR = "backgroundColor";
    private final static String BACKGROUND_ALPHA = "backgroundAlpha";
    private final static String COLOR = "color";
    private final static String FONT = "font";

    private static final double TWO_PI = 2.0 * Math.PI;
    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB color = new RGB(0, 255, 0);
    /** Background alpha. */
    private int backgroundAlpha = 255;
    /** The font name. */
    private String font = "Verdana";
    /** Min value. */
    private double min = 0.0;
    /** Max value. */
    private double max = CompassDashboardPart.TWO_PI;
    /** Auto adjust min and max value. */
    private boolean autoadjust = true;
    private int pos = 0;

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

        this.drawArc(this.getCenter(), (int) (this.getRadius()), 0, 360, RGB.BLACK);
        this.drawArc(this.getCenter(), (int) ((5.0 / 6.0) * this.getRadius()), 0, 360, RGB.BLACK);

        this.drawLine(new Coordinate((int) (this.getCenter().x - ((5.0 / 6.0) * this.getRadius())), this.getCenter().y), new Coordinate(this.getCenter().x - 8, this.getCenter().y), RGB.BLACK);
        this.drawLine(new Coordinate(this.getCenter().x + 8, this.getCenter().y), new Coordinate((int) (this.getCenter().x + ((5.0 / 6.0) * this.getRadius())), this.getCenter().y), RGB.BLACK);

        this.drawLine(new Coordinate(this.getCenter().x, (int) (this.getCenter().y - ((5.0 / 6.0) * this.getRadius()))), new Coordinate(this.getCenter().x, this.getCenter().y - 8), RGB.BLACK);
        this.drawLine(new Coordinate(this.getCenter().x, this.getCenter().y + 8), new Coordinate(this.getCenter().x, (int) (this.getCenter().y + ((5.0 / 6.0) * this.getRadius()))), RGB.BLACK);

        final Coordinate extentN = this.textExtent("N");
        this.drawText("N", new Coordinate(this.getCenter().x - (extentN.x / 2), this.getCenter().y - this.getRadius() - (extentN.y / 2)));

        final Coordinate extentE = this.textExtent("E");
        this.drawText("E", new Coordinate((this.getCenter().x + this.getRadius()) - (extentE.x / 2), this.getCenter().y - (extentE.y / 2)));

        final Coordinate extentS = this.textExtent("S");
        this.drawText("S", new Coordinate(this.getCenter().x - (extentS.x / 2), (this.getCenter().y + this.getRadius()) - (extentS.y / 2)));

        final Coordinate extentW = this.textExtent("W");
        this.drawText("W", new Coordinate(this.getCenter().x - this.getRadius() - (extentW.x / 2), this.getCenter().y - (extentW.y / 2)));

        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        IStreamObject<?> element = null;
        while (iter.hasNext()) {
            element = iter.next();
        }
        if (element != null) {
            final Number value = this.normalize(this.get(element));
            final double angle = value.doubleValue() * 360.0;
            final Path path = new Path(this.getGC().getDevice());
            path.moveTo((int) ((-(2.0 / 3.0) * this.getRadius()) + 4), 1);
            path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), 1);
            path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), 4);
            path.lineTo((int) (((2.0 / 3.0) * this.getRadius()) - 4), 0);
            path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), -4);
            path.lineTo((int) ((1.0 / 6.0) * this.getRadius()), -1);
            path.lineTo((int) ((-(2.0 / 3.0) * this.getRadius()) + 4), -1);
            path.close();
            final Transform transform = new Transform(this.getGC().getDevice());
            transform.translate((int) this.getCenter().x, (int) this.getCenter().y);
            transform.rotate((float) (angle - 90.0));
            this.getGC().setTransform(transform);
            this.fillPath(path, this.color);
            this.getGC().setTransform(null);
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        super.onLoad(saved);
        pos = Integer.valueOf(saved.get(POS)!=null?saved.get(POS):"0");
        min = Double.valueOf(saved.get(MIN)!=null?saved.get(MIN):"0");
        max = Double.valueOf(saved.get(MAX)!=null?saved.get(MAX):"1");
        autoadjust = Boolean.valueOf(saved.get(AUTOADJUST) != null ? saved.get(AUTOADJUST) : "true");
        backgroundColor = RGB.valueOf(saved.get(BACKGROUND_COLOR) != null ? saved.get(BACKGROUND_COLOR) : "255,0,0");
        backgroundAlpha = Integer.valueOf(saved.get(BACKGROUND_ALPHA) != null ? saved.get(BACKGROUND_ALPHA) : "255");
        color = RGB.valueOf(saved.get(COLOR) != null ? saved.get(COLOR) : "0,0,0");
        font = String.valueOf(saved.get(FONT)!=null?saved.get(FONT):"Verdana");
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = super.onSave();
        toSaveMap.put(POS, String.valueOf(pos));
        toSaveMap.put(MIN, String.valueOf(min));
        toSaveMap.put(MAX, String.valueOf(max));
        toSaveMap.put(AUTOADJUST, String.valueOf(autoadjust));
        toSaveMap.put(BACKGROUND_COLOR, String.valueOf(backgroundColor));
        toSaveMap.put(BACKGROUND_ALPHA, String.valueOf(backgroundAlpha));
        toSaveMap.put(COLOR, String.valueOf(color));
        toSaveMap.put(FONT, String.valueOf(font));
        return toSaveMap;
    }

    public Coordinate getCenter() {
        return new Coordinate((int) (this.getRadius() + 10), (int) (this.getRadius() + 10));
    }

    private void adjust() {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        final Iterator<IStreamObject<?>> iter = this.getObjects().iterator();
        while (iter.hasNext()) {
            final IStreamObject<?> element = iter.next();
            final double x = this.get(element).doubleValue();

            if (x > max) {
                max = x;
            }
            if (x < min) {
                min = x;
            }

        }
        this.setMin(min);
        this.setMax(max);
    }

    //@SuppressWarnings("static-method")
    private Tuple<?> getTuple(final IStreamObject<?> element) {
        return (Tuple<?>) element;
    }

    private Number get(final IStreamObject<?> element) {
        final Tuple<?> tuple = this.getTuple(element);
        if ((tuple == null) || (tuple.getAttributes().length <= this.getPos())) {
            return new Double(0.0);
        }
        return (Number) tuple.getAttribute(this.getPos());
    }

    private Number normalize(final Number value) {
        if (value.doubleValue() > this.getMax()) {
            return new Double(1.0);
        }
        if (value.doubleValue() < this.getMin()) {
            return new Double(0.0);
        }
        return new Double(((value.doubleValue() - this.getMin()) / Math.abs(this.getMax() - this.getMin())));
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        final Rectangle bounds = this.getClipping();
        return (Math.min(bounds.width, bounds.height) / 2.0) - 10;
    }

    /**
     * @return the min
     */
    public double getMin() {
        return this.min;
    }

    /**
     * @param min
     *            the min to set
     */
    public void setMin(final double min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public double getMax() {
        return this.max;
    }

    /**
     * @param max
     *            the max to set
     */
    public void setMax(final double max) {
        this.max = max;
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
     * @return the pos
     */
    public int getPos() {
        return this.pos;
    }

    /**
     * @param pos
     *            the pos to set
     */
    public void setPos(final int pos) {
        this.pos = pos;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(final RGB color) {
        this.color = color;
    }

    /**
     * @return the color
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

    public static void main(final String[] args) {

        final Display display = new Display();
        final Shell shell = new Shell(display);

        shell.setSize(500, 500);

        shell.open();
        final CompassDashboardPart compass = new CompassDashboardPart();
        compass.setAutoadjust(false);
        compass.setMin(0.0);
        compass.setMax(360.0);
        final Thread generator = new Thread() {
            /**
             * {@inheritDoc}
             */
            //@SuppressWarnings("boxing")
            @Override
            public void run() {
                final Random rnd = new Random();
                int i = 0;
                while (!this.isInterrupted()) {
                    @SuppressWarnings("rawtypes")
                    final Tuple tuple = new Tuple(3, false);
                    tuple.setAttribute(0, i);
                    tuple.setAttribute(1, rnd.nextDouble() * 100);
                    tuple.setAttribute(2, rnd.nextDouble() * 1000);
                    compass.streamElementReceived(null, tuple, 0);
                    try {
                        Thread.sleep(200);
                    }
                    catch (final InterruptedException e) {
                        // Empty block
                    }
                    i++;
                    if (i > 360) {
                        i = 0;
                    }
                }
            }
        };
        compass.createPartControl(shell, null);
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
