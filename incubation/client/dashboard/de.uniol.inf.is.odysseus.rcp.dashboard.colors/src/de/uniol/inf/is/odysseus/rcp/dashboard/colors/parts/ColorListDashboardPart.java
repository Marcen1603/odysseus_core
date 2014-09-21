package de.uniol.inf.is.odysseus.rcp.dashboard.colors.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

/**
 * 
 * @author MarkMilster
 *
 */
public class ColorListDashboardPart extends AbstractDashboardPart {

	private Canvas canvas;
	private List<Color> colorList = new ArrayList<Color>();
	private Thread updateThread;
	private long updateInterval = 100;
	private boolean showHeartbeats = false;
	/**
	 * @return the updateInterval
	 */
	public long getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * @param updateInterval the updateInterval to set
	 */
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	private int maxElements = 200;
	private int maxElementsLine = 100;
	/**
	 * @return the maxElementsLine
	 */
	public int getMaxElementsLine() {
		return maxElementsLine;
	}

	/**
	 * @param maxElementsLine the maxElementsLine to set
	 */
	public void setMaxElementsLine(int maxElementsLine) {
		this.maxElementsLine = maxElementsLine;
	}

	private GC gc;
	private int colorWidth = 20;
	private Font timestampFont;
	/**
	 * @return the timestampTextSize
	 */
	public int getTimestampTextSize() {
		return timestampTextSize;
	}

	/**
	 * @param timestampTextSize the timestampTextSize to set
	 */
	public void setTimestampTextSize(int timestampTextSize) {
		this.timestampTextSize = timestampTextSize;
		this.timestampFont = new Font(Display.getCurrent(), "Tahoma", timestampTextSize , SWT.NORMAL);
	}

	/**
	 * @return the colorWidth
	 */
	public int getColorWidth() {
		return colorWidth;
	}

	/**
	 * @param colorWidth the colorWidth to set
	 */
	public void setColorWidth(int colorWidth) {
		this.colorWidth = colorWidth;
	}

	/**
	 * @return the colorHeight
	 */
	public int getColorHeight() {
		return colorHeight;
	}

	/**
	 * @param colorHeight the colorHeight to set
	 */
	public void setColorHeight(int colorHeight) {
		this.colorHeight = colorHeight;
	}

	/**
	 * @return the lineSpace
	 */
	public int getLineSpace() {
		return lineSpace;
	}

