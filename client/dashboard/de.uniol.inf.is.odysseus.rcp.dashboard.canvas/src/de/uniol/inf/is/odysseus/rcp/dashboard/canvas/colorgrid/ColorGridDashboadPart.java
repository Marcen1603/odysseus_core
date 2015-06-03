package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorgrid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.CIELCH;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

public class ColorGridDashboadPart extends AbstractCanvasDashboardPart {

	private int width = 600;
	private int height = 600;
	private int xpos = 0;
	private int ypos = 1;
	private int boxWidth = 2;
	private int boxHeight = 2;
	private int value_pos = 2;
	///private long maxDuration = 1000*60*60*24*14;
	private long maxDuration = -1;
	private long maxTime;
	private RGB color = new RGB(0, 255, 0);

	@SuppressWarnings("unchecked")
	private Tuple<? extends ITimeInterval>[][] grid = new Tuple[width][height];

	private RGB getColor(final Number value) {
		final RGB rgb = color;
		final CIELCH colorspace = rgb.toCIELCH();
		return new CIELCH(colorspace.L, colorspace.C, colorspace.H
				+ value.doubleValue()).toCIELab().toXYZ().toRGB();
	}

	public void doPaint() {
		// TODO: Check if is currently redrawing
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				// todo clear older values
				if (grid[x][y] != null){
					if (maxDuration > 0 && grid[x][y].getMetadata().getStart().getMainPoint()
							+ maxDuration < maxTime) {
						fillRectangle(x * boxWidth, y * boxWidth, boxWidth,
								boxHeight, backgroundColor);
						grid[x][y] = null;
					} else {
						Number v = grid[x][y].getAttribute(value_pos);
						fillRectangle(x * boxWidth, y * boxWidth, boxWidth,
								boxHeight, getColor(v));
					}
				}
			}

		}
	}

	@Override
	public void streamElementReceived(IPhysicalOperator operator,
			IStreamObject<?> element, int port) {
		@SuppressWarnings("unchecked")
		Tuple<? extends ITimeInterval> t = (Tuple<? extends ITimeInterval>) element;
		int x = t.getAttribute(xpos);
		int y = t.getAttribute(ypos);
		grid[x][y] = t;
		maxTime = t.getMetadata().getStart().getMainPoint();
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
	}

	@Override
	public void securityPunctuationElementReceived(
			IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
	}

	@Override
	public void paintControl(PaintEvent e) {
		// TODO: use clipping from e
		Point s = this.getCanvas().getSize();
		Image bufferImage = new Image(Display.getCurrent(), s.x, s.y);
		gc = new GC(bufferImage);
		gc.setTextAntialias(SWT.ON);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		doPaint();
		gc.dispose();
		gc = null;
		e.gc.drawImage(bufferImage, 0, 0);
		bufferImage.dispose();
	}

}