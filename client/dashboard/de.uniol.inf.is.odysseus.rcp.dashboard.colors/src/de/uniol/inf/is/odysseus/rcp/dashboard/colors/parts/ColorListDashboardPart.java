package de.uniol.inf.is.odysseus.rcp.dashboard.colors.parts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

/**
 * This Dashboard-Part shows a List of Color-Values marked with Timestamps
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
	 * @param updateInterval
	 *            the updateInterval to set
	 */
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	private int maxElements = 200;
	private int maxElementsLine = 100;

	/**
	 * max Elements per Line will be set by the width of this Dashboard-Part
	 *
	 * @return the maxElementsLine
	 */
	public int getMaxElementsLine() {
		return maxElementsLine;
	}

	/**
	 * @param maxElementsLine
	 *            the maxElementsLine to set
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
	 * @param timestampTextSize
	 *            the timestampTextSize to set
	 */
	public void setTimestampTextSize(int timestampTextSize) {
		this.timestampTextSize = timestampTextSize;
		this.timestampFont = new Font(Display.getCurrent(), "Tahoma",
				timestampTextSize, SWT.NORMAL);
	}

	/**
	 * The width of the color-fields
	 *
	 * @return the colorWidth
	 */
	public int getColorWidth() {
		return colorWidth;
	}

	/**
	 * The width of the color-fields
	 *
	 * @param colorWidth
	 *            the colorWidth to set
	 */
	public void setColorWidth(int colorWidth) {
		this.colorWidth = colorWidth;
	}

	/**
	 * The height of the color-fields
	 *
	 * @return the colorHeight
	 */
	public int getColorHeight() {
		return colorHeight;
	}

	/**
	 * The height of the color-fields
	 *
	 * @param colorHeight
	 *            the colorHeight to set
	 */
	public void setColorHeight(int colorHeight) {
		this.colorHeight = colorHeight;
	}

	/**
	 * The Space between two lines of color-fields
	 *
	 * @return the lineSpace
	 */
	public int getLineSpace() {
		return lineSpace;
	}

	/**
	 * The Space between two lines of color-fields
	 *
	 * @param lineSpace
	 *            the lineSpace to set
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
	 * The distance between two drawn Timestamps, if they are mooving
	 *
	 * @return the movingTimestampDiffElements In the number of Color-Fields
	 */
	public long getMovingTimestampDiffElements() {
		return movingTimestampDiffElements;
	}

	/**
	 * The distance between two drawn Timestamps, if they are mooving
	 *
	 * @param movingTimestampDiffElements
	 *            the movingTimestampDiffElements to set In the number of
	 *            Color-Fields
	 */
	public void setMovingTimestampDiffElements(long movingTimestampDiffElements) {
		this.movingTimestampDiffElements = movingTimestampDiffElements;
	}

	/**
	 * The distance between two drawn Timestamps, if they are fixed in the
	 * position
	 *
	 * @return the fixedTimestampDiffMilliseconds In number of milliseconds
	 */
	public long getFixedTimestampDiffMilliseconds() {
		return fixedTimestampDiffMilliseconds;
	}

	/**
	 * The distance between two drawn Timestamps, if they are fixed in the
	 * position
	 *
	 * @param fixedTimestampDiffMilliseconds
	 *            the fixedTimestampDiffMilliseconds to set In number of
	 *            milliseconds
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
	 * If true warnings will be marked
	 *
	 * @return the showWarnings
	 */
	public boolean isShowWarnings() {
		return showWarnings;
	}

	/**
	 * If true warnings will be marked
	 *
	 * @param showWarnings
	 *            the showWarnings to set
	 */
	public void setShowWarnings(boolean showWarnings) {
		this.showWarnings = showWarnings;
	}

	/**
	 * If true errors will be marked
	 *
	 * @return the showErrors
	 */
	public boolean isShowErrors() {
		return showErrors;
	}

	/**
	 * If true errors will be marked
	 *
	 * @param showErrors
	 *            the showErrors to set
	 */
	public void setShowErrors(boolean showErrors) {
		this.showErrors = showErrors;
	}

	private boolean showErrors;
	private int timestampIndex = 0;
	private int redIndex = 1;

	/**
	 * Index of the red-value in the tuples of the data-stream
	 *
	 * @return the redIndex
	 */
	public int getRedIndex() {
		return redIndex;
	}

	/**
	 * Index of the red-value in the tuples of the data-stream
	 *
	 * @param redIndex
	 *            the redIndex to set
	 */
	public void setRedIndex(int redIndex) {
		this.redIndex = redIndex;
	}

	/**
	 * Index of the green-value in the tuples of the data-stream
	 *
	 * @return the greenIndex
	 */
	public int getGreenIndex() {
		return greenIndex;
	}

	/**
	 * Index of the green-value in the tuples of the data-stream
	 *
	 * @param greenIndex
	 *            the greenIndex to set
	 */
	public void setGreenIndex(int greenIndex) {
		this.greenIndex = greenIndex;
	}

	/**
	 * Index of the blue-value in the tuples of the data-stream
	 *
	 * @return the blueIndex
	 */
	public int getBlueIndex() {
		return blueIndex;
	}

	/**
	 * Index of the blue-value in the tuples of the data-stream
	 *
	 * @param blueIndex
	 *            the blueIndex to set
	 */
	public void setBlueIndex(int blueIndex) {
		this.blueIndex = blueIndex;
	}

	private int greenIndex = 2;
	private int blueIndex = 3;

	private List<Boolean> timestampFlags = new ArrayList<Boolean>();

	/**
	 * Index of the timestamp in the tuples of the data-stream
	 *
	 * @return the timestampIndex
	 */
	public int getTimestampIndex() {
		return timestampIndex;
	}

	/**
	 * Index of the timestamp in the tuples of the data-stream
	 *
	 * @param timestampIndex
	 *            the timestampIndex to set
	 */
	public void setTimestampIndex(int timestampIndex) {
		this.timestampIndex = timestampIndex;
	}

	/**
	 * If true the timestamps will be fixed and will change their value, if a
	 * new color-block will appear at their position on canvas
	 *
	 * @return the fixedTimestamps
	 */
	public boolean isFixedTimestamps() {
		return fixedTimestamps;
	}

	/**
	 * If true the timestamps will be fixed and will change their value, if a
	 * new color-block will appear at their position on canvas
	 *
	 * @param fixedTimestamps
	 *            the fixedTimestamps to set
	 */
	public void setFixedTimestamps(boolean fixedTimestamps) {
		this.fixedTimestamps = fixedTimestamps;
	}

	private DefaultToolTip tt;

	/**
	 * color Points are the upper left position of the color-blocks on canvas
	 */
	private List<Point> colorPoints = new ArrayList<Point>();;

	@Override
	public void createPartControl(final Composite parent, ToolBar toolbar) {
		this.parent = parent;
		this.parent.setLayout(new FillLayout());
		this.canvas = new Canvas(parent, 0);
		this.canvas.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		this.gc = new GC(canvas);
		this.timestampFont = new Font(Display.getCurrent(), "Tahoma",
				timestampTextSize, SWT.NORMAL);
		tt = new DefaultToolTip(canvas, SWT.NONE, false);

		// create MouseMoveListener to handle various tooltips on canvas
		canvas.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				// search for the color which is pointed by the mouse
				search: for (int i = 0; i < colorPoints.size(); i++) {
					Point p = colorPoints.get(i);
					if (p.x <= e.x && (p.x + colorWidth) >= e.x) {
						// mouse is in x-axis in the color-field of p
						if (p.y <= e.y && (p.y + colorHeight) >= e.y) {
							// mouse is in the color-field of p
							if (timestampList.get(i) != null) {
								tt.setText(timestampList.get(i).toString());
							} else {
								tt.setText("No Timestamp Found");
							}
							tt.show(p);
							break search;
						}
					}
				}

			}

		});

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

					waiting(updateInterval);
				}
			}

		});

		updateThread.setName("StreamList Updater");
		updateThread.start();
	}

	/**
	 * This method let the updateThread sleep
	 *
	 * @param length
	 *            Time to sleep
	 */
	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
			// ignore
		}
	}

	/**
	 * Refreshs the canvas and draw every color-field new
	 */
	private void refreshCanvas() {
		int lineNumber = 0;
		int elementsInLine = 0;
		int xPos = 0;
		int yPos = 0;
		double lastTimestamp = 0 - fixedTimestampDiffMilliseconds;

		this.setMaxElementsLine(this.parent.getSize().x / this.colorWidth);

		gc.setFont(timestampFont);
		// to clear everything
		gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
		colorPoints.clear();
		for (int i = 0; i < colorList.size(); i++) {
			gc.setBackground(colorList.get(i));
			if (elementsInLine >= maxElementsLine) {
				lineNumber++;
				elementsInLine = 0;
				xPos = 0;
			}
			yPos = (lineNumber * colorHeight) + (lineNumber * lineSpace);
			gc.fillRectangle(xPos, yPos, colorWidth, colorHeight);
			colorPoints.add(new Point(xPos, yPos));

			if (showWarnings) {
				gc.setBackground(Display.getCurrent().getSystemColor(
						SWT.COLOR_YELLOW));
				gc.fillRectangle(xPos, (lineNumber * colorHeight)
						+ (lineNumber * lineSpace) - colorWidth, colorWidth,
						colorWidth);
			}
			if (showErrors) {
				gc.setBackground(Display.getCurrent().getSystemColor(
						SWT.COLOR_RED));
				gc.fillRectangle(xPos, (lineNumber * colorHeight)
						+ (lineNumber * lineSpace) - colorWidth, colorWidth,
						colorWidth);
			}

			elementsInLine++;
			xPos += colorWidth;

			if (fixedTimestamps) {
				// fixed Timestamps
				if (timestampList.get(i).getTime() >= (lastTimestamp + fixedTimestampDiffMilliseconds)
						&& timestampList.get(i) != null) {
					gc.drawString(String.valueOf(timestampList.get(i)), xPos
							- (colorWidth), (lineNumber * colorHeight)
							+ (lineNumber * lineSpace) + colorHeight, true);
					lastTimestamp = timestampList.get(i).getTime();
				}
			} else {
				// moving Timestamps
				if (timestampFlags.get(i) && timestampList.get(i) != null) {
					gc.drawString(String.valueOf(timestampList.get(i)), xPos
							- (colorWidth), (lineNumber * colorHeight)
							+ (lineNumber * lineSpace) + colorHeight, true);
				}
			}
		}
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		synchronized (colorList) {
			timestampList.add(getTimestamp(element));
			if (!fixedTimestamps) {
				// moving Timestamps
				counter++;
				if (counter == movingTimestampDiffElements) {
					counter = 0;
					timestampFlags.add(true);
				} else {
					timestampFlags.add(false);
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

	/**
	 * Returns the Boolean, which is set at the errorIndex of specified tuple.
	 * So it will check, if this tuple is a error
	 *
	 * @param element
	 *            the tuple which should be testet
	 * @return True if this is a Error, false otherwise
	 */
	private Boolean getError(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		return tuple.getAttribute(errorIndex);
	}

	/**
	 * Returns the Boolean, which is set at the warningIndex of specified tuple.
	 * So it will check, if this tuple is a warning
	 *
	 * @param element
	 *            the tuple which should be testet
	 * @return True if this is a Error, false otherwise
	 */
	private Boolean getWarning(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		return tuple.getAttribute(warningIndex);
	}

	/**
	 * Returns the Timestamp, which is set at the timestampIndex of specified
	 * tuple.
	 *
	 * @param element
	 *            the tuple
	 * @return the timestamp
	 */
	private Date getTimestamp(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		Date timestamp = tuple.getAttribute(timestampIndex);
		return timestamp;
	}

	/**
	 * Converts a tuple to a swt.Color. The red-value, green-value and
	 * blue-value has to be set on the position, given by the redIndex,
	 * greenIndex and blueIndex by the instance of ColorListDashboardPart
	 *
	 * @param element
	 * @return
	 */
	public Color elementToColor(IStreamObject<?> element) {
		Tuple<?> tuple = (Tuple<?>) element;
		Color color = new Color(Display.getCurrent(),
				(int) tuple.getAttribute(redIndex),
				(int) tuple.getAttribute(greenIndex),
				(int) tuple.getAttribute(blueIndex));
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

	/**
	 *
	 * @return true, if you can show an unlimited amount of Color-Values
	 */
	private boolean isInfinite() {
		return maxElements < 0;
	}

	/**
	 * sets the max number of color-values, which will be shown by this
	 * dashboard-part
	 *
	 * @param maxElements
	 */
	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}

	/**
	 *
	 * @return the max number of color-values, which will be shown by this
	 *         dashboard-part
	 */
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
	 * @param showHeartbeats
	 *            the showHeartbeats to set
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
		this.fixedTimestampDiffMilliseconds = Long.valueOf(saved
				.get("FixedTimestampDiffMilliseconds"));
		this.fixedTimestamps = Boolean.valueOf(saved.get("FixedTimestamps"));
		this.lineSpace = Integer.valueOf(saved.get("LineSpace"));
		this.maxElements = Integer.valueOf(saved.get("MaxElements"));
		this.movingTimestampDiffElements = Long.valueOf(saved
				.get("MovingTimestampDiffElements"));
		this.timestampTextSize = Integer
				.valueOf(saved.get("TimestampTextSize"));
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
		saveMap.put("FixedTimestampDiffMilliseconds",
				String.valueOf(fixedTimestampDiffMilliseconds));
		saveMap.put("FixedTimestamps", String.valueOf(fixedTimestamps));
		saveMap.put("LineSpace", String.valueOf(lineSpace));
		saveMap.put("MaxElements", String.valueOf(maxElements));
		saveMap.put("MovingTimestampDiffElements",
				String.valueOf(movingTimestampDiffElements));
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
	 * The position on the tuples, which represent the warning-flag
	 *
	 * @return the warningIndex
	 */
	public int getWarningIndex() {
		return warningIndex;
	}

	/**
	 * The position on the tuples, which represent the warning-flag
	 *
	 * @param warningIndex
	 *            the warningIndex to set
	 */
	public void setWarningIndex(int warningIndex) {
		this.warningIndex = warningIndex;
	}

	/**
	 * The position on the tuples, which represent the error-flag
	 *
	 * @return the errorIndex
	 */
	public int getErrorIndex() {
		return errorIndex;
	}

	/**
	 * The position on the tuples, which represent the error-flag
	 *
	 * @param errorIndex
	 *            the errorIndex to set
	 */
	public void setErrorIndex(int errorIndex) {
		this.errorIndex = errorIndex;
	}

}
