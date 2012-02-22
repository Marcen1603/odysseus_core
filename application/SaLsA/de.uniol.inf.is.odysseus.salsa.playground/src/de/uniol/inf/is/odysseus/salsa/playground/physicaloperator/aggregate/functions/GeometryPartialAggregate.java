package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class GeometryPartialAggregate implements IPartialAggregate<RelationalTuple<? extends IMetaAttribute>>, Iterable<RelationalTuple<? extends IMetaAttribute>> {

	final List<RelationalTuple<? extends IMetaAttribute>> notMElems;
	final List<RelationalTuple<? extends IMetaAttribute>> elems;
	protected volatile static Logger LOGGER = LoggerFactory
			.getLogger(GeometryPartialAggregate.class);

	public GeometryPartialAggregate(RelationalTuple<? extends IMetaAttribute> elem) {
		this.elems = new LinkedList<RelationalTuple<? extends IMetaAttribute>>();
		this.elems.add(elem);

		this.notMElems = new LinkedList<RelationalTuple<? extends IMetaAttribute>>();
		this.notMElems.add(elem);
		// LOGGER.debug("Add first Element");
	}

	public GeometryPartialAggregate(GeometryPartialAggregate p) {
		// LOGGER.debug("Add first ElementLIST");
		this.elems = new LinkedList<RelationalTuple<? extends IMetaAttribute>>(p.elems);
		this.notMElems = new LinkedList<RelationalTuple<? extends IMetaAttribute>>(p.elems);
	}

	public List<RelationalTuple<? extends IMetaAttribute>> getElems() {
		return elems;
	}

	public GeometryPartialAggregate addElem(RelationalTuple<? extends IMetaAttribute> elem) {
		boolean merged = false;
		Geometry geometry1 = (Geometry) elem.getAttribute(0);
		
		for (int i = 0; i < elems.size(); i++) {
			Geometry geometry2 =  (Geometry) elems.get(i).getAttribute(0);
		
			if(geometry1.getEnvelope().crosses(geometry2.getEnvelope())){
			//if (isCrosses(geometry.getEnvelope().getDimension(),geometry_element.getEnvelope().getDimension())) {
				merged = true;
				elems.get(i).setAttribute(0, geometry2.union(geometry1).convexHull());
				elems.add(elems.get(i));
			}
		}
		if (!merged) {
			this.elems.add(elem);
		}
		return this;
	}
	
	  public boolean isCrosses(int dimensionOfGeometryA, int dimensionOfGeometryB) {
		  int[][] matrix = new int[3][3];
		  
		  if ((dimensionOfGeometryA == Dimension.P && dimensionOfGeometryB == Dimension.L) ||
		        (dimensionOfGeometryA == Dimension.P && dimensionOfGeometryB == Dimension.A) ||
		        (dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.A)) {
		      return matches(matrix[Location.INTERIOR][Location.INTERIOR], 'T') &&
		          matches(matrix[Location.INTERIOR][Location.EXTERIOR], 'T');
		    }
		    if ((dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.P) ||
		        (dimensionOfGeometryA == Dimension.A && dimensionOfGeometryB == Dimension.P) ||
		        (dimensionOfGeometryA == Dimension.A && dimensionOfGeometryB == Dimension.L)) {
		      return matches(matrix[Location.INTERIOR][Location.INTERIOR], 'T') &&
		          matches(matrix[Location.EXTERIOR][Location.INTERIOR], 'T');
		    }
		    if (dimensionOfGeometryA == Dimension.L && dimensionOfGeometryB == Dimension.L) {
		      return matrix[Location.INTERIOR][Location.INTERIOR] == 0;
		    }
		    return false;
		  }
	  
	  public static boolean matches(int actualDimensionValue, char requiredDimensionSymbol) {
		    if (requiredDimensionSymbol == '*') {
		      return true;
		    }
		    if (requiredDimensionSymbol == 'T' && (actualDimensionValue >= 0 || actualDimensionValue
		         == Dimension.TRUE)) {
		      return true;
		    }
		    if (requiredDimensionSymbol == 'F' && actualDimensionValue == Dimension.FALSE) {
		      return true;
		    }
		    if (requiredDimensionSymbol == '0' && actualDimensionValue == Dimension.P) {
		      return true;
		    }
		    if (requiredDimensionSymbol == '1' && actualDimensionValue == Dimension.L) {
		      return true;
		    }
		    if (requiredDimensionSymbol == '2' && actualDimensionValue == Dimension.A) {
		      return true;
		    }
		    return false;
		  }
	
//	public GeometryPartialAggregate<T> addElem(T elem) {
//		boolean merged = false;
//		Geometry geometry = (Geometry) ((RelationalTuple) elem).getAttribute(0);
//
//		for (int i = 0; i < elems.size(); i++) {
//			RelationalTuple tuple = (RelationalTuple) elems.get(i);
//			Geometry geometry_element = (Geometry) tuple.getAttribute(0);
//			if (geometry_element.getEnvelope().crosses(geometry.getEnvelope())) {
//				merged = true;
//				final Geometry[] geometrys = new Geometry[2];
//				geometrys[0] = geometry_element;
//				geometrys[1] = geometry;
//				final GeometryFactory geometryFactory = new GeometryFactory();
//				geometry_element = geometryFactory.createGeometryCollection(
//						geometrys);
//
//				tuple.setAttribute(0, geometry_element.convexHull());
//				elems.add((T) tuple);
//			}
//		}
//		if (!merged) {
//			this.elems.add(elem);
//		}
//
//		return this;
//	}

	// Testing
	// public GeometryPartialAggregate<T> addElem(T elem) {
	// boolean merged = false;
	// Geometry geometry = (Geometry)((RelationalTuple)elem).getAttribute(0);
	// // Geometry geometry_buffer = geometry.buffer(100);
	//
	// for(int i = 0; i< notMElems.size(); i++){
	//
	// RelationalTuple tuple2 = (RelationalTuple)notMElems.get(i);
	// Geometry geometry_element = (Geometry)tuple2.getAttribute(0);
	//
	// if(geometry_element.distance(geometry) < 200){
	// merged = true;
	// tuple2.setAttribute(0,geometry_element.union(geometry).convexHull());
	// notMElems.set(i, (T)tuple2);
	// elems.add((T)tuple2);
	// }
	//
	// if(geometry_element.crosses(geometry)){
	// merged = true;
	// tuple2.setAttribute(0,geometry_element.union(geometry).convexHull());
	// notMElems.set(i, (T)tuple2);
	// elems.add((T)tuple2);
	// }
	// }
	// if(!merged){
	// this.notMElems.add(elem);
	// }
	//
	// return this;
	// }

	@Override
	public String toString() {
		return "" + elems;
	}

	@Override
	public ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>> clone() {
		return new ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>>(this);
	}

	@Override
	public Iterator<RelationalTuple<? extends IMetaAttribute>> iterator() {
		return elems.iterator();
	}

	public int size() {
		return elems.size();
	}

}
