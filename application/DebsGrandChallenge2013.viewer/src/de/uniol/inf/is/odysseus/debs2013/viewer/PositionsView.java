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
package de.uniol.inf.is.odysseus.debs2013.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.debs2013.viewer.activator.ViewerStreamSoccerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class PositionsView implements IStreamEditorType{
	private static final Logger LOG = LoggerFactory.getLogger(PositionsView.class);
	private SDFSchema schema;
	
	private Composite soccerViewer;
	
	private ConcurrentHashMap<String, Integer> attributeIndexMap;
	
	private ConcurrentHashMap<Integer, Tuple<?>> currentTuple;
	
	
	private Canvas soccerFieldDraw;
	Label soccerFieldOutline;
	
	Runnable runnable;
	
	final int width = 862;
	final int height = 532;
	
	final int playerSize = 6;
	final int refereeSize = 6;
	final int ballSize = 10;
	final int fontSize = 7;
	
	final int sensorIdToRecognizeTimeProgress = 13;
	
	private ArrayList<Integer> sidBalls;
	private ArrayList<Integer> sidTeamA;
	private ArrayList<Integer> sidTeamB;
	private ArrayList<Integer> sidReferee;
	private HashMap<Integer, Integer> sensorIdToPlayerId;
	
	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: Soccer is only for relational tuple!");
			return;
		}
		Tuple<?> tuple = (Tuple<?>) element;
		
		if(attributeIndexMap.get("sid")!=null){
			currentTuple.put((Integer)tuple.getAttribute(attributeIndexMap.get("sid")), tuple);
		}
