package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

// TODO: Move to own bundle

public class DebsIntensityCalc2 extends AbstractFunction<String> {

	private static final long serialVersionUID = 3694633706627761769L;

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT};
	
	enum SpeedIntensity {
		stop, trot, low, medium, high, sprint
	}
	
	static final SpeedIntensity speedMap[] = { SpeedIntensity.stop,	SpeedIntensity.trot,
		SpeedIntensity.low, SpeedIntensity.medium, SpeedIntensity.high, SpeedIntensity.sprint};
	
	
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
		return "DebsIntensity";
	}

	@Override
	public String getValue() {
		int attribute = new Double(Math.ceil(getNumericalInputValue(0))).intValue();
		return speedMap[attribute].name();
	}

	@Override
	public SDFDatatype getReturnType() {		
		return SDFDatatype.STRING;
	}

}
