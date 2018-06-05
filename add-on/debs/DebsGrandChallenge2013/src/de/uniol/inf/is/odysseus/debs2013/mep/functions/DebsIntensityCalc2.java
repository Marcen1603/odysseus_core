package de.uniol.inf.is.odysseus.debs2013.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class DebsIntensityCalc2 extends AbstractFunction<String> {

	private static final long serialVersionUID = 3694633706627761769L;

	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
			SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE,
			SDFDatatype.FLOAT };
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			accTypes1 };

	enum SpeedIntensity {
		stop, trot, low, medium, high, sprint
	}

	static final SpeedIntensity speedMap[] = { SpeedIntensity.stop,
			SpeedIntensity.trot, SpeedIntensity.low, SpeedIntensity.medium,
			SpeedIntensity.high, SpeedIntensity.sprint };

	public DebsIntensityCalc2() {
		super("DebsIntensity", 1, accTypes, SDFDatatype.STRING);
	}

	@Override
	public String getValue() {
		int attribute = new Double(Math.ceil(getNumericalInputValue(0)))
				.intValue();
		return speedMap[attribute].name();
	}

}
