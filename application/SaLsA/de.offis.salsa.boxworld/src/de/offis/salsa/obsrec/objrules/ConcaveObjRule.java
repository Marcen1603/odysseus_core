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
		Vector2D moveVector = getMoveVector(segment);
		
		List<Coordinate> coords = new ArrayList<Coordinate>();
		List<Coordinate> projCoords = new ArrayList<Coordinate>();

		for (Coordinate s : segment.getCoordinates()) {
			coords.add(new Coordinate((int) s.x, (int) s.y));
			Vector2D temp = new Vector2D(s.x, s.y).add(moveVector).add(moveVector);
			projCoords.add(new Coordinate(temp.getX(), temp.getY()));
		}

		// reverse projected coords so the polygon is correct
		Collections.reverse(projCoords);
		coords.addAll(projCoords);
		
		return Util.createPolygon(coords);
	}
	
	private Vector2D getMoveVector(LineString segment) {
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
		return maxVector.subtract(newVector);
	}
	
//	private Vector2D getMoveVector2(LineString segment){
		// example 1 ----------
//		P - point
//		D - direction of line (unit length)
//		A - point in line
//
//		X - base of the perpendicular line
//
//		    P
//		   /|
//		  / |
//		 /  v
//		A---X----->D
//
//		(P-A).D == |X-A|
//
//		X == A + ((P-A).D)D
//		Desired perpendicular: X-P
		
//		Vector2D A = startVector;
//		Vector2D P = new Vector2D(maxCoord.x, maxCoord.y);
//		Vector2D D = endVector;
//		
//		double magnitude = (P.subtract(A)).dotProduct(D);
//		
//		Vector2D X = D.add(A.scalarMultiply(magnitude));
////		Vector2D X = A.add(D);
//		Debug.addDebugObject(new DebugMarker("A", A.getX(), A.getY()));
//		Debug.addDebugObject(new DebugMarker("P", P.getX(), P.getY()));
//		Debug.addDebugObject(new DebugMarker("D", D.getX(), D.getY()));
//		Debug.addDebugObject(new DebugMarker("X", X.getX(), X.getY()));
////		Debug.addDebugObject(new DebugVector(startVector));
////		Debug.addDebugObject(new DebugVector(D));
////		Debug.addDebugObject(new DebugVector(P));
//		
//		Debug.addDebugObject(new DebugArrow(startCoord.x, startCoord.y, endCoord.x, endCoord.y));
//		Debug.addDebugObject(new DebugArrow(X.getX(), X.getY(), endCoord.x, endCoord.y));
		// example 1 end -------------
//	}
}
