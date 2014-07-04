package de.uniol.inf.is.odysseus.debs2013.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;


public class DebsIntensityCalc_Numeric extends AbstractFunction<Integer> {

	private static final long serialVersionUID = 3694633706627761769L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT};
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{accTypes1};

	enum SpeedIntensity {
		stop, trot, low, medium, high, sprint
	}
	
	static final SpeedIntensity speedMap[] = { SpeedIntensity.stop /* 0 */,
		SpeedIntensity.stop/* 1 */, SpeedIntensity.trot,
		SpeedIntensity.trot, SpeedIntensity.trot,
		SpeedIntensity.trot/* 5 */, SpeedIntensity.trot,
		SpeedIntensity.trot, SpeedIntensity.trot, SpeedIntensity.trot,
		SpeedIntensity.trot, SpeedIntensity.trot /* 11 */,
		SpeedIntensity.low, SpeedIntensity.low,
		SpeedIntensity.low /* 14 */, SpeedIntensity.medium,
		SpeedIntensity.medium, SpeedIntensity.medium /* 17 */,
		SpeedIntensity.high, SpeedIntensity.high, SpeedIntensity.high,
		SpeedIntensity.high, SpeedIntensity.high, SpeedIntensity.high,
		SpeedIntensity.high, SpeedIntensity.high /* 24 */};
	
	
	public DebsIntensityCalc_Numeric() {
		super("DebsIntensityNumeric",1,accTypes,SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		int attribute = new Double(Math.ceil(getNumericalInputValue(0))).intValue();
		if (attribute > 24)
			return SpeedIntensity.sprint.ordinal();
		else 
			return speedMap[attribute].ordinal();
	}

}
