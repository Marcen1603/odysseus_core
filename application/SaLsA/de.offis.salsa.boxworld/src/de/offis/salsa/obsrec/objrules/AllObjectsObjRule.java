package de.offis.salsa.obsrec.objrules;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.models.ObjectType;
import de.offis.salsa.obsrec.util.Util;

@ObjectRule(typeCategory = ObjectType.KONKAV, name = "AllObjects")
public class AllObjectsObjRule implements IObjectRule {

	
	@Override
	public ObjectType getType() {
		return ObjectType.UNKNOWN;
	}

	@Override
	public double getTypeAffinity(List<Sample> segment) {			
		return 1.0;
	}


	public Polygon getPredictedPolygon(List<Sample> segment){
		List<Coordinate> coords = new ArrayList<Coordinate>();		
		
		Integer maxX = null;
		Integer maxY = null;
		
		for(Sample s : segment){
			if(maxX == null)
				maxX = (int) s.getX();
			
			if(maxY == null)
				maxY = (int) s.getY();
			
			if(maxX < s.getX())
				maxX = (int) s.getX();
			
			if(maxY < s.getY())
				maxY = (int) s.getY();
			
			coords.add(new Coordinate((int)s.getX(), (int)s.getY()));
		}
		
		coords.add(new Coordinate(maxX, maxY));		
		
		return Util.createPolygon(coords);
	}
}