//		LOG.info(tuple.getAttribute(1).toString());
		
	}

	@Override
	public void punctuationElementRecieved(IPunctuation point, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp,
			int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		LOG.info("----------- SoccerView opened -----------");
		
		
		List<ISubscription<? extends ISource<Object>>> subs = editorInput.getStreamConnection().getSubscriptions();
		// TODO: Adapt to multiple sources
		setSchema(subs.get(0).getSchema());
//		editor = editorPart;
		
		attributeIndexMap = new ConcurrentHashMap<>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			attributeIndexMap.put(schema.getAttribute(i).getAttributeName(), i);
		}
		
		currentTuple = new ConcurrentHashMap<>();
		
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			LOG.info(schema.getAttribute(i).getAttributeName() + "  "+schema.getAttribute(i).getDatatype().getQualName());
		}
		
		initMetadata();
		
	}

	private void initMetadata() {
		sidBalls = new ArrayList<>();
		sidBalls.add(4);
		sidBalls.add(8);
		sidBalls.add(10);
		sidBalls.add(12);
		
		sidTeamA = new ArrayList<>();
		sidTeamA.add(13);
		sidTeamA.add(14);
		sidTeamA.add(97);
		sidTeamA.add(98);
		sidTeamA.add(47);
		sidTeamA.add(16);
		sidTeamA.add(49);
		sidTeamA.add(88);
		sidTeamA.add(19);
		sidTeamA.add(52);
		sidTeamA.add(53);
		sidTeamA.add(54);
		sidTeamA.add(23);
		sidTeamA.add(24);
		sidTeamA.add(57);
		sidTeamA.add(58);
		sidTeamA.add(59);
		sidTeamA.add(28);
		
		sidTeamB = new ArrayList<>();
		sidTeamB.add(61);
		sidTeamB.add(62);
		sidTeamB.add(99);
		sidTeamB.add(100);
		sidTeamB.add(63);
		sidTeamB.add(64);
		sidTeamB.add(65);
		sidTeamB.add(66);
		sidTeamB.add(67);
		sidTeamB.add(68);
		sidTeamB.add(69);
		sidTeamB.add(38);
		sidTeamB.add(71);
		sidTeamB.add(40);
		sidTeamB.add(73);
		sidTeamB.add(74);
		sidTeamB.add(75);
		sidTeamB.add(44);
		
		sidReferee = new ArrayList<>();
		sidReferee.add(105);
		sidReferee.add(106);
		
		sensorIdToPlayerId = new HashMap<Integer, Integer>();
		sensorIdToPlayerId.put(13, 1);
//		sensorIdToPlayerId.put(14, 1);
		sensorIdToPlayerId.put(47, 2);
//		sensorIdToPlayerId.put(16, 2);
		sensorIdToPlayerId.put(49, 3);
//		sensorIdToPlayerId.put(88, 3);
		sensorIdToPlayerId.put(19, 4);
//		sensorIdToPlayerId.put(52, 4);
		sensorIdToPlayerId.put(53, 5);
//		sensorIdToPlayerId.put(54, 5);
		sensorIdToPlayerId.put(23, 6);
//		sensorIdToPlayerId.put(24, 6);
		sensorIdToPlayerId.put(57, 7);
//		sensorIdToPlayerId.put(58, 7);
		sensorIdToPlayerId.put(59, 8);
//		sensorIdToPlayerId.put(28, 8);
		
		sensorIdToPlayerId.put(61, 11);
//		sensorIdToPlayerId.put(62, 11);
		sensorIdToPlayerId.put(63, 12);
//		sensorIdToPlayerId.put(64, 12);
		sensorIdToPlayerId.put(65, 13);
//		sensorIdToPlayerId.put(66, 13);
		sensorIdToPlayerId.put(67, 14);
//		sensorIdToPlayerId.put(68, 14);
		sensorIdToPlayerId.put(69, 15);
//		sensorIdToPlayerId.put(38, 15);
		sensorIdToPlayerId.put(71, 16);
//		sensorIdToPlayerId.put(40, 16);
		sensorIdToPlayerId.put(73, 17);
//		sensorIdToPlayerId.put(74, 17);
		sensorIdToPlayerId.put(75, 18);
//		sensorIdToPlayerId.put(44, 18);
		
		sensorIdToPlayerId.put(4, 4);
		sensorIdToPlayerId.put(8, 8);
		sensorIdToPlayerId.put(10, 10);
		sensorIdToPlayerId.put(12, 12);
	}

	@Override
	public void initToolbar(ToolBar toolbar) {
		
	}

	@Override
	public void createPartControl(Composite parent) {
//		this.parent = parent;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		
		soccerViewer = new Composite(parent, SWT.BORDER);
		
		soccerFieldDraw = new Canvas(soccerViewer, SWT.BORDER);
		soccerFieldDraw.setSize(width, height);
		soccerFieldDraw.setBackgroundImage(getResizedImage(ViewerStreamSoccerPlugIn.getImageManager().get("soccer_field"),width,height));

		soccerFieldDraw.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				  GC gc = new GC(soccerFieldDraw);
				  Font fontPlayerId = new Font(e.display,"Arial", fontSize, SWT.BOLD | SWT.ITALIC);
				  Font fontTime = new Font(e.display,"Arial", fontSize+2, SWT.BOLD | SWT.ITALIC);
				  gc.setFont(fontPlayerId);
				  
				  for(Entry<Integer, Tuple<?> > entry : currentTuple.entrySet()) {
					    Integer sid = entry.getKey();
					    Tuple<?> soccerTuple = entry.getValue();
					    if(		attributeIndexMap.get("x")!=null & attributeIndexMap.get("y")!=null){
					    	Integer x = (Integer)soccerTuple.getAttribute(attributeIndexMap.get("y"));
					    	Integer y = (Integer)soccerTuple.getAttribute(attributeIndexMap.get("x"));
					    	
					    	if(sidBalls.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						    	gc.fillOval(getCoordX(x), getCoordY(y), ballSize, ballSize);
						    	if(sensorIdToPlayerId.containsKey(sid)){
						    		gc.drawText(sensorIdToPlayerId.get(sid).toString(), getCoordX(x)+10, getCoordY(y), true);
						    	}
						    }else if(sidTeamA.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						    	gc.fillOval(getCoordX(x), getCoordY(y), playerSize, playerSize);
						    	if(sensorIdToPlayerId.containsKey(sid)){
						    		gc.drawText(sensorIdToPlayerId.get(sid).toString(), getCoordX(x)+6, getCoordY(y), true);
						    	}
						    }else if(sidTeamB.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
						    	gc.fillOval(getCoordX(x), getCoordY(y), playerSize, playerSize);
						    	if(sensorIdToPlayerId.containsKey(sid)){
						    		gc.drawText(sensorIdToPlayerId.get(sid).toString(), getCoordX(x)+6, getCoordY(y), true);
						    	}
						    }else if(sidReferee.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
						    	gc.fillOval(getCoordX(x), getCoordY(y), playerSize, playerSize);
						    }
					    }
				  }
				  if(currentTuple.get(sensorIdToRecognizeTimeProgress)!=null && attributeIndexMap.get("ts")!=null){
					  gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					  gc.setFont(fontTime);
					  long millis = (Long.parseLong(currentTuple.get(sensorIdToRecognizeTimeProgress).getAttribute(attributeIndexMap.get("ts")).toString())-10748401988186756L)/1000000000;
					  String time = String.format("%d min %d sec %d ms", 
							    TimeUnit.MILLISECONDS.toMinutes(millis),
							    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
							    millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))
							);
					  gc.drawText("TS: "+currentTuple.get(sensorIdToRecognizeTimeProgress).getAttribute(attributeIndexMap.get("ts")).toString()+"    MS: "+millis+" ms"+"    "+time, 5, 5);
				  }
				  gc.dispose();
			}
		});
		
		SoccerFieldViewUpdater up = new SoccerFieldViewUpdater(soccerFieldDraw);
		up.schedule(100);
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
		
		LOG.info("----------- SoccerView closed-----------");
	}

	private void setSchema(SDFSchema schema) {
		this.schema = schema; // kann auch null sein!
	}
	
	private Image getResizedImage(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0,image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();
//		image.dispose();
		return scaled;
	}
	
	private int getCoordX(int absX){
		return (int)(((absX+33960)/67920f)*790)+36;
	}
	private int getCoordY(int absY){
		return (int)((absY/52489f)*506)+15;
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
	

}
