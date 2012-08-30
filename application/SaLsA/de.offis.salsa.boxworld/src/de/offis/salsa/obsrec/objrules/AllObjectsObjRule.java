package de.offis.salsa.obsrec.objrules;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

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
	public double getTypeAffinity(LineString segment) {			
		return 1.0;
	}


	public Polygon getPredictedPolygon(LineString segment){
		List<Coordinate> coords = new ArrayList<Coordinate>();		
		
		Integer maxX = null;
		Integer maxY = null;
		
		for(Coordinate s : segment.getCoordinates()){
			if(maxX == null)
				maxX = (int) s.x;
			
			if(maxY == null)
				maxY = (int) s.y;
			
			if(maxX < s.x)
				maxX = (int) s.x;
			
			if(maxY < s.y)
				maxY = (int) s.y;
			
			coords.add(new Coordinate((int)s.x, (int)s.y));
		}
		
		coords.add(new Coordinate(maxX, maxY));		
		
		return Util.createPolygon(coords);
	}
}
