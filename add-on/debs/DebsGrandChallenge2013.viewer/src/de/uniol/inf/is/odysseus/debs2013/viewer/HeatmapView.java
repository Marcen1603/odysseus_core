package de.uniol.inf.is.odysseus.debs2013.viewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class HeatmapView extends AbstractSoccerView implements IStreamEditorType{
	private static final Logger LOG = LoggerFactory.getLogger(HeatmapView.class);
	private SDFSchema schema;

	private int firstXValue;

	private Tuple<?> currentTuple;
	private ConcurrentHashMap<Integer, int[]> cellCoordinates;
	private ConcurrentHashMap<Integer, Double> cellValues;
	private ConcurrentHashMap<Integer, Color> colorMap;
	private ConcurrentHashMap<Integer, Double> colorValueMap;

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: Soccer is only for relational tuple!");
			return;
		}

		if(		attributeIndexMap.get("ts")!=null &&
				attributeIndexMap.get("player_id")!=null&&
				attributeIndexMap.get("firstXValue")!=null){

			currentTuple = (Tuple<?>) element;
			for(int i=firstXValue;i<(currentTuple.getAttributes().length-2);i = i+5){
				int[]tempArray = {		(int)currentTuple.getAttribute(i),
										(int)currentTuple.getAttribute(i+1),
										(int)currentTuple.getAttribute(i+2),
										(int)currentTuple.getAttribute(i+3)};

				int hash = getCellHashCode(tempArray);
				cellCoordinates.put(hash, tempArray);
				cellValues.put(hash, (Double)currentTuple.getAttribute(i+4));
			}
		}

	}
	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation point, int port) {}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		LOG.info("----------- HeatmapView opened -----------");


		setSchema(editorInput.getStreamConnection().getOutputSchema());

		attributeIndexMap = new ConcurrentHashMap<>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			if(schema.getAttribute(i).getAttributeName().contains("ts")){
				attributeIndexMap.put("ts", i);
			}else if(schema.getAttribute(i).getAttributeName().contains("player_id")){
				attributeIndexMap.put("player_id", i);
			}else if(schema.getAttribute(i).getAttributeName().contains("_x1")){
				firstXValue = i;
				attributeIndexMap.put("firstXValue", i);
				break;
			}
		}

		for (int i = 0; i < schema.getAttributes().size(); i++) {
			LOG.info(schema.getAttribute(i).getAttributeName() + "  "+schema.getAttribute(i).getDatatype().getQualName());
		}

		initMetadata();

		cellCoordinates = new ConcurrentHashMap<Integer, int[]>();
		cellValues = new ConcurrentHashMap<Integer, Double>();
		colorMap = new ConcurrentHashMap<Integer, Color>();

//		YELLOW -> RED
		colorMap.put(1, new Color(Display.getDefault(), 255,255,178));
		colorMap.put(2, new Color(Display.getDefault(), 254,204,92));
		colorMap.put(3, new Color(Display.getDefault(), 253,141,60));
		colorMap.put(4, new Color(Display.getDefault(), 240,59,32));
	}
	@Override
	public void initToolbar(ToolBar toolbar) {}
	@Override
	public void createPartControl(Composite parent) {
		super.initView(parent);
		soccerFieldDraw.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				  GC gc = new GC(soccerFieldDraw);
				  Font fontTime = new Font(e.display,"Arial", 9, SWT.BOLD | SWT.ITALIC);

				  //Update (value->color)-mapping
				  initializeCellValues(cellValues.values());

				  for(Entry<Integer, int[] > entry : cellCoordinates.entrySet()) {
					    int hash = entry.getKey();
					    int[] cell = entry.getValue();
					    Double percent = cellValues.get(hash);
					    if(percent>0.0){
					    	gc.setAlpha(220);
						    gc.setBackground(getColorForPercent(cellValues.get(hash)));
					    	gc.fillRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1])-getCoordX(cell[3]), getCoordY(cell[2])-getCoordY(cell[0]));
					    	gc.setAlpha(255);
					    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					    	gc.drawRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1])-getCoordX(cell[3]), getCoordY(cell[2])-getCoordY(cell[0]));
					    }
				  }

				  if(currentTuple!=null){
					  gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					  gc.setFont(fontTime);
					  long millis = (Long.parseLong(currentTuple.getAttribute(attributeIndexMap.get("ts")).toString())-10748401988186756L)/1000000000;
					  String time = String.format("%d min %d sec %d ms",
							    TimeUnit.MILLISECONDS.toMinutes(millis),
							    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
							    millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))
							);
					  gc.drawText("TS: "+currentTuple.getAttribute(attributeIndexMap.get("ts")).toString()+"    MS: "+millis+" ms"+"    "+time+"\nPlayerId: "+currentTuple.getAttribute(attributeIndexMap.get("player_id")), 5, 5);
				  }

				  fontTime.dispose();
				  gc.dispose();
			}
		});

		SoccerFieldViewUpdater up = new SoccerFieldViewUpdater(soccerFieldDraw);
		up.schedule(1000);
	}
	protected void initializeCellValues(Collection<Double> values) {
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
	@Override
	public void setFocus() {
		if(soccerViewer!=null){
			soccerViewer.setFocus();
		}
	}
	@Override
	public void dispose() {
		soccerFieldDraw.dispose();
		LOG.info("----------- HeatmapView closed-----------");
	}

	private void setSchema(SDFSchema schema) {
		this.schema = schema; // kann auch null sein!
	}

	class SoccerFieldViewUpdater implements Runnable{
	    private Canvas canvas;
	    private int milliseconds;

	    public SoccerFieldViewUpdater(Canvas canvas) {
	        super();
	        this.canvas = canvas;
	    }

	    public void schedule(int milliseconds){
	        this.milliseconds = milliseconds;
	        canvas.getDisplay().timerExec(milliseconds, this);
	    }

	    @Override
	    public void run() {
	    	if(!canvas.isDisposed()){
		        canvas.redraw();
		        canvas.getDisplay().timerExec(milliseconds, this);
	    	}
	    }
	}
	private int getCellHashCode(int[] array){
		String temp = "";
		for(int i=0;i<array.length;i++){
			temp += array[i];
		}
		return temp.hashCode();
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
}

