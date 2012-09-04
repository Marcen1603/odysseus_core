package de.offis.salsa.obsrec.scansegm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;
import de.offis.salsa.obsrec.util.LinearRegression2D;

@ScanSegmentation(name = "LtSegmentation")
public class LtSegmentation implements IScanSegmentation {

	private GeometryFactory f = new GeometryFactory();
	
	private static final double TRESHOLD = 10;
	
	@Override
	public MultiLineString segmentScan(Measurement measurement) {
		List<LineString> segments = new ArrayList<LineString>();
		
		List<Coordinate> tempSegment = new ArrayList<Coordinate>();
		for(Sample s: measurement.getSamples()){
			if(tempSegment.size() <= 1){
				tempSegment.add(new Coordinate(s.getX(), s.getY()));
				continue;
			}
			
			// compute line
			LinearRegression2D r = new LinearRegression2D(tempSegment);
			Vector2D sV = new Vector2D(0, r.getB());
			Vector2D eV = new Vector2D(1, r.value(1));
			Line rLine = new Line(sV, eV);
			
			// check distance of sample to line
			double distance = rLine.getOffset(new Vector2D(s.getX(), s.getY()));
			
			if(distance > TRESHOLD){
				// close last segment and reset
				segments.add(f.createLineString(tempSegment.toArray(new Coordinate[0])));
				tempSegment.clear();
			} else {
				// just add sample to actual segment, dont close it
				tempSegment.add(new Coordinate(s.getX(), s.getY()));
			}
		}
		
		// since the last sample wont be able to close a 
		// segment, no next sample to compare with, we 
		// have to close it manually here
		segments.add(f.createLineString(tempSegment.toArray(new Coordinate[0])));		
		
		return f.createMultiLineString(segments.toArray(new LineString[0]));
	}
}
