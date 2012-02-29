package de.uniol.inf.is.odysseus.fusion.level1.aggregate.function;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geom.TopologyException;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class GeometryPartialAggregate<T> implements IPartialAggregate<RelationalTuple<? extends IMetaAttribute>>, Iterable<RelationalTuple<? extends IMetaAttribute>>{
	
	final List<RelationalTuple<? extends IMetaAttribute>> elems;
	final double mDistance = 35.00;
	
	
	public GeometryPartialAggregate(RelationalTuple<? extends IMetaAttribute> elem) {
		elems = new LinkedList<RelationalTuple<? extends IMetaAttribute>>();
		addElem(elem);
	}
	
	public GeometryPartialAggregate(GeometryPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		this.elems = new LinkedList<RelationalTuple<? extends IMetaAttribute>>(p.elems);
	}

	public List<RelationalTuple<? extends IMetaAttribute>> getElems() {
		return elems;
	}
	
	public GeometryPartialAggregate<T> addElem(RelationalTuple<? extends IMetaAttribute> elem) {
		boolean merged = false;
		
		Geometry geometry1 =  (Geometry)elem.getAttribute(0);
		
		for (int i = 0; i < elems.size(); i++) {
			Geometry geometry2 =  (Geometry)  elems.get(i).getAttribute(0);
			
			double distance = geometry1.distance(geometry2);
			if(distance < mDistance){
				//System.out.println("distance < " + mDistance);
				if(isCrosses(geometry1.getBoundaryDimension(), geometry2.getBoundaryDimension())){
					//System.out.println("merge");
					merged = true;
					try{
						elems.get(i).setAttribute(0, geometry2.union(geometry1).convexHull());
//					elems.add(getElems().get(i));
					}catch (TopologyException e) {
						e.printStackTrace();
					}
				}	
			}
			
//			if(geometry1.getEnvelope().crosses(geometry2.getEnvelope())){
//			//if (isCrosses(geometry.getEnvelope().getDimension(),geometry_element.getEnvelope().getDimension())) {
//				merged = true;
//				getElems().get(i).setAttribute(0, geometry2.union(geometry1).convexHull());
//				getElems().add(getElems().get(i));
//			}
		}
		if (!merged) {
			this.getElems().add(elem);
		}
		return this;
	}

	@Override
	public String toString() {
		return ""+elems;
	}
	
	@Override
	public ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>> clone(){
		return new ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>>(this);
	}

	@Override
	public Iterator<RelationalTuple<? extends IMetaAttribute>> iterator() {
		return elems.iterator();
	}

	public int size() {
		return elems.size();
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

	
//	
//	protected volatile static Logger LOGGER = LoggerFactory.getLogger(GeometryPartialAggregate.class);
//
//	public GeometryPartialAggregate(RelationalTuple<? extends IMetaAttribute> elem) {
//		super(elem);
//	
//	}
//
//	public GeometryPartialAggregate(GeometryPartialAggregate p) {
//		super(p);
//	}
//	
//	@Override
//	public GeometryPartialAggregate addElem(RelationalTuple<? extends IMetaAttribute> elem) {
//		boolean merged = false;
//		Geometry geometry1 = (Geometry) elem.getAttribute(0);
//		
//		for (int i = 0; i < getElems().size(); i++) {
//			Geometry geometry2 =  (Geometry) getElems().get(i).getAttribute(0);
//		
//			if(geometry1.getEnvelope().crosses(geometry2.getEnvelope())){
//			//if (isCrosses(geometry.getEnvelope().getDimension(),geometry_element.getEnvelope().getDimension())) {
//				merged = true;
//				getElems().get(i).setAttribute(0, geometry2.union(geometry1).convexHull());
//				getElems().add(getElems().get(i));
//			}
//		}
//		if (!merged) {
//			this.getElems().add(elem);
//		}
//		return this;
//	}
//	
//	  public boolean isCrosses(int dimensionOfGeometryA, int dimensionOfGeometryB) {
//		  int[][] matrix = new int[3][3];
//		  
//		  if ((dimensionOfGeometryA == Dimension.P && dimensionOfGeometryB == Dimension.L) ||
//		        (dimensionOfGeometryA == Dimension.P && dimensionOfGeometryB == Dimension.A) ||
//		        (dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.A)) {
//		      return matches(matrix[Location.INTERIOR][Location.INTERIOR], 'T') &&
//		          matches(matrix[Location.INTERIOR][Location.EXTERIOR], 'T');
//		    }
//		    if ((dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.P) ||
//		        (dimensionOfGeometryA == Dimension.A && dimensionOfGeometryB == Dimension.P) ||
//		        (dimensionOfGeometryA == Dimension.A && dimensionOfGeometryB == Dimension.L)) {
//		      return matches(matrix[Location.INTERIOR][Location.INTERIOR], 'T') &&
//		          matches(matrix[Location.EXTERIOR][Location.INTERIOR], 'T');
//		    }
//		    if (dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.L) {
//		      return matrix[Location.INTERIOR][Location.INTERIOR] == 0;
//		    }
//		    return false;
//		  }
//	  
//	  public static boolean matches(int actualDimensionValue, char requiredDimensionSymbol) {
//		    if (requiredDimensionSymbol == '*') {
//		      return true;
//		    }
//		    if (requiredDimensionSymbol == 'T' && (actualDimensionValue >= 0 || actualDimensionValue
//		         == Dimension.TRUE)) {
//		      return true;
//		    }
//		    if (requiredDimensionSymbol == 'F' && actualDimensionValue == Dimension.FALSE) {
//		      return true;
//		    }
//		    if (requiredDimensionSymbol == '0' && actualDimensionValue == Dimension.P) {
//		      return true;
//		    }
//		    if (requiredDimensionSymbol == '1' && actualDimensionValue == Dimension.L) {
//		      return true;
//		    }
//		    if (requiredDimensionSymbol == '2' && actualDimensionValue == Dimension.A) {
//		      return true;
//		    }
//		    return false;
//		  }
//	
//
//	@Override
//	public ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>> clone() {
//		return new ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>>(this);
//	}
//	
//	@Override
//	public List<RelationalTuple<? extends IMetaAttribute>> getElems() {
//		return getElems();
//	}
//

