package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class IntervalCanvas implements PaintListener {

	private static final int INTERVAL_LINE_MAX_HEIGHT_PIXELS = 100;
	private static final int INTERVAL_LINE_MIN_HEIGHT_PIXELS = 10;
	private static final int INTERVAL_LINE_MARGIN_PIXELS = 10;
	
	private final Canvas canvas;
	private final IntervalCanvasDataContainer container;
	
	public IntervalCanvas( Composite parent ) {
		Preconditions.checkNotNull(parent, "parent must not be null!");

		canvas = new Canvas(parent, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		canvas.addPaintListener(this);
		
		container = new IntervalCanvasDataContainer();
	}
	
	public void dispose() {
		canvas.dispose();
		container.clear();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	public void addElement(IStreamObject<? extends ITimeInterval> streamElement) {
		container.add(streamElement);
		redrawCanvasAsync();
	}

	private void redrawCanvasAsync() {
		if( canvas.isDisposed() ) {
			return;
		}

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if( !canvas.isDisposed() ) {
					canvas.redraw();
				}
			}
		});
	}

	@Override
	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
				
		List<IStreamObject<? extends ITimeInterval>> elements = container.getElements();
		if( elements.isEmpty() ) {
			// nothing to draw
			return; 
		}
		
		int intervalLineHeight = Math.min(Math.max(canvas.getBounds().height / elements.size(), INTERVAL_LINE_MAX_HEIGHT_PIXELS), INTERVAL_LINE_MIN_HEIGHT_PIXELS);
		int drawAreaWidth = canvas.getBounds().width - (2 * INTERVAL_LINE_MARGIN_PIXELS);
		
		long minimumTime = elements.get(0).getMetadata().getStart().getMainPoint();
		long maximumTime = elements.get(elements.size() - 1).getMetadata().getEnd().getMainPoint();
		long timeDiff = maximumTime - minimumTime;
		
		// space to the border of the canvas
		int drawY = INTERVAL_LINE_MARGIN_PIXELS;
		
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		for( IStreamObject<? extends ITimeInterval> element : elements ) {
			long start = element.getMetadata().getStart().getMainPoint();
			long end = element.getMetadata().getEnd().getMainPoint();
			
			int startPixels = INTERVAL_LINE_MARGIN_PIXELS + (int) (((double)( start - minimumTime ) / (double)timeDiff) * drawAreaWidth );
			int endPixels = INTERVAL_LINE_MARGIN_PIXELS + (int) (((double)( end - minimumTime ) / (double)timeDiff) * drawAreaWidth );
			
			gc.drawLine(startPixels, drawY, endPixels, drawY);
			gc.drawLine(startPixels, drawY - intervalLineHeight / 2, startPixels, drawY + intervalLineHeight / 2);
			gc.drawLine(endPixels, drawY - intervalLineHeight / 2, endPixels, drawY + intervalLineHeight / 2);
			
			drawY += INTERVAL_LINE_MARGIN_PIXELS;
		}
	}
}
