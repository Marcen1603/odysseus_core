package de.uniol.inf.is.odysseus.rcp.dashboard.part.greenmatrix;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class GreenMatrixDashboardPart extends AbstractDashboardPart implements PaintListener {

	private static final Logger LOG = LoggerFactory.getLogger(GreenMatrixDashboardPart.class);

	private List<String> nextTuples = new ArrayList<String>();
	// Init with 20. The correct number is calculated later
	private StringHolder[] currentTuples = new StringHolder[20];
	private String[] lines = new String[20];

	private Canvas canvas;

	private int currentLine = 0;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		canvas = new Canvas(parent, SWT.BORDER |  SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		canvas.addPaintListener(this);
	}

	@Override
	public void paintControl(PaintEvent e) {
		synchronized (currentTuples) {

			// Create a line
			String newLine = "";
			for (int i = 0; i < currentTuples.length; i++) {

				if (currentTuples[i] != null && currentTuples[i].text.length() <= currentTuples[i].position) {
					// This tuple is on the display, remove it
					currentTuples[i] = null;
				}

				if (currentTuples[i] == null) {
					// We need to fill this gap with the next tuple
					if (!nextTuples.isEmpty()) {
						currentTuples[i] = new StringHolder(nextTuples.get(0), 0);
						nextTuples.remove(0);
					} else {
						// We can't take another letter from here
						continue;
					}
				}

				char nextChar = currentTuples[i].text.charAt(currentTuples[i].position++);
				newLine = newLine + nextChar + " ";
			}

			// currentLine has the newest line
			currentLine = (currentLine + 1) % lines.length;
			lines[currentLine] = newLine;

			// Idea: Save color for each char

			// Draw all lines
			final GC gc = e.gc;
			RGB greenRGB = new RGB(128,223,129);
			Color green = new Color(Display.getCurrent(), greenRGB);
 			//RGB lightGreenRGB = new RGB(226,255,218);
			//Color lightGreen = new Color(Display.getCurrent(), lightGreenRGB);
			//RGB darkGreenRGB = new RGB(68,153,70);
			//Color darkGreen = new Color(Display.getCurrent(), darkGreenRGB);
			gc.setForeground(green);
			Font terminalFont = JFaceResources.getFont(JFaceResources.TEXT_FONT);
			gc.setFont(terminalFont);
			int lineCnt = 0;
			for (int i = currentLine; i != Math.floorMod((currentLine + 1), lines.length); i = Math.floorMod((i - 1),
					lines.length)) {
				if (lines[i] == null)
					break;

				gc.drawText(lines[i], 5, 5 + 20 * lineCnt);
				lineCnt++;
			}

		}

	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {

	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple)) {
			LOG.error("Could not use stream-objects of class {}. Only {} supported.", element.getClass(), Tuple.class);
			return;
		}

		try {
			final Tuple<?> tuple = (Tuple<?>) element;
			String stringRep = tuple.toString();
			// Random number of spaces after tuple
			int num = (int) (Math.random() * lines.length);
			for (int i = 0; i < num; i++) {
				stringRep = stringRep + " ";
			}
			updateStatus(stringRep);
		} catch (final Throwable t) {
			LOG.error("Could not process Tuple {}!", element, t);
		}
	}

	private void updateStatus(String lastTuple) {

		synchronized (currentTuples) {
			nextTuples.add(lastTuple);
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Point size = canvas.getSize();
				int width = size.x;
				int height = size.y;

				// 20 Pixels per char
				int charSize = 20;

				int numRows = width / charSize;
				int numLines = height / charSize;

				if (numLines != lines.length) {
					String[] copy = lines.clone();
					lines = new String[numLines];
					for (int i = 0; i < copy.length && i < lines.length; i++) {
						lines[i] = copy[i];
					}
				}

				if (numRows != currentTuples.length) {
					StringHolder[] copy = currentTuples.clone();
					currentTuples = new StringHolder[numRows];
					for (int i = 0; i < copy.length && i < currentTuples.length; i++) {
						currentTuples[i] = copy[i];
					}
				}

				canvas.redraw();
			}

		});
	}

}

class StringHolder {
	String text;
	int position;

	public StringHolder(String text, int position) {
		this.text = text;
		this.position = position;
	}
}
