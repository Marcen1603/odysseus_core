package de.uniol.inf.is.odysseus.s100.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.s100.common.datatype.GM_Point;
import de.uniol.inf.is.odysseus.s100.common.sdf.schema.SDFS100DataType;

public class ToGMPoint extends AbstractFunction<GM_Point> 
{
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS};
	
	public ToGMPoint() 
	{
		super("toGMPoint", 3, ToGMPoint.ACC_TYPES, SDFS100DataType.GM_POINT);
	}
	
	@Override
	public GM_Point getValue() 
	{
		double longitude = this.getNumericalInputValue(0).doubleValue();
		double latitude = this.getNumericalInputValue(1).doubleValue();
		double altitude = this.getNumericalInputValue(2).doubleValue();
		return new GM_Point(longitude, latitude, altitude);
	}
	
}
