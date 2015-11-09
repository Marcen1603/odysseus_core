package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Preconditions;

public class IntervalCanvas {

	private final Canvas canvas;
	
	public IntervalCanvas( Composite parent ) {
		Preconditions.checkNotNull(parent, "parent must not be null!");

		canvas = new Canvas(parent, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void dispose() {
		this.canvas.dispose();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}
