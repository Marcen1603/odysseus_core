package de.uniol.inf.is.odysseus.s100.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.s100.common.datatype.GPSPoint;
import de.uniol.inf.is.odysseus.s100.common.sdf.schema.SDFS100DataType;

public class ToGPSPoint2D extends AbstractFunction<GPSPoint> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS};
	
	public ToGPSPoint2D() {
		super("toGPSPoint2D", 2, ToGPSPoint2D.ACC_TYPES, SDFS100DataType.S100);
	}
	
	@Override
	public GPSPoint getValue() 
	{
		double longitude = this.getNumericalInputValue(0).doubleValue();
		double latitude = this.getNumericalInputValue(1).doubleValue();
		return new GPSPoint(0, longitude, latitude);
	}
	
}
