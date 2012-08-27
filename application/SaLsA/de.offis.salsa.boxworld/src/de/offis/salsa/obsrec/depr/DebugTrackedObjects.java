package de.offis.salsa.obsrec.depr;

import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.TrackedObject.Type;
import de.offis.salsa.obsrec.SensorMeasurement;
@Deprecated
public class DebugTrackedObjects implements TrackedObjectsProvider {

	private Sample last;
	private List<Sample> tempObjectSamples = new ArrayList<>();
	
	@Override
	public List<TrackedObject> getTrackedObjects(SensorMeasurement measurement) {
		List<TrackedObject> result = new ArrayList<TrackedObject>();
		
		
		for(Sample s : measurement.getData()){
			if(checkNewObjectConditions(s) && last != null){
				result.add(generateBBox(tempObjectSamples));
				this.tempObjectSamples.clear();				
			}
			
			this.tempObjectSamples.add(s);
			
			this.last = s;
		}
		
		result.add(generateBBox(tempObjectSamples));
				
		return result;
	}
	
	private boolean checkNewObjectConditions(Sample current){
		if(last == null){
			return false;
		}
		
		// TODO emulate den infinite wert ...
		if(current.getDist1() > 2000){
			return true;
		}
		
		if(Math.abs(current.getDist1() - last.getDist1()) > 100){
			return true;
		}
		
		return false;		
	}

	private TrackedObject generateBBox(List<Sample> samplesObject){
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		
		boolean firstElement = true;
		
		for(Sample s : samplesObject){
			if(firstElement){
				minX = (int)s.getX();
				maxX = (int)s.getX();
				
				minY = (int)s.getY();
				maxY = (int)s.getY();
				
				firstElement = false;
			}
			
			if(s.getX() < minX){
				minX = (int)s.getX();
			}
			
			if(s.getX() > maxX){
				maxX = (int)s.getX();
			}
			
			if(s.getY() < minY){
				minY = (int)s.getY();
			}
			
			if(s.getY() > maxY){
				maxY = (int)s.getY();
			}
		}
		
		return new TrackedObject(minX, minY, maxX-minX, maxY-minY, Type.ECKIG);
	}
}
