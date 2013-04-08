package de.uniol.inf.is.odysseus.debs2013.viewer;

import java.util.List;
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

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class HeatmapView extends AbstractSoccerView implements IStreamEditorType{
	private static final Logger LOG = LoggerFactory.getLogger(HeatmapView.class);
	private SDFSchema schema;
	
	final int showHeatmapOfPlayerId = 15;
	
	private Tuple<?> currentTuple;
//	private ConcurrentHashMap<int[], Double> cellValues; 
	private ConcurrentHashMap<Integer, int[]> cellCoordinates;
	private ConcurrentHashMap<Integer, Double> cellValues;
	private ConcurrentHashMap<Integer, Color> colorMap;
	
	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: Soccer is only for relational tuple!");
			return;
		}
		
		
		if(		attributeIndexMap.get("ts")!=null &&
				attributeIndexMap.get("player_id")!=null&&
				attributeIndexMap.get("cell_x1")!=null &&
				attributeIndexMap.get("cell_y1")!=null &&
				attributeIndexMap.get("cell_x2")!=null &&
				attributeIndexMap.get("cell_y2")!=null){
			
			currentTuple = (Tuple<?>) element;
			
			int[] tempArray = {		(int)currentTuple.getAttribute(attributeIndexMap.get("cell_x1")),
									(int)currentTuple.getAttribute(attributeIndexMap.get("cell_y1")),
									(int)currentTuple.getAttribute(attributeIndexMap.get("cell_x2")),
									(int)currentTuple.getAttribute(attributeIndexMap.get("cell_y2"))};
			
//			cellValues.put(tempArray, (Double)currentTuple.getAttribute(attributeIndexMap.get("percent_time_in_time_cell")));
			
			int hash = getCellHashCode(tempArray);
			cellCoordinates.put(hash, tempArray);
			cellValues.put(hash, (Double)currentTuple.getAttribute(attributeIndexMap.get("percent_time_in_time_cell")));

//			currentTuple.put((Integer)tuple.getAttribute(attributeIndexMap.get("sid")), tuple);
		}
		
	}
	@Override
	public void punctuationElementRecieved(IPunctuation point, int port) {}
	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp,
			int port) {}
	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		LOG.info("----------- HeatmapView opened -----------");
		
		
		List<ISubscription<? extends ISource<Object>>> subs = editorInput.getStreamConnection().getSubscriptions();
		setSchema(subs.get(0).getSchema());
		
		attributeIndexMap = new ConcurrentHashMap<>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			attributeIndexMap.put(schema.getAttribute(i).getAttributeName(), i);
		}
		
		
		
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			LOG.info(schema.getAttribute(i).getAttributeName() + "  "+schema.getAttribute(i).getDatatype().getQualName());
		}
		
		initMetadata();
		
		cellCoordinates = new ConcurrentHashMap<Integer, int[]>();
		cellValues = new ConcurrentHashMap<Integer, Double>();
		colorMap = new ConcurrentHashMap<Integer, Color>();
		
//		RED -> YELLOW -> GREEN
		colorMap.put(0, new Color(Display.getDefault(), 165,0,38));
		colorMap.put(1, new Color(Display.getDefault(), 215,48,39));
		colorMap.put(2, new Color(Display.getDefault(), 244,109,67));
		colorMap.put(3, new Color(Display.getDefault(), 253,174,97));
		colorMap.put(4, new Color(Display.getDefault(), 254,224,139));
		colorMap.put(5, new Color(Display.getDefault(), 255,255,191));
		colorMap.put(6, new Color(Display.getDefault(), 217,239,139));
		colorMap.put(7, new Color(Display.getDefault(), 166,217,106));
		colorMap.put(8, new Color(Display.getDefault(), 102,189,99));
		colorMap.put(9, new Color(Display.getDefault(), 26,152,80));
		colorMap.put(10, new Color(Display.getDefault(), 0,104,55));
	}
	@Override
	public void initToolbar(ToolBar toolbar) {}
	@Override
	public void createPartControl(Composite parent) {
		super.initView(parent);
		soccerFieldDraw.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				  GC gc = new GC(soccerFieldDraw);
				  Font fontTime = new Font(e.display,"Arial", 9, SWT.BOLD | SWT.ITALIC);

				  
//				  for(Entry<Integer, Double > entry : cellValues.entrySet()) {
//					    int hash = entry.getKey();
//					    double cell = entry.getValue();
//				  }
				  
				  
				  for(Entry<Integer, int[] > entry : cellCoordinates.entrySet()) {
					    int hash = entry.getKey();
					    int[] cell = entry.getValue();
					    gc.setAlpha(220);
					    gc.setBackground(getColorForPercent(cellValues.get(hash)));
				    	gc.fillRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1])-getCoordX(cell[3]), getCoordY(cell[2])-getCoordY(cell[0]));
				    	gc.setAlpha(255);
				    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				    	gc.drawRectangle(getCoordX(cell[3]), getCoordY(cell[0]), getCoordX(cell[1])-getCoordX(cell[3]), getCoordY(cell[2])-getCoordY(cell[0]));
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
					  gc.drawText("TS: "+currentTuple.getAttribute(attributeIndexMap.get("ts")).toString()+"    MS: "+millis+" ms"+"    "+time+"\nPlayerId: "+showHeatmapOfPlayerId+"\nWindow: Full Game", 5, 5);
					  
					  
				  }
				  
				  fontTime.dispose();
				  gc.dispose();
			}
		});
		
		SoccerFieldViewUpdater up = new SoccerFieldViewUpdater(soccerFieldDraw);
		up.schedule(1000);
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
		if(percent==0){
			return colorMap.get(0);
		}else if(percent<2){
			return colorMap.get(1);
		}else if(percent<4){
			return colorMap.get(2);
		}else if(percent<6){
			return colorMap.get(3);
		}else if(percent<8){
			return colorMap.get(4);
		}else if(percent<10){
			return colorMap.get(5);
		}else if(percent<14){
			return colorMap.get(6);
		}else if(percent<18){
			return colorMap.get(7);
		}else if(percent<22){
			return colorMap.get(8);
		}else if(percent<26){
			return colorMap.get(9);
		}else{
			return colorMap.get(10);
		}
	}
}

