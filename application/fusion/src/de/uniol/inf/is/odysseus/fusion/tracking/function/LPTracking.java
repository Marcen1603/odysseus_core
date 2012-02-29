package de.uniol.inf.is.odysseus.fusion.tracking.function;

import java.util.LinkedList;

import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class LPTracking extends AbstractFunction<Integer> {

	private static final long serialVersionUID = -3059065500918983661L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFSpatialDatatype.SPATIAL_POLYGON };

	private static LinkedList<Polygon> list = new LinkedList<Polygon>();

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		} else {
			return accTypes;
		}
	}

	@Override
	public String getSymbol() {
		return "LPTracking";
	}

	@Override
	public Integer getValue() {
		Polygon currentPolygon = (Polygon) this.getInputValue(0);
		int objectClass = (Integer) this.getInputValue(1);
		double mDistance = 0;
		
		
		/*  @ToDo 
		 * 
		 * 	If a objects is not detected for a defined time than remove the objects from the list. 
		 * 	Alternative it is possible to use the context store for a automatic remove of old objects.
		 * 
		 */
		
		if(objectClass == 1){
			mDistance = 35.0;
		}
		if(objectClass == 2){
			mDistance = 40.0;
		}

		int last = 0;
		int candidates = 0;
		for (int i = 0; i < list.size(); i++) {
			Geometry geometry2 = (Geometry) list.get(i);

			double distance = currentPolygon.distance(geometry2);
			if (distance < mDistance) {
				if (isCrosses(currentPolygon.getBoundaryDimension(), geometry2.getBoundaryDimension())) {
					
					if(candidates > 1){
						list.remove(i);
					}
					else{
						list.set(i, currentPolygon);
						last = i;
					}
					candidates++;
				}
			}
		}
		//System.out.println("Objects Canndidates in Area: " + list.size());
		//System.out.println("This Match Canndidates: " + candidates);
		if(last != 0)
			return last;

//		if (objectClass > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				// if (currentPolygon.distance(list.get(i)) < 100.0) {
//				if (currentPolygon.contains(list.get(i).getCentroid())) {
//					list.set(i, currentPolygon);
//					return i;
//				}
//			}
//		}
		list.add(currentPolygon);
		return list.size() - 1;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}
	
	public boolean isCrosses(int dimensionOfGeometryA, int dimensionOfGeometryB) {
		int[][] matrix = new int[3][3];

		if ((dimensionOfGeometryA == Dimension.P && dimensionOfGeometryB == Dimension.L)
				|| (dimensionOfGeometryA == Dimension.P && dimensionOfGeometryB == Dimension.A)
				|| (dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.A)) {
			return matches(matrix[Location.INTERIOR][Location.INTERIOR], 'T')
					&& matches(matrix[Location.INTERIOR][Location.EXTERIOR],
							'T');
		}
		if ((dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.P)
				|| (dimensionOfGeometryA == Dimension.A && dimensionOfGeometryB == Dimension.P)
				|| (dimensionOfGeometryA == Dimension.A && dimensionOfGeometryB == Dimension.L)) {
			return matches(matrix[Location.INTERIOR][Location.INTERIOR], 'T')
					&& matches(matrix[Location.EXTERIOR][Location.INTERIOR],
							'T');
		}
		if (dimensionOfGeometryA == Dimension.L
				&& dimensionOfGeometryB == Dimension.L) {
			return matrix[Location.INTERIOR][Location.INTERIOR] == 0;
		}
		return false;
	}

	public static boolean matches(int actualDimensionValue, char requiredDimensionSymbol) {
		if (requiredDimensionSymbol == '*') {
			return true;
		}
		if (requiredDimensionSymbol == 'T'
				&& (actualDimensionValue >= 0 || actualDimensionValue == Dimension.TRUE)) {
			return true;
		}
		if (requiredDimensionSymbol == 'F'
				&& actualDimensionValue == Dimension.FALSE) {
			return true;
		}
		if (requiredDimensionSymbol == '0'
				&& actualDimensionValue == Dimension.P) {
			return true;
		}
		if (requiredDimensionSymbol == '1'
				&& actualDimensionValue == Dimension.L) {
			return true;
		}
		if (requiredDimensionSymbol == '2'
				&& actualDimensionValue == Dimension.A) {
			return true;
		}
		return false;
	}
}
