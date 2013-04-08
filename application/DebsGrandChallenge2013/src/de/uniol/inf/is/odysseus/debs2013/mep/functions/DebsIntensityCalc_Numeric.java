package de.uniol.inf.is.odysseus.debs2013.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;


public class DebsIntensityCalc_Numeric extends AbstractFunction<Integer> {

	private static final long serialVersionUID = 3694633706627761769L;

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT};
	
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
	
	
	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException("ceil has only 1 argument.");
		}
        return accTypes;
	}

	@Override
	public String getSymbol() {
		return "DebsIntensityNumeric";
	}

	@Override
	public Integer getValue() {
		int attribute = new Double(Math.ceil(getNumericalInputValue(0))).intValue();
		if (attribute > 24)
			return SpeedIntensity.sprint.ordinal();
		else 
			return speedMap[attribute].ordinal();
	}

	@Override
	public SDFDatatype getReturnType() {		
		return SDFDatatype.STRING;
	}

}
