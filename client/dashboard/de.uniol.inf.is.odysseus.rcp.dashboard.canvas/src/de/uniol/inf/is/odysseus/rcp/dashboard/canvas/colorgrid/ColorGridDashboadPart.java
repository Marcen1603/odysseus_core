package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorgrid;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.CIELCH;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

public class ColorGridDashboadPart extends AbstractCanvasDashboardPart implements MouseListener, MouseMoveListener{

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
	private boolean leftButtonPressed;
	private int leftButtonLastX;
	private int leftButtonLastY;

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
	public void mouseMove(MouseEvent e) {
		moveLeftTop(e);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.button == 1){
			leftButtonPressed = true;
			leftButtonLastX = e.x;
			leftButtonLastY = e.y;
			setMoveCursor();
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (e.button == 1){
			moveLeftTop(e);
			leftButtonPressed = false;
			setDefaultCursor();
		}		
	}

	private void moveLeftTop(MouseEvent e) {
		if (leftButtonPressed){
			leftTop = new Point(leftTop.x+(e.x-leftButtonLastX), leftTop.y + (e.y-leftButtonLastY) );
			leftButtonLastX = e.x;
			leftButtonLastY = e.y;
		}
	}


}