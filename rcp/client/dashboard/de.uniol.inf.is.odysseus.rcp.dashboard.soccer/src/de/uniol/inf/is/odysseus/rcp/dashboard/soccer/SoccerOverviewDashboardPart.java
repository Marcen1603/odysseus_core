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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public class SoccerOverviewDashboardPart extends AbstractSoccerDashboardPart implements PaintListener {

	private static final Logger LOG = LoggerFactory.getLogger(SoccerOverviewDashboardPart.class);

	private static final List<Integer> SID_BALLS = SoccerMetadataProvider.getSidBalls();
	private static final List<Integer> SID_TEAM_A = SoccerMetadataProvider.getSidTeamA();
	private static final List<Integer> SID_TEAM_B = SoccerMetadataProvider.getSidTeamB();
	private static final List<Integer> SID_REFEREE = SoccerMetadataProvider.getSidReferee();
	private static final Map<Integer, Integer> SENSOR_PLAYER_MAP = SoccerMetadataProvider.getSensorPlayerMap();

	private static final int PLAYER_SIZE_PIXELS = 6;
	private static final int BALL_SIZE_PIXELS = 10;
	private static final int SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS = 13;

	private final Map<Integer, Tuple<?>> currentTuples = Maps.newConcurrentMap();
	private boolean validAttributes;

	private boolean showNumbers = true;
	private boolean showPlayersA = true;
	private boolean showPlayersB = true;
	private boolean showBall = true;
	private boolean showReferee = true;
	private boolean showField = true;
	private boolean showTime = true;

	private String xPosAttributeName = "x";
	private String yPosAttributeName = "y";
	private String timestampAttributeName = "ts";
	private String sidAttributeName = "sid";

	private int xIndex;
	private int yIndex;
	private int tsIndex;
	private int sidIndex;

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		xIndex = getAttributeIndex(xPosAttributeName);
		yIndex = getAttributeIndex(yPosAttributeName);
		tsIndex = getAttributeIndex(timestampAttributeName);
		sidIndex = getAttributeIndex(sidAttributeName);

		validAttributes = (xIndex > -1 && yIndex > -1);
		if (!validAttributes) {
			LOG.error("Data stream does not have x or y coordinate");
		}
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: Soccer is only for relational tuple!");
			return;
		}
		Tuple<?> tuple = (Tuple<?>) element;

		if (getAttributeIndex("sid") != null) {
			currentTuples.put((Integer) tuple.getAttribute(sidIndex), tuple);
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
		// do nothing
	}

	@Override
	public void paintControl(PaintEvent e) {
		if (!validAttributes) {
			return;
		}

		GC gc = new GC(getCanvas());
		gc.setFont(getPlayerFont());

		if( showField ) {
			renderBackground(gc);
		}

		for (Entry<Integer, Tuple<?>> entry : currentTuples.entrySet()) {
			Integer sid = entry.getKey();
			Tuple<?> soccerTuple = entry.getValue();
			Integer x = (Integer) soccerTuple.getAttribute(yIndex);
			Integer y = (Integer) soccerTuple.getAttribute(xIndex);

			render(gc, sid, x, y);
		}

		if (showTime && currentTuples.get(SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS) != null && tsIndex > -1) {
			renderTimeProgress(gc);
		}
		gc.dispose();
	}

	private void renderTimeProgress(GC gc) {
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.setFont(getFont());
		long millis = (Long.parseLong(currentTuples.get(SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS).getAttribute(tsIndex).toString()) - 10748401988186756L) / 1000000000;
		String time = String.format("%d min %d sec %d ms", TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		gc.drawText("TS: " + currentTuples.get(SENSOR_ID_TO_RECOGNIZE_TIME_PROGRESS).getAttribute(tsIndex).toString() + "    MS: " + millis + " ms" + "    " + time, 5, 5);
	}

	private void render(GC gc, Integer sid, Integer x, Integer y) {
		if (showBall && SID_BALLS.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
			gc.fillOval(getCoordX(x) - (BALL_SIZE_PIXELS / 2), getCoordY(y) - (BALL_SIZE_PIXELS / 2), BALL_SIZE_PIXELS, BALL_SIZE_PIXELS);
			if (showNumbers && SENSOR_PLAYER_MAP.containsKey(sid)) {
				gc.drawText(SENSOR_PLAYER_MAP.get(sid).toString(), getCoordX(x) - (BALL_SIZE_PIXELS / 2) + BALL_SIZE_PIXELS, getCoordY(y) - (BALL_SIZE_PIXELS / 2), true);
			}
		} else if (showPlayersA && SID_TEAM_A.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			gc.fillOval(getCoordX(x) - (PLAYER_SIZE_PIXELS / 2), getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), PLAYER_SIZE_PIXELS, PLAYER_SIZE_PIXELS);
			if (showNumbers && SENSOR_PLAYER_MAP.containsKey(sid)) {
				gc.drawText(SENSOR_PLAYER_MAP.get(sid).toString(), getCoordX(x) - (PLAYER_SIZE_PIXELS / 2) + PLAYER_SIZE_PIXELS, getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), true);
			}
		} else if (showPlayersB && SID_TEAM_B.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			gc.fillOval(getCoordX(x) - (PLAYER_SIZE_PIXELS / 2), getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), PLAYER_SIZE_PIXELS, PLAYER_SIZE_PIXELS);
			if (showNumbers && SENSOR_PLAYER_MAP.containsKey(sid)) {
				gc.drawText(SENSOR_PLAYER_MAP.get(sid).toString(), getCoordX(x) - (PLAYER_SIZE_PIXELS / 2) + PLAYER_SIZE_PIXELS, getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), true);
			}
		} else if (showReferee && SID_REFEREE.contains(sid)) {
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			gc.fillOval(getCoordX(x) - (PLAYER_SIZE_PIXELS / 2), getCoordY(y) - (PLAYER_SIZE_PIXELS / 2), PLAYER_SIZE_PIXELS, PLAYER_SIZE_PIXELS);
		}
	}

	public boolean isShowNumbers() {
		return showNumbers;
	}

	public void setShowNumbers(boolean isShowNumbers) {
		showNumbers = isShowNumbers;
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> config = Maps.newHashMap();
		config.put("showNumbers", String.valueOf(showNumbers));
		config.put("showPlayersA", String.valueOf(showPlayersA));
		config.put("showPlayersB", String.valueOf(showPlayersB));
		config.put("showBall", String.valueOf(showBall));
		config.put("showReferee", String.valueOf(showReferee));
		config.put("showField", String.valueOf(showField));
		config.put("showTime", String.valueOf(showTime));
		config.put("xPosAttributeName", xPosAttributeName);
		config.put("yPosAttributeName", yPosAttributeName);
		config.put("timestampAttributeName", timestampAttributeName);
		config.put("sidAttributeName", sidAttributeName);
		return config;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		showNumbers = Boolean.valueOf(getValue(saved, "showNumbers", "true"));
		showPlayersA = Boolean.valueOf(getValue(saved, "showPlayersA", "true"));
		showPlayersB = Boolean.valueOf(getValue(saved, "showPlayersB", "true"));
		showBall = Boolean.valueOf(getValue(saved, "showBall", "true"));
		showReferee = Boolean.valueOf(getValue(saved, "showReferee", "true"));
		showField = Boolean.valueOf(getValue(saved, "showField", "true"));
		showTime = Boolean.valueOf(getValue(saved, "showTime", "true"));

		xPosAttributeName = (getValue(saved, "xPosAttributeName", "x"));
		yPosAttributeName = (getValue(saved, "yPosAttributeName", "y"));
		timestampAttributeName = (getValue(saved, "timestampAttributeName", "ts"));
		sidAttributeName = (getValue(saved, "sidAttributeName", "sid"));
	}

	private static String getValue( Map<String, String> map, String key, String defaultValue ) {
		if( map.containsKey(key)) {
			return map.get(key);
		}

		return defaultValue;
	}

	public boolean isShowPlayersA() {
		return showPlayersA;
	}

	public void setShowPlayersA(boolean selection) {
		showPlayersA = selection;
	}

	public boolean isShowPlayersB() {
		return showPlayersB;
	}

	public void setShowPlayersB(boolean selection) {
		showPlayersB = selection;
	}

	public boolean isShowBall() {
		return showBall;
	}

	public void setShowBall(boolean showBall) {
		this.showBall = showBall;
	}

	public boolean isShowReferee() {
		return showReferee;
	}

	public void setShowReferee(boolean showReferee) {
		this.showReferee = showReferee;
	}

	public boolean isShowField() {
		return showField;
	}

	public void setShowField(boolean showField) {
		this.showField = showField;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	public String getXPosAttributeName() {
		return xPosAttributeName;
	}

	public void setXPosAttributeName(String xPosAttributeName) {
		this.xPosAttributeName = xPosAttributeName;
		xIndex = getAttributeIndex(xPosAttributeName);
	}

	public String getYPosAttributeName() {
		return yPosAttributeName;
	}

	public void setYPosAttributeName(String yPosAttributeName) {
		this.yPosAttributeName = yPosAttributeName;
		yIndex = getAttributeIndex(yPosAttributeName);
	}

	public String getTimestampAttributeName() {
		return timestampAttributeName;
	}

	public void setTimestampAttributeName(String timestampAttributeName) {
		this.timestampAttributeName = timestampAttributeName;
		tsIndex = getAttributeIndex(timestampAttributeName);
	}

	public String getSIDAttributeName() {
		return sidAttributeName;
	}

	public void setSIDAttributeName(String sidAttributeName) {
		this.sidAttributeName = sidAttributeName;
		sidIndex = getAttributeIndex(sidAttributeName);
	}

}