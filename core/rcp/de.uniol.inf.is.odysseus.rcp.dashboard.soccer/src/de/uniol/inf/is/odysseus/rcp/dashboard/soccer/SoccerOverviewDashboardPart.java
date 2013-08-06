/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class SoccerOverviewDashboardPart extends AbstractDashboardPart implements PaintListener {

	private static final Logger LOG = LoggerFactory.getLogger(SoccerOverviewDashboardPart.class);

	private static final List<Integer> SID_BALLS = SoccerMetadataProvider.getSidBalls();
	private static final List<Integer> SID_TEAM_A = SoccerMetadataProvider.getSidTeamA();
	private static final List<Integer> SID_TEAM_B = SoccerMetadataProvider.getSidTeamB();
	private static final List<Integer> SID_REFEREE = SoccerMetadataProvider.getSidReferee();
	private static final Map<Integer, Integer> SENSOR_PLAYER_MAP = SoccerMetadataProvider.getSensorPlayerMap();

	private static final int PLAYER_SIZE_PIXELS = 6;
	private static final int BALL_SIZE_PIXELS = 10;
	private static final int FONT_SIZE = 8;
	private static final int POINT_SIZE_PIXELS = 8;
	private static final int SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS = 13;
	private static final int UPDATE_INTERVAL_MILLIS = 100;

	private Font playerIDFont;
	private Font timeFont;

	private final Map<Integer, Tuple<?>> currentTuples = Maps.newConcurrentMap();
	private Map<String, Integer> attributeIndexMap;
	private int xIndex;
	private int yIndex;
	private int tsIndex;
	private boolean validAttributes;

	private Composite soccerComposite;
	private Canvas soccerCanvas;

	private CanvasUpdater canvasUpdater;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		parent.setLayout(new FillLayout());
		
		soccerComposite = new Composite(parent, SWT.BORDER);
		soccerComposite.setLayout(new FillLayout());

		soccerCanvas = new Canvas(soccerComposite, SWT.BORDER);
		soccerCanvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		initResources();
		soccerCanvas.addPaintListener(this);
		
		canvasUpdater = new CanvasUpdater(soccerCanvas, UPDATE_INTERVAL_MILLIS);
		canvasUpdater.start();
		
		parent.layout();
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		attributeIndexMap = createAttributeIndexMap(physicalRoots.iterator().next().getOutputSchema());

		xIndex = getAttributeIndex("x");
		yIndex = getAttributeIndex("y");
		tsIndex = getAttributeIndex("ts");

		validAttributes = (xIndex > -1 && yIndex > -1);
		if (!validAttributes) {
			LOG.error("Data stream does not have x or y coordinate");
		}
	}

	@Override
	public void dispose() {
		canvasUpdater.stopRunning();
		
		if( !soccerCanvas.isDisposed() ) {
			soccerCanvas.removePaintListener(this);
			soccerCanvas.dispose();
		}
		
		disposeResources();
	}

	@Override
	public void streamElementRecieved(IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: Soccer is only for relational tuple!");
			return;
		}
		Tuple<?> tuple = (Tuple<?>) element;

		if (attributeIndexMap.get("sid") != null) {
			currentTuples.put((Integer) tuple.getAttribute(attributeIndexMap.get("sid")), tuple);
		}
	}

	@Override
	public void punctuationElementRecieved(IPunctuation point, int port) {
	}

	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
	}

	@Override
	public void paintControl(PaintEvent e) {
		if (!validAttributes) {
			return;
		}

		GC gc = new GC(soccerCanvas);
		gc.setFont(playerIDFont);

		renderBackground(gc);
		
		for (Entry<Integer, Tuple<?>> entry : currentTuples.entrySet()) {
			Integer sid = entry.getKey();
			Tuple<?> soccerTuple = entry.getValue();
			Integer x = (Integer) soccerTuple.getAttribute(yIndex);
			Integer y = (Integer) soccerTuple.getAttribute(xIndex);

			render(gc, sid, x, y);
		}

		if (currentTuples.get(SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS) != null && tsIndex > -1) {
			renderTimeProgress(gc);
		}
		gc.dispose();
	}

	private void renderBackground(GC gc) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gc.setLineWidth(3);
		
		double xFactor = soccerCanvas.getSize().x / 3450.0; // streckung/dehnung
		double yFactor = soccerCanvas.getSize().y / 2160.0; // streckung/dehnung
		
		// side lines
		drawRectangle(gc, 150 * xFactor, 76 * yFactor, 3300 * xFactor, 2083 * yFactor);
		
		// left strafraum
		drawRectangle(gc, 150 * xFactor, 478 * yFactor, 644 * xFactor, 1675 * yFactor);
		drawRectangle(gc, 150 * xFactor, 812 * yFactor, 314 * xFactor, 1350 * yFactor);
		drawRectangle(gc, 120 * xFactor, 975 * yFactor, 150 * xFactor, 1188 * yFactor);
		
		// right
		drawRectangle(gc, 2805 * xFactor, 478 * yFactor, 3300 * xFactor, 1675 * yFactor);
		drawRectangle(gc, 3135 * xFactor, 812 * yFactor, 3300 * xFactor, 1350 * yFactor);
		drawRectangle(gc, 3300 * xFactor, 975 * yFactor, 3330 * xFactor, 1188 * yFactor);
		
		// middle
		gc.drawLine((int)(1725 * xFactor), (int)(76 * yFactor), (int)(1725 * xFactor), (int)(2083 * yFactor));
		gc.drawOval((int)(1450 * xFactor), (int)(810 * yFactor), (int)(550 * xFactor), (int)(540 * yFactor));
		
		// points
		drawPoint(gc, 1725 * xFactor, 1080 * yFactor);
		drawPoint(gc, 480 * xFactor, 1080 * yFactor);
		drawPoint(gc, 2970 * xFactor, 1080 * yFactor);
		
		// arcs
		gc.drawArc((int)(2695 * xFactor), (int)(812 * yFactor), (int)(550 * xFactor), (int)(534 * yFactor), 127, 105);
		gc.drawArc((int)(205 * xFactor), (int)(812 * yFactor), (int)(550 * xFactor), (int)(534 * yFactor), 305, 108);
	}
	
	private static void drawRectangle( GC gc, double x, double y, double x2, double y2 ) {
		gc.drawRectangle((int)(x), (int)(y), (int)(x2 - x), (int)(y2 - y));
	}
	
	private static void drawPoint( GC gc, double x, double y ) {
		gc.fillOval((int)(x - (POINT_SIZE_PIXELS / 2)), (int)(y - (POINT_SIZE_PIXELS / 2)), POINT_SIZE_PIXELS, POINT_SIZE_PIXELS);
	}

	private void renderTimeProgress(GC gc) {
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.setFont(timeFont);
		long millis = (Long.parseLong(currentTuples.get(SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS).getAttribute(tsIndex).toString()) - 10748401988186756L) / 1000000000;
		String time = String.format("%d min %d sec %d ms", TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		gc.drawText("TS: " + currentTuples.get(SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS).getAttribute(tsIndex).toString() + "    MS: " + millis + " ms" + "    " + time, 5, 5);
	}

	private Integer getAttributeIndex(String attributeName) {
		Integer index = attributeIndexMap.get(attributeName);
		return index != null ? index : -1;
	}

	private void disposeResources() {
		playerIDFont.dispose();
		timeFont.dispose();
	}

	private void initResources() {
		playerIDFont = new Font(Display.getCurrent(), "Arial", FONT_SIZE, SWT.BOLD | SWT.ITALIC);
		timeFont = new Font(Display.getCurrent(), "Arial", 9, SWT.BOLD | SWT.ITALIC);
	}

	private void render(GC gc, Integer sid, Integer x, Integer y) {
		if (SID_BALLS.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
			gc.fillOval(getCoordX(x) - (BALL_SIZE_PIXELS / 2), getCoordY(y) - (BALL_SIZE_PIXELS / 2), BALL_SIZE_PIXELS, BALL_SIZE_PIXELS);
			if (SENSOR_PLAYER_MAP.containsKey(sid)) {
				gc.drawText(SENSOR_PLAYER_MAP.get(sid).toString(), getCoordX(x) - (BALL_SIZE_PIXELS / 2) + BALL_SIZE_PIXELS, getCoordY(y) - (BALL_SIZE_PIXELS / 2), true);
			}
		} else if (SID_TEAM_A.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			gc.fillOval(getCoordX(x) - (PLAYER_SIZE_PIXELS / 2), getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), PLAYER_SIZE_PIXELS, PLAYER_SIZE_PIXELS);
			if (SENSOR_PLAYER_MAP.containsKey(sid)) {
				gc.drawText(SENSOR_PLAYER_MAP.get(sid).toString(), getCoordX(x) - (PLAYER_SIZE_PIXELS / 2) + PLAYER_SIZE_PIXELS, getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), true);
			}
		} else if (SID_TEAM_B.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			gc.fillOval(getCoordX(x) - (PLAYER_SIZE_PIXELS / 2), getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), PLAYER_SIZE_PIXELS, PLAYER_SIZE_PIXELS);
			if (SENSOR_PLAYER_MAP.containsKey(sid)) {
				gc.drawText(SENSOR_PLAYER_MAP.get(sid).toString(), getCoordX(x) - (PLAYER_SIZE_PIXELS / 2) + PLAYER_SIZE_PIXELS, getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), true);
			}
		} else if (SID_REFEREE.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			gc.fillOval(getCoordX(x) - (PLAYER_SIZE_PIXELS / 2), getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), PLAYER_SIZE_PIXELS, PLAYER_SIZE_PIXELS);
		}
	}
	
	private int getCoordX( int absX ) {
		int old = getCoordXImpl(absX);
		double factor = soccerCanvas.getSize().x / 862.0; // streckung/dehnung
		return (int)(old * factor);
	}
	
	private int getCoordY( int absY ) {
		int old = getCoordYImpl(absY);
		double factor = soccerCanvas.getSize().y / 532.0; // streckung/dehnung
		return (int)(old * factor);
	}

	private static int getCoordXImpl(int absX) {
		return (int) (((absX + 33960) / 67920f) * 790) + 36;
	}

	private static int getCoordYImpl(int absY) {
		return (int) ((absY / 52489f) * 506) + 15;
	}

	private static Map<String, Integer> createAttributeIndexMap(SDFSchema schema) {
		Map<String, Integer> attributeIndexMap = Maps.newHashMap();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			attributeIndexMap.put(schema.getAttribute(i).getAttributeName(), i);
		}
		return attributeIndexMap;
	}
}