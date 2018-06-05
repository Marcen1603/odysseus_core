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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.IColorSpace;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @author Marco Grawunder
 * @version $Id: AbstractCanvasDashboardPart.java |
 *          AbstractCanvasDashboardPart.java | AbstractCanvasDashboardPart.java
 *          $
 * 
 */
public abstract class AbstractCanvasDashboardPart extends AbstractDashboardPart
		implements PaintListener, MouseListener, MouseMoveListener,
		MouseTrackListener, MouseWheelListener {
	private final static String MAX_ELEMENTS = "maxElements";
	private final static String REPAINT_DELAY = "repaintDelay";
	private final static String LEFT_OFFSET_X = "leftOffsetX";
	private final static String LEFT_OFFSET_Y = "leftOffsetY";

	private Cursor cursor = null;

	private Canvas canvas;
	private final Queue<IStreamObject<?>> queue = new ConcurrentLinkedQueue<>();
	private CanvasUpdater updater;
	protected GC gc;
	private int maxElements = 100;
	private long repaintDelay = 1000;
	private final static long minRepaintDelay = 100;
	protected Color backgroundColor;
	protected Point leftTop = new Point(0, 0);

	List<ChangeListener> listener = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(final Composite parent, final ToolBar toolbar) {
		parent.setLayout(new FillLayout());

		final Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout());

		this.canvas = new Canvas(composite, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		backgroundColor = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		this.canvas.setBackground(backgroundColor);
		this.canvas.addPaintListener(this);

		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addMouseTrackListener(this);
		canvas.addMouseWheelListener(this);

		parent.layout();
		restartUpdater();
	}

	protected void restartUpdater() {
		if (updater != null) {
			updater.terminate();
			try {
				updater.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.updater = new CanvasUpdater(this.canvas, repaintDelay);
		this.updater.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void streamElementReceived(final IPhysicalOperator operator,
			final IStreamObject<?> element, final int port) {
		this.queue.offer(element);
		while (this.queue.size() > this.getMaxElements()) {
			this.queue.poll();
		}
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
		try {
			Point s = getSize();
			Image bufferImage = new Image(Display.getCurrent(), s.x, s.y);
			this.gc = new GC(bufferImage);
			this.gc.setTextAntialias(SWT.ON);
			this.gc.setAdvanced(true);
			this.gc.setAntialias(SWT.ON);
			this.doPaint(e);
			this.gc.dispose();
			this.gc = null;
			e.gc.drawImage(bufferImage, leftTop.x, leftTop.y);
			// System.err.println(-1 * leftTop.x + " " + -1 * leftTop.y + " "
			// + (s.x - leftTop.x) + " " + (s.y - leftTop.y) + " " + 0
			// + " " + 0 + " " + (s.x - leftTop.x) + " "
			// + (s.y - leftTop.y));
			// e.gc.drawImage(bufferImage, -1 * leftTop.x, -1 * leftTop.y, s.x
			// - leftTop.x, s.y - leftTop.y, 0, 0, s.x, s.y);
			// TODO: clipping
			// int width = bufferImage.getBounds().width;
			// int height = bufferImage.getBounds().height;
			// e.gc.drawImage(bufferImage,0,0, s.x, s.y, leftTop.x, leftTop.y,
			// width,height);

			// e.gc.drawImage(bufferImage, leftTop.x, leftTop.y, bufferImage
			// .getBounds().width, bufferImage.getBounds().height,
			// leftTop.x, leftTop.y, new Double(
			// bufferImage.getBounds().width * zoom).intValue(),
			// new Double(bufferImage.getBounds().height * zoom)
			// .intValue());
			bufferImage.dispose();
		} catch (Exception e2) {
			throw e2;
		}
	}

	protected Point getSize() {
		return this.getCanvas().getSize();
	}

	public abstract void doPaint(final PaintEvent e);

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
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onLoad(Map<String, String> saved) {
		maxElements = Integer.valueOf(saved.get(MAX_ELEMENTS) != null ? saved
				.get(MAX_ELEMENTS) : "1000");
		setRepaintDelay(Long.valueOf(saved.get(REPAINT_DELAY) != null ? saved
				.get(REPAINT_DELAY) : "1000"));
		int x = Integer.valueOf(saved.get(LEFT_OFFSET_X) != null ? saved
				.get(LEFT_OFFSET_X) : "0");
		int y = Integer.valueOf(saved.get(LEFT_OFFSET_Y) != null ? saved
				.get(LEFT_OFFSET_Y) : "0");
		leftTop = new Point(x, y);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSaveMap = Maps.newHashMap();
		toSaveMap.put(MAX_ELEMENTS, String.valueOf(maxElements));
		toSaveMap.put(REPAINT_DELAY, String.valueOf(repaintDelay));
		toSaveMap.put(LEFT_OFFSET_X, String.valueOf(leftTop.x));
		toSaveMap.put(LEFT_OFFSET_Y, String.valueOf(leftTop.y));
		return toSaveMap;
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
	public void setMaxElements(final int maxElements) {
		this.maxElements = maxElements;
	}

	public void setBackground(final RGB color) {
		this.gc.setBackground(this.toColor(color));
	}

	public void setForeground(final RGB color) {
		this.gc.setForeground(this.toColor(color));
	}

	public void drawPointer(final int x, final int y, final int radius,
			final double angle, final IColorSpace color) {
		final int scale = (int) ((1.0 / 5.0) * radius);

		final Path path1 = new Path(this.getGC().getDevice());
		path1.moveTo((radius), 0);
		path1.lineTo(0, scale);
		path1.addArc(-1 * scale, -1 * scale, 2 * scale, 2 * scale, -90, -90);
		path1.close();

		final Path path2 = new Path(this.getGC().getDevice());
		path2.moveTo((radius), 0);
		path2.lineTo(0, -scale);
		path2.addArc(-1 * scale, -1 * scale, 2 * scale, 2 * scale, 90, 90);
		path2.close();

		final Transform transform = new Transform(this.getGC().getDevice());
		transform.translate(x, y);
		transform.rotate((float) (angle - 90.0));
		final Color tmpColor = this.gc.getBackground();
		this.getGC().setTransform(transform);
		this.gc.setBackground(this.toColor(color));
		this.gc.fillPath(path1);
		this.gc.setBackground(this.toColor(color.toRGB().getAnalogous()[0]));
		this.gc.fillPath(path2);
		this.gc.setBackground(this.toColor(color.toRGB().getComplement()));
		final RGB[] complements = color.toRGB().getSplitComplements();

		final Pattern pattern = new Pattern(this.gc.getDevice(), 0, 0,
				(2 * scale) - 4, (2 * scale) - 4, this.toColor(complements[0]),
				this.toColor(complements[1]));
		this.gc.setBackgroundPattern(pattern);
		this.gc.fillArc((-1 * scale) + 2, (-1 * scale) + 2, (2 * scale) - 4,
				(2 * scale) - 4, 0, 360);
		this.gc.setBackgroundPattern(null);
		pattern.dispose();
		path1.dispose();
		path2.dispose();
		this.gc.setBackground(tmpColor);
		this.getGC().setTransform(null);
	}

	public void drawXAxis(final int x, final int y, final int width,
			final double from, final double to, final IColorSpace color) {
		final Color tmpForegroundColor = this.gc.getForeground();
		final Color tmpBackgroundColor = this.gc.getBackground();
		this.gc.setForeground(this.toColor(color));
		this.gc.setBackground(this.toColor(color));

		final int ticksize = this.getXTickSize(from, to, width);
		final double scale = width / Math.abs(to - from);
		this.gc.drawLine(x, (y), x + width, (y));
		for (int i = 0; (i / scale) < (width - 5); i += ticksize) {
			final double value = from + i;
			final int tickX = (int) (x + ((value - from) / scale));
			final Coordinate extent = this.textExtent(value + "");
			this.gc.drawLine(tickX, y - 5, tickX, y + 5);
			this.gc.drawText(value + "", (int) (tickX - (extent.x / 2)), y + 6,
					true);
		}
		final Path arrow = new Path(this.getGC().getDevice());
		arrow.moveTo(x + width, y);
		arrow.lineTo((x + width) - 5, y + 3);
		arrow.lineTo((x + width) - 5, y - 3);
		arrow.close();
		this.gc.fillPath(arrow);
		arrow.dispose();

		this.gc.setForeground(tmpForegroundColor);
		this.gc.setBackground(tmpBackgroundColor);
	}

	public void drawYAxis(final int x, final int y, final int height,
			final double from, final double to, final IColorSpace color) {
		final Color tmpForegroundColor = this.gc.getForeground();
		final Color tmpBackgroundColor = this.gc.getBackground();
		this.gc.setForeground(this.toColor(color));
		this.gc.setBackground(this.toColor(color));

		final int ticksize = this.getYTickSize(from, to, height);
		final double scale = height / Math.abs(to - from);
		this.gc.drawLine((x), y, (x), y + height);
		for (int i = 0; (i / scale) < (height - 5); i += ticksize) {
			final double value = from + i;
			final int tickY = (int) ((y + height) - ((value - from) / scale));
			final Coordinate extent = this.textExtent(value + "");
			this.gc.drawLine(x - 5, tickY, x + 5, tickY);
			this.gc.drawText(value + "", (int) (x - extent.x - 6),
					(int) (tickY - (extent.y / 2)), true);
		}
		final Path arrow = new Path(this.getGC().getDevice());
		arrow.moveTo(x, y);
		arrow.lineTo(x - 3, y + 5);
		arrow.lineTo(x + 3, y + 5);
		arrow.close();
		this.gc.fillPath(arrow);
		arrow.dispose();

		this.gc.setForeground(tmpForegroundColor);
		this.gc.setBackground(tmpBackgroundColor);
	}

	public void drawGrid(final int x, final int y, final int width,
			final int height, final double fromX, final double toX,
			final double fromY, final double toY, final IColorSpace color) {
		final Color tmpForegroundColor = this.gc.getForeground();
		final Color tmpBackgroundColor = this.gc.getBackground();
		this.gc.setForeground(this.toColor(color));
		this.gc.setBackground(this.toColor(color));

		final int ticksizeX = this.getXTickSize(fromX, toX, width);
		final int ticksizeY = this.getYTickSize(fromY, toY, height);
		final double scaleX = width / Math.abs(toX - fromX);
		final double scaleY = height / Math.abs(toY - fromY);
		this.gc.drawLine((x), y + height, (x + width), y + height);
		this.gc.drawLine((x), y, (x), y + height);
		for (int i = 0; (i / scaleX) < (width - 5); i += ticksizeX) {
			final double value = fromX + i;
			final int tickX = (int) (x + ((value - fromX) / scaleX));
			final Coordinate extent = this.textExtent(value + "");
			this.gc.drawLine(tickX, (y + height) - 5, tickX, (y + height) + 5);
			this.gc.drawText(value + "", (int) (tickX - (extent.x / 2)),
					(y + height) + 6, true);
		}
		for (int i = 0; (i / scaleY) < (height - 5); i += ticksizeY) {
			final double value = fromY + i;
			final int tickY = (int) ((y + height) - ((value - fromY) / scaleY));
			final Coordinate extent = this.textExtent(value + "");
			this.gc.drawLine(x - 5, tickY, x + 5, tickY);
			this.gc.drawText(value + "", (int) (x - extent.x - 6),
					(int) (tickY - (extent.y / 2)), true);
		}
		final Path arrowX = new Path(this.getGC().getDevice());
		arrowX.moveTo(x + width, y + height);
		arrowX.lineTo((x + width) - 5, y + height + 3);
		arrowX.lineTo((x + width) - 5, (y + height) - 3);
		arrowX.close();
		this.gc.fillPath(arrowX);
		arrowX.dispose();
		final Path arrowY = new Path(this.getGC().getDevice());
		arrowY.moveTo(x, y);
		arrowY.lineTo(x + 3, y + 5);
		arrowY.lineTo(x - 3, y + 5);
		arrowY.close();
		this.gc.fillPath(arrowY);
		arrowY.dispose();

		this.gc.setForeground(tmpForegroundColor);
		this.gc.setBackground(tmpBackgroundColor);
	}

	public void drawText(final String text, final Coordinate coordinate) {
		this.drawText(text, coordinate, false);
	}

	public void drawText(final String text, final Coordinate coordinate,
			final boolean transparent) {
		this.gc.drawText(text, (int) coordinate.x, (int) coordinate.y,
				transparent);
	}

	public void drawLine(final Coordinate start, final Coordinate end,
			final IColorSpace color) {
		final Color tmpColor = this.gc.getForeground();
		this.gc.setForeground(this.toColor(color));
		this.gc.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
		this.gc.setForeground(tmpColor);
	}

	public void drawLine(final Coordinate start, final Coordinate control1,
			final Coordinate control2, final Coordinate end,
			final IColorSpace color) {
		final Path path = new Path(this.gc.getDevice());
		path.moveTo((float) start.x, (float) start.y);
		path.cubicTo((float) control1.x, (float) control1.y,
				(float) control2.x, (float) control2.y, (float) end.x,
				(float) end.y);
		this.drawPath(path, color);
		path.dispose();
	}

	public void drawLine(final Coordinate start, final Coordinate control,
			final Coordinate end, final IColorSpace color) {
		final Path path = new Path(this.gc.getDevice());
		path.moveTo((float) start.x, (float) start.y);
		path.quadTo((float) control.x, (float) control.y, (float) end.x,
				(float) end.y);
		this.drawPath(path, color);
		path.dispose();
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
			path.dispose();
		}
	}

	public void drawRectangle(final Coordinate corner, final int width,
			final int height, final IColorSpace color) {
		final Color tmpColor = this.gc.getForeground();
		this.gc.setForeground(this.toColor(color));
		this.gc.drawRectangle((int) corner.x, (int) corner.y, width, height);
		this.gc.setForeground(tmpColor);
	}

	public void drawRoundRectangle(final Coordinate corner, final int width,
			final int height, final int arcWidth, final int arcHeight,
			final IColorSpace color) {
		final Color tmpColor = this.gc.getForeground();
		this.gc.setForeground(this.toColor(color));
		this.gc.drawRoundRectangle((int) corner.x, (int) corner.y, width,
				height, arcWidth, arcHeight);
		this.gc.setForeground(tmpColor);
	}

	public void drawArc(final Coordinate center, final int radius,
			final int start, final int end, final IColorSpace color) {
		final Color tmpColor = this.gc.getForeground();
		this.gc.setForeground(this.toColor(color));
		this.gc.drawArc((int) (center.x - radius), (int) (center.y - radius),
				2 * radius, 2 * radius, start, end);
		this.gc.setForeground(tmpColor);
	}

	public void drawPath(final Path path, final IColorSpace color) {
		final Color tmpColor = this.gc.getForeground();
		this.gc.setForeground(this.toColor(color));
		this.gc.drawPath(path);
		this.gc.setForeground(tmpColor);
	}

	public void fillRectangle(final Coordinate corner, final int width,
			final int height, final IColorSpace color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(this.toColor(color));
		this.gc.fillRectangle((int) corner.x, (int) corner.y, width, height);
		this.gc.setBackground(tmpColor);
	}

	public void fillRectangle(final int x, final int y, final int width,
			final int height, final IColorSpace color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(this.toColor(color));
		this.gc.fillRectangle(x, y, width, height);
		this.gc.setBackground(tmpColor);
	}

	public void fillRectangle(final int x, final int y, final int width,
			final int height, final Color color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(color);
		this.gc.fillRectangle(x, y, width, height);
		this.gc.setBackground(tmpColor);
	}

	public void fillRoundRectangle(final Coordinate corner, final int width,
			final int height, final int arcWidth, final int arcHeight,
			final IColorSpace color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(this.toColor(color));
		this.gc.fillRoundRectangle((int) corner.x, (int) corner.y, width,
				height, arcWidth, arcHeight);
		this.gc.setBackground(tmpColor);
	}

	public void fillArc(final Coordinate center, final int radius,
			final int start, final int end, final IColorSpace color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(this.toColor(color));
		this.gc.fillArc((int) (center.x - radius), (int) (center.y - radius),
				2 * radius, 2 * radius, start, end);
		this.gc.setBackground(tmpColor);
	}

	public void fillPath(final Path path, final IColorSpace color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(this.toColor(color));
		this.gc.fillPath(path);
		this.gc.setBackground(tmpColor);
	}

	public void fillArc(final Coordinate center, final int radius,
			final int start, final int end, final IColorSpace color1,
			final IColorSpace color2) {
		final Color tmpColor = this.gc.getBackground();
		final Pattern pattern = new Pattern(this.gc.getDevice(),
				(int) (center.x - radius), (int) (center.y - radius),
				2 * radius, 2 * radius, this.toColor(color1),
				this.toColor(color2));
		this.gc.setBackgroundPattern(pattern);
		this.gc.fillArc((int) (center.x - radius), (int) (center.y - radius),
				2 * radius, 2 * radius, start, end);
		pattern.dispose();
		this.gc.setBackgroundPattern(null);
		this.gc.setBackground(tmpColor);
	}

	public void fillRectangle(final Coordinate corner, final int width,
			final int height, final IColorSpace color1, final IColorSpace color2) {
		final Color tmpColor = this.gc.getBackground();
		final Pattern pattern = new Pattern(this.gc.getDevice(),
				(int) corner.x, (int) corner.y, width, height,
				this.toColor(color1), this.toColor(color2));
		this.gc.setBackgroundPattern(pattern);
		this.gc.fillRectangle((int) corner.x, (int) corner.y, width, height);
		pattern.dispose();
		this.gc.setBackgroundPattern(null);
		this.gc.setBackground(tmpColor);
	}

	public void fillRoundRectangle(final Coordinate corner, final int width,
			final int height, final int arcWidth, final int arcHeight,
			final IColorSpace color1, final IColorSpace color2) {
		final Color tmpColor = this.gc.getBackground();
		final Pattern pattern = new Pattern(this.gc.getDevice(),
				(int) corner.x, (int) corner.y, width, height,
				this.toColor(color1), this.toColor(color2));
		this.gc.setBackgroundPattern(pattern);
		this.gc.fillRoundRectangle((int) corner.x, (int) corner.y, width,
				height, arcWidth, arcHeight);
		pattern.dispose();
		this.gc.setBackgroundPattern(null);
		this.gc.setBackground(tmpColor);
	}

	public void fillRectangle(final Coordinate corner, final int width,
			final int height, final IColorSpace color1,
			final IColorSpace color2, final boolean vertical) {
		final Color tmpBackgroundColor = this.gc.getBackground();
		final Color tmpForegroundColor = this.gc.getForeground();
		this.gc.setForeground(this.toColor(color1));
		this.gc.setBackground(this.toColor(color2));
		this.gc.fillGradientRectangle((int) corner.x, (int) corner.y, width,
				height, vertical);
		this.gc.setBackground(tmpBackgroundColor);
		this.gc.setForeground(tmpForegroundColor);
	}

	public void fillRectangle(final Coordinate corner, final int width,
			final int height, final int[] percents, final IColorSpace[] colors,
			final boolean vertical) {
		final Color tmpBackgroundColor = this.gc.getBackground();
		if ((colors == null) || (colors.length == 1)
				|| (percents == null || percents.length != colors.length - 1)) {
			if ((colors != null) && (colors[0] != null)) {
				this.gc.setBackground(this.toColor(colors[0]));
			}
			this.gc.fillRectangle((int) corner.x, (int) corner.y, width, height);
		} else {
			final Color tmpForegroundColor = this.gc.getForeground();
			Color lastColor = this.toColor(colors[0]);
			if (lastColor == null) {
				lastColor = tmpBackgroundColor;
			}
			int pos = 0;
			for (int i = 0; i < percents.length; ++i) {
				this.gc.setForeground(lastColor);
				lastColor = this.toColor(colors[i + 1]);
				if (lastColor == null) {
					lastColor = tmpBackgroundColor;
				}
				this.gc.setBackground(lastColor);
				if (vertical) {
					final int gradientHeight = (((percents[i]) * height) / 100)
							- pos;
					this.gc.fillGradientRectangle((int) corner.x,
							(int) (corner.y + pos), width, gradientHeight,
							vertical);
					pos += gradientHeight;
				} else {
					final int gradientWidth = (((percents[i]) * width) / 100)
							- pos;
					this.gc.fillGradientRectangle((int) (corner.x + pos),
							(int) corner.y, gradientWidth, height, vertical);
					pos += gradientWidth;
				}
			}
			if (vertical && (pos < height)) {
				this.gc.setBackground(tmpBackgroundColor);
				this.gc.fillRectangle((int) corner.x, (int) (corner.y + pos),
						width, height - pos);
			}
			if (!vertical && (pos < width)) {
				this.gc.setBackground(tmpBackgroundColor);
				this.gc.fillRectangle((int) (corner.x + pos), (int) corner.y,
						width - pos, height);
			}
			this.gc.setForeground(tmpForegroundColor);
		}
		this.gc.setBackground(tmpBackgroundColor);
	}

	public void fillRectangle(final Coordinate corner, final int width,
			final int height, final int[] percents, final IColorSpace[] colors) {
		final Color tmpBackgroundColor = this.gc.getBackground();
		if (colors.length == 1) {
			if (colors[0] != null) {
				this.gc.setBackground(this.toColor(colors[0]));
			}
			this.gc.fillRectangle((int) corner.x, (int) corner.y, width, height);
		} else {
			Color lastColor = this.toColor(colors[0]);
			if (lastColor == null) {
				lastColor = tmpBackgroundColor;
			}
			double posX = 0.0;
			double posY = 0.0;

			double diagonal = Math.sqrt(Math.pow(width, 2)
					+ Math.pow(height, 2));
			double angle = Math.acos(width / diagonal);
			for (int i = 0; i < percents.length; ++i) {
				Color color = lastColor;
				lastColor = this.toColor(colors[i + 1]);
				if (lastColor == null) {
					lastColor = tmpBackgroundColor;
				}
				double d = percents[i] * diagonal / 100;
				double cathetusX = Math.abs(d * Math.cos(angle));
				double cathetusY = Math
						.abs(d * Math.cos(Math.PI / 2.0 - angle));

				Path path = new Path(this.gc.getDevice());
				double x1 = posX;
				double y1 = 0.0;
				double x2 = (posX + cathetusX);
				double y2 = 0.0;
				if (x1 > width) {
					y1 = (y1 + (x1 - width) * cathetusY / cathetusX);
					x1 = width;
				}
				if (x2 > width) {
					y2 = (y2 + (x2 - width) * cathetusY / cathetusX);
					x2 = width;
				}

				double x3 = 0;
				double y3 = posY;
				double x4 = 0;
				double y4 = (posY + cathetusY);
				if (y3 > height) {
					x3 = (x3 + (y3 - height) * cathetusX / cathetusY);
					y3 = height;
				}
				if (y4 > height) {
					x4 = (x4 + (y4 - height) * cathetusX / cathetusY);
					y4 = height;
				}
				path.moveTo((float) (corner.x + x1), (float) (corner.y + y1));
				if (y2 > 0) {
					path.lineTo((float) (corner.x + width), (float) (corner.y));
				}
				path.lineTo((float) (corner.x + x2 + 0.5), (float) (corner.y
						+ y2 + 0.5));
				path.lineTo((float) (corner.x + x4 + 0.5), (float) (corner.y
						+ y4 + 0.5));
				if (x4 > 0) {
					path.lineTo((float) (corner.x), (float) (corner.y + height));
				}
				path.lineTo((float) (corner.x + x3), (float) (corner.y + y3));
				path.close();
				final float[] bounds = new float[4];
				path.getBounds(bounds);
				int patternSize = (int) Math.sqrt(2 * Math.pow(d, 2));
				final Pattern pattern = new Pattern(this.gc.getDevice(), 0, 0,
						patternSize, patternSize, color, lastColor);
				this.gc.setBackgroundPattern(pattern);
				this.gc.fillPath(path);
				pattern.dispose();
				path.dispose();

				posX += cathetusX;
				posY += cathetusY;
			}
			this.gc.setBackgroundPattern(null);
		}
		this.gc.setBackground(tmpBackgroundColor);
	}

	public void fillPath(final Path path, final IColorSpace color1,
			final IColorSpace color2) {
		final Color tmpColor = this.gc.getBackground();
		final float[] bounds = new float[4];
		path.getBounds(bounds);
		final Pattern pattern = new Pattern(this.gc.getDevice(), bounds[0],
				bounds[1], bounds[2], bounds[3], this.toColor(color1),
				this.toColor(color2));
		this.gc.setBackgroundPattern(pattern);
		this.gc.drawPath(path);
		pattern.dispose();
		this.gc.setBackgroundPattern(null);
		this.gc.setBackground(tmpColor);
	}

	public void fill(final IColorSpace color) {
		final Color tmpColor = this.gc.getBackground();
		this.gc.setBackground(this.toColor(color));
		final Rectangle bounds = this.gc.getDevice().getBounds();
		this.gc.fillRectangle(bounds);
		this.gc.setBackground(tmpColor);
	}

	public Rectangle getClipping() {
		return this.gc.getClipping();
	}

	public void setAlpha(final int alpha) {
		this.gc.setAlpha(alpha);
	}

	public Color toColor(final IColorSpace value) {
		if (value == null) {
			return this.gc.getDevice().getSystemColor(SWT.COLOR_CYAN);
		}
		final RGB rgb = value.toRGB();
		return new Color(this.gc.getDevice(), new org.eclipse.swt.graphics.RGB(
				(int) (rgb.R), (int) (rgb.G), (int) (rgb.B)));
	}

	public Coordinate textExtent(final String text) {
		final Point extent = this.gc.textExtent(text);
		return new Coordinate(extent.x, extent.y);
	}

	public int getXTickSize(final double from, final double to, final int width) {
		final Coordinate extentTo = this.textExtent(to + " ");
		final Coordinate extentFrom = this.textExtent(from + " ");
		int ticksize = (int) (Math.abs(to - from) / ((width / (Math.max(
				extentTo.x, extentFrom.x)))));
		final int digits = ticksize == 0 ? 1 : (1 + (int) Math.floor(Math
				.log10(Math.abs(ticksize))));
		if (ticksize > (Math.pow(10, digits) / 2)) {
			ticksize = (int) Math.pow(10, digits);
		} else if (ticksize > (Math.pow(10, digits) / 5)) {
			ticksize = (int) (Math.pow(10, digits) / 2);
		} else {
			ticksize = (int) (Math.pow(10, digits) / 5);
		}
		return ticksize;
	}

	public int getYTickSize(final double from, final double to, final int height) {
		final Coordinate extentTo = this.textExtent(to + " ");
		final Coordinate extentFrom = this.textExtent(from + " ");
		int ticksize = (int) (Math.abs(to - from) / ((height / (Math.max(
				extentTo.y, extentFrom.y)))));
		final int digits = ticksize == 0 ? 1 : (1 + (int) Math.floor(Math
				.log10(Math.abs(ticksize))));
		if (ticksize > (Math.pow(10, digits) / 2)) {
			ticksize = (int) Math.pow(10, digits);
		} else if (ticksize > (Math.pow(10, digits) / 5)) {
			ticksize = (int) (Math.pow(10, digits) / 2);
		} else {
			ticksize = (int) (Math.pow(10, digits) / 5);
		}
		return ticksize;
	}

	public GC getGC() {
		return this.gc;
	}

	public int getFontSize(final String text, final String fontName,
			final int width, final int height) {
		int fontSize = 8;
		Font font = new Font(this.getGC().getDevice(), fontName, fontSize,
				SWT.NORMAL);
		this.getGC().setFont(font);
		Coordinate pt = this.textExtent(text);
		double h = pt.y;
		double w = pt.x;
		while ((h < height) && (w < width)) {
			font.dispose();
			font = new Font(this.getGC().getDevice(), fontName, fontSize,
					SWT.NORMAL);
			this.getGC().setFont(font);
			pt = this.textExtent(text);
			h = pt.y;
			w = pt.x;
			fontSize++;
		}
		return fontSize;
	}

	protected void setLeftTop(int x, int y) {
		this.leftTop = new Point(x, y);
		fireChanged();
	}

	public int getOffsetX() {
		return leftTop.x;
	}

	public void setOffsetX(int x) {
		leftTop.x = x;
		dirty();
		fireChanged();
	}

	public int getOffsetY() {
		return leftTop.y;
	}

	public void setOffsetY(int y) {
		leftTop.y = y;
		dirty();
		fireChanged();
	}

	protected void dirty() {
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
	}

	@Override
	public void mouseMove(MouseEvent e) {
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
	}

	@Override
	public void mouseEnter(MouseEvent e) {
	}

	@Override
	public void mouseExit(MouseEvent e) {
	}

	@Override
	public void mouseHover(MouseEvent e) {
	}

	protected void setMoveCursor() {
		if (cursor != null) {
			cursor.dispose();
		}
		cursor = new Cursor(this.getCanvas().getDisplay(), SWT.CURSOR_HAND);
		this.getCanvas().getParent().setCursor(cursor);
	}

	protected void setDefaultCursor() {
		if (cursor != null) {
			cursor.dispose();
		}
		cursor = new Cursor(this.getCanvas().getDisplay(), SWT.CURSOR_ARROW);
		this.getCanvas().getParent().setCursor(cursor);
	}

	public void setRepaintDelay(long repaintDelay) {
		this.repaintDelay = Math.max(minRepaintDelay, repaintDelay);
		restartUpdater();
	}

	public long getRepaintDelay() {
		return repaintDelay;
	}

	public void addChangeListner(ChangeListener l) {
		this.listener.add(l);
	}

	public void removeChangeListener(ChangeListener l) {
		this.listener.remove(l);
	}

	protected void fireChanged() {
		for (ChangeListener l : listener) {
			l.stateChanged(null);
		}
	}

}
