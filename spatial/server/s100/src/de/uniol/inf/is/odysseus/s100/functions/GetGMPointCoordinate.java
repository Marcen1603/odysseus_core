package de.uniol.inf.is.odysseus.s100.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.s100.common.datatype.GM_Point;
import de.uniol.inf.is.odysseus.s100.common.sdf.schema.SDFS100DataType;

public class GetGMPointCoordinate extends AbstractFunction<Double> 
{
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFS100DataType.GM_POINT}, {SDFDatatype.INTEGER}};
	
	public GetGMPointCoordinate() 
	{
		super("GetGMPointCoordinate", 2, GetGMPointCoordinate.ACC_TYPES, SDFDatatype.DOUBLE);
	}
	
	@Override
	public Double getValue() 
	{
		GM_Point point = (GM_Point) this.getInputValue(0);
		int index = getNumericalInputValue(1).intValue();
		
		if (index == 0) return point.position.longitude;
		if (index == 1) return point.position.latitude;
		if (index == 2) return point.position.altitude;
		
		throw new IllegalArgumentException("index has an invalid value");
	}
	
}