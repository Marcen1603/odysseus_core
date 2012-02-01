package de.uniol.inf.is.odysseus.salsa.playground.udf;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

//@UserDefinedFunction(name="salsatransform")
//public class SaLsAUDFunction<R> implements
//		IUserDefinedFunction<R, R> {
//
//
//	@Override
//	public R process(R in, int port) {
//		//System.out.println(this+"("+init+") PROCESS "+in);
//		
//		RelationalTuple<IMetaAttribute> tuple = (RelationalTuple<IMetaAttribute>)in;	
//		
//		//POINT (25 15749)
//		String[] cpoint = tuple.getAttribute(1).toString().substring(7,tuple.getAttribute(1).toString().length()-1).split(" ");	
//		tuple.setAttribute(1, new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(cpoint[0]) , Double.parseDouble(cpoint[1]))));
//		
//		//POLYGON ((10 15734, 40 15764, 20 15744, 30 15754, 10 15734))
//		Coordinate[] coordinates = new Coordinate[5];
//		int i = 0;
//		for(String polyPoint  : tuple.getAttribute(2).toString().substring(9,tuple.getAttribute(2).toString().length()-2).split(",")){				
//			coordinates[i] = new Coordinate(		
//					Double.parseDouble(polyPoint.substring(1).split(" ")[0])
//					, 
//					Double.parseDouble(polyPoint.substring(1).split(" ")[1])
//					);
//			
//			i++;
//		}
//		tuple.setAttribute(2,  new GeometryFactory().createPolygon(new GeometryFactory().createLinearRing(coordinates) , null));
//		
//		System.out.println(tuple.toString());
//		return (R) tuple;
//=======
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@UserDefinedFunction(name = "FusionL1")
public class SaLsAUDFunction<R> implements IUserDefinedFunction<R, R> {

	List<RelationalTuple> tupleList = null;
	List<RelationalTuple> mergetupleList = null;
	RelationalTuple last = null;
	
	double mDistance = 0.00;
	
	@Override
	public void init(String initString) {
		mDistance = Double.parseDouble(initString);
		
		
		
		tupleList = new ArrayList<RelationalTuple>();
		mergetupleList = new ArrayList<RelationalTuple>();
	}

	@Override
	public R process(R in, int port) {
		RelationalTuple<IMetaAttribute> intuple = (RelationalTuple<IMetaAttribute>) in;
		mergetupleList = new ArrayList<RelationalTuple>();
		
		//System.out.println((String)intuple.getAttribute(1));
		if(last != null){
			//System.out.println(intuple.getAttribute(1).equals((String)last.getAttribute(1)));
		}
		last = intuple;
		
		if (((ITimeInterval) intuple.getMetadata()).isValid()) {
			tupleList.add(intuple);

		} else {
			tupleList.add(intuple);
			/*
			System.out.println("------------------------------");
			System.out.println("Current: " + tupleList.size());
			
			for(RelationalTuple tuple : tupleList){
				System.out.println((String)tuple.getAttribute(1));
			}
			System.out.println("------------------------------");
			*/
			if(tupleList.size() > 4){
				tupleList.clear();
			}
			if (tupleList.size() > 1) {
				//System.out.println("Merge Tuple in List: " + tupleList.size());
				for (RelationalTuple<IMetaAttribute> wtuple : tupleList) {

					// System.out.println(((List<RelationalTuple<IMetaAttribute>>)intuple.getAttribute(0)).addAll(((List<RelationalTuple<IMetaAttribute>>)wtuple.getAttribute(0))));

					for (int i = 0; i < ((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).size(); i++) {
						Geometry geometry = (Geometry) ((RelationalTuple) ((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i)).getAttribute(0);
						//Enverlope Runs
						//geometry = geometry.getEnvelope();
						//geometry = geometry.convexHull();
						
						for (int ii = 0; ii < ((List<RelationalTuple<IMetaAttribute>>) wtuple.getAttribute(0)).size(); ii++) {
	
							Geometry geometry_element = (Geometry) ((RelationalTuple) ((List<RelationalTuple<IMetaAttribute>>) wtuple.getAttribute(0)).get(ii)).getAttribute(0);
							//Enverlope Runs
							//geometry_element = geometry_element.getEnvelope();
							
							//geometry_element = geometry_element.convexHull();
							
							//if (geometry.crosses(geometry_element)) {
								//System.out.println("Merge Candidate");
							
							//System.out.println(geometry.distance(geometry_element));
							if(geometry.distance(geometry_element) < mDistance){
								if (isCrosses(geometry.getEnvelope().getDimension(), geometry_element.getEnvelope().getDimension())) {
								//if(geometry.getEnvelope().crosses(geometry_element.getEnvelope())){
									//System.out.println("Merged");
									((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i).setAttribute(0, (geometry_element.union(geometry)).convexHull());
									//System.out.println("Add:" + ((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i));
									mergetupleList.add(((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i));
								}
							//}
							}
							else{
								mergetupleList.add(intuple);
							}
							
							
						}
					}
				}
				
				if(mergetupleList.size() > 0){
					//System.out.println("Send: " + mergetupleList.size());
					intuple.setAttribute(0, mergetupleList);
					tupleList.clear();
					return (R) intuple;	
				}
			}
			
			
			tupleList.clear();
		}
		
		return null;
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

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}


}
