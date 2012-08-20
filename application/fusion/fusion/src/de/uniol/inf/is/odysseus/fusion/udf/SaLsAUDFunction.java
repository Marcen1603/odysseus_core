package de.uniol.inf.is.odysseus.fusion.udf;


import java.util.Iterator;


import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;


import com.vividsolutions.jts.geom.Dimension;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;


@UserDefinedFunction(name = "FusionL1")
public class SaLsAUDFunction extends AbstractSweepArea<Tuple<? extends IMetaAttribute>> implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>,Tuple<? extends IMetaAttribute>> {
	
	double mDistance = 0.00;
	
	@Override
	public void init(String initString) {
		mDistance = Double.parseDouble(initString);
	}

	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
		Tuple<? extends IMetaAttribute> out = null;
		this.insert(in);
		
		if(!this.getElements().isEmpty()){
			if(((ITimeInterval)in.getMetadata()).getStart().after(((ITimeInterval)this.getElements().getFirst().getMetadata()).getEnd())){		
				//System.out.println("Last Element Received.");
				
				for(Tuple<? extends IMetaAttribute> element1: this.getElements()){
					Geometry geometry1 = ((Geometry)element1.getAttribute(0));
					for(Tuple<? extends IMetaAttribute> element2: this.getElements()){
						Geometry geometry2 = ((Geometry)element2.getAttribute(0));
						
						double distance = geometry1.distance(geometry2);
						if(distance < mDistance){
							//System.out.println("distance < " + mDistance);
							if(isCrosses(geometry1.getBoundaryDimension(), geometry2.getBoundaryDimension())){
								out = (Tuple<? extends IMetaAttribute>) in.clone();
								out.setAttribute(0, geometry1.union(geometry2));
							}	
						}
						
					}
				}
				
				//System.out.println("Cleared Sweep Area: " + this.elements.size());
				this.getElements().clear();
			}
		}
		
		return out;
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
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Tuple<? extends IMetaAttribute>> extractElementsBefore(
			PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSweepArea<Tuple<? extends IMetaAttribute>> clone() {
		throw new RuntimeException("Clone not implemented!!");
	}

}
