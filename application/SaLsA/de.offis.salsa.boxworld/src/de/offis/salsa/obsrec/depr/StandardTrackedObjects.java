package de.offis.salsa.obsrec.depr;

import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.SensorMeasurement;
import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.bbox.TrackedObjectsProvider;
@Deprecated
public class StandardTrackedObjects implements TrackedObjectsProvider {

	private Sample last;
	private List<Sample> tempObjectSamples = new ArrayList<>();
	private static final int MIN_OBJECTS = 5;
	
	@Override
	public List<TrackedObject> getTrackedObjects(SensorMeasurement measurement) {
		List<TrackedObject> result = new ArrayList<TrackedObject>();
		
		
		for(Sample s : measurement.getData()){
			if(checkNewObjectConditions(s) && last != null){
				if(tempObjectSamples.size() > MIN_OBJECTS){
					result.add(new TrackedObject(tempObjectSamples));
				}
				this.tempObjectSamples.clear();				
			}
			
			this.tempObjectSamples.add(s);
			
			this.last = s;
		}
		
		result.add(new TrackedObject(tempObjectSamples));
				
		return result;
	}
	
	private boolean checkNewObjectConditions(Sample current){
		if(last == null){
			return false;
		}
		
		// TODO emulate den infinite wert ...
		if(current.getDist1() < 3){
			return true;
		}
		
		if(Math.abs(current.getDist1() - last.getDist1()) > 100){
			return true;
		}
		
		return false;		
	}
}
