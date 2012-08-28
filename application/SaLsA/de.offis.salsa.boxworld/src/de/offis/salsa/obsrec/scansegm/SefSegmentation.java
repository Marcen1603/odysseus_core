package de.offis.salsa.obsrec.scansegm;

import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.SensorMeasurement;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;
/**
 * Successive Edge Following "SEF" method for segmentation of a laser scan. 
 * SEF works directly on the measured distances, no x/y-coordinates, of a scan. 
 * A new segement is completed when the difference betweeen to adjacent measures 
 * exceeds a treshold.
 * 
 * @author Alex
 *
 */
@ScanSegmentation(name = "SefSegmentation")
public class SefSegmentation implements IScanSegmentation {

	private Sample last;
	
	private List<Sample> tempSegment;
	
	private static final int MIN_OBJECTS_PER_SEGMENT = 5;
	private static final int TRESHOLD = 100;
	
	@Override
	public List<List<Sample>> segmentScan(SensorMeasurement measurement) {
		List<List<Sample>> segments = new ArrayList<List<Sample>>();
				
		beginSegment();
		for(Sample s : measurement.getData()){
			if(checkNewObjectConditions(s) && last != null){
				List<Sample> segment = closeSegment();
				if(segment != null){
					segments.add(segment);
				}
				beginSegment();		
			}
			
			addSegmentSample(s);
			
			this.last = s;
		}
		
		List<Sample> segment = closeSegment();
		if(segment != null){
			segments.add(segment);
		}
				
		return segments;
	}
	
	private void beginSegment(){
		this.tempSegment = new ArrayList<Sample>();
	}
	
	private void addSegmentSample(Sample s){
		if(this.tempSegment != null){
			this.tempSegment.add(s);
		}
	}
	
	private List<Sample> closeSegment(){
		if(tempSegment.size() > MIN_OBJECTS_PER_SEGMENT){
			return tempSegment;
		} else {
			return null;
		}
	}
	
	private boolean checkNewObjectConditions(Sample current){
		if(last == null){
			return false;
		}
		
		// TODO emulate den infinite wert ...
		if(current.getDist1() < 3){
			return true;
		}
		
		if(Math.abs(current.getDist1() - last.getDist1()) > TRESHOLD){
			return true;
		}
		
		return false;		
	}
}
