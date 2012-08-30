package de.offis.salsa.obsrec.objrules;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.models.ObjectType;
import de.offis.salsa.obsrec.util.Util;

@ObjectRule(typeCategory = ObjectType.V_FORM, name = "StandardVForm")
public class VFormObjRule implements IObjectRule {
	
//	private static final int TOLERANCE = 30;

	
	@Override
	public ObjectType getType() {
		return ObjectType.V_FORM;
	}

	@Override
	public double getTypeAffinity(LineString segment) {
		Coordinate first = segment.getCoordinateN(0);
		double firstX = first.x;
		double firstY = first.y;
		Coordinate last = segment.getCoordinateN(segment.getNumGeometries() - 1);
		double lastX = last.x;
		double lastY = last.y;

		// das erste und letzte element nicht iterieren ...
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (int i = 1; i < segment.getCoordinates().length - 1; i += 2) {
			Coordinate current = segment.getCoordinateN(i);
			double currentX = current.x;
			double currentY = current.y;
			
			double b = Point2D.distance(firstX, firstY, currentX, currentY);
			double c = Point2D.distance(currentX, currentY, lastX, lastY);
			double a = Point2D.distance(firstX, firstY, lastX, lastY);
			
			double cos_v = (Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2))/(-2 * b * c);
			double acos = Math.acos(cos_v);
			double v = Math.toDegrees(acos);
			
			stats.addValue(v);
		}
		
		if(stats.getMin() < 90){
			// TODO feingranulare wahrscheinlichkeiten
			return 1.0;
		} else {
			return 0.0;
		}
	}

	@Override
	public Polygon getPredictedPolygon(LineString segment) {
		// TODO Auto-generated method stub
		return Util.createPolygon(new ArrayList<Coordinate>());
	}

}
