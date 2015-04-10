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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.gauge;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Random;

import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class LiquidGaugeDashboardPart extends AbstractCanvasDashboardPart {
    private RGB backgroundColor = new RGB(255, 255, 255);
    private RGB liquidColor = new RGB(0, 255, 0);

    private double radius = 100.0;

    /** Min value. */
    private double min = 0.0;
    /** Max value. */
    private double max = 1.0;
    /** Auto adjust min and max value. */
    private boolean autoadjust = true;
    private int pos = 0;

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
        }
        if (element != null) {
            final double value = this.normalize(this.get(element)).doubleValue() * 2.0;
            // Start at 6 o'clock
            final double angle = -90.0;
            final Path path = new Path(this.getGC().getDevice());
            final double delta = Math.toDegrees(Math.acos(1.0 - value));
            final int start = (int) (angle + delta);
            final int end = (int) (angle - delta - start);
            path.addArc((int) (this.getCenter().x - this.getRadius()), (int) (this.getCenter().y - this.getRadius()), (int) (2 * this.getRadius()), (int) (2 * this.getRadius()), start, end);
            final int pathX = (int) (Math.sin(Math.toRadians(delta)) * this.getRadius());

            // Draw waves if they do not exceed the max value
            if ((this.getCenter().y - ((value - 1.0) * this.getRadius()) - ((1.0 / 6.0) * (value / 2.0) * this.getRadius())) >= (this.getCenter().y - this.getRadius())) {
                path.cubicTo((float) ((this.getCenter().x - pathX) + ((1.0 / 3.0) * pathX)),
                        (float) (this.getCenter().y - ((value - 1.0) * this.getRadius()) - ((1.0 / 6.0) * (value / 2.0) * this.getRadius())),
                        (float) ((this.getCenter().x - pathX) + ((2.0 / 3.0) * pathX)),
                        (float) ((this.getCenter().y - ((value - 1.0) * this.getRadius())) + ((1.0 / 6.0) * (value / 2.0) * this.getRadius())), (float) this.getCenter().x,
                        (int) (this.getCenter().y - ((value - 1.0) * this.getRadius())));

                path.cubicTo((float) (this.getCenter().x + ((1.0 / 3.0) * pathX)),
                        (float) (this.getCenter().y - ((value - 1.0) * this.getRadius()) - ((1.0 / 6.0) * (value / 2.0) * this.getRadius())), (float) (this.getCenter().x + ((2.0 / 3.0) * pathX)),
                        (float) ((this.getCenter().y - ((value - 1.0) * this.getRadius())) + ((1.0 / 6.0) * (value / 2.0) * this.getRadius())), (float) (this.getCenter().x + pathX),
                        (int) (this.getCenter().y - ((value - 1.0) * this.getRadius())));
            }
            path.close();
            this.fillPath(path, this.liquidColor);
            final String text = NumberFormat.getIntegerInstance().format(this.get(element));
            final Coordinate extent = this.textExtent(text);
            if ((this.getCenter().y - ((value - 1.0) * this.getRadius()) - ((1.0 / 6.0) * (value / 2.0) * this.getRadius())) <= (this.getCenter().y - (extent.y / 2))) {
                this.setForeground(this.getLiquidColor().getComplement());
                this.setBackground(this.getLiquidColor());
            }
            else {
                this.setForeground(this.getLiquidColor());
                this.setBackground(this.getBackgroundColor());
            }
            this.drawText(text, new Coordinate(this.getCenter().x - (extent.x / 2), this.getCenter().y - (extent.y / 2)), true);
            this.getGC().setTransform(null);
        }
    }

    public Coordinate getCenter() {
        return new Coordinate((int) (2 * this.getRadius()), (int) (2 * this.getRadius()));
    }

    @SuppressWarnings("hiding")
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
        this.setMin((this.getMin() + min) / 2.0);
        this.setMax((this.getMax() + max) / 2.0);

    }

    @SuppressWarnings("static-method")
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
        return new Double((value.doubleValue() - this.getMin()) / Math.abs(this.getMax() - this.getMin()));
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
     * @param liquidColor
     *            the liquidColor to set
     */
    public void setLiquidColor(final RGB liquidColor) {
        this.liquidColor = liquidColor;
    }

    /**
     * @return the liquidColor
     */
    public RGB getLiquidColor() {
        return this.liquidColor;
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
        final LiquidGaugeDashboardPart compass = new LiquidGaugeDashboardPart();
        compass.setRadius(100);
        final Thread generator = new Thread() {
            /**
             * {@inheritDoc}
             */
            @SuppressWarnings("boxing")
            @Override
            public void run() {
                final Random rnd = new Random();
                long i = 0l;
                while (!this.isInterrupted()) {
                    @SuppressWarnings("rawtypes")
                    final Tuple tuple = new Tuple(3, false);
                    tuple.setAttribute(0, i);
                    tuple.setAttribute(1, rnd.nextDouble() * 100);
                    tuple.setAttribute(2, rnd.nextDouble() * 1000);
                    compass.streamElementRecieved(null, tuple, 0);
                    i++;
                    if (i > 100) {
                        i = 0;
                    }
                    try {
                        Thread.sleep(100);
                    }
                    catch (final InterruptedException e) {
                        // Empty block
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
