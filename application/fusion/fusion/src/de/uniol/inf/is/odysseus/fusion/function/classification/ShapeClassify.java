package de.uniol.inf.is.odysseus.fusion.function.classification;

import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;


public class ShapeClassify extends AbstractFunction<Integer> {

	private static final long serialVersionUID = -3059065500918983661L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {SDFSpatialDatatype.SPATIAL_POLYGON };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
        return accTypes;
	}

	@Override
	public String getSymbol() {
		return "ShapeClassify";
	}

	@Override
	public Integer getValue() {
		Polygon polygon = (Polygon) this.getInputValue(0);		
		double area = polygon.getArea();
		
		//Human
		if(area > 200 && area < 2500){
			return 1;
		}
		
		//Scooter
		if(area > 2500 && area < 5000){
			return 2;
		}
		
		return 0;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}

}
