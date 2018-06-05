package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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

	private final Canvas intervalLineCanvas;
	private final Canvas elementDataCanvas;
	private final IntervalLines intervalLines;

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

		intervalLines = new IntervalLines();
	}

	public void dispose() {
		intervalLineCanvas.dispose();
		intervalLines.clear();
	}

	public Canvas getIntervalLineCanvas() {
		return intervalLineCanvas;
	}

	public void addElement(IStreamObject<? extends ITimeInterval> streamElement) {
		intervalLines.add(streamElement);
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
		if (e.widget == intervalLineCanvas) {
			intervalLines.drawLines(e.gc, intervalLineCanvas.getBounds().width, intervalLineCanvas.getBounds().height, mouseY);
		} else {
			intervalLines.drawElements(e.gc, elementDataCanvas.getBounds().width, elementDataCanvas.getBounds().height, mouseY);
		}
	}

	@Override
	public void mouseMove(MouseEvent e) {
		mouseY = e.y;

		redrawAllCanvasAsync();
	}
}
