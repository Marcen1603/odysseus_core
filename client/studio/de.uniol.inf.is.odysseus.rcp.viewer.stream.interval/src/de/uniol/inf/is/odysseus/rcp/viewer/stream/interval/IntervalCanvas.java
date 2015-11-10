package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class IntervalCanvas implements PaintListener, MouseMoveListener {

	private static final int INTERVAL_LINE_MAX_HEIGHT_PIXELS = 100;
	private static final int INTERVAL_LINE_MIN_HEIGHT_PIXELS = 15;

	private static final int VERTICAL_MARGIN_PIXELS = 20;
	private static final int ELEMENT_DATA_HORIZONTAL_MARGIN_PIXELS = 10;

	private final Canvas intervalLineCanvas;
	private final Canvas elementDataCanvas;
	private final IntervalCanvasDataContainer container;

	private final Color lineColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color selectionColor = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);

	private int mouseY;

	public IntervalCanvas(Composite parent) {
		Preconditions.checkNotNull(parent, "parent must not be null!");

		Composite parentComposite = new Composite(parent, SWT.NONE);
		FormLayout form = new FormLayout();
		parentComposite.setLayout(form);

		elementDataCanvas = new Canvas(parentComposite, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		elementDataCanvas.addPaintListener(this);
		elementDataCanvas.addMouseMoveListener(this);

		Sash sash = new Sash(parentComposite, SWT.BORDER | SWT.VERTICAL);

		intervalLineCanvas = new Canvas(parentComposite, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		intervalLineCanvas.addPaintListener(this);
		intervalLineCanvas.addMouseMoveListener(this);

		FormData elementDataCanvasFormData = new FormData();
		elementDataCanvasFormData.left = new FormAttachment(0, 0);
		elementDataCanvasFormData.right = new FormAttachment(sash, 0);
		elementDataCanvasFormData.top = new FormAttachment(0, 0);
		elementDataCanvasFormData.bottom = new FormAttachment(100, 0);
		elementDataCanvas.setLayoutData(elementDataCanvasFormData);

		int limit = 20;
		int percent = 25;
		FormData sashData = new FormData();
		sashData.left = new FormAttachment(percent, 0);
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Rectangle sashRect = sash.getBounds();
				Rectangle shellRect = parentComposite.getClientArea();
				int right = shellRect.width - sashRect.width - limit;
				e.x = Math.max(Math.min(e.x, right), limit);
				if (e.x != sashRect.x) {
					sashData.left = new FormAttachment(0, e.x);
					parentComposite.layout();
				}
			}
		});

		FormData intervalLineCanvasFormData = new FormData();
		intervalLineCanvasFormData.left = new FormAttachment(sash, 0);
		intervalLineCanvasFormData.right = new FormAttachment(100, 0);
		intervalLineCanvasFormData.top = new FormAttachment(0, 0);
		intervalLineCanvasFormData.bottom = new FormAttachment(100, 0);
		intervalLineCanvas.setLayoutData(intervalLineCanvasFormData);

		container = new IntervalCanvasDataContainer();
	}

	public void dispose() {
		intervalLineCanvas.dispose();
		container.clear();
	}

	public Canvas getIntervalLineCanvas() {
		return intervalLineCanvas;
	}

	public void addElement(IStreamObject<? extends ITimeInterval> streamElement) {
		container.add(streamElement);
		redrawAllCanvasAsync();
	}

	private void redrawAllCanvasAsync() {
		redrawCanvasAsync(intervalLineCanvas);
		redrawCanvasAsync(elementDataCanvas);
	}

	private static void redrawCanvasAsync(Canvas canvas) {
		if (canvas.isDisposed()) {
			return;
		}

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.redraw();
				}
			}
		});
	}

	@Override
	public void paintControl(PaintEvent e) {
		List<IStreamObject<? extends ITimeInterval>> elements = container.getElements();
		if (elements.isEmpty()) {
			// nothing to draw
			return;
		}

		if (e.widget == intervalLineCanvas) {
			drawIntervalLines(e.gc, elements);
		} else {
			drawElements(e.gc, elements);
		}
	}

	private void drawIntervalLines(GC gc, List<IStreamObject<? extends ITimeInterval>> elements) {
		int intervalLineHeight = Math.min(Math.max(intervalLineCanvas.getBounds().height / elements.size(), INTERVAL_LINE_MAX_HEIGHT_PIXELS), INTERVAL_LINE_MIN_HEIGHT_PIXELS);
		int halfIntervalLineHeight = intervalLineHeight / 2;
		int drawAreaWidth = intervalLineCanvas.getBounds().width - (2 * VERTICAL_MARGIN_PIXELS);

		long minimumTime = elements.get(0).getMetadata().getStart().getMainPoint();
		long maximumTime = elements.get(elements.size() - 1).getMetadata().getEnd().getMainPoint();
		long timeDiff = maximumTime - minimumTime;

		// space to the border of the canvas
		int drawY = VERTICAL_MARGIN_PIXELS;

		gc.setForeground(lineColor);
		for (IStreamObject<? extends ITimeInterval> element : elements) {
			// mouse highlights line?
			if (mouseY >= drawY - halfIntervalLineHeight && mouseY <= drawY + halfIntervalLineHeight) {
				Color oldColor = gc.getBackground();
				gc.setBackground(selectionColor);

				gc.fillRectangle(0, drawY - halfIntervalLineHeight, intervalLineCanvas.getBounds().width, intervalLineHeight);

				gc.setBackground(oldColor);
			}

			long start = element.getMetadata().getStart().getMainPoint();
			long end = element.getMetadata().getEnd().getMainPoint();

			int startPixels = VERTICAL_MARGIN_PIXELS + (int) (((double) (start - minimumTime) / (double) timeDiff) * drawAreaWidth);
			int endPixels = VERTICAL_MARGIN_PIXELS + (int) (((double) (end - minimumTime) / (double) timeDiff) * drawAreaWidth);

			gc.drawLine(startPixels, drawY, endPixels, drawY);
			gc.drawLine(startPixels, drawY - halfIntervalLineHeight, startPixels, drawY + halfIntervalLineHeight);
			gc.drawLine(endPixels, drawY - halfIntervalLineHeight, endPixels, drawY + halfIntervalLineHeight);

			drawY += VERTICAL_MARGIN_PIXELS;
		}
	}

	private void drawElements(GC gc, List<IStreamObject<? extends ITimeInterval>> elements) {
		int intervalLineHeight = Math.min(Math.max(intervalLineCanvas.getBounds().height / elements.size(), INTERVAL_LINE_MAX_HEIGHT_PIXELS), INTERVAL_LINE_MIN_HEIGHT_PIXELS);
		int halfIntervalLineHeight = intervalLineHeight / 2;
		int drawY = VERTICAL_MARGIN_PIXELS;

		gc.setForeground(lineColor);

		Color oldColor = gc.getBackground();
		for (IStreamObject<? extends ITimeInterval> element : elements) {
			if (mouseY >= drawY - halfIntervalLineHeight && mouseY <= drawY + halfIntervalLineHeight) {
				// mouse highlights line
				gc.setBackground(selectionColor);
				gc.fillRectangle(0, drawY - halfIntervalLineHeight, elementDataCanvas.getBounds().width, intervalLineHeight);
			}

			gc.drawText(element.toString(), ELEMENT_DATA_HORIZONTAL_MARGIN_PIXELS, drawY - intervalLineHeight / 2);
			gc.setBackground(oldColor);

			drawY += VERTICAL_MARGIN_PIXELS;
		}
	}

	@Override
	public void mouseMove(MouseEvent e) {
		mouseY = e.y;

		redrawAllCanvasAsync();
	}
}
