package de.offis.salsa.obsrec.scansegm;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;
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
	
	private List<Coordinate> tempSegment;
	
	private static final int MIN_OBJECTS_PER_SEGMENT = 5;
	private static final int TRESHOLD = 100;
	
	@Override
	public MultiLineString segmentScan(Measurement measurement) {
		
		
		List<LineString> segments = new ArrayList<LineString>();
				
		beginSegment();
		for(Sample s : measurement.getSamples()){
			if(checkNewObjectConditions(s) && last != null){
				LineString segment = closeSegment();
				if(segment != null){
					segments.add(segment);
				}
				beginSegment();		
			}
			
			addSegmentSample(new Coordinate(s.getX(), s.getY()));
			
			this.last = s;
		}
		
		LineString segment = closeSegment();
		if(segment != null){
			segments.add(segment);
		}
				
		return new GeometryFactory().createMultiLineString(segments.toArray(new LineString[0]));
	}
	
	private void beginSegment(){
		this.tempSegment = new ArrayList<Coordinate>();
	}
	
	private void addSegmentSample(Coordinate s){
		if(this.tempSegment != null){
			this.tempSegment.add(s);
		}
	}
	
	private LineString closeSegment(){
		if(tempSegment.size() > MIN_OBJECTS_PER_SEGMENT){
			return new GeometryFactory().createLineString(tempSegment.toArray(new Coordinate[0]));
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
