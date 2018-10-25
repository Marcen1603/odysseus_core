package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public class HeatmapDashboardPart extends AbstractSoccerDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(HeatmapDashboardPart.class);

	private int tsIndex;
	private int playerIDIndex;
	private int firstXValueIndex;

	private boolean validAttributes;

	private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, int[]>> cellCoordinates = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Double>> cellValues = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer, Color> colorMap = new ConcurrentHashMap<>();

	private ConcurrentHashMap<Integer, Double> colorValueMap = new ConcurrentHashMap<>();
	private Tuple<?> lastReceivedTuple;

	private String selectedPlayerIDString = "16";
	private int selectedPlayerID = 16;

	public HeatmapDashboardPart() {
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		String selPlayerID = saved.get("selectedPlayerID");

		colorMap.put(1, new Color(PlatformUI.getWorkbench().getDisplay(), 255,255,178));
		colorMap.put(2, new Color(PlatformUI.getWorkbench().getDisplay(), 254,204,92));
		colorMap.put(3, new Color(PlatformUI.getWorkbench().getDisplay(), 253,141,60));
		colorMap.put(4, new Color(PlatformUI.getWorkbench().getDisplay(), 240,59,32));
		selectedPlayerIDString = Strings.isNullOrEmpty(selPlayerID) ? "16" : selPlayerID;
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		tsIndex = getAttributeIndex("ts");
		playerIDIndex = getAttributeIndex("player_id");
		firstXValueIndex = 2; // getAttributeIndex("firstXValue"); why?

		validAttributes = tsIndex != -1 && playerIDIndex != -1 && firstXValueIndex != -1;
		if( !validAttributes ) {
			LOG.error("Attributes for heatmap are not valid");
		}
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: Soccer is only for relational tuple!");
			return;
		}

		if (validAttributes) {

			Tuple<?> currentTuple = (Tuple<?>) element;
			Integer playerID = (Integer)currentTuple.getAttribute(playerIDIndex);
			ConcurrentHashMap<Integer, Double> values = getCellValuesOfPlayer(playerID);
			ConcurrentHashMap<Integer, int[]> coordinates = getCellCoordinatesOfPlayer(playerID);

			for (int i = firstXValueIndex; i < (currentTuple.getAttributes().length - 2); i += 5) {

				int[] tempArray = { (int) currentTuple.getAttribute(i), (int) currentTuple.getAttribute(i + 1), (int) currentTuple.getAttribute(i + 2), (int) currentTuple.getAttribute(i + 3) };
				int hash = getCellHashCode(tempArray);

				coordinates.put(hash, tempArray);
				values.put(hash, (Double) currentTuple.getAttribute(i + 4));
			}

			lastReceivedTuple = currentTuple;
		}
	}

	private ConcurrentHashMap<Integer, Double> getCellValuesOfPlayer(Integer playerID) {
		if( cellValues.containsKey(playerID)) {
			return cellValues.get(playerID);
		}

		ConcurrentHashMap<Integer, Double> values = new ConcurrentHashMap<>();
		cellValues.put(playerID, values);
		return values;
	}

	private ConcurrentHashMap<Integer, int[]> getCellCoordinatesOfPlayer(Integer playerID) {
		if( cellCoordinates.containsKey(playerID)) {
			return cellCoordinates.get(playerID);
		}

		ConcurrentHashMap<Integer, int[]> values = new ConcurrentHashMap<>();
		cellCoordinates.put(playerID, values);
		return values;
	}

	private static int getCellHashCode(int[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
		}
		return sb.toString().hashCode();
	}

	@Override
	public void paintControl(PaintEvent e) {
		GC gc = new GC(getCanvas());

		ConcurrentHashMap<Integer, Double> values = getCellValuesOfPlayer(selectedPlayerID);
		ConcurrentHashMap<Integer, int[]> coordinates = getCellCoordinatesOfPlayer(selectedPlayerID);

		recalculateColorMap(values.values());

		renderBackground(gc);

		for (Entry<Integer, int[]> entry : coordinates.entrySet()) {
			int hash = entry.getKey();
			int[] cell = entry.getValue();
			Double percent = values.get(hash);
			if (percent > 0.0) {
				gc.setAlpha(220);
				gc.setBackground(getColorForPercent(values.get(hash)));
				gc.fillRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1]) - getCoordX(cell[3]), getCoordY(cell[2]) - getCoordY(cell[0]));
				gc.setAlpha(255);
				gc.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_BLACK));
				gc.drawRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1]) - getCoordX(cell[3]), getCoordY(cell[2]) - getCoordY(cell[0]));
			}
		}

		if (lastReceivedTuple != null) {

			gc.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			gc.setForeground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_BLACK));
			gc.setFont(getFont());

			long millis = (Long.parseLong(lastReceivedTuple.getAttribute(tsIndex).toString()) - 10748401988186756L) / 1000000000;
			String time = String.format("%d min %d sec %d ms", TimeUnit.MILLISECONDS.toMinutes(millis),
					TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
					millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
			gc.drawText("Time: " + time + "\nHeatmap of playerID " + selectedPlayerIDString, 5, 5);
		}

		gc.dispose();
	}

	private void recalculateColorMap(Collection<Double> values) {
		ArrayList<Double> list = new ArrayList<>();
		for (Double value : values) {
			if(value>0.0){
				list.add(value);
			}
		}
		Collections.sort(list);
		colorValueMap = new ConcurrentHashMap<>();
		if(list.size()>0){
			colorValueMap.put(1, list.get((int)Math.ceil((list.size()*0.4))-1));
			colorValueMap.put(2, list.get((int)Math.ceil((list.size()*0.7))-1));
			colorValueMap.put(3, list.get((int)Math.ceil((list.size()*0.9))-1));
		}
	}

	private Color getColorForPercent(Double percent){
		if(percent<colorValueMap.get(1)){
			return colorMap.get(1);
		}else if(percent<colorValueMap.get(2)){
			return colorMap.get(2);
		}else if(percent<colorValueMap.get(3)){
			return colorMap.get(3);
		}else{
			return colorMap.get(4);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> map = Maps.newHashMap();
		map.put("selectedPlayerID", selectedPlayerIDString);
		return map;
	}

	public void setSelectedPlayerID( String playerIDString ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(playerIDString));

		if( !playerIDString.equals(selectedPlayerIDString) ) {
			selectedPlayerIDString = playerIDString;
			selectedPlayerID = tryParseToInteger(playerIDString);

			fireChangeEvent();
		}
	}

	public String getSelectedPlayerID() {
		return selectedPlayerIDString;
	}

	private static int tryParseToInteger(String text) {
		try {
			return Integer.parseInt(text);
		} catch( Throwable t ) {
			return 1;
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
		// do nothing
	}
}
