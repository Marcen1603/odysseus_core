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

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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

public class PositionsView extends AbstractSoccerView implements IStreamEditorType{
	private static final Logger LOG = LoggerFactory.getLogger(PositionsView.class);
	private SDFSchema schema;

	private ConcurrentHashMap<Integer, Tuple<?>> currentTuple;

	Runnable runnable;

	final int playerSize = 6;
	final int refereeSize = 6;
	final int ballSize = 10;
	final int fontSize = 8;

	final int sensorIdToRecognizeTimeProgress = 13;

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, Object element, int port) {
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
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation point, int port) {}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		LOG.info("----------- PositionsView opened -----------");


		setSchema(editorInput.getStreamConnection().getOutputSchema());

		attributeIndexMap = new ConcurrentHashMap<>();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			attributeIndexMap.put(schema.getAttribute(i).getAttributeName(), i);
		}



		for (int i = 0; i < schema.getAttributes().size(); i++) {
			LOG.info(schema.getAttribute(i).getAttributeName() + "  "+schema.getAttribute(i).getDatatype().getQualName());
		}

		initMetadata();

		currentTuple = new ConcurrentHashMap<>();

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
				  Font fontPlayerId = new Font(e.display,"Arial", fontSize, SWT.BOLD | SWT.ITALIC);
				  Font fontTime = new Font(e.display,"Arial", 9, SWT.BOLD | SWT.ITALIC);
				  gc.setFont(fontPlayerId);

				  for(Entry<Integer, Tuple<?> > entry : currentTuple.entrySet()) {
					    Integer sid = entry.getKey();
					    Tuple<?> soccerTuple = entry.getValue();
					    if(		attributeIndexMap.get("x")!=null & attributeIndexMap.get("y")!=null){
					    	Integer x = (Integer)soccerTuple.getAttribute(attributeIndexMap.get("y"));
					    	Integer y = (Integer)soccerTuple.getAttribute(attributeIndexMap.get("x"));

					    	if(sidBalls.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
						    	gc.fillOval(getCoordX(x)-(ballSize/2), getCoordY(y)-(ballSize/2), ballSize, ballSize);
						    	if(sensorIdToPlayerId.containsKey(sid)){
						    		gc.drawText(sensorIdToPlayerId.get(sid).toString(), getCoordX(x)-(ballSize/2)+ballSize, getCoordY(y)-(ballSize/2), true);
						    	}
						    }else if(sidTeamA.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						    	gc.fillOval(getCoordX(x)-(playerSize/2), getCoordY(y)-(playerSize/2), playerSize, playerSize);
						    	if(sensorIdToPlayerId.containsKey(sid)){
						    		gc.drawText(sensorIdToPlayerId.get(sid).toString(), getCoordX(x)-(playerSize/2)+playerSize, getCoordY(y)-(playerSize/2), true);
						    	}
						    }else if(sidTeamB.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
						    	gc.fillOval(getCoordX(x)-(playerSize/2), getCoordY(y)-(playerSize/2), playerSize, playerSize);
						    	if(sensorIdToPlayerId.containsKey(sid)){
						    		gc.drawText(sensorIdToPlayerId.get(sid).toString(), getCoordX(x)-(playerSize/2)+playerSize, getCoordY(y)-(playerSize/2), true);
						    	}
						    }else if(sidReferee.contains(sid)){
						    	gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
						    	gc.fillOval(getCoordX(x)-(playerSize/2), getCoordY(y)-(playerSize/2), playerSize, playerSize);
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
				  fontPlayerId.dispose();
				  fontTime.dispose();
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
		LOG.info("----------- PositionsView closed-----------");
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


}
