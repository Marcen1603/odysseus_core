package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class IntervalLines {
	
	private static final long DEFAULT_DRAWING_SPACE = 1000;
	private static final int INTERVAL_LINE_MAX_HEIGHT_PIXELS = 100;
	private static final int INTERVAL_LINE_MIN_HEIGHT_PIXELS = 15;
	private static final int VERTICAL_MARGIN_PIXELS = 5;
	private static final int ELEMENT_DATA_HORIZONTAL_MARGIN_PIXELS = 10;

	private final List<IntervalLine> storedElements = Lists.newArrayList();

	private final Color lineColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color selectionColor = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	
	private Long minimumStartTime;
	private Long maximumStartTime;
	private Long minimumEndTime;
	private Long maximumEndTime;

	public void add(IStreamObject<? extends ITimeInterval> newElement) {
		IntervalLine intervalLine = new IntervalLine(newElement);
		
		synchronized (storedElements) {
			storedElements.add(intervalLine);
			
			updateTimeBorders(newElement);
		}
	}

	private void updateTimeBorders(IStreamObject<? extends ITimeInterval> newElement) {
		ITimeInterval metadata = newElement.getMetadata();
		PointInTime startPoint = metadata.getStart();
		if( !startPoint.isInfinite() ) {
			if( minimumStartTime == null ||  startPoint.getMainPoint() < minimumStartTime ) {
				minimumStartTime = startPoint.getMainPoint();
			}
			if( maximumStartTime == null ||  startPoint.getMainPoint() > maximumStartTime ) {
				maximumStartTime = startPoint.getMainPoint();
			}
		}
		
		PointInTime endPoint = metadata.getEnd();
		if( !endPoint.isInfinite() ) {
			if( minimumEndTime == null || endPoint.getMainPoint() < minimumEndTime ) {
				minimumEndTime = endPoint.getMainPoint();
			}
			if(maximumEndTime == null || endPoint.getMainPoint() > maximumEndTime ) {
				maximumEndTime = endPoint.getMainPoint();
			}
		}
	}

	public void clear() {
		synchronized (storedElements) {
			storedElements.clear();
			
			minimumStartTime = null;
			maximumEndTime = null;
		}
	}

	public void drawLines(GC gc, int width, int height, int highlightY) {
		synchronized( storedElements ) {
			if( storedElements.isEmpty() ) {
				// nothing to draw
				return;
			}
			
			// calculating the drawing ratio
			long timeDifference;
			long startTime;
			long endTime;
			if( maximumEndTime != null && minimumStartTime != null ) {
				// we have start and end times to define our drawing ratio
				// do nothing here
				startTime = minimumStartTime;
				endTime = maximumEndTime;
			} else if( maximumEndTime != null ) {
				// we have no start timestamp to work with...
				// we use the lowest end timestamp
				startTime = minimumEndTime;
				endTime = maximumEndTime;
			} else if( minimumStartTime != null ) {
				// we have no end timestamp to work with
				// we use the highest starttimestamp
				startTime = minimumStartTime;
				endTime = maximumStartTime;
			} else {
				// we have no start and no end timestamps here
				// every element will be drawn as a long line
				startTime = 0;
				endTime = DEFAULT_DRAWING_SPACE;
			}
			timeDifference = endTime - startTime;
			
			int intervalLineHeight = Math.min(Math.max(height / storedElements.size(), INTERVAL_LINE_MAX_HEIGHT_PIXELS), INTERVAL_LINE_MIN_HEIGHT_PIXELS);
			int halfIntervalLineHeight = intervalLineHeight / 2;
			int drawAreaWidth = width - (2 * VERTICAL_MARGIN_PIXELS);
			
			// determine if older elements should be removed
			removeOldElementsIfNeeded(height, intervalLineHeight);
	
			// space to the border of the canvas
			int drawY = VERTICAL_MARGIN_PIXELS;
	
			gc.setForeground(lineColor);
			for (IntervalLine intervalLine : storedElements) {
				// mouse highlights line?
				if (highlightY >= drawY - halfIntervalLineHeight && highlightY <= drawY + halfIntervalLineHeight) {
					Color oldColor = gc.getBackground();
					gc.setBackground(selectionColor);
					gc.fillRectangle(0, drawY - halfIntervalLineHeight, width, intervalLineHeight);
					gc.setBackground(oldColor);
				}
	
				// depending on the infinity of the timestamps, we have to draw the interval lines differently
				if( intervalLine.isStartInfinite() && intervalLine.isEndInfinite() ) {
					// only a line... "infinite" to "infinite"
					gc.drawLine(VERTICAL_MARGIN_PIXELS, drawY, width, drawY);

					Color oldColor = gc.getBackground();
					gc.setBackground(lineColor);
					gc.fillOval(VERTICAL_MARGIN_PIXELS - halfIntervalLineHeight, drawY - halfIntervalLineHeight, halfIntervalLineHeight * 2, halfIntervalLineHeight * 2);
					gc.fillOval(width - halfIntervalLineHeight, drawY - halfIntervalLineHeight, halfIntervalLineHeight * 2, halfIntervalLineHeight * 2);
					gc.setBackground(oldColor);

				} else if( intervalLine.isStartInfinite() ) {
					// we have only an end timestamp
					int endPixels = VERTICAL_MARGIN_PIXELS + (int) (((intervalLine.getEnd() - startTime) / (double) timeDifference) * drawAreaWidth);
					gc.drawLine(VERTICAL_MARGIN_PIXELS, drawY, endPixels, drawY);
					gc.drawLine(endPixels, drawY - halfIntervalLineHeight, endPixels, drawY + halfIntervalLineHeight);

					Color oldColor = gc.getBackground();
					gc.setBackground(lineColor);
					gc.fillOval(VERTICAL_MARGIN_PIXELS - halfIntervalLineHeight, drawY - halfIntervalLineHeight, halfIntervalLineHeight * 2, halfIntervalLineHeight * 2);
					gc.setBackground(oldColor);
				
				} else if( intervalLine.isEndInfinite() ) {
					// we have only a start timestamp
					int startPixels = VERTICAL_MARGIN_PIXELS + (int) (((intervalLine.getStart() - startTime) / (double) timeDifference) * drawAreaWidth);
					gc.drawLine(startPixels, drawY, width, drawY);
					gc.drawLine(startPixels, drawY - halfIntervalLineHeight, startPixels, drawY + halfIntervalLineHeight);	
					
					Color oldColor = gc.getBackground();
					gc.setBackground(lineColor);
					gc.fillOval(width - halfIntervalLineHeight, drawY - halfIntervalLineHeight, halfIntervalLineHeight * 2, halfIntervalLineHeight * 2);
					gc.setBackground(oldColor);
					
				} else {
					// we have both, start and end timestamp
					int startPixels = VERTICAL_MARGIN_PIXELS + (int) (((intervalLine.getStart() - startTime) / (double) timeDifference) * drawAreaWidth);
					int endPixels = VERTICAL_MARGIN_PIXELS + (int) (((intervalLine.getEnd() - startTime) / (double) timeDifference) * drawAreaWidth);
		
					gc.drawLine(startPixels, drawY, endPixels, drawY);
					gc.drawLine(startPixels, drawY - halfIntervalLineHeight, startPixels, drawY + halfIntervalLineHeight);
					gc.drawLine(endPixels, drawY - halfIntervalLineHeight, endPixels, drawY + halfIntervalLineHeight);
				}
				
				drawY += ( VERTICAL_MARGIN_PIXELS + intervalLineHeight );
			}
		}
	}

	private void removeOldElementsIfNeeded(int height, int intervalLineHeight) {
		int maxElementCount = height / ( intervalLineHeight + VERTICAL_MARGIN_PIXELS );
		
		boolean hasRemoved = false;
		while( storedElements.size() > maxElementCount ) {
			storedElements.remove(0);
			hasRemoved = true;
		}
		
		if( hasRemoved ) {
			minimumEndTime = null;
			maximumEndTime = null;
			minimumStartTime = null;
			maximumStartTime = null;
			for( IntervalLine line : storedElements ) {
				updateTimeBorders(line.getElement());
			}
		}
	}
	
	public void drawElements(GC gc, int width, int height, int highlightY) {
		synchronized( storedElements ) {
			if( storedElements.isEmpty() ) {
				// nothing to draw
				return;
			}
			
			int intervalLineHeight = Math.min(Math.max(height / storedElements.size(), INTERVAL_LINE_MAX_HEIGHT_PIXELS), INTERVAL_LINE_MIN_HEIGHT_PIXELS);
			int halfIntervalLineHeight = intervalLineHeight / 2;
			int drawY = VERTICAL_MARGIN_PIXELS;
			
			gc.setForeground(lineColor);
			
			removeOldElementsIfNeeded(height, intervalLineHeight);
			
			Color oldColor = gc.getBackground();
			for (IntervalLine intervalLine : storedElements) {
				if (highlightY >= drawY - halfIntervalLineHeight && highlightY <= drawY + halfIntervalLineHeight) {
					// mouse highlights line
					gc.setBackground(selectionColor);
					gc.fillRectangle(0, drawY - halfIntervalLineHeight, width, intervalLineHeight);
				}
				
				gc.drawText(intervalLine.getElement().toString(), ELEMENT_DATA_HORIZONTAL_MARGIN_PIXELS, drawY - intervalLineHeight / 2);
				gc.setBackground(oldColor);
				
				drawY += ( VERTICAL_MARGIN_PIXELS + intervalLineHeight );
			}
		}
	}

}
