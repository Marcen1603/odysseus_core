package de.uniol.inf.is.odysseus.rcp.dashboard.colors.parts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Maps;

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
	private List<Date> timestampList = new ArrayList<Date>();
	private List<Boolean> warningList = new ArrayList<Boolean>();
	private List<Boolean> errorList = new ArrayList<Boolean>();
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
	private int warningIndex = 4;
	private int errorIndex = 5;
	private boolean showWarnings;
	/**
	 * @return the showWarnings
	 */
	public boolean isShowWarnings() {
		return showWarnings;
	}

	/**
	 * @param showWarnings the showWarnings to set
	 */
	public void setShowWarnings(boolean showWarnings) {
		this.showWarnings = showWarnings;
	}

	/**
	 * @return the showErrors
	 */
	public boolean isShowErrors() {
		return showErrors;
	}

	/**
	 * @param showErrors the showErrors to set
	 */
	public void setShowErrors(boolean showErrors) {
		this.showErrors = showErrors;
	}

	private boolean showErrors;
	private int timestampIndex = 0;
	private int redIndex = 1;
	/**
	 * @return the redIndex
	 */
	public int getRedIndex() {
		return redIndex;
	}

	/**
	 * @param redIndex the redIndex to set
	 */
	public void setRedIndex(int redIndex) {
		this.redIndex = redIndex;
	}

	/**
	 * @return the greenIndex
	 */
	public int getGreenIndex() {
		return greenIndex;
	}

	/**
	 * @param greenIndex the greenIndex to set
	 */
	public void setGreenIndex(int greenIndex) {
		this.greenIndex = greenIndex;
	}

	/**
	 * @return the blueIndex
	 */
	public int getBlueIndex() {
		return blueIndex;
	}

	/**
	 * @param blueIndex the blueIndex to set
	 */
	public void setBlueIndex(int blueIndex) {
		this.blueIndex = blueIndex;
	}

	private int greenIndex = 2;
	private int blueIndex = 3;

	/**
	 * @return the timestampIndex
	 */
	public int getTimestampIndex() {
		return timestampIndex;
	}

	/**
	 * @param timestampIndex the timestampIndex to set
	 */
	public void setTimestampIndex(int timestampIndex) {
		this.timestampIndex = timestampIndex;
	}

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
		
		this.setMaxElementsLine(this.parent.getSize().x / this.colorWidth);
		
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
			if (showWarnings) {
				gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				gc.fillRectangle(xPos, (lineNumber*colorHeight)+(lineNumber*lineSpace) - colorWidth, colorWidth, colorWidth);
			}
			if (showErrors) {
				gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				gc.fillRectangle(xPos, (lineNumber*colorHeight)+(lineNumber*lineSpace) - colorWidth, colorWidth, colorWidth);
			}
			
			elementsInLine ++;
			xPos += colorWidth;

			if (fixedTimestamps) {
				// fixed Timestamps
				if (timestampList.get(i).getTime() >= (lastTimestamp+fixedTimestampDiffMilliseconds)) {
					gc.drawString(String.valueOf(timestampList.get(i)), xPos-(colorWidth),  (lineNumber*colorHeight)+(lineNumber*lineSpace)+colorHeight, true);
					lastTimestamp = timestampList.get(i).getTime();
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
			
			warningList.add(getWarning(element));
			errorList.add(getError(element));
			colorList.add(elementToColor(element));
			
			if (!isInfinite() && colorList.size() > maxElements) {
				// colorList.size() equals timestampList.size() 
				colorList.remove(0);
				timestampList.remove(0);
				warningList.remove(0);
				errorList.remove(0);
			}
		}
	}
	
	private Boolean getError(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		return tuple.getAttribute(errorIndex);
	}
	
	private Boolean getWarning(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		return tuple.getAttribute(warningIndex);
	}

	private Date getTimestamp(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		Date timestamp = tuple.getAttribute(timestampIndex);
		return timestamp;
	}

	public Color elementToColor(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		Color color = new Color(Display.getCurrent(), (int) tuple.getAttribute(redIndex), (int) tuple.getAttribute(greenIndex),  (int) tuple.getAttribute(blueIndex));
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
	
	@Override
	public void onLoad(Map<String, String> saved) {
		showHeartbeats = Boolean.valueOf(saved.get("ShowHeartbeats"));
		updateInterval = Long.valueOf(saved.get("UpdateInterval"));
		this.colorHeight = Integer.valueOf(saved.get("ColorHeight"));
		this.colorWidth = Integer.valueOf(saved.get("ColorWidth"));
		this.fixedTimestampDiffMilliseconds = Long.valueOf(saved.get("FixedTimestampDiffMilliseconds"));
		this.fixedTimestamps = Boolean.valueOf(saved.get("FixedTimestamps"));
		this.lineSpace = Integer.valueOf(saved.get("LineSpace"));
		this.maxElements = Integer.valueOf(saved.get("MaxElements"));
		this.movingTimestampDiffElements = Long.valueOf(saved.get("MovingTimestampDiffElements"));
		this.timestampTextSize = Integer.valueOf(saved.get("TimestampTextSize"));
		this.showWarnings = Boolean.valueOf(saved.get("ShowWarnings"));
        this.showErrors = Boolean.valueOf(saved.get("ShowErrors"));	
        this.warningIndex = Integer.valueOf(saved.get("WarningIndex"));
        this.errorIndex = Integer.valueOf(saved.get("ErrorIndex"));
        this.timestampIndex = Integer.valueOf(saved.get("TimestampIndex"));
        this.redIndex = Integer.valueOf(saved.get("RedIndex"));
        this.blueIndex = Integer.valueOf(saved.get("BlueIndex"));
        this.greenIndex = Integer.valueOf(saved.get("GreenIndex"));
	}
	
	@Override
	public Map<String, String> onSave() {
		Map<String, String> saveMap = Maps.newHashMap();
		saveMap.put("ShowHeartbeats", String.valueOf(showHeartbeats));
		saveMap.put("UpdateInterval", String.valueOf(updateInterval));
		saveMap.put("ColorHeight", String.valueOf(colorHeight));
		saveMap.put("ColorWidth", String.valueOf(colorWidth));
		saveMap.put("FixedTimestampDiffMilliseconds", String.valueOf(fixedTimestampDiffMilliseconds));
		saveMap.put("FixedTimestamps", String.valueOf(fixedTimestamps));
		saveMap.put("LineSpace", String.valueOf(lineSpace));
		saveMap.put("MaxElements", String.valueOf(maxElements));
		saveMap.put("MovingTimestampDiffElements", String.valueOf(movingTimestampDiffElements));
		saveMap.put("TimestampTextSize", String.valueOf(timestampTextSize));
		saveMap.put("ShowWarnings", String.valueOf(showWarnings));
		saveMap.put("ShowErrors", String.valueOf(showErrors));
		saveMap.put("WarningIndex", String.valueOf(warningIndex));
		saveMap.put("ErrorIndex", String.valueOf(errorIndex));
		saveMap.put("TimestampIndex", String.valueOf(timestampIndex));
		saveMap.put("RedIndex", String.valueOf(redIndex));
		saveMap.put("GreenIndex", String.valueOf(greenIndex));
		saveMap.put("BlueIndex", String.valueOf(blueIndex));
		return saveMap;
	}

	/**
	 * @return the warningIndex
	 */
	public int getWarningIndex() {
		return warningIndex;
	}

	/**
	 * @param warningIndex the warningIndex to set
	 */
	public void setWarningIndex(int warningIndex) {
		this.warningIndex = warningIndex;
	}

	/**
	 * @return the errorIndex
	 */
	public int getErrorIndex() {
		return errorIndex;
	}

	/**
	 * @param errorIndex the errorIndex to set
	 */
	public void setErrorIndex(int errorIndex) {
		this.errorIndex = errorIndex;
	}

}
