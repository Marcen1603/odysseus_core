package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth.ChoroplethLayer;

public class DynamicBuffer {
//	private static final Logger LOG = LoggerFactory.getLogger(DynamicBuffer.class);
	
	private ScreenManager screenManager;
	private StreamMapEditorPart streamMapEditor;
	
	private HashMap<String, LayerUpdater> connections;
	private HashMap<LayerUpdater, DefaultTISweepArea<IStreamObject<? extends ITimeInterval>>> puffer;
	
	
	private TimeSliderControl timeSliderControl;
	
	private ArrayList<PointInTime> timeList;
	
	
	public DynamicBuffer(StreamMapEditorPart streamMapEditor, MapEditorModel mapEditorModel, ScreenManager screenManager) {
		super();
		this.streamMapEditor = streamMapEditor;
		this.screenManager = screenManager;
		this.timeSliderControl = new TimeSliderControl(this, screenManager.getTimeSliderComposite());
		puffer = new HashMap<>();
		connections = new HashMap<>();

		
		timeList = new ArrayList<>();
		timeList.add(new PointInTime(0));
	}

	public void addTuple(LayerUpdater layerUpdater, IStreamObject<? extends ITimeInterval> element) {
		PointInTime start = element.getMetadata().getStart();
		PointInTime end = element.getMetadata().getEnd();
		
		if(!timeList.contains(start)){
			timeList.add(start);
		}
		if(end.isInfinite()==false){
			if(!timeList.contains(end)){
				timeList.add(end);
			}
		}
		Collections.sort(timeList);
		
		screenManager.getDisplay().asyncExec(new Runnable() {
			public void run() {
				timeSliderControl.setMaxValue(timeList.size()-1);
			}
		});
		
		
		
		
		
		if(puffer.get(layerUpdater)==null){
			puffer.put(layerUpdater, new DefaultTISweepArea<>());
		}
		DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> tempSweep = puffer.get(layerUpdater);
		
		tempSweep.insert(element);
		
		
		
		screenManager.getDisplay().asyncExec(new Runnable() {
			public void run() {
				draw(timeSliderControl.getIntervalMin(), timeSliderControl.getIntervalMax());
			}
		});
		
		
		
//		DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> sweepArea = puffer.get(layerUpdater);
//		sweepArea.insert(element);
//		
//		
//		
//		for (IStreamObject<? extends ITimeInterval> iStreamObject : sweepArea) {
//			System.out.println(iStreamObject.toString());
//		}
		
		
		
		
		
//		for (ILayer layer : layerUpdater) {
//			if(layer instanceof ChoroplethLayer){
//				((ChoroplethLayer)layer).addElement(element);
//			}else{
//				layer.addTuple((Tuple<?>) element);
//				if (layer.getTupleCount() > streamMapEditor.getMaxTuplesCount()) {
//					layer.removeLast();
//				}
//			}
//		}
//
//		
//		streamMapEditor.getScreenManager().getDisplay().asyncExec(new Runnable() {	
//			public void run() {
//				streamMapEditor.getScreenManager().getCanvas().redraw();
//			}
//		});
	}
	public void addConnection(String valueOf, LayerUpdater updater) {
		connections.put(valueOf, updater);
	}

	public void removeConnection(String key) {
		puffer.remove(connections.get(key));
		connections.remove(key);
		reorganiseTimeList();
	}
	
	private void reorganiseTimeList() {
		timeList = new ArrayList<>();
		timeList.add(new PointInTime(0));
		for (LayerUpdater element : puffer.keySet()) {
			DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> sweep = puffer.get(element);
			PointInTime last = sweep.getMaxTs();
			last.plus(1);
			Iterator<IStreamObject<? extends ITimeInterval>> iter = sweep.queryElementsStartingBefore(last);
			
			while(iter.hasNext()){
				IStreamObject<? extends ITimeInterval> nextObject = iter.next();
				PointInTime start = nextObject.getMetadata().getStart();
				PointInTime end = nextObject.getMetadata().getEnd();
				if(!timeList.contains(start)){
					timeList.add(start);
				}
				if(end.isInfinite()==false){
					if(!timeList.contains(end)){
						timeList.add(end);
					}
				}
			}
		}
		Collections.sort(timeList);
		screenManager.getDisplay().asyncExec(new Runnable() {
			public void run() {
				timeSliderControl.setMaxValue(timeList.size()-1);
			}
		});
	}

	public void draw(int start, int end){
		if(timeList.size()>1 && timeList.get(start)!=null && timeList.get(end)!=null){
			fillLayersWithTuples(timeList.get(start), timeList.get(end));
			
			streamMapEditor.getScreenManager().getDisplay().asyncExec(new Runnable() {	
				public void run() {
					streamMapEditor.getScreenManager().getCanvas().redraw();
				}
			});
		}
	}
	
	public void fillLayersWithTuples(PointInTime start, PointInTime end){
		TimeInterval interval = new TimeInterval(start, end.plus(1));
		
		for (LayerUpdater element : puffer.keySet()) {
			DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> sweep = puffer.get(element);
			Iterator<IStreamObject<? extends ITimeInterval>> iter = sweep.queryOverlaps(interval);
			
			ArrayList<IStreamObject<? extends ITimeInterval>> elementList = new ArrayList<>();
			while(iter.hasNext()){
				elementList.add(iter.next());
			}
			
			for (ILayer iLayer : element) {
				if(iLayer instanceof ChoroplethLayer){
					ChoroplethLayer layer = (ChoroplethLayer)iLayer;
					layer.clean();
					
					for (IStreamObject<? extends ITimeInterval> iStreamObject : elementList) {
						layer.addTuple((Tuple<?>) iStreamObject);
					}
					
				}				
			}
		}
	}

	public ArrayList<PointInTime> getTimeList() {
		return timeList;
	}
}
