/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.fusion.aggregate.function.spatialfusion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geom.TopologyException;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class GeometryPartialAggregate<T> implements IPartialAggregate<Tuple<? extends IMetaAttribute>>, Iterable<Tuple<? extends IMetaAttribute>>{
	

	final List<Tuple<? extends IMetaAttribute>> elems;
	final double mDistance = 200.00;

	
	public GeometryPartialAggregate(Tuple<? extends IMetaAttribute> elem) {
		elems = new LinkedList<Tuple<? extends IMetaAttribute>>();
		addElem(elem);
	}
	
	public GeometryPartialAggregate(GeometryPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
		this.elems = new LinkedList<Tuple<? extends IMetaAttribute>>(p.elems);
	}

	public List<Tuple<? extends IMetaAttribute>> getElems() {
		return elems;
	}
	
	public GeometryPartialAggregate<T> addElem(Tuple<? extends IMetaAttribute> elem) {
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
	public ElementPartialAggregate<Tuple<? extends IMetaAttribute>> clone(){
		return new ElementPartialAggregate<Tuple<? extends IMetaAttribute>>(this);
	}

	@Override
	public Iterator<Tuple<? extends IMetaAttribute>> iterator() {
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
//	public GeometryPartialAggregate(Tuple<? extends IMetaAttribute> elem) {
//		super(elem);
//	
//	}
//
//	public GeometryPartialAggregate(GeometryPartialAggregate p) {
//		super(p);
//	}
//	
//	@Override
//	public GeometryPartialAggregate addElem(Tuple<? extends IMetaAttribute> elem) {
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
//	public ElementPartialAggregate<Tuple<? extends IMetaAttribute>> clone() {
//		return new ElementPartialAggregate<Tuple<? extends IMetaAttribute>>(this);
//	}
//	
//	@Override
//	public List<Tuple<? extends IMetaAttribute>> getElems() {
//		return getElems();
//	}
//

