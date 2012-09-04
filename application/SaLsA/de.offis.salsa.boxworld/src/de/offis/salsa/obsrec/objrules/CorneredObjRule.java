package de.offis.salsa.obsrec.objrules;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.models.ObjectType;
import de.offis.salsa.obsrec.util.Util;

@ObjectRule(typeCategory = ObjectType.ECKIG, name = "StandardEckig")
public class CorneredObjRule implements IObjectRule {
	
	private static final int TOLERANCE = 30;
	
	@Override
	public ObjectType getType() {
		return ObjectType.ECKIG;
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

		
		int upperBound = 90 + TOLERANCE;
		int lowerBound = 90 - TOLERANCE;
		double min = stats.getMin();
//		return 1.0;
		if( min < upperBound && min > lowerBound){
			
			double peak = (upperBound - lowerBound)/2.0;
			double normVal = min - lowerBound;
			
			if(normVal <= peak){
				return peak * normVal;
			} else {
				return -peak * normVal + 1.0;
			}
		} else {
			return 0.0;
		}
	}

	public Polygon getPredictedPolygon(LineString segment) {		
		double moveVectorMagnitude = getMoveVectorMagnitude(segment);
		
		// we need the unitvector of the first and last sample
		Coordinate startCoord = segment.getCoordinateN(0);
		Coordinate endCoord = segment
				.getCoordinateN(segment.getNumPoints() - 1);

		Vector2D startVector = new Vector2D(startCoord.x, startCoord.y);
		Vector2D endVector = new Vector2D(endCoord.x, endCoord.y);
		
		Vector2D startUnitVector = new Vector2D(1, startVector);
		Vector2D endUnitVector = new Vector2D(1, endVector);
		startUnitVector = startUnitVector.scalarMultiply(1 / startUnitVector
				.getNorm());
		endUnitVector = endUnitVector.scalarMultiply(1 / endUnitVector
				.getNorm());
		
		
		List<Coordinate> coords = new ArrayList<Coordinate>();
		List<Coordinate> projCoords = new ArrayList<Coordinate>();		

		for (Coordinate s : segment.getCoordinates()) {
			coords.add(new Coordinate((int) s.x, (int) s.y));
			
		}

		// add proj coords for last sample
		Vector2D start = startUnitVector.scalarMultiply(moveVectorMagnitude).add(startVector);
		projCoords.add(new Coordinate(start.getX(), start.getY()));
		
		// add proj coords for first sample
		Vector2D end = endUnitVector.scalarMultiply(moveVectorMagnitude).add(endVector);
		projCoords.add(new Coordinate(end.getX(), end.getY()));
		
		Collections.reverse(projCoords);
		coords.addAll(projCoords);
		
		return Util.createPolygon(coords);
	}
	
	private double getMoveVectorMagnitude(LineString segment) {
		// erstelle gerade von start zu endpunkt ...
		Coordinate startCoord = segment.getCoordinateN(0);
		Coordinate endCoord = segment
				.getCoordinateN(segment.getNumPoints() - 1);

		Vector2D startVector = new Vector2D(startCoord.x, startCoord.y);
		Vector2D endVector = new Vector2D(endCoord.x, endCoord.y);

		Line startEndLine = new Line(startVector, endVector);

		// get the point from this segment which has the highest distance to the
		// line
		double maxDistance = 0;
		Coordinate maxCoord = null;
		for (Coordinate c : segment.getCoordinates()) {
			double distance = Math.abs(startEndLine.getOffset(new Vector2D(c.x,
					c.y)));
			if (maxDistance < distance) {
				maxDistance = distance;
				maxCoord = c;
			}
		}

		// http://paulbourke.net/geometry/pointline/
		// compute the closest point on line to P3
		Vector2D P1 = startVector;
		Vector2D P3 = new Vector2D(maxCoord.x, maxCoord.y);
		Vector2D P2 = endVector;

		double u = (((P3.getX() - P1.getX()) * (P2.getX() - P1.getX())) + ((P3
				.getY() - P1.getY()) * (P2.getY() - P1.getY())))
				/ ((P2.subtract(P1).dotProduct(P2.subtract(P1))));

		// schnittpunkt
		// x = x1 + u (x2 - x1)
		// y = y1 + u (y2 - y1)

		double x = P1.getX() + u * (P2.getX() - P1.getX());
		double y = P1.getY() + u * (P2.getY() - P1.getY());
//		Debug.addDebugObject(new DebugMarker("NEW", x, y));

		Vector2D maxVector = new Vector2D(maxCoord.x, maxCoord.y);
		Vector2D newVector = new Vector2D(x, y);
		
		
		// magnitude is the desired length for the newMoveVector
		double magnitude = maxVector.subtract(newVector).getNorm();
		
		return magnitude*2;
	}

}
