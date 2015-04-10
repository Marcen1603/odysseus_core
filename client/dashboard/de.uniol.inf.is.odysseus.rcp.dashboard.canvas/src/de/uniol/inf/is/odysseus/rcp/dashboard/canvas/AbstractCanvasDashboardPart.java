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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.IColorSpace;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public abstract class AbstractCanvasDashboardPart extends AbstractDashboardPart implements PaintListener {
    private Canvas canvas;
    private final Queue<IStreamObject<?>> queue = new ConcurrentLinkedQueue<>();
    private CanvasUpdater updater;
    private GC gc;
    private int maxElements = 100;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(final Composite parent, final ToolBar toolbar) {
        parent.setLayout(new FillLayout());

        final Composite composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new FillLayout());

        this.canvas = new Canvas(composite, SWT.BORDER);
        this.canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        this.canvas.addPaintListener(this);

        parent.layout();
        this.updater = new CanvasUpdater(this.canvas);
        this.updater.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void streamElementRecieved(final IPhysicalOperator operator, final IStreamObject<?> element, final int port) {
        this.queue.offer(element);
        while (this.queue.size() > getMaxElements()) {
            this.queue.poll();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void punctuationElementRecieved(final IPhysicalOperator operator, final IPunctuation punctuation, final int port) {
        // Empty method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void securityPunctuationElementRecieved(final IPhysicalOperator operator, final ISecurityPunctuation punctuation, final int port) {
        // Empty method
    }

    /**
     * @return the canvas
     */
    public Canvas getCanvas() {
        return this.canvas;
    }

    /**
     * @return the object
     */
    public Collection<IStreamObject<?>> getObjects() {
        return Collections.unmodifiableCollection(this.queue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintControl(final PaintEvent e) {
        this.gc = new GC(this.getCanvas());
        this.gc.setTextAntialias(SWT.ON);
        this.gc.setAdvanced(true);
        this.gc.setAntialias(SWT.ON);
        this.doPaint();
        this.gc.dispose();
        this.gc = null;
    }

    public abstract void doPaint();

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        if (this.updater != null) {
            this.updater.interrupt();
        }
        if ((this.canvas != null) && (!this.canvas.isDisposed())) {
            this.canvas.removePaintListener(this);
            this.canvas.dispose();
        }
    }

    /**
     * @return the maxElements
     */
    public int getMaxElements() {
        return this.maxElements;
    }

    /**
     * @param maxElements
     *            the maxElements to set
     */
    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    public void setBackground(final RGB color) {
        this.gc.setBackground(this.toColor(color));
    }

    public void setForeground(final RGB color) {
        this.gc.setForeground(this.toColor(color));
    }

    public void drawText(final String text, final Coordinate coordinate) {
        this.drawText(text, coordinate, false);
    }

    public void drawText(final String text, final Coordinate coordinate, final boolean transparent) {
        this.gc.drawText(text, (int) coordinate.x, (int) coordinate.y, transparent);
    }

    public void drawLine(final Coordinate start, final Coordinate end, final IColorSpace color) {
        final Color tmpColor = this.gc.getForeground();
        this.gc.setForeground(this.toColor(color));
        this.gc.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        this.gc.setForeground(tmpColor);
    }

    public void drawLine(final Coordinate start, final Coordinate control1, final Coordinate control2, final Coordinate end, final IColorSpace color) {
        final Path path = new Path(this.gc.getDevice());
        path.moveTo((float) start.x, (float) start.y);
        path.cubicTo((float) control1.x, (float) control1.y, (float) control2.x, (float) control2.y, (float) end.x, (float) end.y);
        this.drawPath(path, color);
    }

    public void drawLine(final Coordinate start, final Coordinate control, final Coordinate end, final IColorSpace color) {
        final Path path = new Path(this.gc.getDevice());
        path.moveTo((float) start.x, (float) start.y);
        path.quadTo((float) control.x, (float) control.y, (float) end.x, (float) end.y);
        this.drawPath(path, color);
    }

    public void drawLine(final Coordinate[] points, final IColorSpace color) {
        if (points.length > 0) {
            final Path path = new Path(this.gc.getDevice());
            path.moveTo((float) points[0].x, (float) points[0].y);
            for (int i = 1; i < points.length; i++) {
                final Coordinate point = points[i];
                path.lineTo((float) point.x, (float) point.y);
            }
            this.drawPath(path, color);
        }
    }

    public void drawPath(final Path path, final IColorSpace color) {
        final Color tmpColor = this.gc.getForeground();
        this.gc.setForeground(this.toColor(color));
        this.gc.drawPath(path);
        this.gc.setForeground(tmpColor);
    }

    public void drawRectangle(final Coordinate corner, final int width, final int height, final IColorSpace color) {
        final Color tmpColor = this.gc.getForeground();
        this.gc.setForeground(this.toColor(color));
        this.gc.drawRectangle((int) corner.x, (int) corner.y, width, height);
        this.gc.setForeground(tmpColor);
    }

    public void drawCircle(final Coordinate center, final int radius, final int start, final int end, final IColorSpace color) {
        final Color tmpColor = this.gc.getForeground();
        this.gc.setForeground(this.toColor(color));
        this.gc.drawArc((int) (center.x - radius), (int) (center.y - radius), 2 * radius, 2 * radius, start, end);
        this.gc.setForeground(tmpColor);
    }

    public void fillCircle(final Coordinate center, final int radius, final int start, final int end, final IColorSpace color) {
        final Color tmpColor = this.gc.getBackground();
        this.gc.setBackground(this.toColor(color));
        this.gc.fillArc((int) (center.x - radius), (int) (center.y - radius), 2 * radius, 2 * radius, start, end);
        this.gc.setBackground(tmpColor);
    }

    public void fillPath(final Path path, final IColorSpace color) {
        final Color tmpColor = this.gc.getBackground();
        this.gc.setBackground(this.toColor(color));
        this.gc.fillPath(path);
        this.gc.setBackground(tmpColor);
    }

    public void fill(final IColorSpace color) {
        final Color tmpColor = this.gc.getBackground();
        this.gc.setBackground(this.toColor(color));
        final Rectangle bounds = this.gc.getDevice().getBounds();
        this.gc.fillRectangle(bounds);
        this.gc.setBackground(tmpColor);
    }

    public void setAlpha(final int alpha) {
        this.gc.setAlpha(alpha);
    }

    public Color toColor(final IColorSpace value) {
        final RGB rgb = value.toRGB();
        return new Color(this.gc.getDevice(), new org.eclipse.swt.graphics.RGB((int) (rgb.R), (int) (rgb.G), (int) (rgb.B)));
    }

    public Coordinate textExtent(final String text) {
        final Point extent = this.gc.textExtent(text);
        return new Coordinate(extent.x, extent.y);
    }

    public GC getGC() {
        return this.gc;
    }
}
