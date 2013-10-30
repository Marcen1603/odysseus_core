package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class HeatmapDashboardPart extends AbstractSoccerDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(HeatmapDashboardPart.class);
	
	private int tsIndex;
	private int playerIDIndex;
	private int firstXValueIndex;
	
	private boolean validAttributes;

	private final ConcurrentHashMap<Integer, int[]> cellCoordinates = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer, Double> cellValues = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer, Color> colorMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, Double> colorValueMap = new ConcurrentHashMap<>();
	private Tuple<?> lastReceivedTuple;

	public HeatmapDashboardPart() {
		colorMap.put(1, new Color(Display.getDefault(), 255,255,178));
		colorMap.put(2, new Color(Display.getDefault(), 254,204,92));
		colorMap.put(3, new Color(Display.getDefault(), 253,141,60));
		colorMap.put(4, new Color(Display.getDefault(), 240,59,32));
	}
	
	@Override
	public void init(IFile dashboardFile, IProject containingProject, IWorkbenchPart containingPart) {
		super.init(dashboardFile, containingProject, containingPart);
		
	}
	
	@Override
	public void onLoad(Map<String, String> saved) {
		super.onLoad(saved);
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
		
		Tuple<?> currentTuple = (Tuple<?>) element;
		Integer playerID = (Integer)currentTuple.getAttribute(playerIDIndex);
		if (validAttributes && playerID == 16) {
			
			for (int i = firstXValueIndex; i < (currentTuple.getAttributes().length - 2); i += 5) {
				
				int[] tempArray = { (int) currentTuple.getAttribute(i), (int) currentTuple.getAttribute(i + 1), (int) currentTuple.getAttribute(i + 2), (int) currentTuple.getAttribute(i + 3) };
				int hash = getCellHashCode(tempArray);
				
				cellCoordinates.put(hash, tempArray);
				cellValues.put(hash, (Double) currentTuple.getAttribute(i + 4));
			}
			
			lastReceivedTuple = currentTuple;
		}
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

		recalculateColorMap(cellValues.values());
		
		renderBackground(gc);
		
		for (Entry<Integer, int[]> entry : cellCoordinates.entrySet()) {
			int hash = entry.getKey();
			int[] cell = entry.getValue();
			Double percent = cellValues.get(hash);
			if (percent > 0.0) {
				gc.setAlpha(220);
				gc.setBackground(getColorForPercent(cellValues.get(hash)));
				gc.fillRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1]) - getCoordX(cell[3]), getCoordY(cell[2]) - getCoordY(cell[0]));
				gc.setAlpha(255);
				gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				gc.drawRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1]) - getCoordX(cell[3]), getCoordY(cell[2]) - getCoordY(cell[0]));
			}
		}

		if (lastReceivedTuple != null) {
			
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setFont(getFont());
			
			long millis = (Long.parseLong(lastReceivedTuple.getAttribute(tsIndex).toString()) - 10748401988186756L) / 1000000000;
			String time = String.format("%d min %d sec %d ms", TimeUnit.MILLISECONDS.toMinutes(millis),
					TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
					millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
			gc.drawText("TS: " + lastReceivedTuple.getAttribute(tsIndex).toString() + "    MS: " + millis + " ms" + "    " + time + "\nPlayerId: " + lastReceivedTuple.getAttribute(playerIDIndex), 5,
					5);
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
		return super.onSave();
	}
	
	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
		// do nothing
	}

	@Override
	public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
		// do nothing
	}
}