	/**
	 * @param lineSpace the lineSpace to set
	 */
	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}

	private int colorHeight = 80;
	private int lineSpace = 20;
	private List<Double> timestampList = new ArrayList<Double>();
	private long movingTimestampDiffElements = 10; // in Elements
	/**
	 * @return the movingTimestampDiffElements
	 */
	public long getMovingTimestampDiffElements() {
		return movingTimestampDiffElements;
	}

	/**
	 * @param movingTimestampDiffElements the movingTimestampDiffElements to set
	 */
	public void setMovingTimestampDiffElements(long movingTimestampDiffElements) {
		this.movingTimestampDiffElements = movingTimestampDiffElements;
	}

	/**
	 * @return the fixedTimestampDiffMilliseconds
	 */
	public long getFixedTimestampDiffMilliseconds() {
		return fixedTimestampDiffMilliseconds;
	}

	/**
	 * @param fixedTimestampDiffMilliseconds the fixedTimestampDiffMilliseconds to set
	 */
	public void setFixedTimestampDiffMilliseconds(
			long fixedTimestampDiffMilliseconds) {
		this.fixedTimestampDiffMilliseconds = fixedTimestampDiffMilliseconds;
	}

	private long fixedTimestampDiffMilliseconds = 100000; // in Milliseconds
	private long counter;
	private int timestampTextSize = 12;
	private boolean fixedTimestamps = true;
	private Composite parent;

	/**
	 * @return the fixedTimestamps
	 */
	public boolean isFixedTimestamps() {
		return fixedTimestamps;
	}

	/**
	 * @param fixedTimestamps the fixedTimestamps to set
	 */
	public void setFixedTimestamps(boolean fixedTimestamps) {
		this.fixedTimestamps = fixedTimestamps;
	}

	@Override
	public void createPartControl(final Composite parent, ToolBar toolbar) {
		this.parent = parent;
		this.parent.setLayout(new FillLayout());
		this.canvas = new Canvas(parent, 0);
		this.canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		this.gc = new GC(canvas);
		this.timestampFont = new Font(Display.getCurrent(), "Tahoma", timestampTextSize , SWT.NORMAL);
		
		
		
		updateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!parent.isDisposed()) {
					final Display disp = PlatformUI.getWorkbench().getDisplay();
					if (!disp.isDisposed()) {
						disp.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (!canvas.isDisposed()) {
									refreshCanvas();
								}
							}
						});
					}

					waiting(updateInterval );
				}
			}

		});

		updateThread.setName("StreamList Updater");
		updateThread.start();
	}
	
	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
		}
	}
	
	private void refreshCanvas() {
		int lineNumber = 0;
		int elementsInLine = 0;
		int xPos = 0;
		double lastTimestamp = 0-fixedTimestampDiffMilliseconds;	
		
		System.out.println();
		System.out.println("x: " + this.parent.getSize().x);
		System.out.println("y: " + this.parent.getSize().y);
		
		this.setMaxElementsLine(this.parent.getSize().x / this.colorWidth);
		System.out.println(this.getMaxElementsLine());
		
		gc.setFont(timestampFont);
		//to clear everything
		gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
		for (int i=0; i < colorList.size(); i++) {
			gc.setBackground(colorList.get(i));
			if (elementsInLine >= maxElementsLine) {
				lineNumber ++;
				elementsInLine = 0;
				xPos = 0;
			}
			gc.fillRectangle(xPos, (lineNumber*colorHeight)+(lineNumber*lineSpace), colorWidth, colorHeight );
			elementsInLine ++;
			xPos += colorWidth;

			if (fixedTimestamps) {
				// fixed Timestamps
				if (timestampList.get(i) >= (lastTimestamp+fixedTimestampDiffMilliseconds)) {
					gc.drawString(String.valueOf(timestampList.get(i)), xPos-(colorWidth),  (lineNumber*colorHeight)+(lineNumber*lineSpace)+colorHeight, true);
					lastTimestamp = timestampList.get(i);
				}
			} else {
				// moving Timestamps
				if (timestampList.get(i) != null) {
					gc.drawString(String.valueOf(timestampList.get(i)), xPos-(colorWidth),  (lineNumber*colorHeight)+(lineNumber*lineSpace)+colorHeight, true);
				}
			}
		}
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		synchronized (colorList) {
			if (fixedTimestamps) {
				timestampList.add(getTimestamp(element));
			} else {
				// moving Timestamps
				counter ++;
				if (counter == movingTimestampDiffElements) {
					counter = 0;
					timestampList.add(getTimestamp(element));
				} else {
					timestampList.add(null);
				}
			}
			
			colorList.add(elementToColor(element));
			if (!isInfinite() && colorList.size() > maxElements) {
				// colorList.size() equals timestampList.size() 
				colorList.remove(0);
				timestampList.remove(0);
			}
		}
	}
	
	private Double getTimestamp(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		//TODO vorher klar machen ob an Position 1 ueberhaupt der Timestamp ist
		return tuple.getAttribute(1);
	}

	public Color elementToColor(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		//TODO alle drei Werte nehmen -> das hier ist nur für Testzwecke mit einem Wert
		Double d = tuple.getAttribute(0);
		Color color = new Color(Display.getCurrent(), 0, 0,  d.intValue());
		return color;
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
		synchronized (colorList) {
			if (!point.isHeartbeat() || showHeartbeats) {
				if (!isInfinite() && colorList.size() > maxElements) {
					colorList.remove(0);
				}
			}
		}
	}
	
	private boolean isInfinite() {
		return maxElements < 0;
	}

	@Override
	public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
		punctuationElementRecieved(senderOperator, sp, port);
	}

	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}

	public int getMaxElements() {
		return this.maxElements;
	}

	/**
	 * @return the showHeartbeats
	 */
	public boolean isShowHeartbeats() {
		return showHeartbeats;
	}

	/**
	 * @param showHeartbeats the showHeartbeats to set
	 */
	public void setShowHeartbeats(boolean showHeartbeats) {
		this.showHeartbeats = showHeartbeats;
	}

}
