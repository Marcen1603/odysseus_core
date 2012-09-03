package de.offis.salsa.obsrec.objrules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.models.ObjectType;
import de.offis.salsa.obsrec.util.Util;

@ObjectRule(typeCategory = ObjectType.KONKAV, name = "StandardKonkav")
public class ConcaveObjRule implements IObjectRule {

	@Override
	public ObjectType getType() {
		return ObjectType.KONKAV;
	}

	@Override
	public double getTypeAffinity(LineString segment) {
		Coordinate first = segment.getCoordinateN(0);
		double firstX = first.x;
		double firstY = first.y;
		Coordinate last = segment.getCoordinateN(segment.getNumPoints() - 1);
		double lastX = last.x;
		double lastY = last.y;

		// das erste und letzte element nicht iterieren ...
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (int i = 1; i < segment.getCoordinates().length - 1; i++) {
			Coordinate current = segment.getCoordinateN(i);
			double currentX = current.x;
			double currentY = current.y;

			double b = Point2D.distance(firstX, firstY, currentX, currentY);
			double c = Point2D.distance(currentX, currentY, lastX, lastY);
			double a = Point2D.distance(firstX, firstY, lastX, lastY);

			double cos_v = (Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2))
					/ (-2 * b * c);
			double acos = Math.acos(cos_v);
			double v = Math.toDegrees(acos);

			if (firstY < currentY || lastY < currentY) {
				v = 360 - v;
			}

			stats.addValue(v);
		}

		if (stats.getMin() > 180) {
			double normVal = stats.getMin() - 180;						
			
			double result = -(1.0/180.0) * normVal + 1.0;
			return result;			
		} else {
			return 0.0;
		}
	}

	public Polygon getPredictedPolygon(LineString segment) {
		List<Coordinate> coords = new ArrayList<Coordinate>();

		Integer maxX = null;
		Integer maxY = null;

		for (Coordinate s : segment.getCoordinates()) {
			if (maxX == null)
				maxX = (int) s.x;

			if (maxY == null)
				maxY = (int) s.y;

			if (maxX < s.x)
				maxX = (int) s.x;

			if (maxY < s.y)
				maxY = (int) s.y;

			coords.add(new Coordinate((int) s.x, (int) s.y));
		}

		coords.add(new Coordinate(maxX, maxY));

		return Util.createPolygon(coords);
	}
}
