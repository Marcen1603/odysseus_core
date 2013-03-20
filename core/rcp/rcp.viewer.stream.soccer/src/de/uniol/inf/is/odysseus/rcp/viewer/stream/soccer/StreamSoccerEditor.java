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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.soccer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.soccer.activator.ViewerStreamSoccerPlugIn;

public class StreamSoccerEditor implements IStreamEditorType{
	private static final Logger LOG = LoggerFactory.getLogger(StreamSoccerEditor.class);
	private SDFSchema schema;
//	private StreamEditor editor;
//	private Composite parent;
	
	private Composite soccerViewer;
	
	private HashMap<String, Integer> attributeIndexMap;
	
	private HashMap<Integer, SoccerTuple> currentTuple;
	
	
	private Canvas soccerFieldDraw;
	Label soccerFieldOutline;
	
	Runnable runnable;
	
	final int width = 862;
	final int height = 532;
	
	private ArrayList<Integer> sidBalls;
	private ArrayList<Integer> sidTeamA;
	private ArrayList<Integer> sidTeamB;
	private ArrayList<Integer> sidReferee;
	
	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: Soccer is only for relational tuple!");
			return;
		}
		Tuple<?> tuple = (Tuple<?>) element;
//		@SuppressWarnings("unchecked")
//		IStreamObject<? extends ITimeInterval> timeTuple = (IStreamObject<? extends ITimeInterval>) element;
		
		SoccerTuple soccerTuple = new SoccerTuple();		
		if(attributeIndexMap.get("sid")!=null){
			soccerTuple.setSid((Integer)tuple.getAttribute(attributeIndexMap.get("sid")));
		}
		if(attributeIndexMap.get("ts")!=null){
			soccerTuple.setStartTs((Long)tuple.getAttribute(attributeIndexMap.get("ts")));
		}
//		soccerTuple.setStartTs(timeTuple.getMetadata().getStart());
//		soccerTuple.setEndTs(timeTuple.getMetadata().getEnd());
		if(attributeIndexMap.get("x")!=null){
			soccerTuple.setX((Integer)tuple.getAttribute(attributeIndexMap.get("x")));
		}
		if(attributeIndexMap.get("y")!=null){
			soccerTuple.setY((Integer)tuple.getAttribute(attributeIndexMap.get("y")));
		}
		if(attributeIndexMap.get("z")!=null){
			soccerTuple.setZ((Integer)tuple.getAttribute(attributeIndexMap.get("z")));
		}
		if(attributeIndexMap.get("v")!=null){
			soccerTuple.setV((Number)tuple.getAttribute(attributeIndexMap.get("v")));
		}
		if(attributeIndexMap.get("a")!=null){
			soccerTuple.setA((Integer)tuple.getAttribute(attributeIndexMap.get("a")));
		}
		if(attributeIndexMap.get("vx")!=null){
			soccerTuple.setVx((Integer)tuple.getAttribute(attributeIndexMap.get("vx")));
		}
		if(attributeIndexMap.get("vy")!=null){
			soccerTuple.setVy((Integer)tuple.getAttribute(attributeIndexMap.get("vy")));
		}
		if(attributeIndexMap.get("vz")!=null){
			soccerTuple.setVz((Integer)tuple.getAttribute(attributeIndexMap.get("vz")));
		}
		if(attributeIndexMap.get("ax")!=null){
			soccerTuple.setAx((Integer)tuple.getAttribute(attributeIndexMap.get("ax")));
		}
		if(attributeIndexMap.get("ay")!=null){
			soccerTuple.setAy((Integer)tuple.getAttribute(attributeIndexMap.get("ay")));
		}
		if(attributeIndexMap.get("az")!=null){
			soccerTuple.setAz((Integer)tuple.getAttribute(attributeIndexMap.get("az")));
		}
		
//		LOG.info(tuple.getAttribute(1).toString());
//		LOG.info(soccerTuple.toString());
//		LOG.info("tupX: "+soccerTuple.getX()+" tupY: "+soccerTuple.getY()+" X: "+getCoordX(soccerTuple.getY())+" Y: "+getCoordY(soccerTuple.getX()));
		
		currentTuple.put(soccerTuple.getSid(), soccerTuple);
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
		
		attributeIndexMap = new HashMap<>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			attributeIndexMap.put(schema.getAttribute(i).getAttributeName(), i);
		}
		
		currentTuple = new HashMap<>();
		
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			LOG.info(schema.getAttribute(i).getAttributeName() + "  "+schema.getAttribute(i).getDatatype().getQualName());
		}
		
		initSids();
		
	}

	private void initSids() {
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
		soccerFieldDraw.setSize(862,532);
		soccerFieldDraw.setBackgroundImage(getResizedImage(ViewerStreamSoccerPlugIn.getImageManager().get("soccer_field"),width,height));
//		
		soccerFieldDraw.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				  GC gc = new GC(soccerFieldDraw);
				  
				  for(Entry<Integer, SoccerTuple > entry : currentTuple.entrySet()) {
					    Integer sid = entry.getKey();
					    SoccerTuple soccerTuple = entry.getValue();
					    if(sidBalls.contains(sid)){
					    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					    	gc.fillOval(getCoordX(soccerTuple.getY()), getCoordY(soccerTuple.getX()), 10, 10);
					    }else if(sidTeamA.contains(sid)){
					    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					    	gc.fillOval(getCoordX(soccerTuple.getY()), getCoordY(soccerTuple.getX()), 6, 6);
					    }else if(sidTeamB.contains(sid)){
					    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
					    	gc.fillOval(getCoordX(soccerTuple.getY()), getCoordY(soccerTuple.getX()), 6, 6);
					    }else if(sidReferee.contains(sid)){
					    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					    	gc.fillOval(getCoordX(soccerTuple.getY()), getCoordY(soccerTuple.getX()), 6, 6);
					    }
				  }
				  if(currentTuple.get(4)!=null){
					  gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					  gc.drawText("TS: "+currentTuple.get(4).getStartTs().toString()+" MS: "+(Long.parseLong(currentTuple.get(4).getStartTs().toString())-10748401988186756L)/1000000000+" ms", 5, 5);
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
